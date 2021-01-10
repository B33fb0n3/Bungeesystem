package de.b33fb0n3.bungeesystem.listener;

/**
 * Plugin made by B33fb0n3YT
 * 10.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.Ban;
import de.b33fb0n3.bungeesystem.utils.DBUtil;
import de.b33fb0n3.bungeesystem.utils.UUIDFetcher;
import de.b33fb0n3.bungeesystem.utils.WarnManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat implements Listener {

    private Configuration settings;
    private Configuration blacklist;
    private Configuration standardBans;
    private DataSource source;
    private HashMap<ProxiedPlayer, ProxiedPlayer> activechats;

    public Chat(Plugin plugin, Configuration settings, Configuration blacklist, DataSource source, Configuration standardBans, HashMap<ProxiedPlayer, ProxiedPlayer> activechats) {
        this.settings = settings;
        this.standardBans = standardBans;
        this.blacklist = blacklist;
        this.source = source;
        this.activechats = activechats;
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    private HashMap<String, Long> spamCooldown = new HashMap<>();
    private HashMap<String, String> lastMessage = new HashMap<>();
    private List<String> badWords;

    @EventHandler
    public void onChat(ChatEvent e) {
        if (e.getMessage().startsWith("/"))
            return;

        badWords = blacklist.getStringList("Blacklist.Words");

        ProxiedPlayer pp = (ProxiedPlayer) e.getSender();

        if (spamCooldown.containsKey(pp.getName()))
            if (spamCooldown.get(pp.getName()) > System.currentTimeMillis()) {
                pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Bitte Spamme nicht!"));
                e.setCancelled(true);
                return;
            }

        if (settings.getBoolean("Toggler.chat.spam"))
            if (!pp.hasPermission("bungeecord.*"))
                if (!pp.hasPermission("bungeecord.spam.bypass"))
                    spamCooldown.put(pp.getName(), System.currentTimeMillis() + random(500, 3000));

        if (lastMessage.get(pp.getName()) != null) {
            if (lastMessage.get(pp.getName()).contains(e.getMessage())) {
                pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Du darfst die Nachricht nicht nochmal senden!"));
                e.setCancelled(true);
                return;
            }
        }

        if (settings.getBoolean("Toggler.chat.doublemessage"))
            if (!pp.hasPermission("bungeecord.*"))
                if (!pp.hasPermission("bungeecord.doublemessage.bypass"))
                    lastMessage.put(pp.getName(), e.getMessage());

        if (settings.getBoolean("Toggler.chat.blacklist"))
            if (!pp.hasPermission("bungeecord.*")) {
                if (!pp.hasPermission("bungeecord.blackWords.bypass")) {
                    for (String badWord : badWords) {
                        String[] split2;
                        for (int length3 = (split2 = e.getMessage().split(" ")).length, l = 0; l < length3; l++) {
                            final String s2 = split2[l];
                            boolean check;
                            if (blacklist.getBoolean("Blacklist.hardMode"))
                                check = s2.equalsIgnoreCase(badWord) || s2.toLowerCase().contains(badWord.toLowerCase()) || s2.toLowerCase().contains(badWord.toLowerCase().toLowerCase()) || s2.toLowerCase().contains(badWord.toUpperCase().toLowerCase());
                            else
                                check = s2.equalsIgnoreCase(badWord);
                            if (check) {
                                e.setCancelled(true); // Onlin(ez)eit wird blockiert!
                                pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Achte auf deine Wortwahl!"));
                                if (settings.getBoolean("Blacklist.direkterBan")) {
                                    new Ban(pp.getUniqueId(), null, source, settings, standardBans).banByStandard(2, e.getSender().getSocketAddress().toString().replace("/", "").split(":")[0]);
                                } else {
                                    int maxWarns = settings.getInt("Warns.MaxWarns");
                                    ArrayList<String> warnArray = new ArrayList<>();
                                    int i = 1;
                                    int whatCount = DBUtil.getWhatCount(source, pp.getUniqueId(), "warn");
                                    while (true) {
                                        try {
                                            String line = ChatColor.translateAlternateColorCodes('&', settings.getString("WarnMessage.line" + i)).replace("%warnCount%", String.valueOf(whatCount + 1)).replace("%maxWarns%", String.valueOf(maxWarns)).replace("%grund%", "Wortwahl (" + badWord + ")");
                                            warnArray.add(line);
                                            i++;
                                            if (i > settings.getInt("WarnMessage.lines"))
                                                break;
                                        } catch (Exception e1) {
                                            break;
                                        }
                                    }
                                    WarnManager warnManager = new WarnManager(pp.getUniqueId(), UUIDFetcher.getUUID("PLUGIN"), "Wortwahl (" + badWord + ")", System.currentTimeMillis(), settings, source);
                                    warnManager.addWarn();
                                    pp.disconnect(new TextComponent(ChatColor.translateAlternateColorCodes('&', String.join("\n", warnArray))));
                                    if (whatCount >= maxWarns) {
                                        new Ban(pp.getUniqueId(), null, source, settings, standardBans).banByStandard(2, e.getSender().getSocketAddress().toString().replace("/", "").split(":")[0]);
                                        warnManager.deleteAllWarns();
                                        pp.disconnect(new TextComponent(settings.getString("BanDisconnected").replace("%absatz%", "\n").replace("%reason%", "Wortwahl (" + badWord + ")")));
                                        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "Der Spieler wurde, für mehr als " + Bungeesystem.herH + maxWarns + Bungeesystem.normal + " Warnungen, gebannt!"));
                                    }
                                }
                                for (final ProxiedPlayer current : ProxyServer.getInstance().getPlayers()) {
                                    if (current.hasPermission("bungeecord.blackWords.info") || current.hasPermission("bungeecord.*")) {
                                        TextComponent tc = new TextComponent();
                                        tc.setText(Bungeesystem.Prefix + Bungeesystem.herH + pp.getName() + Bungeesystem.normal + " schreibt böse Sachen: ");

                                        TextComponent tc1 = new TextComponent();
                                        tc1.setText(e.getMessage());
                                        tc1.setColor(ChatColor.AQUA);
                                        tc.addExtra(tc1);

                                        current.sendMessage(tc);
                                    }
                                }
                                return;
                            }
                        }
                    }
                }
            }
        if (e.getSender() instanceof ProxiedPlayer) {
            Ban ban = new Ban(((ProxiedPlayer) e.getSender()).getUniqueId(), e.getSender().getSocketAddress().toString().replace("/", "").split(":")[0], source, settings, standardBans);
            if (ban.isBanned()) {
                if (ban.getBan() == 0 || ban.containsIP() == 0) {
                    e.setCancelled(true);
                    pp.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', settings.getString("MutedMessage").replace("%bis%", ban.getPerma() == 0 ? Bungeesystem.formatTime(ban.getBis()) : "Permanent").replace("%grund%", ban.getGrund()).replace("%absatz%", "\n"))));
                    return;
                }
            }
        }
        String m = e.getMessage().trim();
        float uppercaseletters = 0;

        for (int i = 0; i < m.length(); i++) {
            if (Character.isUpperCase(m.charAt(i)) && Character.isLetter(m.charAt(i))) {
                uppercaseletters++;
            }
        }

        if (e.getMessage().length() > 3) {
            if (uppercaseletters / (float) m.length() > 0.3F) {
                if (settings.getBoolean("Toggler.chat.caps"))
                    if (!pp.hasPermission("bungeecord.*")) {
                        if (!pp.hasPermission("bungeecord.caps.bypass")) {
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Bitte Deaktiviere deine Caps / Feststell -Taste!"));
                            e.setCancelled(true);
                            return;
                        }
                    }
            }
        }

        if (settings.getBoolean("Toggler.chat.ads"))
            if (!pp.hasPermission("bungeecord.*")) {
                if (!pp.hasPermission("bungeecord.ads.bypass")) {
                    final Pattern pattern = Pattern.compile("(?i)(((([a-zA-Z0-9-]+\\.)+(gs|ts|adv|no|uk|us|de|eu|com|net|noip|to|gs|me|info|biz|tv|au|cc|tk))+(\\:[0-9]{2,5})?))");
                    final Pattern pattern2 = Pattern.compile("(?i)(((([0-9]{1,3}\\.){3}[0-9]{1,3})(\\:[0-9]{2,5})?))");
                    final Pattern pattern3 = Pattern.compile("([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,}(:[0-9]{1,5})?");
                    final Pattern pattern4 = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}(:[0-9]{1,5})?");
                    final Pattern pattern5 = Pattern.compile("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?");
                    final Matcher matcher = pattern.matcher(e.getMessage());
                    final Matcher matcher2 = pattern2.matcher(e.getMessage());
                    final Matcher matcher3 = pattern3.matcher(e.getMessage());
                    final Matcher matcher4 = pattern4.matcher(e.getMessage());
                    final Matcher matcher5 = pattern5.matcher(e.getMessage());
                    if (matcher(matcher, e, pp, e.getMessage())) return;
                    if (matcher(matcher2, e, pp, e.getMessage())) return;
                    if (matcher(matcher3, e, pp, e.getMessage())) return;
                    if (matcher(matcher4, e, pp, e.getMessage())) return;
                    if (matcher(matcher5, e, pp, e.getMessage())) return;
                }
            }
        if (activechats.containsKey(pp)) {
            e.setCancelled(true);
            ProxiedPlayer pt = activechats.get(pp);
            pt.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.herH + pp.getName() + " §f» " + Bungeesystem.normal + e.getMessage()));
            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.herH + "Du §f» " + Bungeesystem.normal + e.getMessage()));
        }
        if (activechats.containsValue(pp)) {
            e.setCancelled(true);
            for (ProxiedPlayer key : activechats.keySet()) {
                key.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.herH + pp.getName() + " §f» " + Bungeesystem.normal + e.getMessage()));
            }
            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.herH + "Du §f» " + Bungeesystem.normal + e.getMessage()));
        }
        inserChatMessage(e.getMessage(), pp.getUniqueId(), pp.getServer().getInfo().getName());
    }

    private boolean matcher(Matcher matcher, ChatEvent e, ProxiedPlayer pp, String message) {
        if (matcher.find()) {
            e.setCancelled(true);
            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("AntiAd"))));
            for (final ProxiedPlayer current : ProxyServer.getInstance().getPlayers()) {
                if (current.hasPermission("bungeecord.ads.info") || current.hasPermission("bungeecord.*")) {
                    TextComponent tc = new TextComponent();
                    tc.setText(Bungeesystem.Prefix + Bungeesystem.herH + pp.getName() + Bungeesystem.normal + " macht Werbung: ");

                    TextComponent tc1 = new TextComponent();
                    tc1.setText(Bungeesystem.herH + message);
                    tc.addExtra(tc1);

                    TextComponent tc2 = new TextComponent();
                    tc2.setText(Bungeesystem.other2 + " [" + Bungeesystem.fehler + "MUTEN" + Bungeesystem.other2 + "]");
                    tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.fehler + "Klicke um den Spieler zu muten!")));
                    tc2.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ban " + pp.getName() + " "));
                    tc.addExtra(tc2);

                    current.sendMessage(tc);
                }
            }
            return true;
        }
        return false;
    }

    private void inserChatMessage(String message, UUID uuid, String serverName) {
        try (Connection conn = source.getConnection(); PreparedStatement ps = conn.prepareStatement("INSERT INTO chat(message, uuid, timestamp, server) VALUES(?,?,?,?)")) {
            ps.setString(1, message);
            ps.setString(2, uuid.toString());
            ps.setLong(3, System.currentTimeMillis());
            ps.setString(4, serverName);
            ps.executeUpdate();
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not inser chat message: " + message, e);
        }
    }

    private int random(double min, double max) {
        Random rdm = new Random();
        return (int) (rdm.nextInt((int) (max - min + 1)) + min);
    }
}
