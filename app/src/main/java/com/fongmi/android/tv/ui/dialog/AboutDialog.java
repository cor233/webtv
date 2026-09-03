package com.fongmi.android.tv.ui.dialog;

import androidx.fragment.app.FragmentActivity;

import com.fongmi.android.tv.BuildConfig;
import com.fongmi.android.tv.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public final class AboutDialog {

    private AboutDialog() {
    }

    public static void show(FragmentActivity activity, Runnable updateAction) {
        String message = activity.getString(R.string.about_message, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, BuildConfig.FLAVOR_mode, BuildConfig.FLAVOR_abi);
        new MaterialAlertDialogBuilder(activity)
                .setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton(R.string.about_check_update, (dialog, which) -> {
                    if (updateAction != null) updateAction.run();
                })
                .setNegativeButton(R.string.update_settings, (dialog, which) -> UpdateSettingsDialog.show(activity))
                .setNeutralButton(R.string.dialog_negative, null)
                .show();
    }
}
