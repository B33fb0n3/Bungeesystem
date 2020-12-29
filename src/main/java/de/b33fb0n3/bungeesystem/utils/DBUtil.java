package de.b33fb0n3.bungeesystem.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
