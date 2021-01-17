package de.b33fb0n3.bungeesystem.utils;

import de.b33fb0n3.bungeesystem.Bungeesystem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Plugin made by B33fb0n3YT
 * 28.12.2020
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

public final class DBUtil {
    public static String prettySQLException(SQLException ex) {
        return "SQLException: " + ex.getMessage() + "\n"
                + "SQLState: " + ex.getSQLState() + "\n"
                + "VendorError: " + ex.getErrorCode();
    }

    public static String rowToString(ResultSet rs) throws SQLException {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            list.add(rs.getString(i));
        }

        return String.join(", ", list);
    }

    public static int getWhatCount(DataSource source, UUID player, String type) {
        try (Connection conn = source.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) AS count FROM history WHERE TargetUUID = ? AND Type = ? ORDER BY ERSTELLT DESC");) {
            ps.setString(1, player.toString());
            ps.setString(2, type);
            ResultSet rs = ps.executeQuery();
            while (rs.first())
                return rs.getInt(1);
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not count for " + type, e);
        }
        return -1;
    }

    public static boolean timeExists(DataSource source, long erstellt) {
        try (Connection conn = source.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM bannedPlayers WHERE TimeStamp = ?")) {
            ps.setLong(1, erstellt);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not check if timestamp exists", e);
        }
        return false;
    }
}
