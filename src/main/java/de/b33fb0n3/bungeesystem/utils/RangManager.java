package de.b33fb0n3.bungeesystem.utils;

import de.b33fb0n3.bungeesystem.Bungeesystem;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Plugin made by B33fb0n3YT
 * 04.08.2019
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

public class RangManager {

    private ProxiedPlayer pp;
    private DataSource source;

    public RangManager(ProxiedPlayer pp, DataSource source) {
        this.pp = pp;
        this.source = source;
    }

    public void calcUpdatePower() {
        long highestPower = 0;
        for (String rang : Bungeesystem.raenge.getSection("Raenge").getKeys()) {
            long RangPower = Bungeesystem.raenge.getLong("Raenge." + rang + ".Power");
            String perm = Bungeesystem.raenge.getString("Raenge." + rang + ".Permission") == null ? "" : Bungeesystem.raenge.getString("Raenge." + rang + ".Permission");
            if (getPp().hasPermission(perm)) {
                setPower(RangPower);
                return;
            }
            if (highestPower < RangPower) {
                highestPower = RangPower;
            }
        }
        if (pp.hasPermission("bungeecord.*")) {
            setPower(highestPower);
            return;
        }
        setPower(0L);
    }

    public Long getPower(UUID target) {
        Playerdata playerdata = new Playerdata(target);
        return Long.parseLong(String.valueOf(playerdata.getPower()));
    }

    private void setPower(Long power) {
        try (Connection conn = source.getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE playerdata SET power = ? WHERE UUID = ?")) {
            ps.setLong(1, power);
            ps.setString(2, getPp().getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not set power", e);
        }
    }

    public ProxiedPlayer getPp() {
        return pp;
    }
}
