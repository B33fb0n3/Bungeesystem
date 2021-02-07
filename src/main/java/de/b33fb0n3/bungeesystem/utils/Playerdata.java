package de.b33fb0n3.bungeesystem.utils;

import de.b33fb0n3.bungeesystem.Bungeesystem;
import net.md_5.bungee.api.ProxyServer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Plugin made by B33fb0n3YT
 * 01.04.2020
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

public class Playerdata {

    private UUID uuid;
    private String name;
    private String lastip;
    private long firstjoin;
    private long lastonline;
    private int bansMade;
    private int warnsMade;
    private int reportsMade;
    private int bansReceive;
    private int warnsReceive;
    private int power;
    private DataSource source;

    public Playerdata(UUID uuid) {
        this.uuid = uuid;
        this.source = Bungeesystem.getPlugin().getDataSource();
        loadData();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastip() {
        return lastip;
    }

    public void setLastip(String lastip) {
        this.lastip = lastip;
    }

    public long getFirstjoin() {
        return firstjoin;
    }

    public void setFirstjoin(long firstjoin) {
        this.firstjoin = firstjoin;
    }

    public long getLastonline() {
        return lastonline;
    }

    public void setLastonline(long lastonline) {
        this.lastonline = lastonline;
    }

    public int getBansMade() {
        return bansMade;
    }

    public void setBansMade(int bansMade) {
        this.bansMade = bansMade;
    }

    public int getWarnsMade() {
        return warnsMade;
    }

    public void setWarnsMade(int warnsMade) {
        this.warnsMade = warnsMade;
    }

    public int getReportsMade() {
        return reportsMade;
    }

    public void setReportsMade(int reportsMade) {
        this.reportsMade = reportsMade;
    }

    public int getBansReceive() {
        return bansReceive;
    }

    public void setBansReceive(int bansReceive) {
        this.bansReceive = bansReceive;
    }

    public int getWarnsReceive() {
        return warnsReceive;
    }

    public void setWarnsReceive(int warnsReceive) {
        this.warnsReceive = warnsReceive;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void loadData() {
        try (Connection conn = source.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM playerdata WHERE UUID = ?")) {
            ps.setString(1, getUuid().toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                setName(rs.getString("Name"));
                setLastip(rs.getString("lastIP"));
                setFirstjoin(rs.getLong("firstJoin"));
                setLastonline(rs.getLong("lastOnline"));
                setBansMade(rs.getInt("bansMade"));
                setWarnsMade(rs.getInt("warnsMade"));
                setReportsMade(rs.getInt("reportsMade"));
                setBansReceive(rs.getInt("bansReceive"));
                setWarnsReceive(rs.getInt("warnsReceive"));
                setPower(rs.getInt("power"));
            }
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not load playerdata", e);
        }
    }

    public void createPlayer(UUID target, String ip, String name) {
        setUuid(target);
        setName(name);
        try (Connection conn = source.getConnection(); PreparedStatement ps = conn.prepareStatement("INSERT INTO playerdata (UUID,Name,lastIP,firstJoin,lastOnline,bansMade,warnsMade,reportsMade,bansReceive,warnsReceive, power) VALUES(?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE UUID=UUID")) {
            ps.setString(1, getUuid().toString()); // uuid
            ps.setString(2, getName()); // name
            ps.setString(3, ip == null ? null : ip.replace("/", "").split(":")[0]); // lastIP
            ps.setLong(4, System.currentTimeMillis()); // firstJoin
            ps.setLong(5, -1); // lastOnline
            ps.setInt(6, 0); // bansMade
            ps.setInt(7, 0); // warnsMade
            ps.setInt(8, 0); // reportsMade
            ps.setInt(9, 0); //  bansReceived
            ps.setInt(10, 0); // warnsReceived
            ps.setLong(11, 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not create playerdata", e);
        }
    }

    public void updatePlayerData(String what, String ip) {
        String value = null;
        switch (what.toLowerCase()) {
            case "bansmade":
                value = String.valueOf((this.getBansMade() + 1));
                break;
            case "warnsmade":
                value = String.valueOf((this.getWarnsMade() + 1));
                break;
            case "reportsmade":
                value = String.valueOf((this.getReportsMade() + 1));
                break;
            case "bansreceive":
                value = String.valueOf((this.getBansReceive() + 1));
                break;
            case "warnsreceive":
                value = String.valueOf((this.getWarnsReceive() + 1));
                break;
            case "lastip":
                value = ip;
                break;
        }
        try (Connection conn = source.getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE playerdata SET " + what + " = ? WHERE UUID = ?")) {
            ps.setString(1, value);
            ps.setString(2, this.getUuid().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not update playerdata", e);
        }
    }
}
