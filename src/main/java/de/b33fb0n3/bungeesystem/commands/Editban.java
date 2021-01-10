package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 10.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.Ban;
import de.b33fb0n3.bungeesystem.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Editban extends Command {

    public Editban(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("bungeecord.editban.edit") || sender.hasPermission("bungeecord.*")) {
            // editban <name> <type> <value>
            if (args.length == 1) {
                sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutze folgende Types:\n" + Bungeesystem.herH + "1 §f» " + Bungeesystem.normal + "Änder, ob es ein Ban oder Mute ist\n" + Bungeesystem.herH + "2 §f» " + Bungeesystem.normal + "Änder bis wann der Ban geht\n" + Bungeesystem.herH + "3 §f» " + Bungeesystem.normal + "Änder den Grund"));
                return;
            }
            if (args.length >= 2) {
                UUID ut = UUIDFetcher.getUUID(args[0]);
                if (ut == null) {
                    sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Dieser Spieler existiert nicht!"));
                    return;
                }
                Ban ban = new Ban(ut, null, Bungeesystem.getPlugin().getDataSource(), Bungeesystem.settings, Bungeesystem.standardBans);
                if (!ban.isBanned()) {
                    sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Der Spieler " + Bungeesystem.herH + args[0] + Bungeesystem.fehler + " ist nicht gebannt!"));
                    return;
                }
                ProxiedPlayer pp = null;
                if (sender instanceof ProxiedPlayer)
                    pp = (ProxiedPlayer) sender;
                if (args[1].equalsIgnoreCase("1")) { // Ban (Ban/Mute)
                    if (args.length == 3) {
                        int value;
                        try {
                            value = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Der Wert muss eine Zahl sein!"));
                            return;
                        }
                        if (value == 1 || value == 0) {
                            if(ban.getBan() == value) {
                                sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Der Ban existiert bereits in der Form!"));
                                return;
                            }
                            ban.editban(1, args[2], pp == null ? UUIDFetcher.getUUID("CONSOLE").toString() : pp.getUniqueId().toString());
                        } else {
                            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Der Wert muss eine " + Bungeesystem.herH + "1 " + Bungeesystem.fehler + "oder eine " + Bungeesystem.herH + "0 " + Bungeesystem.fehler + "sein!"));
                            return;
                        }
                    } else {
                        sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Gib noch einen neuen Wert (Value) ein"));                        return;
                    }
                } else if (args[1].equalsIgnoreCase("2")) { // Bis
                    if (args.length == 3) {
                        final String regex = "^(0\\d|1\\d|2\\d|3[0-1])\\/(0[1-9]|1[0-2])\\/(20\\d\\d)T(0\\d|1\\d|2[0-3])\\:([0-5]\\d)";

                        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                        final Matcher matcher = pattern.matcher(args[2]);

                        while (matcher.find() || args[2].equalsIgnoreCase("-1")) {
                            ban.editban(2, args[2], pp == null ? UUIDFetcher.getUUID("CONSOLE").toString() : pp.getUniqueId().toString());
                            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + "Der Ban wurde verändert!"));
                            return;
                        }
                        if (!matcher.find()) {
                            TextComponent tc = new TextComponent();
                            tc.setText(Bungeesystem.Prefix + Bungeesystem.fehler + "Das Datum konnte nicht gefunden werden!");
                            TextComponent tc1 = new TextComponent(Bungeesystem.other2 + " [" + Bungeesystem.fehler + "MEHR" + Bungeesystem.other2 + "]");
                            tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.normal + "Stelle sicher, dass du es im richtigen Format geschrieben hast!\n" + Bungeesystem.normal + "Richtig: " + Bungeesystem.herH + "DD/MM/YYYYThh:mm " + Bungeesystem.other2 + "(" + Bungeesystem.normal + "Beispiel: " + Bungeesystem.herH + "10/04/2020T14:56" + Bungeesystem.other2 + ")\n" + Bungeesystem.normal + "Falsch: " + Bungeesystem.herH + args[2])));
                            tc.addExtra(tc1);
                            sender.sendMessage(tc);
                            return;
                        }
                    } else {
                        sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Gib noch einen neuen Wert (Value) ein"));
                        return;
                    }
                } else if (args[1].equalsIgnoreCase("3")) { // Grund
                    if (args.length >= 3) {
                        String newReason = "";
                        for (int i = 2; i < args.length; i++) {
                            newReason = newReason + args[i] + " ";
                        }
                        if(newReason.equalsIgnoreCase(ban.getGrund())) {
                            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Der Ban existiert bereits in der Form!"));
                            return;
                        }
                        ban.editban(3, newReason, pp == null ? UUIDFetcher.getUUID("CONSOLE").toString() : pp.getUniqueId().toString());
                    } else {
                        sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Gib noch einen neuen Wert (Value) ein"));
                        return;
                    }
                } else {
                    sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutze folgende Types:\n" + Bungeesystem.herH + "1 §f» " + Bungeesystem.normal + "Änder, ob es ein Ban oder Mute ist\n" + Bungeesystem.herH + "2 §f» " + Bungeesystem.normal + "Änder bis wann der Ban geht\n" + Bungeesystem.herH + "3 §f» " + Bungeesystem.normal + "Änder den Grund"));
                    return;
                }
                sender.sendMessage(new TextComponent(Bungeesystem.Prefix + "Der Ban wurde verändert!"));
            } else
                sender.sendMessage(new TextComponent(Bungeesystem.helpMessage.replace("%begriff%", "editban")));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.noPerm));
    }

}
