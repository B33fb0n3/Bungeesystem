package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 29.12.2020
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Reports extends Command {

    public Reports(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer pp = (ProxiedPlayer) sender;
            ReportManager reportManager = new ReportManager(pp);
            if (pp.hasPermission("bungeecord.reports") || pp.hasPermission("bungeecord.*")) {
                if (args.length == 0) { // /reports
                    reportManager.sendLastReports();
                    return;
                }
                if (args.length == 2 || args.length == 1) { // /reports <Spieler> <Seite>
                    int seite = 1; // falls args = 1
                    if (args.length == 2) {
                        try {
                            seite = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Bitte gib eine Zahl ein").create());
                            return;
                        }
                    }
                    sendHelp(seite, pp, args[0]);
                } else
                    pp.sendMessage(new ComponentBuilder(Bungeesystem.helpMessage.replace("%begriff%", "reports")).create());
            } else
                pp.sendMessage(new ComponentBuilder(Bungeesystem.noPerm).create());
        } else
            sender.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist kein Spieler!").create());
    }

    private void sendHelp(int seite, ProxiedPlayer pp, String targetName) {

        UUID uuid = UUIDFetcher.getUUID(targetName);
        if (uuid == null) {
            pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Dieser Spieler existiert nicht!").create());
            return;
        }
        int zeilen = 10;
        int allReports = DBUtil.getWhatCount(Bungeesystem.getPlugin().getDataSource(), uuid, "report", true);
        int allPages = allReports / zeilen;
        int eineSeitePlus = seite + 1;
        int eineSeiteMinus = seite - 1;
        if (allReports % zeilen > 0) {
            allPages++;
        }

        if (seite > allPages || seite == 0) {
            pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Hier gibt es keine Reports!").create());
            return;
        }
//        TextComponent tc = new TextComponent();
//        tc.setText(Bungeesystem.normal + "Reports von " + Bungeesystem.herH + targetName + " " + Bungeesystem.other2 + "(" + Bungeesystem.herH + seite + Bungeesystem.other2 + "/" + Bungeesystem.herH + allPages + Bungeesystem.other2+")");
//        TextComponent tc1 = new TextComponent();
//        tc1.setText(" §8[§cLÖSCHE ALLE§8]");
//        tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report delall " + targetName));
//        tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Lösche §4alle §7Reports §c(§7§lnicht rückgänging§c)").create()));
//        tc.addExtra(tc1);
//        pp.sendMessage(tc);
        pp.sendMessage(new ComponentBuilder(Bungeesystem.normal + "Reports von " + Bungeesystem.herH + targetName + " " + Bungeesystem.other2 + "(" + Bungeesystem.herH + seite + Bungeesystem.other2 + "/" + Bungeesystem.herH + allPages + Bungeesystem.other2 + ")").create());

        HistoryManager historyManager = new HistoryManager();
        List<HistoryElemt> reports = historyManager.readHistory(uuid, zeilen, seite, "report", false);
        for (HistoryElemt report : reports) {
            TextComponent tc = new TextComponent();
            tc.setText(Bungeesystem.Prefix);

            TextComponent tc1 = new TextComponent();
            tc1.setText(Bungeesystem.normal + ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Reports.Message").replace("%grund%", report.getGrund()).replace("%wer%", UUIDFetcher.getName(report.getTargetUUID())).replace("%von%", UUIDFetcher.getName(report.getVonUUID())).replace("%time%", Bungeesystem.getPlugin().formatTime(report.getErstellt()))));
            tc.addExtra(tc1);

            TextComponent tc2 = new TextComponent();
            tc2.setText(Bungeesystem.other2 + "[" + Bungeesystem.fehler + "MEHR" + Bungeesystem.other2 + "]");

            ArrayList<String> hoverArray = new ArrayList<>();
            int i = 1;
            while (true) {
                try {
                    String line = ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Reports.hover." + i).replace("%grund%", report.getGrund()).replace("%wer%", UUIDFetcher.getName(report.getTargetUUID())).replace("%von%", UUIDFetcher.getName(report.getVonUUID())).replace("%time%", Bungeesystem.getPlugin().formatTime(report.getErstellt())));
                    hoverArray.add(line);
                    i++;
                    if (i > 3)
                        break;
                } catch (Exception e1) {
                    break;
                }
            }
            tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.join("\n", hoverArray))));
            tc.addExtra(tc2);

            TextComponent tc3 = new TextComponent();
            tc3.setText(Bungeesystem.other2 + " [" + Bungeesystem.fehler + "LÖSCHEN" + Bungeesystem.other2 + "]");
            tc3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report del " + report.getErstellt()));
            tc3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text((Bungeesystem.other2 + "Lösche den Report"))));
            tc.addExtra(tc3);

            pp.sendMessage(tc);
            tc.getExtra().clear();
        }

        pfeile(eineSeiteMinus, eineSeitePlus, pp, targetName);
    }

    private void pfeile(int eineSeiteMinus, int eineSeitePlus, ProxiedPlayer pp, String targetName) {
        TextComponent tc = new TextComponent();
        tc.setText(Bungeesystem.Prefix);

        TextComponent tc1 = new TextComponent();
        tc1.setText("§f«« ");
        tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports " + targetName + " " + eineSeiteMinus));
        tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§8(§7Seite: " + Bungeesystem.herH + eineSeiteMinus + "§8)")));
        tc.addExtra(tc1);

        TextComponent tc2 = new TextComponent();
        tc2.setText(Bungeesystem.other2 + "[" + Bungeesystem.fehler + "KLICK" + Bungeesystem.other2 + "]");
        tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.fehler + "Klick auf die Pfeile!")));
        tc.addExtra(tc2);

        TextComponent tc3 = new TextComponent();
        tc3.setText(" §f»»");
        tc3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports " + targetName + " " + eineSeitePlus));
        tc3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§8(§7Seite: " + Bungeesystem.herH + eineSeitePlus + "§8)")));
        tc.addExtra(tc3);

        pp.sendMessage(tc);
    }
}
