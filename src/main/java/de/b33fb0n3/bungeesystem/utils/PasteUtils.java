package de.b33fb0n3.bungeesystem.utils;

/**
 * Plugin made by B33fb0n3YT
 * 25.07.2019
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */


import de.b33fb0n3.bungeesystem.Bungeesystem;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;

public class PasteUtils {

    private static String pasteURL = "https://www.toptal.com/developers/hastebin/documents";

    /**
     * A simple implementation of the Hastebin Client API, allowing data to be pasted online for
     * players to access.
     *
     * @param urlParameters The string to be sent in the body of the POST request
     * @return A formatted URL which links to the pasted file
     */
    public static String paste(String urlParameters) {
        //Create Client
        HttpClient client = HttpClient.newHttpClient();

        //Create connection
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(pasteURL))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(urlParameters))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String key = response.body();
            String[] keySplit = key.split("\"");
            return "https://www.toptal.com/developers/hastebin/" + keySplit[3];
        } catch (IOException | InterruptedException e) {
            Bungeesystem.logger().log(Level.WARNING, "failed to paste", e);
        }
        return null;
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
//    public static synchronized String getPaste(String ID) {
//        String URLString = pasteURL + "raw/" + ID + "/";
//        try {
//            URL URL = new URL(URLString);
//            HttpURLConnection connection = (HttpURLConnection) URL.openConnection();
//            connection.setDoOutput(true);
//            connection.setConnectTimeout(10000);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String paste = "";
//            while (reader.ready()) {
//                String line = reader.readLine();
//                if (line.contains("package")) continue;
//                if (paste.equals("")) paste = line;
//                else paste = paste + "\n" + line;
//            }
//            return paste;
//        } catch (IOException e) {
//            return "";
//        }
//    }


}
