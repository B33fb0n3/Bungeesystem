package de.b33fb0n3.bungeesystem.listener;

import de.b33fb0n3.bungeesystem.Bungeesystem;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

/**
 * Plugin made by B33fb0n3YT
 * 10.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

public class BanAdd implements Listener {

//    public BanAdd(Plugin plugin) {
//        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
//    }
//
//    @EventHandler
//    public void onChat(ChatEvent e) {
//        if (e.getMessage().startsWith("/"))
//            return;
//        if (!e.getSender().equals(p)) return;
//        if (BanAddRECODE.finished) return;
//        e.setCancelled(true);
//        try {
//            switch (phase) {
//                case 1:
//                    if (e.getMessage().equalsIgnoreCase("1") || e.getMessage().equalsIgnoreCase("0")) {
//                        perma = Integer.parseInt(e.getMessage());
//                        phase++;
//                        startPhase(phase);
//                    } else
//                        p.sendMessage(Main.Prefix + Main.fehler + "Benutz: " + Main.herH + "1 " + Main.fehler + "oder " + Main.herH + "0");
//                    break;
//                case 2:
//                    if (e.getMessage().equalsIgnoreCase("1") || e.getMessage().equalsIgnoreCase("0")) {
//                        ban = Integer.parseInt(e.getMessage());
//                        phase++;
//                        startPhase(phase);
//                    } else
//                        p.sendMessage(Main.Prefix + Main.fehler + "Benutz: " + Main.herH + "1 " + Main.fehler + "oder " + Main.herH + "0");
//                    break;
//                case 3:
//                    grund = e.getMessage();
//                    phase++;
//                    startPhase(phase);
//                    break;
//                case 4:
//                    try {
//                        format = e.getMessage();
//                        DateUnit.valueOf((format.toUpperCase()));
//                    } catch (IllegalArgumentException e1) {
//                        p.sendMessage(Main.herH + format + Main.fehler + " ist keine gültige Einheit!");
//                        p.sendMessage(Main.Prefix + Main.normal + "Gültige Einheiten: ");
//                        for (DateUnit date : DateUnit.values()) {
//                            p.sendMessage(Main.herH + date);
//                        }
//                        return;
//                    }
//                    phase++;
//                    startPhase(phase);
//                    break;
//                case 5:
//                    dauer = Integer.parseInt(e.getMessage());
//                    if (dauer > 0) {
//                        phase++;
//                        startPhase(phase);
//                    } else
//                        p.sendMessage(Main.Prefix + Main.fehler + "Benutze eine Zahl die größer als 0 ist!");
//                    break;
//                case 6:
//                    if (e.getMessage().equalsIgnoreCase("1") || e.getMessage().equalsIgnoreCase("0")) {
//                        report = Integer.parseInt(e.getMessage());
//                        phase++;
//                        startPhase(phase);
//                    } else
//                        p.sendMessage(Main.Prefix + Main.fehler + "Benutz: " + Main.herH + "1 " + Main.fehler + "oder " + Main.herH + "0");
//                    break;
//                case 7:
//                    if (e.getMessage().equalsIgnoreCase("0")) {
//                        finishSetup();
//                        return;
//                    }
//                    perm = e.getMessage();
//                    finishSetup();
//                    break;
//            }
//        } catch (NumberFormatException e1) {
//            p.sendMessage(Bungeesystem.Prefix + Bungeesystem.fehler + "Gebe eine Zahl ein!");
//        }
//    }

}
