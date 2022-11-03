package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 17.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.Ban;
import de.b33fb0n3.bungeesystem.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class Unban extends Command {

    public Unban(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("bungeecord.unban") || sender.hasPermission("bungeecord.*")) {
            if (args.length == 1) {
                UUID pt = UUIDFetcher.getUUID(args[0]);
                if (pt == null) {
                    sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Dieser Spieler existiert nicht!"));
                    return;
                }
                Ban ban = new Ban(pt, null, Bungeesystem.getPlugin().getDataSource(), Bungeesystem.settings, Bungeesystem.standardBans);
                ban.isBanned().whenComplete((bannedResult, exception) -> {
                    if (bannedResult) {
                        ban.unban(true, sender instanceof ProxiedPlayer ? sender.getName() : "CONSOLE").whenComplete((result, ex) -> {
                            if (!result)
                                sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Ein Fehler ist aufgetreten!"));
                        });
                    } else
                        sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Dieser Spieler ist nicht gebannt!"));
                });
            } else
                sender.sendMessage(new TextComponent(Bungeesystem.helpMessage.replace("%begriff%", "unban")));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.noPerm));
    }

}
