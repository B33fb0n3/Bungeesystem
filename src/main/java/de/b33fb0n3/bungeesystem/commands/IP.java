package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 17.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.Playerdata;
import de.b33fb0n3.bungeesystem.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class IP extends Command {

    public IP(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
            if (sender.hasPermission("bungeecord.ip") || sender.hasPermission("bungeecord.*")) {
                if (args.length == 1) {
                    UUID ut = UUIDFetcher.getUUID(args[0]);
                    if(ut == null) {
                        sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Dieser Spieler existiert nicht!"));
                        return;
                    }
                    String ip = new Playerdata(ut).getLastip();
                    if (ip == null || ip.equals("")) {
                        sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.herH + args[0] + Bungeesystem.fehler + " war noch nie auf dem Netzwerk!"));
                        return;
                    }
                    sender.sendMessage(new TextComponent(Bungeesystem.Prefix + "Die IP von "+ Bungeesystem.herH + args[0] + Bungeesystem.normal+" ist: "+Bungeesystem.herH + ip));
                } else
                    sender.sendMessage(new TextComponent(Bungeesystem.helpMessage.replace("%begriff%", "ip")));
            } else
                sender.sendMessage(new TextComponent(Bungeesystem.noPerm));
    }
}
