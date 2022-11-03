package de.b33fb0n3.bungeesystem.commands;

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.Playerdata;
import de.b33fb0n3.bungeesystem.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Plugin made by B33fb0n3YT
 * 31.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

public class Accounts extends Command {

    public Accounts(String name) {
        super(name);
    }

    private ArrayList<String> ips = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> warschon = new ArrayList<>();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("bungeecord.accounts") || sender.hasPermission("bungeecord.*")) {
            if (args.length == 0) {
                sendAccounts(sender);
            } else if (args.length == 1) {
                String lastIP;
                if (!args[0].contains(".")) {

                    UUID ut = UUIDFetcher.getUUID(args[0]);
                    if (ut == null) {
                        sender.sendMessage(new TextComponent(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Gebe einen richtigen Spieler an!")));
                        return;
                    }
                    Playerdata playerdata = new Playerdata(ut);
                    lastIP = playerdata.getLastip();
                    if(lastIP == null || lastIP == "") {
                        sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Dieser Spieler war noch nie auf dem Netzwerk!"));
                        return;
                    }
                } else
                    lastIP = args[0];

                try {
                    //Split
                    String[] lastIPSplit = lastIP.split("\\.");
                    lastIP = lastIPSplit[0] + "." + lastIPSplit[1] + "." + lastIPSplit[2];
                } catch (ArrayIndexOutOfBoundsException e) {
                    sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Bitte verwende eine richtige IP (XXX.XXX.XXX)"));
                    return;
                }

                int count = 0;
                try (Connection conn = Bungeesystem.getPlugin().getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM playerdata WHERE lastIP LIKE '%" + lastIP + "%'");) {
                    ResultSet rs = ps.executeQuery();
                    sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.normal + "Accounts von " + Bungeesystem.herH + args[0]));
                    while (rs.next()) {
                        TextComponent tc = new TextComponent();
                        String name = rs.getString("Name");
                        if (!name.equalsIgnoreCase(args[0])) {
                            tc.setText(Bungeesystem.Prefix + Bungeesystem.herH + name);
                            tc.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ban " + name + " "));
                            tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.fehler + name + Bungeesystem.normal + " bannen?")));
                            if (sender instanceof ProxiedPlayer) {
                                sender.sendMessage(tc);
                                count++;
                            } else
                                sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.herH + name));
                        }
                    }
                } catch (SQLException e) {
                    Bungeesystem.logger().log(Level.WARNING, "could not read account data", e);
                }
                if (count <= 0)
                    sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Keine Alt Accounts gefunden!"));
            } else
                sender.sendMessage(new TextComponent(Bungeesystem.helpMessage.replace("%begriff%", "accounts")));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.noPerm));
    }

    private void sendAccounts(CommandSender sender) {
        try (Connection conn = Bungeesystem.getPlugin().getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT lastIP, Name FROM playerdata GROUP BY lastIP HAVING COUNT(lastIP) > 1");) {
            ips.clear();
            warschon.clear();
            // ALLE Doppelten IPs werden ausgelesen
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Es konnten keine Alt-Accounts gefunden werden!"));
                return;
            }

            while (rs.next()) {
                names.clear();
                String lastIP = rs.getString("lastIP");
                final String name = rs.getString("Name");

                // Split
                String[] lastIPSplit = lastIP.split("\\.");
                lastIP = lastIPSplit[0] + "." + lastIPSplit[1] + "." + lastIPSplit[2];

                if (!warschon.contains(lastIP)) {

                    try (Connection conn1 = Bungeesystem.getPlugin().getDataSource().getConnection(); PreparedStatement ps1 = conn1.prepareStatement("SELECT lastIP,Name FROM playerdata WHERE lastIP LIKE '%" + lastIP + "%'")) {
                        // READ Names
                        ResultSet rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                            // Alt Accounts Namen von der einen IP
                            names.add(Bungeesystem.normal + rs1.getString("Name"));
                        }
                    } catch (SQLException e) {
                        Bungeesystem.logger().log(Level.WARNING, "could not read names for /accounts", e);
                    }

                    // Message
                    TextComponent tc = new TextComponent();
                    tc.setText(Bungeesystem.herH + name + " ");
                    TextComponent tc1 = new TextComponent();
                    tc1.setText(Bungeesystem.other2 + "[" + Bungeesystem.fehler + "MEHR" + Bungeesystem.other2 + "]");
                    tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.normal + "IP: " + Bungeesystem.herH + ((sender.hasPermission("bungeecord.ip") || sender.hasPermission("bungeecord.*")) ? lastIP + ".XXX" : "§k123.123.123") + "\n" + String.join("\n", names))));
                    tc.addExtra(tc1);
                    sender.sendMessage(tc);
                    warschon.add(lastIP);
                }
            }
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not read data for /accounts", e);
        }
    }

}
