package de.b33fb0n3.bungeesystem.utils;

import de.b33fb0n3.bungeesystem.Bungeesystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Plugin made by B33fb0n3YT
 * 23.10.2019
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

public class HistoryManager {

    public HistoryManager() {
    }

    public void insertInDB(UUID targetUUID, UUID vonUUID, String type, String grund, long erstellt, long bis, int perma, int ban) {
        try (Connection conn = Bungeesystem.getPlugin().getDataSource().getConnection(); PreparedStatement createReport = conn.prepareStatement("INSERT INTO history (TargetUUID,VonUUID,Type,Grund,Erstellt,Bis,Perma,Ban) VALUES(?,?,?,?,?,?,?,?)");) {
            createReport.setString(1, targetUUID.toString());
            createReport.setString(2, vonUUID.toString());
            createReport.setString(3, type);
            createReport.setString(4, grund);
            createReport.setLong(5, erstellt);
            createReport.setLong(6, bis == -1 ? -2 : bis);
            createReport.setInt(7, perma == -1 ? -2 : perma);
            createReport.setInt(8, ban == -1 ? -2 : ban);
            createReport.executeUpdate();
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "cannot insert report into db", e);
        }
    }

    public List<HistoryElemt> readHistory(UUID target, int limit, int page, String what, boolean lastOnes) {
        List<HistoryElemt> reports = new LinkedList<>();
        page = page * 10 - 10;
        String sql = "SELECT * FROM history " + (target == null ? "" : "WHERE TargetUUID = ?") + (lastOnes ? "" : " AND Type = ? ") + "ORDER BY Erstellt DESC LIMIT ? OFFSET ?";
        try (Connection conn = Bungeesystem.getPlugin().getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (target != null)
                ps.setString(1, target.toString());
            if (!lastOnes)
                ps.setString(2, what);
            ps.setInt(lastOnes ? (target == null ? 1 : 2) : 3, limit);
            ps.setInt(lastOnes ? (target == null ? 2 : 3) : 4, page);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reports.add(new HistoryElemt(UUID.fromString(rs.getString("TargetUUID")), UUID.fromString(rs.getString("VonUUID")), rs.getString("Type"), rs.getString("Grund"), rs.getLong("Erstellt"), rs.getLong("Bis"), rs.getInt("Perma"), rs.getInt("Ban"), rs.getString("VonEntbannt")));
            }
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "could net read history elmts", e);
            return null;
        }
        return reports;
    }
}
