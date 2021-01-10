package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 29.12.2020
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.Cooldowns;
import de.b33fb0n3.bungeesystem.utils.RangManager;
import de.b33fb0n3.bungeesystem.utils.ReportManager;
import de.b33fb0n3.bungeesystem.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Report extends Command {

    public Report(String name) {
        super(name);
    }

    private List<String> sendReports = Bungeesystem.getPlugin().getSendReports();
    private List<String> reasons = new ArrayList<>();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer pp = (ProxiedPlayer) sender;
                ReportManager reportManager = new ReportManager(pp);
            if (args.length == 1) {
                if (pp.hasPermission("bungeecord.report.login") || pp.hasPermission("bungeecord.*")) {
                    if (args[0].equalsIgnoreCase("login")) {
                        if (!reportManager.isLoggedIn(sendReports))
                            reportManager.login(sendReports);
                        else {
                            pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist bereits eingeloggt!").create());
                            return;
                        }
                        pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + "Du hast dich eingeloggt").create());
                    } else if (args[0].equalsIgnoreCase("logout")) {
                        if (reportManager.isLoggedIn(sendReports))
                            reportManager.logout(sendReports);
                        else {
                            pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist gar nicht eingeloggt!").create());
                            return;
                        }
                        pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + "Du hast dich ausgeloggt").create());
                    } else
                        pp.sendMessage(new ComponentBuilder(Bungeesystem.helpMessage.replace("%begriff%", "report")).create());
                } else
                    pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutze: " + Bungeesystem.other + "/report <Spieler> <Grund>").create());
                return;
            }
            if (args.length == 2) {
                if (pp.hasPermission("bungeecord.report.create") || pp.hasPermission("bungeecord.*")) {
                    if (args[0].equalsIgnoreCase("tp")) {
                        if (pp.hasPermission("bungeecord.report.tp") || pp.hasPermission("bungeecord.*")) {
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                            if (target == null) {
                                pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Dieser Spieler ist nicht auf dem Netzwerk!").create());
                                return;
                            }
                            pp.connect(target.getServer().getInfo());
                        } else {
                            pp.sendMessage(new ComponentBuilder(Bungeesystem.noPerm).create());
                        }
                        return;
                    } else if (args[0].equalsIgnoreCase("del")) {
                        if (pp.hasPermission("bungeecord.report.del") || pp.hasPermission("bungeecord.*")) {
                            String id = args[1];
                            if(reportManager.deleteReport(id)) {
                                pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + "Der Report wurde gelöscht!").create());
                            } else
                                pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Ein Fehler ist aufgetreten!").create());
                        } else {
                            pp.sendMessage(new ComponentBuilder(Bungeesystem.noPerm).create());
                        }
                        return;
                    }
//                    else if (args[0].equalsIgnoreCase("delall")) {
//                        if (pp.hasPermission("bungeecord.report.delall") || pp.hasPermission("bungeecord.*")) {
//                            MySQL.deleteAllReports(UUIDFetcher.getUUID(args[1]));
//                            pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + "Alle Reports wurden von §b" + args[1] + " §agelöscht!");
//                            return;
//                        } else
//                            pp.sendMessage(new ComponentBuilder(Bungeesystem.noPerm);
//                    }
                    Cooldowns cooldown = new Cooldowns("Report", pp);
                    if (cooldown.isOnCooldown()) {
                        pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Du kannst deinen nächsten Report gleich erst senden!").create());
                        return;
                    }
                    loadArrayList();
                    String grund = args[1];
                    if (!reasons.contains(grund)) {
                        pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Verwende folgende Gründe: ").create());
                        StringBuilder sb = new StringBuilder();
                        for (String s : reasons) {
                            sb.append(s + Bungeesystem.other2 + ", " + Bungeesystem.normal);
                        }
                        pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + sb.toString().substring(0, sb.length() - 6)).create());
                        return;
                    }
                    UUID pt = UUIDFetcher.getUUID(args[0]);
                    if (pt == null) {
                        sender.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Dieser Spieler existiert nicht!").create());
                        return;
                    }
                    if (Bungeesystem.settings.getBoolean("Toggler.power")) {
                        RangManager rangManager = new RangManager(pp, Bungeesystem.getPlugin().getDataSource());
                        if (!(rangManager.getPower(pp.getUniqueId()) >= rangManager.getPower(pt))) {
                            pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Diesen Spieler darfst du nicht reporten!").create());
                            return;
                        }
                    }
                    if (pp.getUniqueId() == pt) {
                        pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Du kannst dich nicht selbst reporten!").create());
                        return;
                    }
                    reportManager = new ReportManager(grund, pt, pp, System.currentTimeMillis());
                    reportManager.sendReport(sendReports);
                    reportManager.insertReportInDB();
                    pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.normal + "Dein Report wird nun weitergeleitet!").create());
                    if (Bungeesystem.settings.getBoolean("Cooldown.Report.aktive"))
                        cooldown.startCooldown();
                } else
                    pp.sendMessage(new ComponentBuilder(Bungeesystem.noPerm).create());
            } else
                pp.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutze: " + Bungeesystem.other + "/report <Spieler> <Grund>").create());
        } else
            sender.sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist kein Spieler!").create());
    }

    private void loadArrayList() {
        reasons.clear();
        for (String banID : Bungeesystem.ban.getSection("BanIDs").getKeys()) {
            if (Bungeesystem.ban.getBoolean("BanIDs." + banID + ".Reportable"))
                reasons.add(Bungeesystem.ban.getString("BanIDs." + banID + ".Reason"));
        }
        if (reasons.size() <= 0) {
            reasons.add(Bungeesystem.fehler + "Keine Gefunden!");
        }
    }
}
