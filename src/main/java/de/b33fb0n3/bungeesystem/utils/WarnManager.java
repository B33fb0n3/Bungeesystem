package de.b33fb0n3.bungeesystem.utils;

/**
 * Plugin made by B33fb0n3YT
 * 10.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class WarnManager {

    private UUID targetUUID;
    private UUID vonUUID;
    private String grund;
    private long timestamp;
    private Configuration settings;
    private DataSource source;

    public WarnManager(UUID targetUUID, UUID vonUUID, String grund, long timestamp, Configuration settings, DataSource source) {
        this.targetUUID = targetUUID;
        this.vonUUID = vonUUID;
        this.grund = grund;
        this.timestamp = timestamp;
        this.settings = settings;
        this.source = source;
    }

    public void addWarn() {
        new HistoryManager().insertInDB(getTargetUUID(), getVonUUID(), "warn", getGrund(), getTimestamp(), -1, -1, -1);
        String message = (Bungeesystem.Prefix + getSettings().getString("WarnInfo").replace("%player%", UUIDFetcher.getName(getVonUUID())).replace("%target%", UUIDFetcher.getName(getTargetUUID())).replace("%reason%", getGrund())).replace("&", "§");
        Bungeesystem.logger().info(message);

        Playerdata playerdata = new Playerdata(targetUUID);
        playerdata.updatePlayerData("warnsReceive", null);
        playerdata.updatePlayerData("warnsMade", null);

        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
            if ((all.hasPermission("bungeecord.informations") || all.hasPermission("bungeecord.*")) && !all.getName().equalsIgnoreCase(UUIDFetcher.getName(vonUUID)))
                all.sendMessage(new TextComponent(message));
        }
    }

    public void deleteWarn(String id) {
        try (Connection conn = source.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM history WHERE Erstellt = ?");) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not delete warn with id " + id, e);
        }
    }

    public void deleteAllWarns() {
        try (Connection conn = source.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM history WHERE TargetUUID = ? AND Type = ?");) {
            ps.setString(1, getTargetUUID().toString());
            ps.setString(2, "warn");
            ps.executeUpdate();
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not delete all warns", e);
        }
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

    public UUID getVonUUID() {
        return vonUUID;
    }

    public String getGrund() {
        return grund;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Configuration getSettings() {
        return settings;
    }

}
