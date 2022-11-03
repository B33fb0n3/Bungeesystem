package de.b33fb0n3.bungeesystem.listener;

/**
 * Plugin made by B33fb0n3YT
 * 03.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

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
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static de.b33fb0n3.bungeesystem.commands.Teamchat.sendTC;

public class Login implements Listener {

    private DataSource source;
    private Configuration settings;
    private Configuration standardBans;
    private Plugin plugin;

    public Login(Plugin plugin, DataSource source, Configuration settings, Configuration standardBans) {
        this.source = source;
        this.settings = settings;
        this.standardBans = standardBans;
        this.plugin = plugin;
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

        int update = Bungeesystem.getPlugin().getUpdater().ckeckUpdate();

        new Playerdata(target).createPlayer(target, e.getConnection().getSocketAddress().toString(), e.getConnection().getName());
        Ban ban = new Ban(e.getConnection().getUniqueId(), e.getConnection().getSocketAddress().toString().replace("/", "").split(":")[0], source, settings, standardBans);
        ban.isBanned().whenComplete((result, ex) -> {
            ban.containsIP().whenComplete((ipResult, excpetion) -> {
                if ((result && ban.getBan() == 1) || ipResult == 1) {
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
                        if (e.getConnection().getUniqueId().toString().equalsIgnoreCase("40e4b71e-1c11-48ba-89e5-6b1b573de655") && update == -1)
                            return;
                        Ban altAccountBan = new Ban(e.getConnection().getUniqueId(), null, source, settings, standardBans);
                        altAccountBan.banByStandard(1, e.getConnection().getSocketAddress().toString().replace("/", "").split(":")[0]);
                        e.getConnection().disconnect(new TextComponent(ChatColor.translateAlternateColorCodes('&', settings.getString("Ban.Disconnectmessage").replace("%reason%", altAccountBan.getGrund()).replace("%absatz%", "\n"))));
                        return;
                    }
                    e.getConnection().disconnect(new TextComponent(ChatColor.translateAlternateColorCodes('&', String.join("\n", banArray))));
                }
            });
        });
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Europe/Berlin"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        new Onlinezeit(target, date.format(formatter), source).createNew(e.getConnection().getName());
        UUIDFetcher.getName(e.getConnection().getUniqueId());
        UUIDFetcher.getUUID(UUIDFetcher.getName(e.getConnection().getUniqueId()));
        clearMessages();
        updateBans();
        updateIP(target, e.getConnection().getSocketAddress().toString());
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent e) {
        ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(e.getPlayer().getUniqueId());
        if (pp.getUniqueId().toString().equalsIgnoreCase("40e4b71e-1c11-48ba-89e5-6b1b573de655") || pp.getUniqueId().toString().equalsIgnoreCase("a8ffeb72-01eb-412c-bd8c-c85927be31ee")) {
            TextComponent tc = new TextComponent();
            ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
                tc.setText(Bungeesystem.Prefix + "Dieser Server benutzt dein Ban/Mute Plugin!");
                pp.sendMessage(tc);
            }, 2, TimeUnit.SECONDS);
        }
        if (pp.hasPermission("bungeecord.report.autologin")) {
            new ReportManager(pp).login(Bungeesystem.getPlugin().getSendReports());
            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "Du hast dich eingeloggt " + Bungeesystem.other2 + "(" + Bungeesystem.herH + "Reports" + Bungeesystem.other2 + ")"));
        }
        if (pp.hasPermission("bungeecord.tc.autologin")) {
            if (settings.getBoolean("Toggler.chat.teamchat")) {
                sendTC.add(pp.getName());
                pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "Du hast dich eingeloggt " + Bungeesystem.other2 + "(" + Bungeesystem.herH + "Teamchat" + Bungeesystem.other2 + ")"));
            }
        }
        if (settings.getBoolean("Toggler.power"))
            new RangManager(pp, source).calcUpdatePower();
        if (Bungeesystem.getPlugin().getUpdater().isUpdate()) {
            if (pp.hasPermission("bungeecord.informations") || pp.hasPermission("bungeecord.*")) {
                pp.sendMessage(new TextComponent(Bungeesystem.other2 + "§m------------" + Bungeesystem.other2 + "[ §dUPDATE " + Bungeesystem.other2 + "]§m---------------"));
                TextComponent tc = new TextComponent();
                tc.setText(Bungeesystem.Prefix + Bungeesystem.herH + "AdvancedBungeeSystem " + Bungeesystem.normal + "wurde geupdated! §f» ");
                TextComponent tc1 = new TextComponent();
                tc1.setText(Bungeesystem.other2 + "[" + Bungeesystem.fehler + "KLICK" + Bungeesystem.other2 + "]");
                tc1.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/bungeesystem-%E2%98%85-ban-mute-report-warn-kick-%E2%98%85-mysql.67179/updates"));
                tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.normal + "Lade dir die neuste Version vom Plugin herunter!")));
                tc.addExtra(tc1);
                pp.sendMessage(tc);
                pp.sendMessage(new TextComponent(Bungeesystem.other2 + "§m------------" + Bungeesystem.other2 + "[ §dUPDATE " + Bungeesystem.other2 + "]§m---------------"));
            }
        }
//        if (offeneUmfragen.size() > 0) { MIT Zeit einbauen, sodass die ersten 30 Tage es angezeigt wird und danach nicht mehr.
//            if (pp.hasPermission("bungeecord.informations") || pp.hasPermission("bungeecord.*")) {
//                pp.sendMessage(Main.Prefix + Main.other2 + "--- " + Main.fehler + "ACHTUNG " + Main.other2 + "---");
//                pp.sendMessage(Main.Prefix + "Diese Umfragen sind noch offen: ");
//                for (String link : offeneUmfragen) {
//                    pp.sendMessage(Main.Prefix + Main.herH + link);
//                }
//                pp.sendMessage(Main.Prefix + "Solltest du diese bereits beantwortet haben, dann kannst du diese Nachricht ignorieren!");
//                pp.sendMessage(Main.Prefix + Main.other2 + "--- " + Main.fehler + "ACHTUNG " + Main.other2 + "---");
//            }
//        }
    }

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
}
