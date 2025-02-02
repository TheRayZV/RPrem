package org.rprem;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class RPremPlaceholder extends PlaceholderExpansion {

    private final RPremPlugin plugin;
    private final SubscriptionManager subscriptionManager;

    public RPremPlaceholder(RPremPlugin plugin, SubscriptionManager subscriptionManager) {
        this.plugin = plugin;
        this.subscriptionManager = subscriptionManager;
    }

    @Override
    public String getIdentifier() {
        return "rprem";
    }

    @Override
    public String getAuthor() {
        return "TheRayZV";
    }

    @Override
    public String getVersion() {
        return "1.1";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.equals("time")) {
            String timeLeft = subscriptionManager.getSubscriptionTimeLeft(player.getUniqueId());
            return timeLeft != null ? timeLeft : "Нет подписки";
        }

        return null;
    }

}