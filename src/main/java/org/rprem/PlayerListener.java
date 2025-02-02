package org.rprem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final RPremPlugin plugin;
    private final SubscriptionManager subscriptionManager;

    public PlayerListener(RPremPlugin plugin, SubscriptionManager subscriptionManager) {
        this.plugin = plugin;
        this.subscriptionManager = subscriptionManager;

        startSubscriptionCheckTask();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        checkSubscription(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    }

    private void checkSubscription(Player player) {
        UUID playerUUID = player.getUniqueId();
        long expiry = subscriptionManager.getSubscriptionExpiry(playerUUID);

        if (expiry == -1) {
            player.kickPlayer(plugin.getConfig().getString("messages.no-subscription"));
        } else if (expiry < System.currentTimeMillis() && expiry != Long.MAX_VALUE) {
            player.kickPlayer(plugin.getConfig().getString("messages.subscription-expired"));
        }
    }

    private void startSubscriptionCheckTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                checkSubscription(player);
            }
        }, 0L, 1200L);
    }
}