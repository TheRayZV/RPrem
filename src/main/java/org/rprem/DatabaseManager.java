package org.rprem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private final RPremPlugin plugin;
    private Connection connection;

    public DatabaseManager(RPremPlugin plugin) {
        this.plugin = plugin;
        initializeDatabase();
    }

    private void initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/" + plugin.getConfig().getString("database.file"));
            connection.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS subscriptions (" +
                            "uuid TEXT PRIMARY KEY, " +
                            "expiry BIGINT)"
            );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public long getSubscriptionExpiry(String uuid) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT expiry FROM subscriptions WHERE uuid = ?");
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getLong("expiry");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void addSubscription(String uuid, long expiry) {
        try {
            PreparedStatement stmt = connection.prepareStatement("REPLACE INTO subscriptions (uuid, expiry) VALUES (?, ?)");
            stmt.setString(1, uuid);
            stmt.setLong(2, expiry);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeSubscription(String uuid) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM subscriptions WHERE uuid = ?");
            stmt.setString(1, uuid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}