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
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.IOException;
import java.util.logging.Level;

public class ChangeID extends Command {

    public ChangeID(String name) {
        super(name);
    }

    private String reason, reason2;
    private int time, time2;
    private String format, format2;
    private boolean ban, ban2;
    private boolean perma, perma2;
    private boolean report, report2;

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            final ProxiedPlayer pp = (ProxiedPlayer) sender;
            if(pp.hasPermission("bungeecord.changeid") || pp.hasPermission("bungeecord.*")) {
                if(args.length == 2) {
                    try {
                        int oldID = Integer.parseInt(args[0]);
                        int newID = Integer.parseInt(args[1]);
                        if(idExists(oldID)) {
                            setNewID(oldID, newID, pp);
                        } else
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler+"Die ID "+Bungeesystem.herH+oldID+Bungeesystem.fehler+" existiert nicht!"));
                    } catch (NumberFormatException e) {
                        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler+"Gebe eine Zahl ein!"));
                    }
                } else
                    pp.sendMessage(new TextComponent(Bungeesystem.helpMessage.replace("%begriff%", "changeid")));
            } else
                pp.sendMessage(new TextComponent(Bungeesystem.noPerm));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler+"Du bist kein Spieler!"));
    }

    private void setNewID(int oldID, int newID, ProxiedPlayer pp) {
        if(idExists(newID)) {
            copyReason(oldID, true);
            copyReason(newID, false);
            addBan(reason, time, newID, format, ban, perma, report);
            addBan(reason2, time2, oldID, format2, ban2, perma2, report2);
            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "BanID "+Bungeesystem.herH + oldID + Bungeesystem.normal+" wurde mit der BanID "+Bungeesystem.herH + newID + Bungeesystem.normal+" getauscht!"));
        } else {
            copyReason(oldID, true);
            addBan(reason, time, newID, format, ban, perma, report);
            BanRemove.removeBan(oldID);
            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "BanID "+Bungeesystem.herH + oldID + Bungeesystem.normal+" wurde die ID "+Bungeesystem.herH + newID + Bungeesystem.normal+" zugewiesen"));
        }
    }

    private boolean idExists(int id) {
        if (!Bungeesystem.ban.getSection("BanIDs").contains(id + "")) {
            return false;
        }
        return true;
    }

    private void copyReason(int id, boolean caze) {
        if(caze) {
            reason = Bungeesystem.ban.getString("BanIDs." + id + ".Reason");
            time = Bungeesystem.ban.getInt("BanIDs." + id + ".Time");
            format = Bungeesystem.ban.getString("BanIDs." + id + ".Format");
            ban = Bungeesystem.ban.getBoolean("BanIDs." + id + ".Ban");
            perma = Bungeesystem.ban.getBoolean("BanIDs." + id + ".Perma");
            report = Bungeesystem.ban.getBoolean("BanIDs." + id + ".Reportable");
        } else {
            reason2 = Bungeesystem.ban.getString("BanIDs." + id + ".Reason");
            time2 = Bungeesystem.ban.getInt("BanIDs." + id + ".Time");
            format2 = Bungeesystem.ban.getString("BanIDs." + id + ".Format");
            ban2 = Bungeesystem.ban.getBoolean("BanIDs." + id + ".Ban");
            perma2 = Bungeesystem.ban.getBoolean("BanIDs." + id + ".Perma");
            report2 = Bungeesystem.ban.getBoolean("BanIDs." + id + ".Reportable");
        }

    }

    private void addBan(String grund, int dauer, int id, String format, boolean banOrMute, boolean perma, boolean report) {
        Bungeesystem.ban.set("BanIDs."+id + ".Reason", grund);
        Bungeesystem.ban.set("BanIDs."+id + ".Time", dauer);
        Bungeesystem.ban.set("BanIDs."+id + ".Format", format);
        Bungeesystem.ban.set("BanIDs."+id + ".Ban", banOrMute);
        Bungeesystem.ban.set("BanIDs."+id + ".Perma", perma);
        Bungeesystem.ban.set("BanIDs." + id + ".Reportable", report);
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(Bungeesystem.ban, Bungeesystem.banFile);
        } catch (IOException e) {
            Bungeesystem.logger().log(Level.WARNING, "cloud not change ban id for id " + id, e);
        }
    }

}
