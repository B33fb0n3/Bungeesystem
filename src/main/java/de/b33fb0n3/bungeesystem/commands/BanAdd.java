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
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.IOException;
import java.util.logging.Level;

public class BanAdd extends Command {

    public BanAdd(String name) {
        super(name);
    }

    public static int phase = 1;
    public static String grund = "";
    public static int ban = -1;
    public static int perma = -1;
    public static int dauer = -1;
    public static int report = -1;
    public static String format = "";
    public static String perm = "";
    private static int banID;
    public static boolean finished = false;
    public static ProxiedPlayer p;
    private static TextComponent hilfe = new TextComponent();
    private static TextComponent text = new TextComponent();
    private static TextComponent info = new TextComponent();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer pp = (ProxiedPlayer) sender;
            if (pp.hasPermission("bungeecord.banadd") || pp.hasPermission("bungeecord.*")) {
                if (args.length == 1) {
                    try {
                        banID = Integer.parseInt(args[0]);
                    } catch (NumberFormatException e) {
                        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Gebe eine Zahl ein!"));
                        return;
                    }
                    if (idExists(banID)) {
                        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Diese ID existiert bereits!"));
                        return;
                    }
                    p = pp;
                    startSetup();
                } else
                    pp.sendMessage(new TextComponent(Bungeesystem.helpMessage.replace("%begriff%", "banadd")));
            } else
                pp.sendMessage(new TextComponent(Bungeesystem.noPerm));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist kein Spieler!"));
    }

    private boolean idExists(int id) {
        if (!Bungeesystem.ban.getSection("BanIDs").contains(id + "")) {
            return false;
        }
        return true;
    }

    public static void finishSetup() {
        p.sendMessage(new TextComponent(Bungeesystem.Prefix + "Der Ban wurde unter der ID " + Bungeesystem.herH + banID + Bungeesystem.normal + " erstellt!"));
        finished = true;

        Bungeesystem.ban.set("BanIDs." + banID + ".Reason", grund);
        Bungeesystem.ban.set("BanIDs." + banID + ".Time", dauer);
        Bungeesystem.ban.set("BanIDs." + banID + ".Format", format);
        Bungeesystem.ban.set("BanIDs." + banID + ".Ban", ban == 1);
        Bungeesystem.ban.set("BanIDs." + banID + ".Perma", perma == 1);
        Bungeesystem.ban.set("BanIDs." + banID + ".Reportable", report == 1);
        Bungeesystem.ban.set("BanIDs." + banID + ".Permission", perm);
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(Bungeesystem.ban, Bungeesystem.banFile);
        } catch (IOException e) {
            Bungeesystem.logger().log(Level.WARNING, "cloud not update fileconfiguration for bans", e);
        }
    }

    public static void startPhase(int phase) {
        if (text.getExtra() != null)
            text.getExtra().clear();
        info.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.normal + "ID: " + Bungeesystem.herH + banID + "\n" + Bungeesystem.normal + "Perma: " + Bungeesystem.herH + (perma == -1 ? "???" : (perma == 0 ? Bungeesystem.fehler + "Nein" : Bungeesystem.normal + "Ja")) + "\n" + Bungeesystem.normal + "Status: " + Bungeesystem.herH + (ban == -1 ? "???" : (ban == 0 ? "Mute" : "Ban")) + "\n" + Bungeesystem.normal + "Grund: " + Bungeesystem.herH + (grund.equalsIgnoreCase("") ? "???" : grund) + "\n" + Bungeesystem.normal + "Dauer: " + Bungeesystem.herH + (perma == 1 ? "Permanent" : (dauer == -1 ? "???" : dauer)) + "\n" + Bungeesystem.normal + "Format: " + Bungeesystem.herH + (perma == 1 ? "Permanent" : (format.equalsIgnoreCase("") ? "???" : format.toUpperCase())) + "\n" + Bungeesystem.normal + "Report: " + Bungeesystem.herH + (report == -1 ? "???" : (report == 0 ? Bungeesystem.fehler + "Nein" : Bungeesystem.normal + "Ja")) + "\n" + Bungeesystem.normal + "Permission: " + Bungeesystem.herH + (perm == "" ? "???" : perm))));
        switch (phase) {
            case 1: // PERMA?
                text.setText(Bungeesystem.normal + "Soll der Ban " + Bungeesystem.herH + "Permanent " + Bungeesystem.normal + "sein?");
                hilfe.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.normal + "Schreibe in den Chat\n" + Bungeesystem.herH + "1 §f» " + Bungeesystem.normal + "Ja!\n" + Bungeesystem.herH + "0 §f» " + Bungeesystem.fehler + "Nein!")));
                break;
            case 2: // MUTE / BAN
                text.setText(Bungeesystem.normal + "Soll der Ban ein " + Bungeesystem.herH + "Mute " + Bungeesystem.normal + "oder ein " + Bungeesystem.herH + "Ban " + Bungeesystem.normal + "sein?");
                hilfe.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.normal + "Schreibe in den Chat\n" + Bungeesystem.herH + "1 §f» " + Bungeesystem.herH + "Ban!\n" + Bungeesystem.herH + "0 §f» " + Bungeesystem.herH + "Mute!")));
                break;
            case 3: // GRUND
                text.setText(Bungeesystem.normal + "Wie soll der " + Bungeesystem.herH + "Grund " + Bungeesystem.normal + "lauten?");
                hilfe.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.normal + "Beispiele:\n" + Bungeesystem.herH + "Ban eines Admins\n" + Bungeesystem.herH + "Hacking\n" + Bungeesystem.herH + "Hausverbot\n" + Bungeesystem.herH + "...")));
                break;
            case 4: // FORMAT
                if (perma == 1) {
                    BanAdd.phase = 6;
                    startPhase(BanAdd.phase);
                    return;
                }
                text.setText(Bungeesystem.normal + "In welchem " + Bungeesystem.herH + "Format " + Bungeesystem.herH + "soll der Ban gespeichert werden? " + Bungeesystem.other2 + "(siehe Hilfe)");
                hilfe.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.normal + "Verfügbare Formate: \n" + Bungeesystem.herH + "MIN\n" + Bungeesystem.herH + "HOUR\n" + Bungeesystem.herH + "DAY\n" + Bungeesystem.herH + "WEEK\n" + Bungeesystem.herH + "MON\n" + Bungeesystem.herH + "YEAR")));
                break;
            case 5: // DAUER
                text.setText(Bungeesystem.herH + "Wie lang " + Bungeesystem.normal + "soll der Ban gehen?");
                hilfe.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.normal + "Beispiele:\n" + Bungeesystem.herH + "1\n" + Bungeesystem.herH + "5 \n" + Bungeesystem.herH + "7 \n" + Bungeesystem.herH + "...\nDas Format wird dann automatisch rangesetzt!")));
                break;
            case 6: // REPORT
                text.setText(Bungeesystem.normal + "Soll dieser Ban als " + Bungeesystem.herH + "Report " + Bungeesystem.normal + "angegeben werden dürfen?");
                hilfe.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.normal + "Schreibe in den Chat\n" + Bungeesystem.herH + "1 §f» " + Bungeesystem.normal + "Ja!\n" + Bungeesystem.herH + "0 §f» " + Bungeesystem.fehler + "Nein!")));
                if (perma == 1) {
                    format = "HOUR";
                    dauer = 10;
                }
                break;
            case 7: // PERMISSION?
                text.setText(Bungeesystem.normal + "Braucht man eine " + Bungeesystem.herH + "Permission" + Bungeesystem.normal + ", um die Ban-ID benutzen zu können?");
                hilfe.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.normal + "Schreibe in den Chat\n" + Bungeesystem.herH + "0 §f» " + Bungeesystem.normal + "Keine!\n§f» " + Bungeesystem.normal + "Ansonsten einfach die Permission reinschreiben")));
                break;
        }
        text.addExtra(hilfe);
        text.addExtra(info);
        p.sendMessage(text);
    }

    private void startSetup() {
        reset();
        hilfe.setText(" " + Bungeesystem.other2 + "[" + Bungeesystem.fehler + "HILFE" + Bungeesystem.other2 + "]");
        info.setText(" " + Bungeesystem.other2 + "[" + Bungeesystem.fehler + "INFO" + Bungeesystem.other2 + "]");
        startPhase(phase);
    }

    private void reset() {
        phase = 1;
        grund = "";
        ban = -1;
        perma = -1;
        dauer = -1;
        report = -1;
        format = "";
        perm = "";
        finished = false;
        text = new TextComponent();
        hilfe = new TextComponent();
        info = new TextComponent();
    }

}
