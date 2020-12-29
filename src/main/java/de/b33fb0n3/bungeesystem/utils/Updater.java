package de.b33fb0n3.bungeesystem.utils;

import com.google.common.io.Resources;
import de.b33fb0n3.bungeesystem.Bungeesystem;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;

/**
 * Plugin made by B33fb0n3YT
 * 29.12.2020
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

public class Updater {

    private static final String SPIGOT_URL = "https://api.spigotmc.org/legacy/update.php?resource=%d";
    private boolean update = false;

    private Bungeesystem plugin;

    public Updater(Bungeesystem plugin) {
        this.plugin = plugin;
    }

    public int ckeckUpdate() {
        // 1 = neue Version
        // 0 = gleiche Version
        // -1 = wurde nicht gefunden

        String fetchedVersion;
        float floatVersion;
        try {
            URL url = new URL(String.format(SPIGOT_URL, 67179));
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "Chrome/5.0");
            fetchedVersion = Resources.toString(httpURLConnection.getURL(), Charset.defaultCharset());
            floatVersion = Float.parseFloat(fetchedVersion);
            httpURLConnection.disconnect();
            float currentVersion = Float.parseFloat(plugin.getDescription().getVersion());
            if(floatVersion > currentVersion) {
                setUpdate(true);
                return 1;
            } else if(floatVersion == currentVersion) {
                return 0;
            } else
                return -1;
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to get update information" , e);
            return -1;
        }
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
