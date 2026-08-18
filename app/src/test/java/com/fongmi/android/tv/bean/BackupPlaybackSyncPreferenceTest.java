package com.fongmi.android.tv.bean;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BackupPlaybackSyncPreferenceTest {

    @Test
    public void playbackSyncPreferencesFollowSettingsOption() {
        SyncOptions settings = new SyncOptions().config(false).spider(false).settings(true);
        SyncOptions spider = new SyncOptions().config(false).spider(true).settings(false);

        assertTrue(Backup.include("viewing_record_sync_enabled", settings));
        assertTrue(Backup.include("viewing_record_sync_local_write", settings));
        assertTrue(Backup.include("playback_remote_sync_config", settings));
        assertTrue(Backup.include("playback_webhook_config", settings));
        assertFalse(Backup.include("playback_webhook_privacy_accepted", settings));
        assertFalse(Backup.include("playback_remote_sync_config", spider));
    }

    @Test
    public void exportedPlaybackConfigsNeverContainSecretsOrHeaders() {
        String source = "[{\"name\":\"sync\",\"url\":\"https://example.test\",\"token\":\"plain\",\"secret\":\"hidden\",\"headers\":{\"Authorization\":\"Bearer leak\"},\"enabled\":true}]";

        String redacted = (String) Backup.redact("playback_remote_sync_config", source);
        JsonArray configs = JsonParser.parseString(redacted).getAsJsonArray();
        JsonObject config = configs.get(0).getAsJsonObject();

        assertEquals("sync", config.get("name").getAsString());
        assertFalse(config.get("enabled").getAsBoolean());
        assertFalse(config.has("token"));
        assertFalse(config.has("secret"));
        assertFalse(config.has("headers"));
        assertFalse(redacted.contains("plain"));
        assertFalse(redacted.contains("Bearer leak"));
    }

    @Test
    public void fullBackupSetterAlsoRedactsPlaybackSecrets() {
        Backup backup = new Backup();
        backup.setPrefers(Map.of("playback_webhook_config", "[{\"url\":\"https://example.test\",\"token\":\"plain\"}]"));

        String exported = (String) backup.getPrefers().get("playback_webhook_config");

        assertFalse(exported.contains("plain"));
        assertFalse(JsonParser.parseString(exported).getAsJsonArray().get(0).getAsJsonObject().has("token"));
    }

    @Test
    public void malformedSensitiveConfigIsExcludedSafely() {
        assertEquals("[]", Backup.redact("playback_webhook_config", "not-json"));
    }
}
