package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 24.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.DBUtil;
import de.b33fb0n3.bungeesystem.utils.HistoryElemt;
import de.b33fb0n3.bungeesystem.utils.HistoryManager;
import de.b33fb0n3.bungeesystem.utils.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
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

public class Warns extends Command {

    public Warns(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer pp = (ProxiedPlayer) sender;
            if (pp.hasPermission("bungeecord.warns") || pp.hasPermission("bungeecord.*")) {
                if (args.length == 2 || args.length == 1) { // /warns <Spieler> <Seite>
                    int seite = 1;
                    try {
                        seite = Integer.parseInt(args[1]);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
                    }
                    sendHelp(seite, pp, args[0]);
                } else
                    pp.sendMessage(new TextComponent(Bungeesystem.helpMessage.replace("%begriff%", "warns")));
            } else
                pp.sendMessage(new TextComponent(Bungeesystem.noPerm));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist kein Spieler!"));
    }

    private void sendHelp(int seite, ProxiedPlayer pp, String targetName) {

        UUID uuid = UUIDFetcher.getUUID(targetName);
        int zeilen = 10;
        int allwarns = DBUtil.getWhatCount(Bungeesystem.getPlugin().getDataSource(), uuid, "warn", true);
        int allPages = allwarns / zeilen;
        int eineSeitePlus = seite + 1;
        int eineSeiteMinus = seite - 1;
        if (allwarns % zeilen > 0) {
            allPages++;
        }

        if (seite > allPages || seite == 0) {
            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Hier gibt es keine Warnungen!"));
            return;
        }
        pp.sendMessage(new TextComponent(" "));
        pp.sendMessage(new TextComponent(Bungeesystem.normal + "Warnungen von " + Bungeesystem.herH + targetName + " " + Bungeesystem.other2 + "(" + Bungeesystem.herH + seite + Bungeesystem.other2 + "/" + Bungeesystem.herH + allPages + Bungeesystem.other2 + ")"));

        HistoryManager historyManager = new HistoryManager();
        List<HistoryElemt> warns = historyManager.readHistory(uuid, zeilen, seite, "warn", false);
        for(HistoryElemt warn : warns) {
            TextComponent tc = new TextComponent();
            tc.setText(Bungeesystem.Prefix);

            TextComponent tc1 = new TextComponent();
            tc1.setText(Bungeesystem.normal + ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Warns.Message").replace("%grund%", warn.getGrund()).replace("%wer%", UUIDFetcher.getName(warn.getTargetUUID())).replace("%von%", UUIDFetcher.getName(warn.getVonUUID())).replace("%time%", Bungeesystem.formatTime(warn.getErstellt()))));
            tc.addExtra(tc1);

            TextComponent tc2 = new TextComponent();
            tc2.setText(Bungeesystem.other2 + "[" + Bungeesystem.fehler + "MEHR" + Bungeesystem.other2 + "]");
            ArrayList<String> hoverArray = new ArrayList<>();
            int i = 1;
            while (true) {
                try {
                    String line = ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Warns.hover." + i).replace("%grund%", warn.getGrund()).replace("%wer%", UUIDFetcher.getName(warn.getTargetUUID())).replace("%von%", UUIDFetcher.getName(warn.getVonUUID())).replace("%time%", Bungeesystem.formatTime(warn.getErstellt())));
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
            tc3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warn del " + warn.getErstellt()));
            tc3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text((Bungeesystem.other2 + "Lösche diese Warnung"))));
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
        tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warns " + targetName + " " + eineSeiteMinus));
        tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§8(§7Seite: " + Bungeesystem.herH + eineSeiteMinus + "§8)")));
        tc.addExtra(tc1);

        TextComponent tc2 = new TextComponent();
        tc2.setText(Bungeesystem.other2 + "[" + Bungeesystem.fehler + "KLICK" + Bungeesystem.other2 + "]");
        tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.fehler + "Klick auf die Pfeile!")));
        tc.addExtra(tc2);

        TextComponent tc3 = new TextComponent();
        tc3.setText(" §f»»");
        tc3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warns " + targetName + " " + eineSeitePlus));
        tc3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§8(§7Seite: " + Bungeesystem.herH + eineSeitePlus + "§8)")));
        tc.addExtra(tc3);

        pp.sendMessage(tc);
    }

}
