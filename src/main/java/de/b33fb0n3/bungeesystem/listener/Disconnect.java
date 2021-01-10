package de.b33fb0n3.bungeesystem.listener;

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.Onlinezeit;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

/**
 * Plugin made by B33fb0n3YT
 * 10.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

public class Disconnect implements Listener {

    private DataSource source;

    public Disconnect(Plugin plugin, DataSource source) {
        this.source = source;
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e) {
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Europe/Berlin"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        new Onlinezeit(e.getPlayer().getUniqueId(), date.format(formatter), source).leave();
        try (Connection conn = source.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE playerdata SET lastOnline = ? WHERE UUID = ?");) {
            ps.setLong(1, System.currentTimeMillis());
            ps.setString(2, e.getPlayer().getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e1) {
            Bungeesystem.logger().log(Level.WARNING, "cloud not update lastonline for a player", e1);
        }
    }

}
