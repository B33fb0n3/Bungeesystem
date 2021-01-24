package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 17.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.RangManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

public class Kick extends Command {

    public Kick(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("bungeecord.kick") || sender.hasPermission("bungeecord.*")) {
            if (args.length >= 1) {
                ProxiedPlayer pt = ProxyServer.getInstance().getPlayer(args[0]);
                if (pt == null) {
                    sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Dieser Spieler ist nicht auf dem Netzwerk!"));
                    return;
                }
                if (Bungeesystem.settings.getBoolean("Toggler.power")) {
                    if (sender instanceof ProxiedPlayer) {
                        ProxiedPlayer pp = (ProxiedPlayer) sender;
                        RangManager rangManager = new RangManager(pp, Bungeesystem.getPlugin().getDataSource());
                        if (!(rangManager.getPower(pp.getUniqueId()) > rangManager.getPower(pt.getUniqueId()))) {
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Diesen Spieler darfst du nicht kicken!"));
                            return;
                        }
                    }
                }

                String grund = "";
                for (int i = 1; i < args.length; i++) {
                    grund = grund + args[i] + " ";
                }

                ArrayList<String> kickArray = new ArrayList<>();
                int i = 1;
                while (true) {
                    try {
                        String line = ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("KickMessage.line" + i)).replace("%von%", sender instanceof ProxiedPlayer ? sender.getName() : "CONSOLE").replace("%grund%", grund);
                        kickArray.add(line);
                        i++;
                        if (i > Bungeesystem.settings.getInt("KickMessage.lines"))
                            break;
                    } catch (Exception e1) {
                        break;
                    }
                }
                pt.disconnect(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', String.join("\n", kickArray))).create());
                sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.herH + args[0] + Bungeesystem.normal + " wurde gekickt!"));
                for (ProxiedPlayer current : ProxyServer.getInstance().getPlayers()) {
                    if (current.hasPermission("bungeecord.informations")) {
                        if (!current.equals(sender))
                            current.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.herH + sender.getName() + Bungeesystem.normal + " hat " + Bungeesystem.herH + pt.getName() + Bungeesystem.normal + " für " + Bungeesystem.herH + grund + Bungeesystem.normal + " gekickt!"));
                    }
                }
            } else
                sender.sendMessage(new TextComponent(Bungeesystem.helpMessage.replace("%begriff%", "kick")));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.noPerm));
    }

}
