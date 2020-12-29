package de.b33fb0n3.bungeesystem;


import de.b33fb0n3.bungeesystem.utils.ConnectionPoolFactory;
import net.md_5.bungee.api.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Plugin made by B33fb0n3YT
 * 28.12.2020
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

public class DataSourceProvider {
    private ConnectionPoolFactory factory;

    public DataSourceProvider(ConnectionPoolFactory factory) {
        this.factory = factory;
    }

    /**
     * Get a data source for the plugin.
     * <p>
     * A data source will be valid till the server is shut down. A reload will not invalidate the data source.
     * <p>
     * A plugin can not request multiple data sources.
     *
     * @param plugin plugin which requests the data source
     * @return a new data source for the plugin or a already created data source.
     * @throws SQLException if the data source could not be created for some reason.
     */
    public DataSource getPluginDataSource(Plugin plugin) throws SQLException {
        return factory.getPluginDataSource(plugin);
    }
}
