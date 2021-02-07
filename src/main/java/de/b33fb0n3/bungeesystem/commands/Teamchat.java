package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 31.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

public class Teamchat extends Command {

    public Teamchat(String name) {
        super(name, "", "tc");
    }

    public static ArrayList<String> sendTC = new ArrayList<>();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer pp = (ProxiedPlayer) sender;
            if (pp.hasPermission("bungeecord.tc.login") || pp.hasPermission("bungeecord.*")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("login")) {
                        if (!sendTC.contains(pp.getName())) {
                            sendTC.add(pp.getName());
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "Du hast dich eingeloggt"));
                            String sendedMessage = ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("TeamchatPrefix"));
                            sendedMessage = sendedMessage.replace("%sender%", pp.getName()).replace("%msg%", Bungeesystem.normal + "hat sich eingeloggt!");
                            for (ProxiedPlayer pt : ProxyServer.getInstance().getPlayers()) {
                                if (sendTC.contains(pt.getName()) && !(pt.getName().equalsIgnoreCase(pp.getName())))
                                    pt.sendMessage(new TextComponent(sendedMessage));
                            }
                        } else {
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist bereits eingeloggt!"));
                        }
                        return;
                    } else if (args[0].equalsIgnoreCase("logout")) {
                        if (sendTC.contains(pp.getName())) {
                            sendTC.remove(pp.getName());
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "Du hast dich ausgeloggt"));
                            String sendedMessage = ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("TeamchatPrefix"));
                            sendedMessage = sendedMessage.replace("%sender%", pp.getName()).replace("%msg%", Bungeesystem.fehler + "hat sich ausgeloggt!");
                            for (ProxiedPlayer pt : ProxyServer.getInstance().getPlayers()) {
                                if (sendTC.contains(pt.getName()))
                                    pt.sendMessage(new TextComponent(sendedMessage));
                            }
                        } else {
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist gar nicht eingeloggt!"));
                        }
                        return;
                    } else if (args[0].equalsIgnoreCase("users")) {
                        int i = 0;
                        for (String player : sendTC) {
                            i++;
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.herH + i + ". " + Bungeesystem.normal + player));
                        }
                        return;
                    }
                }
            }
            if (!sendTC.contains(pp.getName())) {
                pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Bitte log dich zuerst ein!"));
                pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutze: " + Bungeesystem.other + "/tc <login/logout>"));
                return;
            }
            if (pp.hasPermission("bungeecord.tc.send") || pp.hasPermission("bungeecord.*")) {
                if (args.length >= 1) {
                    String msg = "";
                    for (int i = 0; i < args.length; i++) {
                        msg = msg + args[i] + " ";
                    }
                    String sendedMessage = ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("TeamchatPrefix"));
                    sendedMessage = sendedMessage.replace("%sender%", pp.getName()).replace("%msg%", msg);
                    for (ProxiedPlayer pt : ProxyServer.getInstance().getPlayers()) {
                        if (sendTC.contains(pt.getName()))
                            pt.sendMessage(new TextComponent(sendedMessage));
                    }
                } else
                    pp.sendMessage(new TextComponent(Bungeesystem.helpMessage.replace("%begriff%", "teamchat")));
            } else
                pp.sendMessage(new TextComponent(Bungeesystem.noPerm));
        } else
            sender.sendMessage(new TextComponent(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist kein Spieler!")));
    }

}
