package org.rprem;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.rprem.utils.TimeUtils;

import java.util.UUID;

public class CommandHandler implements CommandExecutor {

    private final RPremPlugin plugin;
    private final SubscriptionManager subscriptionManager;

    public CommandHandler(RPremPlugin plugin, SubscriptionManager subscriptionManager) {
        this.plugin = plugin;
        this.subscriptionManager = subscriptionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(plugin.getConfig().getString("messages.invalid-command"));
            return true;
        }

        String action = args[0];
        String playerName = args[1];

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);

            if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
                sender.sendMessage("Игрок " + playerName + " не найден в базе данных.");
                return;
            }

            UUID playerUUID = offlinePlayer.getUniqueId();

            switch (action.toLowerCase()) {
                case "add" -> {
                    if (args.length < 3) {
                        sender.sendMessage(plugin.getConfig().getString("messages.invalid-command"));
                        return;
                    }

                    String time = args[2];
                    long duration = TimeUtils.parseTime(time);

                    if (duration == -1) {
                        sender.sendMessage(plugin.getConfig().getString("messages.invalid-time-format"));
                        return;
                    }

                    subscriptionManager.addSubscription(playerUUID, duration);
                    sender.sendMessage(plugin.getConfig().getString("messages.subscription-added")
                            .replace("%player%", playerName)
                            .replace("%time%", time));
                }
                case "rem" -> {
                    subscriptionManager.removeSubscription(playerUUID);
                    sender.sendMessage(plugin.getConfig().getString("messages.subscription-removed")
                            .replace("%player%", playerName));
                }
                default -> sender.sendMessage(plugin.getConfig().getString("messages.invalid-command"));
            }
        });

        return true;
    }
}
