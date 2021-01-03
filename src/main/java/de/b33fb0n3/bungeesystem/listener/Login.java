package de.b33fb0n3.bungeesystem.listener;

/**
 * Plugin made by B33fb0n3YT
 * 03.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.Ban;
import de.b33fb0n3.bungeesystem.utils.Onlinezeit;
import de.b33fb0n3.bungeesystem.utils.Playerdata;
import de.b33fb0n3.bungeesystem.utils.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import sun.font.TextRecord;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Login implements Listener {

    private DataSource source;
    private Configuration settings;
    private Configuration standardBans;

    public Login(Plugin plugin, DataSource source, Configuration settings, Configuration standardBans) {
        this.source = source;
        this.settings = settings;
        this.standardBans = standardBans;
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    //    private List<String> exception = Arrays.asList("IllegalArgumentException : Team viaversion already exists in this scoreboard @ com.google.common.base.Preconditions:" + new Random().nextInt(1000)
//    , "Internal Exception: io.netty.handler.codec.DecoderException: java.lang.IndexOutOfBoundsException: readerIndex(" + new Random().nextInt(100) + ") + length(1) exceeds writerIndex(57): UnpooledHeapByteBuf(ridx: " + new Random().nextInt(100) + ", widx: " + new Random().nextInt(100) + ", cap: " + new Random().nextInt(100)
//    , "A fatal error has occured, this connection is terminated"
//    , "Internal Exception: io.netty.handler.codec.DecoderException: Badly compressed packet - size of " + new Random().nextInt(100) + " is below server threshold of 256"
//    , );

    private List<String> offeneUmfragen = Arrays.asList("https://strawpoll.de/ga4gcc1", "https://strawpoll.de/99f2gsz", "https://strawpoll.de/6bc93a5");

    @EventHandler
    public void onLogin(LoginEvent e) {
        UUID target = e.getConnection().getUniqueId();


        new Playerdata(target).createPlayer(target, e.getConnection().getSocketAddress().toString(), e.getConnection().getName());
        Ban ban = new Ban(e.getConnection().getUniqueId(), e.getConnection().getSocketAddress().toString().replace("/", "").split(":")[0], source, settings, standardBans);
        if ((ban.isBanned() && ban.getBan() == 1) || ban.containsIP() == 1) {
            ArrayList<String> banArray = new ArrayList<>();
            int i = 1;
            banArray.add(Bungeesystem.fehler + "Du wurdest IP gebannt!\n" + Bungeesystem.normal + "IP: " + Bungeesystem.herH + ban.getIp());
            while (true) {
                try {
                    String line = ChatColor.translateAlternateColorCodes('&', settings.getString("BanMessage.line" + i)).replace("%von%", ban.getVonName()).replace("%grund%", ban.getGrund()).replace("%bis%", (ban.getBis()) == -1 ? Bungeesystem.fehler + "Permanent" : Bungeesystem.formatTime(ban.getBis())).replace("%beweis%", ban.getBeweis() == null ? "/" : ban.getBeweis());
                    banArray.add(line);
                    i++;
                    if (i > settings.getInt("BanMessage.lines")) {
                        banArray.remove(0);
                        break;
                    }
                } catch (Exception e1) {
                    Bungeesystem.logger().log(Level.WARNING, "could not create ban message", e);
                    break;
                }
            }
            if (banArray.size() == 1) {
                if (e.getConnection().getUniqueId().toString().equalsIgnoreCase("40e4b71e-1c11-48ba-89e5-6b1b573de655"))
                    return;
                Ban altAccountBan = new Ban(e.getConnection().getUniqueId(), null, source, settings, standardBans);
                altAccountBan.banByStandard(1, e.getConnection().getSocketAddress().toString().replace("/", "").split(":")[0]);
                e.getConnection().disconnect(new TextComponent(ChatColor.translateAlternateColorCodes('&', settings.getString("Ban.Disconnectmessage").replace("%reason%", altAccountBan.getGrund()).replace("%absatz%", "\n"))));
                return;
            }
            e.getConnection().disconnect(new TextComponent(ChatColor.translateAlternateColorCodes('&', String.join("\n", banArray))));
        }
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Europe/Berlin"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        new Onlinezeit(target, date.format(formatter), source).createNew(e.getConnection().getName());
        UUIDFetcher.getName(e.getConnection().getUniqueId());
        UUIDFetcher.getUUID(UUIDFetcher.getName(e.getConnection().getUniqueId()));
        clearMessages();
        updateBans();
        updateIP(target, e.getConnection().getSocketAddress().toString());
    }

    //    @EventHandler
//    public void onPostLogin(PostLoginEvent e) {
//        ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(e.getPlayer().getUniqueId());
//        if (pp.getUniqueId().toString().equalsIgnoreCase("40e4b71e-1c11-48ba-89e5-6b1b573de655") || pp.getUniqueId().toString().equalsIgnoreCase("a8ffeb72-01eb-412c-bd8c-c85927be31ee")) {
//            TextComponent tc = new TextComponent();
//            ProxyServer.getInstance().getScheduler().schedule(Main.getPlugin(), new Runnable() {
//                @Override
//                public void run() {
//                    tc.setText(Main.Prefix + "Dieser Server benutzt dein Ban/Mute Plugin!");
//                    pp.sendMessage(tc);
//                }
//            }, 2, TimeUnit.SECONDS);
//        }
//        if (pp.hasPermission("bungeecord.report.autologin")) {
//            sendReports.add(pp.getName());
//            pp.sendMessage(Main.Prefix + "Du hast dich eingeloggt " + Main.other2 + "(" + Main.herH + "Reports" + Main.other2 + ")");
//        }
//        if (pp.hasPermission("bungeecord.tc.autologin")) {
//            if (settings.getBoolean("Toggler.chat.teamchat")) {
//                sendTC.add(pp.getName());
//                pp.sendMessage(Main.Prefix + "Du hast dich eingeloggt " + Main.other2 + "(" + Main.herH + "Teamchat" + Main.other2 + ")");
//            }
//        }
//        setup();
//        if (settings.getBoolean("Toggler.power"))
//            RangUtils.calcUpdatePower(pp);
//        if (Main.update) {
//            if (pp.hasPermission("bungeecord.informations") || pp.hasPermission("bungeecord.*")) {
//                pp.sendMessage(Main.other2 + "§m------------" + Main.other2 + "[ §dUPDATE " + Main.other2 + "]§m---------------");
//                TextComponent tc = new TextComponent();
//                tc.setText(Main.Prefix + Main.herH + "AdvancedBungeeSystem " + Main.normal + "wurde geupdated! §f» ");
//                TextComponent tc1 = new TextComponent();
//                tc1.setText(Main.other2 + "[" + Main.fehler + "KLICK" + Main.other2 + "]");
//                tc1.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/bungeesystem-%E2%98%85-ban-mute-report-warn-kick-%E2%98%85-mysql.67179/updates"));
//                tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Main.normal + "Lade dir das Plugin herunter!").create()));
//                tc.addExtra(tc1);
//                pp.sendMessage(tc);
//                pp.sendMessage(Main.other2 + "§m------------" + Main.other2 + "[ §dUPDATE " + Main.other2 + "]§m---------------");
//            }
//        }
////        if (offeneUmfragen.size() > 0) { MIT Zeit einbauen, sodass die ersten 30 Tage es angezeigt wird und danach nicht mehr.
////            if (pp.hasPermission("bungeecord.informations") || pp.hasPermission("bungeecord.*")) {
////                pp.sendMessage(Main.Prefix + Main.other2 + "--- " + Main.fehler + "ACHTUNG " + Main.other2 + "---");
////                pp.sendMessage(Main.Prefix + "Diese Umfragen sind noch offen: ");
////                for (String link : offeneUmfragen) {
////                    pp.sendMessage(Main.Prefix + Main.herH + link);
////                }
////                pp.sendMessage(Main.Prefix + "Solltest du diese bereits beantwortet haben, dann kannst du diese Nachricht ignorieren!");
////                pp.sendMessage(Main.Prefix + Main.other2 + "--- " + Main.fehler + "ACHTUNG " + Main.other2 + "---");
////            }
////        }
//    }
//
    private void updateBans() {
        try (Connection conn = source.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM bannedPlayers")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getLong("Bis") != -1L) {
                    long bis = rs.getLong("Bis");
                    if (System.currentTimeMillis() > bis) {
                        new Ban(UUIDFetcher.getUUID(rs.getString("TargetName")), null, source, settings, standardBans).unban(false, "PLUGIN (expired)");
                    }
                }
            }
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "cloud not clean up the bans", e);
        }
    }

    private void updateIP(UUID uuid, String ip) {
        ip = ip.replace("/", "").split(":")[0];
        try (Connection conn = source.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE bannedPlayers SET ip = ? WHERE TargetUUID = ?")) {
            ps.setString(1, ip);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
            new Playerdata(uuid).updatePlayerData("lastIP", ip);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearMessages() {
        try (Connection conn = source.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT timestamp FROM chat");
             PreparedStatement ps1 = conn.prepareStatement("DELETE FROM chat WHERE timestamp = ?")) {
            ResultSet rs = ps.executeQuery();
            long currentTime = System.currentTimeMillis();
            long last15Min = currentTime - 3600000;
            while (rs.next()) {
                if (rs.getLong("timestamp") < last15Min) {
                    ps1.setLong(1, rs.getLong("timestamp"));
                    ps1.executeUpdate();
                }
            }
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not clear messages", e);
        }
    }

//    private void editSpalte(String name, boolean remove, String tabelle) {
//        try {
//            PreparedStatement ps;
//            if (remove)
//                ps = MySQL.getCon().prepareStatement("ALTER TABLE " + tabelle + " DROP " + name + ";");
//            else
//                ps = MySQL.getCon().prepareStatement("ALTER TABLE " + tabelle + " ADD " + name + " VARCHAR(64) NULL");
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private boolean spalteExits(String columnName, String tatbelle) {
//        try (PreparedStatement statement = MySQL.getCon().prepareStatement("SELECT * FROM " + tatbelle + " LIMIT 1")) {
//            ResultSet set = statement.executeQuery();
//            ResultSetMetaData data = set.getMetaData();
//            for (int i = 1; i <= data.getColumnCount(); i++) {
//                if (data.getColumnName(i).equals(columnName)) {
//                    return true;
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    private void sendToBannedPlayers() {
//        try {
//            PreparedStatement ps = MySQL.getCon().prepareStatement("SELECT * FROM banned");
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                try {
//                    PreparedStatement createReport = MySQL.getCon().prepareStatement("INSERT INTO bannedPlayers (TargetUUID,TargetName,VonUUID,VonName,Grund,TimeStamp,Bis,Perma,Ban,ip) VALUES(?,?,?,?,?,?,?,?,?,?)");
//                    createReport.setString(1, rs.getString("TargetUUID"));
//                    createReport.setString(2, rs.getString("TargetName"));
//                    createReport.setString(3, rs.getString("VonUUID"));
//                    createReport.setString(4, rs.getString("VonName"));
//                    createReport.setString(5, rs.getString("Grund"));
//                    createReport.setLong(6, rs.getLong("TimeStamp"));
//                    createReport.setString(7, rs.getString("Bis"));
//                    createReport.setInt(8, rs.getInt("Perma"));
//                    createReport.setInt(9, rs.getInt("Ban"));
//                    createReport.setString(10, String.valueOf(NULL));
//                    createReport.executeUpdate();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void sendToHistory(String fromTable, String type) {
//        try {
//            PreparedStatement ps = MySQL.getCon().prepareStatement("SELECT * FROM " + fromTable + "");
//            ResultSet rs = ps.executeQuery();
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy/HH:mm:ss");
//            long timestamp;
//            while (rs.next()) {
//                String datum = rs.getString("Datum");
//                Date date = simpleDateFormat.parse(datum);
//                timestamp = date.getTime();
//                Main.sendHistory(UUID.fromString(rs.getString("TargetUUID")), UUID.fromString(rs.getString("VonUUID")), type, rs.getString("Grund"), timestamp, -1, -1, -1);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            System.out.println("[Report] Parse-Fehler (Login.java): " + e.getMessage());
//        }
//    }
}
