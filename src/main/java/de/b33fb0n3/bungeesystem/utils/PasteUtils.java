package de.b33fb0n3.bungeesystem.utils;

/**
 * Plugin made by B33fb0n3YT
 * 25.07.2019
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */


import com.google.gson.JsonParser;
import de.b33fb0n3.bungeesystem.Bungeesystem;
import net.md_5.bungee.api.ProxyServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

public class PasteUtils {

    private static String pasteURL = "https://hasteb.in/";

    /**
     * A simple implementation of the Hastebin Client API, allowing data to be pasted online for
     * players to access.
     *
     * @param urlParameters The string to be sent in the body of the POST request
     * @return A formatted URL which links to the pasted file
     */
    public synchronized static String paste(String urlParameters) {
        final HttpURLConnection[] connection = {null};
        final BufferedReader[] rd = {null};
        final boolean[] error = {false};
        ProxyServer.getInstance().getScheduler().runAsync(Bungeesystem.getPlugin(), new Runnable() {
            @Override
            public void run() {
                try {
                    //Create connection
                    URL url = new URL(pasteURL + "documents");
                    connection[0] = (HttpURLConnection) url.openConnection();
                    connection[0].setRequestMethod("POST");
                    connection[0].setDoInput(true);
                    connection[0].setDoOutput(true);

                    //Send request
                    DataOutputStream wr = new DataOutputStream(connection[0].getOutputStream());
                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();

                    //Get Response
                    rd[0] = new BufferedReader(new InputStreamReader(connection[0].getInputStream()));
                } catch (IOException e) {
                    error[0] = true;
                } finally {
                    if (connection[0] == null)
                        error[0] = true;
                    assert connection[0] != null;
                    connection[0].disconnect();
                }
            }
        });
        if (error[0])
            return null;
        try {
            JsonParser parser = new JsonParser();
            //            return pasteURL + new JSONObject(rd.readLine()).getString("key");
            return pasteURL + parser.parse(rd[0]).getAsJsonObject().get("key").getAsString();
        } catch (NullPointerException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not create paste link", e);
        }
        return "error";
    }

    /**
     * Returns the URL of the server being used.
     *
     * @return API to use for posting data
     */
    public static String getPasteURL() {
        return pasteURL;
    }

    /**
     * Sets the URL used by the paste method, allowing for the server logs are pasted to to be
     * dynamically changed.
     *
     * @param URL API URL of HasteBin instance
     */
    public static void setPasteURL(String URL) {
        pasteURL = URL;
    }

    /**
     * Grabs a HasteBin file from the internet and attempts to return the file with formatting
     * intact.
     *
     * @return String HasteBin Raw Text
     */
    public static synchronized String getPaste(String ID) {
        String URLString = pasteURL + "raw/" + ID + "/";
        try {
            URL URL = new URL(URLString);
            HttpURLConnection connection = (HttpURLConnection) URL.openConnection();
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String paste = "";
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.contains("package")) continue;
                if (paste.equals("")) paste = line;
                else paste = paste + "\n" + line;
            }
            return paste;
        } catch (IOException e) {
            return "";
        }
    }


}
