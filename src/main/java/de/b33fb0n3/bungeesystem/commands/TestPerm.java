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
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TestPerm extends Command {

    public TestPerm(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        int update = Bungeesystem.getPlugin().getUpdater().ckeckUpdate();
        if (update == -1) {
            if (sender instanceof ProxiedPlayer) {
                final ProxiedPlayer pp = (ProxiedPlayer) sender;
                if (args.length == 1) {
                    pp.sendMessage(new TextComponent(Bungeesystem.Prefix + pp.hasPermission(args[0])));
                } else
                    pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutze: " + Bungeesystem.other + "/testperm <Perm>"));
            } else
                sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist kein Spieler!"));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Der Developer-Modus ist nicht aktiviert!"));
    }

}
