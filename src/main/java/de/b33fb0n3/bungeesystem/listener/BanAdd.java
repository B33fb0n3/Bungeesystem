package de.b33fb0n3.bungeesystem.listener;

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.DateUnit;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

/**
 * Plugin made by B33fb0n3YT
 * 17.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

public class BanAdd implements Listener {

    public BanAdd(Plugin plugin) {
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onChat(ChatEvent e) {
        if (e.getMessage().startsWith("/"))
            return;
        if (!e.getSender().equals(de.b33fb0n3.bungeesystem.commands.BanAdd.p)) return;
        if (de.b33fb0n3.bungeesystem.commands.BanAdd.finished) return;
        e.setCancelled(true);
        try {
            switch (de.b33fb0n3.bungeesystem.commands.BanAdd.phase) {
                case 1:
                    if (e.getMessage().equalsIgnoreCase("1") || e.getMessage().equalsIgnoreCase("0")) {
                        de.b33fb0n3.bungeesystem.commands.BanAdd.perma = Integer.parseInt(e.getMessage());
                        de.b33fb0n3.bungeesystem.commands.BanAdd.phase++;
                        de.b33fb0n3.bungeesystem.commands.BanAdd.startPhase(de.b33fb0n3.bungeesystem.commands.BanAdd.phase);
                    } else
                        de.b33fb0n3.bungeesystem.commands.BanAdd.p.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutz: " + Bungeesystem.herH + "1 " + Bungeesystem.fehler + "oder " + Bungeesystem.herH + "0"));
                    break;
                case 2:
                    if (e.getMessage().equalsIgnoreCase("1") || e.getMessage().equalsIgnoreCase("0")) {
                        de.b33fb0n3.bungeesystem.commands.BanAdd.ban = Integer.parseInt(e.getMessage());
                        de.b33fb0n3.bungeesystem.commands.BanAdd.phase++;
                        de.b33fb0n3.bungeesystem.commands.BanAdd.startPhase(de.b33fb0n3.bungeesystem.commands.BanAdd.phase);
                    } else
                        de.b33fb0n3.bungeesystem.commands.BanAdd.p.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutz: " + Bungeesystem.herH + "1 " + Bungeesystem.fehler + "oder " + Bungeesystem.herH + "0"));
                    break;
                case 3:
                    de.b33fb0n3.bungeesystem.commands.BanAdd.grund = e.getMessage();
                    de.b33fb0n3.bungeesystem.commands.BanAdd.phase++;
                    de.b33fb0n3.bungeesystem.commands.BanAdd.startPhase(de.b33fb0n3.bungeesystem.commands.BanAdd.phase);
                    break;
                case 4:
                    try {
                        de.b33fb0n3.bungeesystem.commands.BanAdd.format = e.getMessage();
                        DateUnit.valueOf((de.b33fb0n3.bungeesystem.commands.BanAdd.format.toUpperCase()));
                    } catch (IllegalArgumentException e1) {
                        de.b33fb0n3.bungeesystem.commands.BanAdd.p.sendMessage(new TextComponent(Bungeesystem.herH + de.b33fb0n3.bungeesystem.commands.BanAdd.format + Bungeesystem.fehler + " ist keine gültige Einheit!"));
                        de.b33fb0n3.bungeesystem.commands.BanAdd.p.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.normal + "Gültige Einheiten: "));
                        for (DateUnit date : DateUnit.values()) {
                            de.b33fb0n3.bungeesystem.commands.BanAdd.p.sendMessage(new TextComponent(Bungeesystem.herH + date));
                        }
                        return;
                    }
                    de.b33fb0n3.bungeesystem.commands.BanAdd.phase++;
                    de.b33fb0n3.bungeesystem.commands.BanAdd.startPhase(de.b33fb0n3.bungeesystem.commands.BanAdd.phase);
                    break;
                case 5:
                    de.b33fb0n3.bungeesystem.commands.BanAdd.dauer = Integer.parseInt(e.getMessage());
                    if (de.b33fb0n3.bungeesystem.commands.BanAdd.dauer > 0) {
                        de.b33fb0n3.bungeesystem.commands.BanAdd.phase++;
                        de.b33fb0n3.bungeesystem.commands.BanAdd.startPhase(de.b33fb0n3.bungeesystem.commands.BanAdd.phase);
                    } else
                        de.b33fb0n3.bungeesystem.commands.BanAdd.p.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutze eine Zahl die größer als 0 ist!"));
                    break;
                case 6:
                    if (e.getMessage().equalsIgnoreCase("1") || e.getMessage().equalsIgnoreCase("0")) {
                        de.b33fb0n3.bungeesystem.commands.BanAdd.report = Integer.parseInt(e.getMessage());
                        de.b33fb0n3.bungeesystem.commands.BanAdd.phase++;
                        de.b33fb0n3.bungeesystem.commands.BanAdd.startPhase(de.b33fb0n3.bungeesystem.commands.BanAdd.phase);
                    } else
                        de.b33fb0n3.bungeesystem.commands.BanAdd.p.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutz: " + Bungeesystem.herH + "1 " + Bungeesystem.fehler + "oder " + Bungeesystem.herH + "0"));
                    break;
                case 7:
                    if (e.getMessage().equalsIgnoreCase("0")) {
                        de.b33fb0n3.bungeesystem.commands.BanAdd.finishSetup();
                        return;
                    }
                    de.b33fb0n3.bungeesystem.commands.BanAdd.perm = e.getMessage();
                    de.b33fb0n3.bungeesystem.commands.BanAdd.finishSetup();
                    break;
            }
        } catch (NumberFormatException e1) {
            de.b33fb0n3.bungeesystem.commands.BanAdd.p.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Gebe eine Zahl ein!"));
        }
    }

}
