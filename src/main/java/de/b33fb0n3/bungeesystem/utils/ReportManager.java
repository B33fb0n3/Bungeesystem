package de.b33fb0n3.bungeesystem.utils;

import de.b33fb0n3.bungeesystem.Bungeesystem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Plugin made by B33fb0n3YT
 * 29.12.2020
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

public class ReportManager {

    private String grund;
    private UUID target;
    private ProxiedPlayer reporter;
    private long erstellt;
    private boolean loggedIn;

    public ReportManager(ProxiedPlayer pp) {
        this.loggedIn = false;
        this.reporter = pp;
    }

    public ReportManager(String grund, UUID target, ProxiedPlayer reporter, long erstellt) {
        this.grund = grund;
        this.target = target;
        this.reporter = reporter;
        this.erstellt = erstellt;
    }

    public void insertReportInDB() {
        new Playerdata(target).updatePlayerData("reportsMade", null);
        new HistoryManager().insertInDB(getTarget(), getReporter().getUniqueId(), "report", getGrund(), getErstellt(), -1, -1, -1, null, null);
    }

    public void sendReport(List<String> sendReports) {
        String targetName = UUIDFetcher.getName(getTarget());
        TextComponent tc = new TextComponent();
        tc.setText(Bungeesystem.other2 + "[" + Bungeesystem.fehler + "JUMP TO" + Bungeesystem.other2 + "]");
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.other2 + "Zum Spieler teleportieren")));
        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report tp " + targetName));

        List<TextComponent> list = new ArrayList<>();
        int i = 1;
        while (true) {
            try {
                String line = ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("ReportMessage.line" + i)).replace("%target%", targetName).replace("%von%", getReporter().getName()).replace("%grund%", getGrund());
                i++;
                if (line.endsWith("%teleport%")) {
                    TextComponent tc3 = new TextComponent();
                    tc3.setText("\n" + line.replace("%teleport%", ""));
                    list.add(tc3);
                    list.add(tc);
                } else if (line.equalsIgnoreCase("%teleport%")) {
                    TextComponent tc3 = new TextComponent();
                    tc3.setText("\n");
                    list.add(tc3);
                    list.add(tc);
                } else if (line.contains("%teleport%")) {
                    String[] splited = line.split("%teleport%");
                    for (int a = 0; a < splited.length; a++) {
                        if (a > 0) {
                            list.add(tc);
                            TextComponent tc1 = new TextComponent();
                            tc1.setText(splited[a]);
                            list.add(tc1);
                        } else {
                            TextComponent tc2 = new TextComponent();
                            tc2.setText("\n" + splited[a]);
                            list.add(tc2);
                        }
                    }
                } else {
                    TextComponent tc3 = new TextComponent();
                    if (list.size() == 0) {
                        tc3.setText("" + line);
                    } else
                        tc3.setText("\n" + line);
                    list.add(tc3);
                }
                if (i > Bungeesystem.settings.getInt("ReportMessage.lines"))
                    break;
            } catch (Exception e1) {
                break;
            }
        }
        TextComponent finalTc = new TextComponent();
        finalTc.setText("");
        for (TextComponent textComponent : list) {
            finalTc.addExtra(textComponent);
        }
        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
            if (all.hasPermission("bungeecord.report.see") || all.hasPermission("bungeecord.*")) {
                if (sendReports.contains(all.getName()))
                    all.sendMessage(finalTc);
            }
        }
        list.clear();
    }

    public boolean deleteReport(String id) throws SQLException {
        try (Connection conn = Bungeesystem.getPlugin().getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM history WHERE Erstellt = ?");) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not delete report with id " + id, e);
        }
        return false;
    }

    public void sendLastReports() {
        try (Connection conn = Bungeesystem.getPlugin().getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM history WHERE Type= ? ORDER BY Erstellt DESC LIMIT 10");) {
            ps.setString(1, "report");
            ResultSet rs = ps.executeQuery();
            getReporter().sendMessage(new ComponentBuilder(Bungeesystem.other2 + "--- §3Wer" + Bungeesystem.other2 + " ---- §3Grund " + Bungeesystem.other2 + "---- §3Von" + Bungeesystem.other2 + " ---").create());
            int count = 1;
            int count2 = 0;
            while (rs.next()) {
                TextComponent tc = new TextComponent();
                final String targetName = UUIDFetcher.getName(UUID.fromString(rs.getString("TargetUUID")));
                tc.setText(Bungeesystem.herH + count + ". " + Bungeesystem.normal + targetName + " §f» " + Bungeesystem.herH + rs.getString("Grund") + " §f» " + Bungeesystem.normal + UUIDFetcher.getName(UUID.fromString(rs.getString("VonUUID"))));

                TextComponent tc1 = new TextComponent();
                tc1.setText(Bungeesystem.other2 + " [" + Bungeesystem.fehler + "TP" + Bungeesystem.other2 + "]");
                tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.other2 + "Klick zum Teleportieren")));
                tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report tp " + targetName));
                tc.addExtra(tc1);

                getReporter().sendMessage(tc);
                count++;
                count2++;
            }
            if (count2 < 1) {
                getReporter().sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Keine Reports vorhanden.").create());
            }
            getReporter().sendMessage(new ComponentBuilder(Bungeesystem.other2 + "--- §3Wer" + Bungeesystem.other2 + " ---- §3Grund " + Bungeesystem.other2 + "---- §3Von" + Bungeesystem.other2 + " ---").create());
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not output last reports" , e);
        }
    }

    public void login(List<String> sendReports) {
        sendReports.add(getReporter().getName());
    }

    public void logout(List<String> sendReports) {
        sendReports.remove(getReporter().getName());
    }

    public boolean isLoggedIn(List<String> sendReports) {
        return sendReports.contains(getReporter().getName());
    }

    public String getGrund() {
        return grund;
    }

    public void setGrund(String grund) {
        this.grund = grund;
    }

    public UUID getTarget() {
        return target;
    }

    public void setTarget(UUID target) {
        this.target = target;
    }

    public ProxiedPlayer getReporter() {
        return reporter;
    }

    public void setReporter(ProxiedPlayer reporter) {
        this.reporter = reporter;
    }

    public long getErstellt() {
        return erstellt;
    }

    public void setErstellt(long erstellt) {
        this.erstellt = erstellt;
    }
}
