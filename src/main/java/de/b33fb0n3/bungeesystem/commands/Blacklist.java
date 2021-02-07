package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 31.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.IOException;
import java.util.List;

public class Blacklist extends Command {

    public Blacklist(String name) {
        super(name, null, "bl");
    }

    private List<String> blacklistWords = Bungeesystem.blacklist.getStringList("Blacklist.Words");

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("bungeecord.blacklist") || sender.hasPermission("bungeecord.*")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    for (String badWord : blacklistWords) {
                        sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.herH + badWord));
                    }
                } else
                    sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutze: " + Bungeesystem.other + "/bl list <Wort>"));
            } else if (args.length >= 2) {
                if (args[0].equalsIgnoreCase("add")) {
                    for (int i = 1; i < args.length; i++) {
                        if (!blacklistWords.contains(args[i])) {
                            updateWords(args[i], false);
                            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + "Das Wort " + Bungeesystem.herH + args[i] + Bungeesystem.normal + " wurde hinzugefügt"));
                        } else
                            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Das Wort " + Bungeesystem.herH + args[i] + Bungeesystem.fehler + " wurde bereits hinzugefügt!"));
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    for (int i = 1; i < args.length; i++) {
                        if (blacklistWords.contains(args[i])) {
                            updateWords(args[i], true);
                            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + "Das Wort " + Bungeesystem.herH + args[i] + Bungeesystem.normal + " wurde entfernt!"));
                        } else
                            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Das Wort " + Bungeesystem.herH + args[i] + Bungeesystem.fehler + " existiert nicht!"));
                    }
                } else
                    sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutze: " + Bungeesystem.other + "/bl <add/remove> <Wort>"));
            } else
                sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutze: " + Bungeesystem.other + "/bl <add/remove/list> <Wort>"));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.noPerm));
    }

    private void updateWords(String wort, boolean remove) {
        if (remove) {
            blacklistWords.remove(wort);
        } else {
            blacklistWords.add(wort);
        }
        Bungeesystem.blacklist.set("Blacklist.Words", blacklistWords);
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(Bungeesystem.blacklist, Bungeesystem.blacklistFile);
            Bungeesystem.blacklist = ConfigurationProvider.getProvider(YamlConfiguration.class).load(Bungeesystem.blacklistFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
