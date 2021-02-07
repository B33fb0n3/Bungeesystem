package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 31.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.Cooldowns;
import de.b33fb0n3.bungeesystem.utils.PasteUtils;
import de.b33fb0n3.bungeesystem.utils.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public class ChatLog extends Command {

    public ChatLog(String name) {
        super(name);
    }

    private ArrayList<String> messages = new ArrayList<>();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer pp = (ProxiedPlayer) sender;
            if (pp.hasPermission("bungeecord.chatlogs.create") || pp.hasPermission("bungeecord.*")) {
                if (args.length == 1) {
                    UUID ut = UUIDFetcher.getUUID(args[0]);
                    if (ut == null) {
                        sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Gebe einen richtigen Spieler an!"));
                        return;
                    }
                    try (Connection conn = Bungeesystem.getPlugin().getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM chat WHERE uuid = '" + ut.toString() + "';")) {
                        if (!ps.executeQuery().next()) {
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Dieser Spieler hat keine aktuellen Nachrichten!"));
                            return;
                        }
                    } catch (SQLException e) {
                        Bungeesystem.logger().log(Level.WARNING, "could not check if player has messages", e);
                        return;
                    }
                    Cooldowns cooldowns = new Cooldowns("Chatlog", pp);
                    if (cooldowns.isOnCooldown()) {
                        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Chatlog.Oncooldown"))));
                        return;
                    }
                    pp.sendMessage(new TextComponent(Bungeesystem.Prefix + ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Chatlog.created"))));
                    fillArraylist(ut);
                    String url = PasteUtils.paste(String.join("\n", messages));
                    if (Bungeesystem.settings.getBoolean("Chatlog.userGetLink")) {
                        TextComponent tc = new TextComponent();
                        tc.setText(Bungeesystem.Prefix + ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Chatlog.userGetLinkMessage").replace("%url%", url)));
                        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7(§cClick§7)")));
                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                        pp.sendMessage(tc);
                    }
                    if (Bungeesystem.settings.getBoolean("Cooldown.Chatlog.aktive"))
                        cooldowns.startCooldown();
                    for (ProxiedPlayer current : ProxyServer.getInstance().getPlayers()) {
                        if (current.hasPermission("bungeecord.chatlog.see") || current.hasPermission("bungeecord.*")) {
                            TextComponent tc = new TextComponent();
                            tc.setText(Bungeesystem.Prefix + "Der Spieler " + Bungeesystem.herH + pp.getName() + Bungeesystem.normal + " hat ein Chatlog erstellt! " + Bungeesystem.other2 + "(");

                            TextComponent tc1 = new TextComponent();
                            tc1.setText(Bungeesystem.fehler + url);
                            tc1.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                            tc.addExtra(tc1);

                            TextComponent tc2 = new TextComponent();
                            tc2.setText(Bungeesystem.other2 + ")");
                            tc.addExtra(tc2);
                            current.sendMessage(tc);
                        }
                    }
                } else
                    pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutze: " + Bungeesystem.other + "/chatlog <Spieler>"));
            } else
                pp.sendMessage(new TextComponent(Bungeesystem.noPerm));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist kein Spieler!"));
    }

    private void fillArraylist(UUID uuid) {
        messages.clear();
        messages.add("[--------- DIE LETZTEN 5 MIN SIND HIER AUFGELISTET! ---------]");
        long lastMessage = 0;
        try (Connection conn = Bungeesystem.getPlugin().getDataSource().getConnection(); PreparedStatement ps1 = conn.prepareStatement("SELECT timestamp FROM chat WHERE uuid = ? ORDER BY TIMESTAMP DESC LIMIT 1")) {
            ps1.setString(1, uuid.toString());
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                lastMessage = rs1.getLong("timestamp");
            }
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not get last message timestamp", e);
        }

        try (Connection conn = Bungeesystem.getPlugin().getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM chat WHERE uuid = ?");) {

            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            long millis = 300000;
            long vor15Min = lastMessage - millis;

            while (rs.next()) {
                if (rs.getLong("timestamp") > vor15Min) {
                    try (Connection conn1 = Bungeesystem.getPlugin().getDataSource().getConnection();
                         PreparedStatement ps2 = conn1.prepareStatement("DELETE FROM chat WHERE timestamp = ?");) {
                        ps2.setLong(1, rs.getLong("timestamp"));
                        ps2.executeUpdate();
                    } catch (SQLException e) {
                        Bungeesystem.logger().log(Level.WARNING, "could not delete message from player", e);
                    }
                    messages.add("[" + new Time(rs.getLong("timestamp")).toString() + " / " + rs.getString("server") + "] " + UUIDFetcher.getName(UUID.fromString(rs.getString("uuid"))) + ": " + rs.getString("message"));
                }
            }
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not ");
        }
        messages.add("[--------------------------- JETZT --------------------------]");
    }

//    private static String newHaste(String contents) {
//        HttpURLConnection connection = null;
//        try {
//            connection = (HttpURLConnection) new URL("https://hastebin.com/documents").openConnection();
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//            connection.setRequestProperty("user-agent", "Java/HastebinAPI");
//            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
//            writer.write(contents);
//            writer.flush();
//            writer.close();
//            JsonParser parser = new JsonParser();
//            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
//            String key = parser.parse(reader).getAsJsonObject().get("key").getAsString();
//            reader.close();
//            connection.disconnect();
//            return "https://hastebin.com/" + key;
//        } catch (IOException e) {
//            assert connection != null;
//            connection.disconnect();
//            e.printStackTrace();
//        }
//        return "Fehler";
//    }

}
