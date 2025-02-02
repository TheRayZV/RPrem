package org.rprem;

import org.bukkit.configuration.file.FileConfiguration;
import org.rprem.utils.TimeUtils;

import java.util.UUID;

public class SubscriptionManager {

    private final RPremPlugin plugin;
    private final DatabaseManager databaseManager;

    public SubscriptionManager(RPremPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public long getSubscriptionExpiry(UUID uuid) {
        return databaseManager.getSubscriptionExpiry(uuid.toString());
    }

    public void addSubscription(UUID uuid, long duration) {
        long expiry = getSubscriptionExpiry(uuid);
        if (expiry == -1 || expiry == Long.MAX_VALUE) {
            expiry = System.currentTimeMillis();
        }

        if (duration == Long.MAX_VALUE) {
            expiry = Long.MAX_VALUE;
        } else {
            expiry += duration;
        }

        databaseManager.addSubscription(uuid.toString(), expiry);
    }

    public void removeSubscription(UUID uuid) {
        databaseManager.removeSubscription(uuid.toString());
    }

    public String getSubscriptionTimeLeft(UUID uuid) {
        long expiry = getSubscriptionExpiry(uuid);
        if (expiry == -1) {
            return null;
        }

        if (expiry == Long.MAX_VALUE) {
            return plugin.getConfig().getString("messages.lifetime-placeholder", "Навсегда");
        }

        long diff = expiry - System.currentTimeMillis();
        if (diff <= 0) {
            return null;
        }

        return TimeUtils.formatTime(diff);
    }
}