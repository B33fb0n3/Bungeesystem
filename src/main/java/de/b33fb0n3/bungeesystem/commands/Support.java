package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 31.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.Cooldowns;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Support extends Command {

    public Support(String name) {
        super(name);
    }

    public static HashMap<ProxiedPlayer, ProxiedPlayer> activechats = new HashMap<>();
    private static LinkedHashMap<ProxiedPlayer, String> waitingForSupport = new LinkedHashMap<>();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer pp = (ProxiedPlayer) sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("stop")) {
                    if (activechats.containsValue(pp) || activechats.containsKey(pp)) {
                        for (ProxiedPlayer key : activechats.keySet()) {
                            if (key == pp) {
                                activechats.get(pp).sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.herH + pp.getName() + Bungeesystem.normal + " hat den Supportchat beendet!"));
                            } else {
                                key.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.herH + pp.getName() + Bungeesystem.normal + " hat den Supportchat beendet!"));
                            }
                            activechats.remove(key);
                        }
                        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "Du hast den Supportchat beendet!"));
                        return;
                    }
                    pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist in keinem aktiven Supportchat."));
                    return;
                }
                if (pp.hasPermission("bungeecord.support.accept") || pp.hasPermission("bungeecord.*")) {
                    if (args[0].equalsIgnoreCase("accept")) {
                        if (waitingForSupport.size() <= 0) {
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Es benötigt niemand Hilfe!"));
                            return;
                        }
                        ProxiedPlayer pt = ProxyServer.getInstance().getPlayer(waitingForSupport.keySet().toArray()[0].toString());
                        // SUPPORTER
                        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "Du bist nun im Supportchat mit " + Bungeesystem.herH + pt.getName()));
                        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "Betreff: " + Bungeesystem.herH + waitingForSupport.get(pt)));
                        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "Du kannst mit " + Bungeesystem.other + "/support stop" + Bungeesystem.normal + " den Supportchat verlassen!"));

                        // TARGET
                        pt.sendMessage(new TextComponent(Bungeesystem.Prefix + "Du bist nun im Supportchat mit " + Bungeesystem.herH + pp.getName()));
                        pt.sendMessage(new TextComponent(Bungeesystem.Prefix + "Um Antworten zu können, schreibe ganz normal in den Chat."));
                        pt.sendMessage(new TextComponent(Bungeesystem.Prefix + "Du kannst mit " + Bungeesystem.other + "/support stop" + Bungeesystem.normal + " den Supportchat verlassen!"));

                        activechats.put(pt, pp);
                        waitingForSupport.remove(pt);
                    } else if (args[0].equalsIgnoreCase("list")) {
                        if(waitingForSupport.size() <= 0) {
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Es gibt zurzeit keine offenen Supportanfragen!"));
                            return;
                        }
                        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "§n"+"Spieler" + Bungeesystem.other2 +"§n"+ " ● " + Bungeesystem.normal + "§n"+"Betreff"));
                        for (int i = 0; i < waitingForSupport.size(); i++) {
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.herH + waitingForSupport.keySet().toArray()[i].toString() + Bungeesystem.other2 + " ●" + Bungeesystem.herH + waitingForSupport.get(ProxyServer.getInstance().getPlayer(waitingForSupport.keySet().toArray()[i].toString()))));
                        }
                        TextComponent tc = new TextComponent();
                        tc.setText(Bungeesystem.Prefix + "Nimm die älteste Anfrage an: ");
                        TextComponent tc1 = new TextComponent();
                        tc1.setText(Bungeesystem.other2 + "["+Bungeesystem.fehler+"KLICK"+Bungeesystem.other2+"]");
                        tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.other2 + "Nimm die älteste Anfrage an")));
                        tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/support accept"));
                        tc.addExtra(tc1);
                        pp.sendMessage(tc);
                    } else {
                        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Du kannst keine Supportanfragen erstellen!"));
                    }
                } else if ((pp.hasPermission("bungeecord.support.create") || pp.hasPermission("bungeecord.*"))) {
                    int supporter = 0;
                    for (ProxiedPlayer current : ProxyServer.getInstance().getPlayers()) {
                        if (current.hasPermission("bungeecord.support.accept") || current.hasPermission("bungeecord.*"))
                            supporter++;
                    }
                    if (supporter > 0) {
                        String betreff = "";
                        for (String arg : args) {
                            betreff = betreff + " " + arg;
                        }
                        if (!waitingForSupport.containsKey(pp)) {
                            Cooldowns cooldowns = new Cooldowns("Support", pp);
                            if (!cooldowns.isOnCooldown()) {
                                waitingForSupport.put(pp, betreff);
                                if (Bungeesystem.settings.getBoolean("Cooldown.Support.aktive"))
                                    cooldowns.startCooldown();
                                pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "Du hast die " + Bungeesystem.herH + "Support Warteschlange §abetreten! " + Bungeesystem.other2 + "(" + Bungeesystem.fehler + betreff + Bungeesystem.other2 + " )"));
                                pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.herH + supporter + Bungeesystem.normal + " Supporter sind gerade Online!"));
                                for (ProxiedPlayer current : ProxyServer.getInstance().getPlayers()) {
                                    if (current.hasPermission("bungeecord.support.accept")) {
                                        TextComponent tc = new TextComponent();
                                        tc.setText(Bungeesystem.Prefix + Bungeesystem.herH + pp.getName() + Bungeesystem.normal + " hat eine Supportanfrage geschickt! " + Bungeesystem.other2 + "(" + Bungeesystem.fehler + betreff + Bungeesystem.other2 + " )");
                                        TextComponent tc1 = new TextComponent();
                                        tc1.setText(Bungeesystem.other2 + " [§aANNEHMEN" + Bungeesystem.other2 + "]");
                                        tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/support accept"));
                                        tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.other2 + "Nimm die älteste Supportanfrage an!")));
                                        tc.addExtra(tc1);
                                        current.sendMessage(tc);
                                    }
                                }
                            } else
                                pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Du kannst erst bald wieder Support anfordern!"));
                        } else {
                            waitingForSupport.remove(pp);
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "Du hast die " + Bungeesystem.herH + "Support Warteschlange §cverlassen!"));
                        }
                    } else
                        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Zurzeit ist kein Supporter online!"));
                } else
                    pp.sendMessage(new TextComponent(Bungeesystem.noPerm));
            } else
                pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutze: " + Bungeesystem.other + "/support <Betreff>"));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist kein Spieler!"));
    }

}
