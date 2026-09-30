package com.fongmi.android.tv.remote;

import android.text.TextUtils;
import com.fongmi.android.tv.App;
import com.fongmi.android.tv.utils.Task;
import com.google.gson.JsonObject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/** Minimal public-relay device agent. It is inactive until explicitly enabled. */
public final class RemoteAgent {
    private static final long POLL_MS = 2000L;
    private static volatile RemoteAgent instance;
    private final Set<String> handled = new HashSet<>();
    private ScheduledFuture<?> poll;
    private WebSocket socket;
    private long retryAt;

    private RemoteAgent() {}
    public static RemoteAgent get() { if (instance == null) synchronized (RemoteAgent.class) { if (instance == null) instance = new RemoteAgent(); } return instance; }

    public synchronized void start() {
        if (poll != null && !poll.isCancelled()) return;
        poll = Task.scheduler().scheduleWithFixedDelay(this::tick, 0, POLL_MS, TimeUnit.MILLISECONDS);
        RemoteAgentService.start(App.get());
    }

    public synchronized void stop() {
        if (poll != null) poll.cancel(false);
        poll = null;
        if (socket != null) socket.close(1000, "disabled");
        socket = null;
        synchronized (handled) { handled.clear(); }
        RemoteAgentService.stop(App.get());
    }

    public boolean isRunning() { return poll != null && !poll.isCancelled(); }

    private void tick() {
        RemoteModels.Profile profile = RemoteStore.get();
        if (!profile.enabled || TextUtils.isEmpty(profile.serverUrl)) { if (isRunning()) stop(); return; }
        try {
            RemoteClient client = new RemoteClient(profile);
            if (!RemoteStore.hasIdentity()) { client.register(); return; }
            if (socket == null && System.currentTimeMillis() >= retryAt) openSocket(client);
            if (socket != null) return;
            RemoteModels.PollResponse response = client.poll();
            if (response != null && response.command != null) dispatch(client, response.command);
        } catch (Throwable ignored) { retryAt = System.currentTimeMillis() + 5000L; }
    }

    private void openSocket(RemoteClient client) {
        try {
            socket = client.openWebSocket(new WebSocketListener() {
                @Override public void onOpen(WebSocket webSocket, Response response) { webSocket.send(client.hello()); }
                @Override public void onMessage(WebSocket webSocket, String text) {
                    try {
                        JsonObject object = App.gson().fromJson(text, JsonObject.class);
                        if (object != null && "command".equals(string(object, "type")) && object.has("command")) dispatch(client, App.gson().fromJson(object.get("command"), RemoteModels.Command.class));
                    } catch (Throwable ignored) {}
                }
                @Override public void onClosed(WebSocket webSocket, int code, String reason) { socket = null; retryAt = System.currentTimeMillis() + 1000L; }
                @Override public void onFailure(WebSocket webSocket, Throwable t, Response response) { socket = null; retryAt = System.currentTimeMillis() + 1000L; }
            });
        } catch (Throwable ignored) { socket = null; retryAt = System.currentTimeMillis() + 5000L; }
    }

    private void dispatch(RemoteClient client, RemoteModels.Command command) {
        if (command == null || TextUtils.isEmpty(command.id)) return;
        synchronized (handled) {
            if (!handled.add(command.id)) return;
            if (handled.size() > 256) {
                java.util.Iterator<String> iterator = handled.iterator();
                if (iterator.hasNext()) { iterator.next(); iterator.remove(); }
            }
        }
        Task.execute(() -> {
            JsonObject result = RemoteCommandExecutor.execute(command);
            boolean ok = !result.has("error");
            try {
                WebSocket current = socket;
                if (current != null) {
                    JsonObject ack = new JsonObject();
                    ack.addProperty("type", "ack");
                    ack.addProperty("commandId", command.id);
                    ack.addProperty("ok", true);
                    current.send(App.gson().toJson(ack));
                    current.send(client.resultMessage(command.id, ok, result));
                }
                client.result(command.id, ok, result);
            } catch (Throwable ignored) {}
        });
    }

    private static String string(JsonObject object, String key) { return object != null && object.has(key) ? object.get(key).getAsString() : ""; }
}
