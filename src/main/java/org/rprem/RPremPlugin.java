package org.rprem;

import org.bukkit.plugin.java.JavaPlugin;

public class RPremPlugin extends JavaPlugin {

    private DatabaseManager databaseManager;
    private SubscriptionManager subscriptionManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        databaseManager = new DatabaseManager(this);
        subscriptionManager = new SubscriptionManager(this, databaseManager);

        getCommand("rprem").setExecutor(new CommandHandler(this, subscriptionManager));

        getServer().getPluginManager().registerEvents(new PlayerListener(this, subscriptionManager), this);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new RPremPlaceholder(this, subscriptionManager).register();
        }
    }

    @Override
    public void onDisable() {
        databaseManager.closeConnection();
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public SubscriptionManager getSubscriptionManager() {
        return subscriptionManager;
    }
}