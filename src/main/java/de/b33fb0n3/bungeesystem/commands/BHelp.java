package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 17.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;

public class BHelp extends Command {

    public BHelp(String name) {
        super(name);
    }

    private List<String> begriffe = Arrays.asList("report", "reports", "ban", "bans", "changeid", "banadd", "banremove", "unban", "history", "reset", "check", "warn", "warns", "kick", "accounts", "ip", "blacklist", "chatlog", "chatlogs", "support", "teamchat", "tc", "editban", "feedback", "bug", "onlinezeit");
    private List<String> begriffeALLG = Arrays.asList("Report/s", "Ban/s", "Check", "Warn/s", "Accounts/IP", "Blacklist", "Other");

    // ALLGEMEINE ÜBERSICHT
    /*
    -> Report/s [MEHR]
    -> Ban/s -> ChangeID -> BanADD/BanREMOVE -> Unban -> kick -> editban
    -> check -> Reset -> history -> Onlinezeit
    -> Warn/s
    -> Accounts/IP
    -> Blacklist -> chatlog/s -> support
    -> Other -> Teamchat -> Feedback -> Bug
     */

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer pp = (ProxiedPlayer) sender;
            if (pp.hasPermission("bungeecord.bhelp") || pp.hasPermission("bungeecord.*")) {
                if (args.length == 0) {
                    TextComponent tc1 = new TextComponent();
                    for (String begriff : begriffeALLG) {
                        tc1.setText(Bungeesystem.Prefix + begriff + " ");
                        TextComponent hover = new TextComponent();
                        hover.setText(Bungeesystem.other2 + "[" + Bungeesystem.fehler + "MEHR" + Bungeesystem.other2 + "]");
                        switch (begriff) {
                            case "Report/s":
                                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§f» " + Bungeesystem.other2 + "Report\n§f» " + Bungeesystem.other2 + "Reports")));
                                hover.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bhelp reports reports"));
                                break;
                            case "Ban/s":
                                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§f» " + Bungeesystem.other2 + "Ban\n§f» " + Bungeesystem.other2 + "Bans\n§f» " + Bungeesystem.other2 + "ChangeID\n§f» " + Bungeesystem.other2 + "BanADD\n§f» " + Bungeesystem.other2 + "BanREMOVE\n§f» " + Bungeesystem.other2 + "Unban\n§f» " + Bungeesystem.other2 + "Kick\n§f» " + Bungeesystem.other2 + "Editban")));
                                hover.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bhelp bans bans"));
                                break;
                            case "Check":
                                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§f» " + Bungeesystem.other2 + "Check\n§f» " + Bungeesystem.other2 + "Reset\n§f» " + Bungeesystem.other2 + "History\n§f» " + Bungeesystem.other2 + "Onlinezeit")));
                                hover.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bhelp check check"));
                                break;
                            case "Warn/s":
                                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§f» " + Bungeesystem.other2 + "Warn\n§f» " + Bungeesystem.other2 + "Warns")));
                                hover.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bhelp warns warns"));
                                break;
                            case "Accounts/IP":
                                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§f» " + Bungeesystem.other2 + "Accounts\n§f» " + Bungeesystem.other2 + "IP")));
                                hover.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bhelp accounts accounts"));
                                break;
                            case "Blacklist":
                                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§f» " + Bungeesystem.other2 + "Blacklist\n§f» " + Bungeesystem.other2 + "Chatlog\n§f» " + Bungeesystem.other2 + "Chatlogs\n§f» " + Bungeesystem.other2 + "Support")));
                                hover.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bhelp blacklist blacklist"));
                                break;
                            case "Other":
                                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§f» " + Bungeesystem.other2 + "Teamchat\n" + "§f» " + Bungeesystem.other2 + "Feedback\n" + "§f» " + Bungeesystem.other2 + "Bug")));
                                hover.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bhelp other other"));
                                break;
                            default:
                                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§cERROR")));
                        }
                        tc1.addExtra(hover);
                        pp.sendMessage(tc1);
                        if (tc1.getExtra() != null)
                            tc1.getExtra().clear();
                    }
                } else if (args.length == 1) {
                    if (begriffe.contains(args[0].toLowerCase())) {
                        getInfo(args[0], pp);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (String begriff : begriffe)
                            sb.append(Bungeesystem.herH + begriff + Bungeesystem.normal + ", ");
                        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Verwende einen dieser Begriffe: " + sb.toString().substring(0, sb.length() - 2)));
                    }
                } else if (args.length == 2) {
                    switch (args[1]) {
                        case "reports":
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp report");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp reports");
                            break;
                        case "bans":
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp ban");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp bans");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp changeid");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp banadd");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp banremove");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp unban");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp kick");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp editban");
                            break;
                        case "check":
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp check");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp reset");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp history");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp onlinezeit");
                            break;
                        case "warns":
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp warn");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp warns");
                            break;
                        case "accounts":
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp accounts");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp ip");
                            break;
                        case "blacklist":
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp blacklist");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp chatlog");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp chatlogs");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp support");
                            break;
                        case "other":
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp tc");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp feedback");
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "bhelp bug");
                            break;
                        default:
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Diese Seite wurde nicht gefunden!"));
                            break;
                    }
                } else
                    pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutze: " + Bungeesystem.other + "/bhelp <Begriff>" + Bungeesystem.other2 + " oder " + Bungeesystem.other + "/bhelp"));
            } else
                pp.sendMessage(new TextComponent(Bungeesystem.noPerm));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist kein Spieler!"));
    }

    private void getInfo(String begriff, ProxiedPlayer pp) {
        begriff = begriff.toLowerCase();
        TextComponent funktion = new TextComponent();
        funktion.setText("ERROR");
        TextComponent hover = new TextComponent();
        hover.setText(Bungeesystem.other2 + " [" + Bungeesystem.fehler + "MEHR" + Bungeesystem.other2 + "]");
        switch (begriff) {
            case "report":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Report");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/report <Spieler> <Grund> §f » " + Bungeesystem.other + "bungeecord.report.create\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Melde andere Spieler \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + Bungeesystem.settings.getLong("Cooldown.Report.time") + " " + Bungeesystem.settings.getString("Cooldown.Report.format").toLowerCase() + " Cooldown \n" +
                                Bungeesystem.herH + "§l/report <login/logout> §f » " + Bungeesystem.other + "bungeecord.report.login\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Logge dich ein/aus\n" +
                                Bungeesystem.herH + "§lUm Reports zu sehen: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Eingeloggt sein\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "bungeecord.report.see\n" +
                                Bungeesystem.herH + "§lSonstige Permissions/Infos: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "bungeecord.report.tp §f➤ " + Bungeesystem.other + "um sich zu Reports zu teleportieren\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "bungeecord.report.del §f➤ " + Bungeesystem.other + "um Reports löschen zu können\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "bungeecord.report.autologin §f➤ " + Bungeesystem.other + "um automatisch eingeloggt zu werden\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Die Report Message kannst du in der settings.yml anpassen"
                )));
                break;
            case "reports":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Reports");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/reports §f » " + Bungeesystem.other + "bungeecord.reports\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Damit kannst du die letzten 10 Reports sehen \n" +
                                Bungeesystem.herH + "§l/reports <Spieler> <Seite> §f » " + Bungeesystem.other + "bungeecord.reports\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Damit kannst du alle Reports vom Spieler sehen\n" +
                                Bungeesystem.herH + "§lSonstige Informationen: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Hier kannst du Reports löschen"
                )));
                break;
            case "ban":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Ban");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/ban <Spieler> <BanID> §f » " + Bungeesystem.other + "bungeecord.ban\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Entferne böse Spieler vom Server \n" +
                                Bungeesystem.herH + "§l/unban <Spieler> §f » " + Bungeesystem.other + "bungeecord.unban\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Entbannt einen Spieler\n" +
                                Bungeesystem.herH + "§lUm Bans zu sehen: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "bungeecord.informations\n" +
                                Bungeesystem.herH + "§lSonstige Permissions/Infos: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Unendlich BanIDs\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Ausgabe umstellbar in der settings.yml\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Multiplizierung der Banzeit nach dem ersten Ban\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Sortierte Ausgabe der BanIDs\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Man sieht nur die Bans, für die man auch bannen darf\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "bungeecord.ban.information §f➤ " + Bungeesystem.other + "damit bekommt man eine gekürzte Information über einen Ban (perfekt für die User geeignet)"
                )));
                break;
            case "bans":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Bans");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/bans <Spieler> <Seite> §f » " + Bungeesystem.other + "bungeecord.bans\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeige alle Bans die ein Spieler erhalten hat \n" +
                                Bungeesystem.herH + "§lSonstige Informationen: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Ihr könnte direkt auf unterschiedliche Seiten gehen\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Springe eine Seite vor/zurück indem du auf die Pfeile klickst"
                )));
                break;
            case "changeid":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "ChangeID");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/changeid <alteID> <neueID> §f » " + Bungeesystem.other + "bungeecord.changeid\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Ändere die ID eines Bans \n" +
                                Bungeesystem.herH + "§lSonstige Informationen: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Sollte auf der neuen ID schon ein anderer Ban Grund sein, werden beide getauscht"
                )));
                break;
            case "banadd":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "BanADD");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/banadd <Ban-ID> §f » " + Bungeesystem.other + "bungeecord.banadd\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Erstelle neue Ban Gründe \n" +
                                Bungeesystem.herH + "§lSonstige Informationen: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Ihr werdet durch eine Art \"Tutorial\" geleitet\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Solltet ihr was falsches in den Chat schreiben, könnt ihr es einfach wiederholen"
                )));
                break;
            case "banremove":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "BanREMOVE");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/banremove <Ban-ID> §f » " + Bungeesystem.other + "bungeecord.banremove\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Lösche alte Ban Gründe \n" +
                                Bungeesystem.herH + "§lSonstige Informationen: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Schreibt ihr anstatt der ID \"all\", werden alle Ban-Gründe gelöscht"
                )));
                break;
            case "unban":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Unban");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/unban <Spieler> §f » " + Bungeesystem.other + "bungeecord.unban\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Entbannte gebannte Spieler \n" +
                                Bungeesystem.herH + "§lUm Unbans zu sehen: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "bungeecord.informations"
                )));
                break;
            case "history":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "History");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/history§f » " + Bungeesystem.other + "bungeecord.history\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt die letzten 10 Ereignisse\n" +
                                Bungeesystem.herH + "§l/history <Spieler> <Seite>§f » " + Bungeesystem.other + "bungeecord.history\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt alle Vorkommnisse eines Spielers\n" +
                                Bungeesystem.herH + "§lSonstige Informationen: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Einträge werden nach aktualität sortiert ausgegeben"
                )));
                break;
            case "reset":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Reset");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/reset <Spieler>§f » " + Bungeesystem.other + "bungeecord.reset\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Setze einen Spieler zurück\n" +
                                Bungeesystem.herH + "§lSonstige Informationen: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Vom Spieler werden fast alle Daten gelöscht"
                )));
                break;
            case "check":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Check");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/check <Spieler>§f » " + Bungeesystem.other + "bungeecord.check\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Informiere dich über einen Spieler\n" +
                                Bungeesystem.herH + "§lSonstige Informationen: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Die Informationen kannst du auch in der settings.yml anpassen"
                )));
                break;
            case "warn":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Warn");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/warn <Spieler> <Grund>§f » " + Bungeesystem.other + "bungeecord.warn\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Warne einen Spieler\n" +
                                Bungeesystem.herH + "§lSonstige Permissions/Infos: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "bungeecord.informations §f➤ " + Bungeesystem.other + "damit bekommt man über eine Warnung eine Benachrichtigung\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Ein Spieler kann " + Bungeesystem.settings.getInt("Warns.MaxWarns") + " Warnungen erhalten\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Du kannst den Grund auch weglassen\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Die Kick-Nachricht kann in der settings.yml angepasst werden"
                )));
                break;
            case "warns":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Warns");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/warns <Spieler> <Seite>§f » " + Bungeesystem.other + "bungeecord.warns\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Schau dir alle erhaltenen Warns eines Spielers an\n" +
                                Bungeesystem.herH + "§lSonstige Permissions: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "bungeecord.warn.del §f➤ " + Bungeesystem.other + "damit kannst du Warnungen löschen"
                )));
                break;
            case "kick":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Kick");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/kick <Spieler> <Grund>§f » " + Bungeesystem.other + "bungeecord.kick\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Schmeiß eine Spieler vom Server\n" +
                                Bungeesystem.herH + "§lSonstige Permissions/Infos: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "bungeecord.informations §f➤ " + Bungeesystem.other + "damit bekommt man über eine Warnung eine Benachrichtigung\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Die Kick-Nachricht kann in der settings.yml angepasst werden\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Im Grund könnt ihr Colorcodes verwenden"
                )));
                break;
            case "accounts":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Accounts");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/accounts <Spieler>§f » " + Bungeesystem.other + "bungeecord.accounts\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt mögliche Alt-Accounts eines Spielers\n" +
                                Bungeesystem.herH + "§l/accounts§f » " + Bungeesystem.other + "bungeecord.accounts\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt mögliche Alt-Accounts\n" +
                                Bungeesystem.herH + "§lSonstige Informationen: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Wenn ihr die Alt-Accounts eines Spielers zeigt, könnt ihr diese auch mit einem Klick, auf ihren Namen, bannen\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Die Kick-Nachricht kann in der settings.yml angepasst werden"
                )));
                break;
            case "ip":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "IP");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/ip <Spieler>§f » " + Bungeesystem.other + "bungeecord.ip\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeige die IP eines Spielers"
                )));
                break;
            case "blacklist":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Blacklist");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/blacklist <add/remove/list> <Wort>§f » " + Bungeesystem.other + "bungeecord.blacklist\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Passe die verbotenen Wörter an\n" +
                                Bungeesystem.herH + "§lSonstige Permissions/Infos: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "bungeecord.blackWords.bypass §f➤ " + Bungeesystem.other + "damit man alles schreiben kann\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "bungeecord.blackWords.info §f➤ " + Bungeesystem.other + "damit man eine Info bekommt, wenn jemand etwas verbotenes schreibt\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Die Blacklist verhindert das schreiben von verbotenen Wörtern"
                )));
                break;
            case "chatlog":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Chatlog");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/chatlog <Spieler>§f » " + Bungeesystem.other + "bungeecord.chatlogs.create\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Erstelle einen Chatlog über einen Spieler\n" +
                                Bungeesystem.herH + "§lSonstige Permissions/Infos: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "bungeecord.chatlog.see §f➤ " + Bungeesystem.other + "Damit siehst du direkt die Chatlogs, wenn einer erstellt wurde\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Es werden die letzten Nachrichten eines Spielers in den Chatlog getan"
                )));
                break;
            case "chatlogs":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Chatlogs");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/chatlogs§f » " + Bungeesystem.other + "bungeecord.chatlogs\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt die zuletzt erstellten Chatlogs\n" +
                                Bungeesystem.herH + "§lSonstige Informationen: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Solltet ihr mal keinen Link bekommen, ist hasteb.in offline. Wartet einfach ein wenig, dann geht der Server wieder Online"
                )));
                break;
            case "support":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Support");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/support <Betreff>§f » " + Bungeesystem.other + "bungeecord.support.create\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Tritt der Support Warteschlange bei\n" +
                                Bungeesystem.herH + "§l/support accept§f » " + Bungeesystem.other + "bungeecord.support.accept\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Damit kannst du ein Supportgespräch annehmen\n" +
                                Bungeesystem.herH + "§l/support list§f » " + Bungeesystem.other + "bungeecord.support.accept\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Liste alle offenen Supportgespärche auf\n" +
                                Bungeesystem.herH + "§lSonstige Informationen: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Es wird immer das Supportgespräch genommen, das schon am längsten \"wartet\""
                )));
                break;
            case "teamchat": case "tc":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Teamchat");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/tc <Nachricht>§f » " + Bungeesystem.other + "bungeecord.tc.send\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Versende Nachrichten an dein Team.\n" +
                                Bungeesystem.herH + "§l/tc <login/logout>§f » " + Bungeesystem.other + "bungeecord.tc.login\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Damit kannst du dich ein bzw. aus-loggen\n" +
                                Bungeesystem.herH + "§lSonstige Permissions/Infos: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "bungeecord.tc.autologin §f➤ " + Bungeesystem.other + "Damit wirst du automatisch, in den Teamchat, eingeloggt\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Du kannst als Befehl auch /teamchat nutzen"
                )));
                break;
            case "editban":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Editban");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/editban <Name> <Type> <Value>§f » " + Bungeesystem.other + "bungeecord.editban.edit\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Editiere einen Ban.\n"+
                                Bungeesystem.herH + "§lSonstige Informationen: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Eine Liste der Types bekommst du, wenn du nur den Namen eingibst\n"+
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Möchtest du die Bandauer auf \"Permanent\" stellen, dann stelle die Bandauer auf \"-1\""
                )));
                break;
            case "feedback":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Feedback");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/feedback§f » " + Bungeesystem.other + "bungeecord.feedback\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Hinterlass ein Feedback."
                )));
                break;
            case "bug":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Bug");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/bug§f » " + Bungeesystem.other + "bungeecord.bug\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Melde eine Bug direkt."
                )));
                break;
            case "onlinezeit":
                funktion.setText(Bungeesystem.normal + "Funktion: " + Bungeesystem.herH + "Onlinezeit");

                hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                        Bungeesystem.other2 + "Benutzung: \n" +
                                Bungeesystem.herH + "§l/onlinezeit§f » " + Bungeesystem.other + "bungeecord.onlinezeit.other\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt alle Spieler, die heute online waren bzw. sind\n"+
                                Bungeesystem.herH + "§l/onlinezeit§f » " + Bungeesystem.other + "bungeecord.onlinezeit.own\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Damit siehst du, wie lange du heute schon online warst\n"+
                                Bungeesystem.herH + "§l/onlinezeit <Datum>§f » " + Bungeesystem.other + "bungeecord.onlinezeit.datum\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt alle Spieler, die am Datum online waren\n"+
                                Bungeesystem.herH + "§l/onlinezeit <Spieler>§f » " + Bungeesystem.other + "bungeecord.onlinezeit.player\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt, wie lange der Spieler heute online war\n"+
                                Bungeesystem.herH + "§l/onlinezeit <Spieler> <Datum>§f » " + Bungeesystem.other + "bungeecord.onlinezeit.player.datum\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt dir, wie lang der Spieler an einem Datum online war\n" +
                                Bungeesystem.herH + "§l/onlinezeit total§f » " + Bungeesystem.other + "bungeecord.onlinezeit.total\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt dir, wie lange du insgesamt online warst\n" +
                                Bungeesystem.herH + "§l/onlinezeit total <Spieler>§f » " + Bungeesystem.other + "bungeecord.onlinezeit.total.other\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt dir, wie lang der Spieler insgesamt online war\n" +
                                Bungeesystem.herH + "§l/onlinezeit total top§f » " + Bungeesystem.other + "bungeecord.onlinezeit.total.top\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt dir, die 3 'besten' Spieler insgesamt an\n" +
                                Bungeesystem.herH + "§l/onlinezeit week§f » " + Bungeesystem.other + "bungeecord.onlinezeit.week.own\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt dir, eine Übersicht der Woche von dir\n" +
                                Bungeesystem.herH + "§l/onlinezeit week <Spieler>§f » " + Bungeesystem.other + "bungeecord.onlinezeit.week.other\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt dir, eine Übersicht der Woche eines Spielers\n" +
                                Bungeesystem.herH + "§l/onlinezeit week top§f » " + Bungeesystem.other + "bungeecord.onlinezeit.week.top\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt dir, die 3 'besten' Spieler der Woche an\n" +
                                Bungeesystem.herH + "§l/onlinezeit trend§f » " + Bungeesystem.other + "bungeecord.onlinezeit.trend.own\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt dir, deinen Trend der letzten 7 Tage\n" +
                                Bungeesystem.herH + "§l/onlinezeit trend <Tage>§f » " + Bungeesystem.other + "bungeecord.onlinezeit.trend.days\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt dir, deinen Trend für X Tage\n" +
                                Bungeesystem.herH + "§l/onlinezeit trend <Spieler>§f » " + Bungeesystem.other + "bungeecord.onlinezeit.trend.other\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt dir, einen Trend eines Spielers\n" +
                                Bungeesystem.herH + "§l/onlinezeit trend <Spieler> <Tage>§f » " + Bungeesystem.other + "bungeecord.onlinezeit.other.days\n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "Zeigt dir, einen Trend eines Spielers für X Tage\n" +
                                Bungeesystem.herH + "§lSonstige Permissions/Infos: \n" +
                                Bungeesystem.other2 + " ● " + Bungeesystem.other + "bungeecord.onlinezeit.* §f➤ " + Bungeesystem.other + "Damit hast du alle Permissions für die Onlinezeit."
                )));
                break;
            default:
                break;
        }
        funktion.addExtra(hover);
        pp.sendMessage(funktion);
    }
}
