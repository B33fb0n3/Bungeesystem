package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 24.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.Ban;
import de.b33fb0n3.bungeesystem.utils.DBUtil;
import de.b33fb0n3.bungeesystem.utils.HistoryElemt;
import de.b33fb0n3.bungeesystem.utils.HistoryManager;
import de.b33fb0n3.bungeesystem.utils.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public class History extends Command {

    public History(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer pp = (ProxiedPlayer) sender;
            if (pp.hasPermission("bungeecord.history") || pp.hasPermission("bungeecord.*")) {
                if (args.length == 0) {
                    HistoryManager manager = new HistoryManager();
                    List<HistoryElemt> elemts = manager.readHistory(null, 10, 1, "", true);
                    int count = 0;
                    for (HistoryElemt elemt : elemts) {
                        count++;
                        String type = elemt.getType();
                        Ban ban = new Ban(elemt.getTargetUUID(), null, Bungeesystem.getPlugin().getDataSource(), Bungeesystem.settings, Bungeesystem.standardBans);
                        TextComponent tc = new TextComponent();
                        tc.setText(Bungeesystem.herH + count + ". " + Bungeesystem.normal + UUIDFetcher.getName(elemt.getTargetUUID()) + " §f» " + Bungeesystem.herH + Character.toUpperCase(type.charAt(0)) + type.substring(1) + " §f» " + Bungeesystem.normal + Bungeesystem.formatTime(elemt.getErstellt()));
                        TextComponent tc1 = new TextComponent();
                        tc1.setText(Bungeesystem.other2 + " [" + Bungeesystem.fehler + "MEHR" + Bungeesystem.other2 + "]");
                        tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/check " + UUIDFetcher.getName(elemt.getTargetUUID())));
                        try {
                            tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.normal + "Von: " + Bungeesystem.herH + UUIDFetcher.getName(elemt.getVonUUID()) + "\n" + Bungeesystem.normal + "Grund: " + Bungeesystem.herH + elemt.getGrund() + "\n" + Bungeesystem.normal + "Bis: " + Bungeesystem.herH + (elemt.getPerma() == -2 ? "/" : (elemt.getPerma() == 1 ? "Permanent" : Bungeesystem.formatTime(elemt.getBis()))) + "\n" + Bungeesystem.normal + "Status: " + Bungeesystem.herH + (elemt.getBan() == -2 ? "/" : (elemt.getBan() == 1 ? "Ban" : "Mute")) + "\n" + Bungeesystem.normal + "Aktiv: " + Bungeesystem.herH + (type.equalsIgnoreCase("ban") ? (ban.isBanned().get() ? "§a✔" : "§c✖") : "§a✔") + "\n" + Bungeesystem.normal + "Entbannt von: " + Bungeesystem.herH + (elemt.getVonEntbannt() == null ? "Keiner" : elemt.getVonEntbannt()))));
                        } catch (InterruptedException | ExecutionException e) {
                            Bungeesystem.logger().log(Level.WARNING, "could not get() isbanned result");
                        }
                        tc.addExtra(tc1);
                        pp.sendMessage(tc);
                    }
                    if (count == 0)
                        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Es wurden keine Einträge gefunden!"));
                } else if (args.length == 2 || args.length == 1) {
                    UUID ut = UUIDFetcher.getUUID(args[0]);
                    if (ut == null) {
                        sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Gebe eine richtige Person ein!"));
                        return;
                    }
                    int seite = 1;
                    try {
                        seite = Integer.parseInt(args[1]);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
                    }
                    sendHelp(seite, pp, ut);
                } else
                    pp.sendMessage(new TextComponent(Bungeesystem.helpMessage.replace("%begriff%", "history")));
            } else
                pp.sendMessage(new TextComponent(Bungeesystem.noPerm));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Diesen Befehl kannst du nur Ingame benutzen!"));
    }

    private void sendHelp(int seite, ProxiedPlayer pp, UUID uuid) {
        int zeilen = 10;
        DBUtil.getWhatCount(Bungeesystem.getPlugin().getDataSource(), uuid, "", false).whenComplete((allReports, ex) -> {
            int allPages = allReports / zeilen;
            int eineSeitePlus = seite + 1;
            int eineSeiteMinus = seite - 1;
            if (allReports % zeilen > 0) {
                allPages++;
            }

            if (seite > allPages || seite == 0) {
                pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Hier gibt es keine Einträge!"));
                return;
            }
            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "History von " + Bungeesystem.herH + UUIDFetcher.getName(uuid) + " " + Bungeesystem.other2 + "(" + Bungeesystem.herH + seite + Bungeesystem.other2 + "/" + Bungeesystem.herH + allPages + Bungeesystem.other2 + ")"));

            HistoryManager historyManager = new HistoryManager();
            List<HistoryElemt> histroy = historyManager.readHistory(uuid, zeilen, seite, "", true);
            for (HistoryElemt history : histroy) {
                TextComponent tc = new TextComponent();
                String msg = Bungeesystem.settings.getString("History.Message");
                String type = history.getType();
                String typeForm = Character.toUpperCase(type.charAt(0)) + type.substring(1);
                Date date = new Date(history.getErstellt());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
                msg = msg.replace("%type%", typeForm).replace("%grund%", history.getGrund()).replace("%time%", simpleDateFormat.format(date)).replace("%von%", UUIDFetcher.getName(history.getVonUUID())).replace("%bis%", (history.getBis() == -2 ? "Permanent" : Bungeesystem.formatTime(history.getBis()))).replace("%zeit%", new Time(history.getErstellt()).toString()).replace("%status%", (history.getBan() != -2 ? (history.getBan() == 0 ? "Mute" : "Ban") : "Keiner")).replace("%entbanner%", history.getVonEntbannt() == null ? "Keiner" : history.getVonEntbannt());
                tc.setText(ChatColor.translateAlternateColorCodes('&', msg));

                TextComponent tc1 = new TextComponent();
                tc1.setText(Bungeesystem.other2 + "[" + Bungeesystem.fehler + "MEHR" + Bungeesystem.other2 + "]");

                ArrayList<String> hoverArray = new ArrayList<>();
                // ban null? warum kann isBanned nicht ausgelesen werden?
                int i = 1;
                while (true) {
                    try {
                        int finalI = i;
                        String line = ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("History.hover." + finalI)).replace("%von%", UUIDFetcher.getName(history.getVonUUID())).replace("%bis%", (history.getBis() == -2 ? "Permanent" : Bungeesystem.formatTime(history.getBis()))).replace("%zeit%", new Time(history.getErstellt()).toString()).replace("%status%", (history.getBan() != -2 ? (history.getBan() == 0 ? "Mute" : "Ban") : "Keiner")).replace("%type%", typeForm).replace("%grund%", history.getGrund()).replace("%time%", simpleDateFormat.format(date)).replace("%aktiv%", type.equalsIgnoreCase("ban") ? ((history.getVonEntbannt() == null) ? "§a✔" : "§c✖") : "§a✔").replace("%entbanner%", history.getVonEntbannt() == null ? "Keiner" : history.getVonEntbannt());
                        hoverArray.add(line);
                        i++;
                        if (i > 6)
                            break;
                    } catch (Exception e1) {
                        break;
                    }
                }
                tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.join("\n", hoverArray))));
                tc.addExtra(tc1);

                pp.sendMessage(tc);
                tc.getExtra().clear();
            }

            pfeile(eineSeiteMinus, eineSeitePlus, pp, uuid);
        });
    }

    private void pfeile(int eineSeiteMinus, int eineSeitePlus, ProxiedPlayer pp, UUID uuid) {
        TextComponent tc = new TextComponent();
        tc.setText(Bungeesystem.Prefix);

        TextComponent tc1 = new TextComponent();
        tc1.setText("§f«« ");
        tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/history " + UUIDFetcher.getName(uuid) + " " + eineSeiteMinus));
        tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§8(§7Seite: " + Bungeesystem.herH + eineSeiteMinus + "§8)")));
        tc.addExtra(tc1);

        TextComponent tc2 = new TextComponent();
        tc2.setText(Bungeesystem.other2 + "[" + Bungeesystem.fehler + "MEHR" + Bungeesystem.other2 + "]");
        tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§cKlick auf die Pfeile!")));
        tc.addExtra(tc2);

        TextComponent tc3 = new TextComponent();
        tc3.setText(" §f»»");
        tc3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/history " + UUIDFetcher.getName(uuid) + " " + eineSeitePlus));
        tc3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§8(§7Seite: " + Bungeesystem.herH + eineSeitePlus + "§8)")));
        tc.addExtra(tc3);

        pp.sendMessage(tc);
    }
}
