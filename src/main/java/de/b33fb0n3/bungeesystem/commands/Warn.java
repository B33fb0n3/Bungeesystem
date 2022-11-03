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
import de.b33fb0n3.bungeesystem.utils.RangManager;
import de.b33fb0n3.bungeesystem.utils.WarnManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

public class Warn extends Command {

    public Warn(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer pp = (ProxiedPlayer) sender;
            if (pp.hasPermission("bungeecord.warn") || pp.hasPermission("bungeecord.*")) {
                if (args.length >= 1) {
                    if (args[0].equalsIgnoreCase("del")) {
                        String id = args[1];
                        WarnManager warnManager = new WarnManager();
                        warnManager.setSource(Bungeesystem.getPlugin().getDataSource());
                        warnManager.deleteWarn(id);
                        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "Der Warn wurde gelöscht!"));
                        return;
                    }
                    if (ProxyServer.getInstance().getPlayer(args[0]) == null) {
                        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Dieser Spieler ist nicht auf dem Netzwerk!"));
                        return;
                    }
                    ProxiedPlayer pt = ProxyServer.getInstance().getPlayer(args[0]);
                    if (Bungeesystem.settings.getBoolean("Toggler.power")) {
                        RangManager rangManager = new RangManager(pp, Bungeesystem.getPlugin().getDataSource());
                        if (!(rangManager.getPower(pp.getUniqueId()) > rangManager.getPower(pt.getUniqueId()))) {
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Diesen Spieler darfst du nicht warnen!"));
                            return;
                        }
                    }
                    String grund = "";
                    for (int i = 1; i < args.length; i++) {
                        grund = grund + args[i] + " ";
                    }
                    WarnManager warnManager = new WarnManager(pt.getUniqueId(), pp.getUniqueId(), grund, System.currentTimeMillis(), Bungeesystem.settings, Bungeesystem.getPlugin().getDataSource());
                    warnManager.addWarn();
                    pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "Du hast " + Bungeesystem.herH + args[0] + Bungeesystem.normal + " für " + Bungeesystem.herH + grund + Bungeesystem.normal + " gewarnt!"));
                    int maxWarns = Bungeesystem.settings.getInt("Warns.MaxWarns");
                    ArrayList<String> warnArray = new ArrayList<>();
                    final int[] i = {1};
                    String finalGrund = grund;
                    DBUtil.getWhatCount(Bungeesystem.getPlugin().getDataSource(), pt.getUniqueId(), "warn", true).whenComplete((whatCountTarget, ex) -> {
                        while (true) {
                            try {
                                String line = ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("WarnMessage.line" + i[0])).replace("%warnCount%", String.valueOf(whatCountTarget)).replace("%maxWarns%", String.valueOf(maxWarns)).replace("%grund%", finalGrund);
                                warnArray.add(line);
                                i[0]++;
                                if (i[0] > Bungeesystem.settings.getInt("WarnMessage.lines"))
                                    break;
                            } catch (Exception e1) {
                                break;
                            }
                        }
                        pt.disconnect(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', String.join("\n", warnArray))).create());
                        if (whatCountTarget >= maxWarns) {
                            new Ban(pt.getUniqueId(), null, Bungeesystem.getPlugin().getDataSource(), Bungeesystem.settings, Bungeesystem.standardBans).banByStandard(3, pt.getSocketAddress().toString().replace("/", "").split(":")[0]);
                            warnManager.deleteAllWarns();
                            pt.disconnect(new TextComponent(Bungeesystem.settings.getString("BanDisconnected").replace("%absatz%", "\n").replace("%reason%", finalGrund)));
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "Der Spieler wurde, für mehr als " + Bungeesystem.herH + maxWarns + Bungeesystem.normal + " Warnungen, gebannt!"));
                        }
                    });
                } else
                    pp.sendMessage(new TextComponent(Bungeesystem.helpMessage.replace("%begriff%", "warn")));
            } else
                pp.sendMessage(new TextComponent(Bungeesystem.noPerm));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist kein Spieler!"));
    }
}
