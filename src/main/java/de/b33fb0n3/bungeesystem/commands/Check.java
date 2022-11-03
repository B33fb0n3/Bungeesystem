package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 17.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.*;
import de.b33fb0n3.bungeesystem.utils.Ban;
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
import java.util.UUID;
import java.util.logging.Level;

public class Check extends Command {

    public Check(String name) {
        super(name);
    }

    private TextComponent tc;
    private TextComponent tc1;
    private boolean targetConnected;

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("bungeecord.check") || sender.hasPermission("bungeecord.*")) {
            if (args.length == 1) {
                UUID ut = UUIDFetcher.getUUID(args[0]);
                if (ut == null) {
                    sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Gebe eine richtige Person ein!"));
                    return;
                }
                ProxiedPlayer pt = ProxyServer.getInstance().getPlayer(ut);
                if (pt == null)
                    targetConnected = false;
                else if (pt.isConnected())
                    targetConnected = true;
                sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.normal + "Checke " + Bungeesystem.herH + args[0]));
                check(ut, sender);
            } else
                sender.sendMessage(new TextComponent(Bungeesystem.helpMessage.replace("%begriff%", "check")));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.noPerm));
    }

    private void resetTC() {
        tc = new TextComponent();
        tc1 = new TextComponent();
    }

    private void check(UUID ut, CommandSender sender) {
        Ban ban = new Ban(ut, null, Bungeesystem.getPlugin().getDataSource(), Bungeesystem.settings, Bungeesystem.standardBans);
        resetTC();
        if (tc.getExtra() != null)
            tc.getExtra().clear();
        ban.isBanned().whenComplete((result, ex) -> {
            if (result) {
                tc.setText(Bungeesystem.Prefix + ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Check.status").replace("%status%", (ban.getBan() == 0 ? "Gemutet" : "Gebannt"))));
                String hover1 = Bungeesystem.settings.getString("Check.hover.1");
                String hover2 = Bungeesystem.settings.getString("Check.hover.2");
                String hover3 = Bungeesystem.settings.getString("Check.hover.3");
                String hover4 = Bungeesystem.settings.getString("Check.hover.4");
                if (sender instanceof ProxiedPlayer) {
                    tc1.setText(Bungeesystem.other2 + " [" + Bungeesystem.fehler + "MEHR" + Bungeesystem.other2 + "]");
                    if (!ban.getBeweis().equals("/"))
                        tc1.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, ban.getBeweis()));
                    tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.translateAlternateColorCodes('&', hover1 + "\n" + hover2 + "\n" + hover3 + "\n" + hover4 + (!ban.getBeweis().equals("/") ? ("\n" + Bungeesystem.normal + "Klick um den " + Bungeesystem.herH + "Beweislink" + Bungeesystem.normal + " zu öffnen!") : "\n" + Bungeesystem.fehler + "Kein Beweislink angegeben")).replace("%name%", ban.getVonName()).replace("%reason%", ban.getGrund()).replace("%bis%", ban.getBis() == -1 ? "§3Permanent" : Bungeesystem.formatTime(ban.getBis())).replace("%editby%", ban.getEditBy()))));
                } else {
                    tc1.setText(ChatColor.translateAlternateColorCodes('&', "\n" + hover1 + "\n" + hover2 + "\n" + hover3 + "\n" + hover4 + "\n" + Bungeesystem.normal + "Beweis: " + Bungeesystem.herH + ban.getBeweis()).replace("%name%", ban.getVonName()).replace("%reason%", ban.getGrund()).replace("%bis%", ban.getBis() == -1 ? "§3Permanent" : Bungeesystem.formatTime(ban.getBis())).replace("%editby%", ban.getEditBy()));
                }
                tc.addExtra(tc1);
            } else
                tc.setText(Bungeesystem.Prefix + ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Check.status").replace("%status%", "Nicht Gebannt/Gemuted")));
            sender.sendMessage(tc);

            resetTC();
            if (tc.getExtra() != null)
                tc.getExtra().clear();
            DBUtil.getWhatCount(Bungeesystem.getPlugin().getDataSource(), ut, "report", true).whenComplete((reports, exe) -> {

                tc.setText(Bungeesystem.Prefix + ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Check.reports").replace("%reportCount%", (reports == -1 || reports == 0 ? "§cKeine" : String.valueOf(reports)))));
                if (reports >= 1) {
                    tc1.setText(Bungeesystem.other2 + " [" + Bungeesystem.fehler + "MEHR" + Bungeesystem.other2 + "]");
                    tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.other2 + "(" + Bungeesystem.fehler + "Click" + Bungeesystem.other2 + ")")));
                    tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports " + UUIDFetcher.getName(ut) + " 1"));
                    if (sender instanceof ProxiedPlayer)
                        tc.addExtra(tc1);
                }
                sender.sendMessage(tc);
                resetTC();

                if (tc.getExtra() != null)
                    tc.getExtra().clear();
                DBUtil.getWhatCount(Bungeesystem.getPlugin().getDataSource(), ut, "warn", true).whenComplete((warns, exc) -> {
                    tc.setText(Bungeesystem.Prefix + ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Check.warns").replace("%warnsCount%", (warns == -1 || warns == 0 ? "§cKeine" : String.valueOf(warns)))));
                    if (warns != 0) {
                        tc1.setText(Bungeesystem.other2 + " [" + Bungeesystem.fehler + "MEHR" + Bungeesystem.other2 + "]");
                        tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.other2 + "(" + Bungeesystem.fehler + "Click" + Bungeesystem.other2 + ")")));
                        tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warns " + UUIDFetcher.getName(ut)));
                        if (sender instanceof ProxiedPlayer)
                            tc.addExtra(tc1);
                    }
                    sender.sendMessage(tc);

                    resetTC();
                    if (tc.getExtra() != null)
                        tc.getExtra().clear();
                    DBUtil.getWhatCount(Bungeesystem.getPlugin().getDataSource(), ut, "ban", true).whenComplete((bans, exception) -> {
                        tc.setText(Bungeesystem.Prefix + ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Check.bans").replace("%bansCount%", (bans == -1 || bans == 0 ? "§cKeine" : String.valueOf(bans)))));
                        if (bans != 0) {
                            tc1.setText(Bungeesystem.other2 + " [" + Bungeesystem.fehler + "MEHR" + Bungeesystem.other2 + "]");
                            tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.other2 + "(" + Bungeesystem.fehler + "Click" + Bungeesystem.other2 + ")")));
                            tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bans " + UUIDFetcher.getName(ut)));
                            if (sender instanceof ProxiedPlayer)
                                tc.addExtra(tc1);
                        }
                        sender.sendMessage(tc);

                        resetTC();
                        if (tc.getExtra() != null)
                            tc.getExtra().clear();
                        int historyCount = bans + warns + reports;
                        tc.setText(Bungeesystem.Prefix + ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Check.history").replace("%historyCount%", (historyCount == -1 || historyCount == 0 ? "§cKeine" : String.valueOf(historyCount)))));
                        if (historyCount != 0) {
                            tc1.setText(Bungeesystem.other2 + " [" + Bungeesystem.fehler + "MEHR" + Bungeesystem.other2 + "]");
                            tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.other2 + "(" + Bungeesystem.fehler + "Click" + Bungeesystem.other2 + ")")));
                            tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/history " + UUIDFetcher.getName(ut)));
                            if (sender instanceof ProxiedPlayer)
                                tc.addExtra(tc1);
                        }
                        sender.sendMessage(tc);

                        resetTC();
                        //                        STATS
                        if (tc.getExtra() != null)
                            tc.getExtra().clear();
                        tc.setText(Bungeesystem.Prefix + Bungeesystem.normal + ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Check.stats")));

                        ArrayList<String> hoverArray = new ArrayList<>();
                        int i = 1;
                        hoverArray.add(Bungeesystem.fehler + "Dieser Spieler war noch nie auf dem Netzwerk!");
                        Playerdata playerdata = new Playerdata(ut);
                        while (true) {
                            try {
                                String line = ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Check.hover2." + i)).replace("%ip%", (playerdata.getLastip() == null || playerdata.getLastip().equals("")) ? Bungeesystem.fehler + "War noch nie hier :/" : ((sender.hasPermission("bungeecord.ip") || sender.hasPermission("bungeecord.*")) ? playerdata.getLastip() : "§k123.123.123.123")).replace("%firstJoin%", playerdata.getFirstjoin() == 0 ? Bungeesystem.fehler + "War noch nie hier :/" : (Bungeesystem.formatTime(playerdata.getFirstjoin()))).replace("%lastOnline%", playerdata.getLastonline() == 0 ? Bungeesystem.fehler + "War noch nie hier :/" : (playerdata.getLastonline() == -1 ? "Ist das erste mal hier ;)" : (targetConnected ? "Ist gerade Online :)" : Bungeesystem.formatTime(playerdata.getLastonline())))).replace("%reportsMade%", playerdata.getReportsMade() + "").replace("%warnsReceive%", playerdata.getWarnsReceive() + "").replace("%bansReceive%", playerdata.getBansReceive() + "").replace("%warnsMade%", playerdata.getWarnsMade() + "").replace("%bansMade%", playerdata.getBansMade() + "").replace("%power%", Bungeesystem.settings.getBoolean("Toggler.power") ? String.valueOf(playerdata.getPower()) : Bungeesystem.fehler + "Nicht aktiviert!");
                                hoverArray.add(line);
                                i++;
                                if (i > 9) {
                                    hoverArray.remove(0);
                                    break;
                                }
                            } catch (Exception e1) {
                                break;
                            }
                        }

                        tc1.setText(Bungeesystem.other2 + "[" + Bungeesystem.fehler + "MEHR" + Bungeesystem.other2 + "]");
                        tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.join("\n", hoverArray))));
                        tc.addExtra(tc1);

                        de.b33fb0n3.bungeesystem.utils.Onlinezeit onlinezeit = new de.b33fb0n3.bungeesystem.utils.Onlinezeit(sender, Bungeesystem.getPlugin().getDataSource());
                        if (!(sender instanceof ProxiedPlayer)) {
                            String h = hoverArray.get(0);
                            h = "\n" + h + "\n";
                            hoverArray.remove(0);
                            sender.sendMessage(new TextComponent(tc.getText() + h + String.join("\n", hoverArray)));

//                            ONLINEZEIT CONSOLE
                            onlinezeit.sendTrend(ut, 7, true, ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Check.onlinezeit")));
                            return;
                        }

                        sender.sendMessage(tc);
                        resetTC();

//                        ONLINEZEIT
                        onlinezeit.sendTrend(ut, 7, false, ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("Check.onlinezeit")));
                    });
                });
            });
        });
    }
}
