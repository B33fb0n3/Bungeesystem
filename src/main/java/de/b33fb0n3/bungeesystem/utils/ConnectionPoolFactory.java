package de.b33fb0n3.bungeesystem.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.b33fb0n3.bungeesystem.Bungeesystem;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Plugin made by B33fb0n3YT 26.12.2020 F*CKING SKIDDER! Licensed by B33fb0n3YT © All rights reserved
 */


public class ConnectionPoolFactory {

    private Configuration config;
    private Map<Class<? extends Plugin>, HikariDataSource> dataPools = new HashMap<>();

    public ConnectionPoolFactory(Configuration config) {
        this.config = config;
    }

    public DataSource getPluginDataSource(Plugin plugin) throws SQLException {
        if (dataPools.containsKey(plugin.getClass())) {
            return dataPools.get(plugin.getClass());
        }

        String port = String.valueOf(config.getInt("port"));

        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.mariadb.jdbc.MariaDbDataSource");
        props.setProperty("dataSource.serverName", config.getString("host"));
        props.setProperty("dataSource.portNumber", port);
        props.setProperty("dataSource.user", config.getString("username"));
        props.setProperty("dataSource.password", config.getString("passwort"));
        props.setProperty("dataSource.databaseName", config.getString("datenbank"));

        HikariConfig hikariConfig = new HikariConfig(props);
        hikariConfig.setMaximumPoolSize(10);

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        dataPools.computeIfAbsent(plugin.getClass(), k -> new HikariDataSource(hikariConfig));

        try (Connection conn = dataSource.getConnection()) {
            conn.isValid(5 * 1000);
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "Invalid data for data source. Could not connect.\n" + DBUtil.prettySQLException(e), e);
            dataPools.remove(plugin.getClass());
            throw e;
        }

        Bungeesystem.logger().info("Created new connection pool for Bungeesystem.");

        return dataSource;
    }

    public void shutdown() {
        for (HikariDataSource value : dataPools.values()) {
            value.close();
        }
        Bungeesystem.logger().info("Data Pools closed successfully.");
    }
}
