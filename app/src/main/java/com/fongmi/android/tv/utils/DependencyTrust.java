package com.fongmi.android.tv.utils;

import android.app.Activity;
import android.os.Looper;
import android.text.TextUtils;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.api.config.VodConfig;
import com.github.catvod.utils.Prefers;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DependencyTrust {

    private static final String PREFIX = "dependency_trust_";

    public static boolean confirm(String type, String url, String hashType, String hash, File file) {
        if (TextUtils.isEmpty(url) || !url.startsWith("http")) return true;
        if (TextUtils.isEmpty(hash)) return false;
        String key = key(type, url, hashType, hash);
        if (Prefers.getBoolean(key, false)) return true;
        if (Looper.myLooper() == Looper.getMainLooper()) return false;
        Activity activity = App.activity();
        if (activity == null || activity.isFinishing()) return false;
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] result = new boolean[]{false};
        App.post(() -> new MaterialAlertDialogBuilder(activity)
                .setTitle("远程依赖确认")
                .setMessage(message(type, url, hashType, hash, file))
                .setNegativeButton("拒绝", (dialog, which) -> latch.countDown())
                .setPositiveButton("信任并加载", (dialog, which) -> {
                    Prefers.put(key, true);
                    result[0] = true;
                    latch.countDown();
                })
                .setOnCancelListener(dialog -> latch.countDown())
                .show());
        try {
            latch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return result[0];
    }

    private static String key(String type, String url, String hashType, String hash) {
        String source = VodConfig.getUrl();
        String raw = source + "|" + type + "|" + url + "|" + hashType + "|" + hash;
        return PREFIX + Integer.toHexString(raw.hashCode());
    }

    private static String message(String type, String url, String hashType, String hash, File file) {
        StringBuilder sb = new StringBuilder();
        sb.append("类型：").append(type).append('\n');
        sb.append("配置源：").append(VodConfig.getUrl()).append('\n');
        sb.append("URL：").append(url).append('\n');
        sb.append("Hash：").append(hashType).append(':').append(hash).append('\n');
        if (file != null && file.exists()) sb.append("大小：").append(file.length()).append(" bytes\n");
        sb.append('\n').append("仅信任你确认来源的远程代码。");
        return sb.toString();
    }
}
