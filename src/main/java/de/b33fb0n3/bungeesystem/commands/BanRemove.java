package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 17.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class BanRemove extends Command {

    public BanRemove(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer pp = (ProxiedPlayer) sender;
            if (pp.hasPermission("bungeecord.banremove") || pp.hasPermission("bungeecord.*")) {
                if (args.length == 1) {
                    int id = 0;
                    try {
                        id = Integer.parseInt(args[0]);
                    } catch (NumberFormatException e) {
                        if (args[0].equalsIgnoreCase("all")) {
                            for (int banID : Bungeesystem.ban.getSection("BanIDs").getKeys().stream().map(Integer::parseInt).sorted().collect(Collectors.toList())) {
                                removeBan(banID);
                            }
                            pp.sendMessage(Bungeesystem.Prefix + "Alle Ban-IDs wurden gelöscht!");
                        } else
                            pp.sendMessage(Bungeesystem.Prefix + Bungeesystem.fehler +"Gebe eine Zahl ein!");
                        return;
                    }
                    if (!Bungeesystem.ban.getSection("BanIDs").contains(id + "")) {
                        pp.sendMessage(Bungeesystem.Prefix + Bungeesystem.fehler + "Diese ID existiert nicht!");
                        return;
                    }
                    removeBan(id);
                    pp.sendMessage(Bungeesystem.Prefix + "Die ID "+Bungeesystem.herH + id + Bungeesystem.normal +" wurde entfernt!");
                } else
                    pp.sendMessage(Bungeesystem.helpMessage.replace("%begriff%", "banremove"));
            } else
                pp.sendMessage(Bungeesystem.noPerm);
        } else
            sender.sendMessage(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist kein Spieler!");
    }

    public static void removeBan(int id) {
        Bungeesystem.ban.set("BanIDs." + id + ".Reason", null);
        Bungeesystem.ban.set("BanIDs." + id + ".Time", null);
        Bungeesystem.ban.set("BanIDs." + id + ".Format", null);
        Bungeesystem.ban.set("BanIDs." + id + ".BanUtil", null);
        Bungeesystem.ban.set("BanIDs." + id + ".Perma", null);
        Bungeesystem.ban.set("BanIDs." + id + ".Reportable", null);
        Bungeesystem.ban.set("BanIDs." + id + "", null);
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(Bungeesystem.ban, Bungeesystem.banFile);
        } catch (IOException e) {
            Bungeesystem.logger().log(Level.WARNING, "could not save removed ban", e);
        }
    }

}
