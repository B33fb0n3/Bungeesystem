package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 10.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.DateUnit;
import de.b33fb0n3.bungeesystem.utils.RangManager;
import de.b33fb0n3.bungeesystem.utils.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class Ban extends Command {

    public Ban(String name) {
        super(name, "", "mute");
    }

    private ArrayList<Integer> bans = new ArrayList<>();
    private ArrayList<Integer> mutes = new ArrayList<>();
    private ArrayList<Integer> permas = new ArrayList<>();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("bungeecord.ban") || sender.hasPermission("bungeecord.*")) {
            if (args.length == 2 || args.length == 3) {
                UUID ptUUID = UUIDFetcher.getUUID(args[0]);
                if (ptUUID == null) {
                    sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Dieser Spieler existiert nicht!"));
                    return;
                }

                if (Bungeesystem.settings.getBoolean("Toggler.power")) {
                    if (sender instanceof ProxiedPlayer) {
                        ProxiedPlayer pp = (ProxiedPlayer) sender;
                        RangManager rangManager = new RangManager(pp, Bungeesystem.getPlugin().getDataSource());
                        if (!(rangManager.getPower(pp.getUniqueId()) > rangManager.getPower(ptUUID))) {
                            pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Diesen Spieler darfst du nicht bannen!"));
                            return;
                        }
                    }
                }
                int banid = 0;
                try {
                    banid = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Gebe eine Zahl ein!"));
                    return;
                }
                if (!Bungeesystem.ban.getSection("BanIDs").contains(banid + "")) {
                    sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Diese ID existiert nicht!"));
                    return;
                }
                String banIdPerm = Bungeesystem.ban.getString("BanIDs." + banid + ".Permission");
                if (!banIdPerm.equals("")) {
                    if (!sender.hasPermission("bungeecord.*")) {
                        if (!sender.hasPermission(banIdPerm)) {
                            sender.sendMessage(new TextComponent(Bungeesystem.noPerm + Bungeesystem.other2 + " (" + Bungeesystem.herH + banIdPerm + Bungeesystem.other2 + ")"));
                            return;
                        }
                    }
                }
                String grund = Bungeesystem.ban.getString("BanIDs." + banid + ".Reason");
                boolean perma = Bungeesystem.ban.getBoolean("BanIDs." + banid + ".Perma");
                boolean ban = Bungeesystem.ban.getBoolean("BanIDs." + banid + ".Ban");
                int permaint = 0;
                int banint = 0;
                if (perma) {
                    permaint = 1;
                }
                if (ban) {
                    banint = 1;
                }
                de.b33fb0n3.bungeesystem.utils.Ban currentBan = new de.b33fb0n3.bungeesystem.utils.Ban(ptUUID, null, Bungeesystem.getPlugin().getDataSource(), Bungeesystem.settings, Bungeesystem.standardBans);
                if (currentBan.isBanned()) {
                    if (currentBan.getBan() == 1 && !ban) {
                        sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Wenn der Spieler gebannt ist bringt ein Mute auch nichts mehr!"));
                        return;
                    }
                    currentBan.unban(false, "PLUGIN"); // erstmal //
                    // hier abfragen, ob er den aktuellen Ban verändern will?
                }
                DateUnit unit;
                try {
                    unit = DateUnit.valueOf((Bungeesystem.ban.getString("BanIDs." + banid + ".Format")).toUpperCase());
                } catch (IllegalArgumentException | NullPointerException e) {
                    sender.sendMessage(new TextComponent(Bungeesystem.herH + (Bungeesystem.ban.getString("BanIDs." + banid + ".Format")) + Bungeesystem.fehler + " ist keine gültiges Format!"));
                    sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.normal + "Gültige Einheiten: "));
                    for (DateUnit date : DateUnit.values()) {
                        sender.sendMessage(new TextComponent(Bungeesystem.herH + date));
                    }
                    return;
                }
                long current = System.currentTimeMillis();
                int time = Bungeesystem.ban.getInt("BanIDs." + banid + ".Time");
                int banCount = (currentBan.getBanCount(grund, true) + 1);
                long millis = 0;
                double y = 0;

                if (Bungeesystem.settings.getBoolean("Ban.permaafter3")) {
                    if (banCount > 3)
                        permaint = 1;
                }
                double pow = Math.pow(2, banCount);
                y = time * pow;
                if (banCount == 1)
                    y = y - time;
                millis = Math.round(y * (unit.getToSec() * 1000));

                long unban = current + millis;
                if (permaint == 1)
                    unban = -1;
                String beweis = "/";
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(ptUUID);
                if (args.length == 3) {
                    beweis = args[2];
                }

                new de.b33fb0n3.bungeesystem.utils.Ban(ptUUID, sender.getName(), grund, System.currentTimeMillis(), unban, permaint, banint, target != null ? target.getSocketAddress().toString().replace("/", "").split(":")[0] : "NULL", beweis, Bungeesystem.getPlugin().getDataSource(), Bungeesystem.settings, Bungeesystem.standardBans);
            } else {
                if (Bungeesystem.settings.getBoolean("BanPlaceholder.aktive"))
                    sendBans(sender);
                else
                    sendBanHelp(sender);
            }
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.noPerm));
    }

    private void sortBans(CommandSender sender) {
        mutes.clear();
        bans.clear();
        permas.clear();
        for (int banID : Bungeesystem.ban.getSection("BanIDs").getKeys().stream().map(Integer::parseInt).sorted().collect(Collectors.toList())) {
            String perm = Bungeesystem.ban.getString("BanIDs." + banID + ".Permission");
            if (!perm.equalsIgnoreCase("")) {
                if (!sender.hasPermission("bungeecord.*")) {
                    if (!sender.hasPermission(perm))
                        continue;
                }
            }
            if (!Bungeesystem.ban.getBoolean("BanIDs." + banID + ".Ban") && !Bungeesystem.ban.getBoolean("BanIDs." + banID + ".Perma")) {
                mutes.add(banID);
            }
            if (Bungeesystem.ban.getBoolean("BanIDs." + banID + ".Ban") && !Bungeesystem.ban.getBoolean("BanIDs." + banID + ".Perma")) {
                bans.add(banID);
            }
            if (Bungeesystem.ban.getBoolean("BanIDs." + banID + ".Perma")) {
                permas.add(banID);
            }
        }
    }

    private void sendBans(CommandSender sender) {
        sortBans(sender);
        for (int i = 1; i < 7; i++) {
            String message = "";
            switch (Bungeesystem.settings.getString("BanPlaceholder.line" + i)) {
                case "%bans%":
                    for (int banID : bans) {
                        message = Bungeesystem.settings.getString("BanReasons");
                        message = message.replace("%id%", banID + ".").replace("%reason%", Bungeesystem.ban.getString("BanIDs." + banID + ".Reason")).replace("%time%", (Bungeesystem.ban.getBoolean("BanIDs." + banID + ".Perma") ? "§4Permanent" : Bungeesystem.ban.getInt("BanIDs." + banID + ".Time") + " " + Bungeesystem.ban.getString("BanIDs." + banID + ".Format"))).replace("%status%", (Bungeesystem.ban.getBoolean("BanIDs." + banID + ".Ban") ? "Ban" : "Mute"));
                        sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
                    }
                    break;
                case "%mutes%":
                    for (int banID : mutes) {
                        message = Bungeesystem.settings.getString("BanReasons");
                        message = message.replace("%id%", banID + ".").replace("%reason%", Bungeesystem.ban.getString("BanIDs." + banID + ".Reason")).replace("%time%", (Bungeesystem.ban.getBoolean("BanIDs." + banID + ".Perma") ? "§4Permanent" : Bungeesystem.ban.getInt("BanIDs." + banID + ".Time") + " " + Bungeesystem.ban.getString("BanIDs." + banID + ".Format"))).replace("%status%", (Bungeesystem.ban.getBoolean("BanIDs." + banID + ".Ban") ? "Ban" : "Mute"));
                        sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
                    }
                    break;
                case "%permas%":
                    for (int banID : permas) {
                        message = Bungeesystem.settings.getString("BanReasons");
                        message = message.replace("%id%", banID + ".").replace("%reason%", Bungeesystem.ban.getString("BanIDs." + banID + ".Reason")).replace("%time%", (Bungeesystem.ban.getBoolean("BanIDs." + banID + ".Perma") ? "§4Permanent" : Bungeesystem.ban.getInt("BanIDs." + banID + ".Time") + " " + Bungeesystem.ban.getString("BanIDs." + banID + ".Format"))).replace("%status%", (Bungeesystem.ban.getBoolean("BanIDs." + banID + ".Ban") ? "Ban" : "Mute"));
                        sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
                    }
                    break;
                default:
                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Bungeesystem.settings.getString("BanPlaceholder.line" + i))));
                    break;
            }
        }

    }

    private void sendBanHelp(CommandSender sender) {
        for (int banID : Bungeesystem.ban.getSection("BanIDs").getKeys().stream().map(Integer::parseInt).sorted().collect(Collectors.toList())) {

            String perm = Bungeesystem.ban.getString("BanIDs." + banID + ".Permission");
            if (!perm.equalsIgnoreCase("")) {
                if (!sender.hasPermission("bungeecord.*")) {
                    if (!sender.hasPermission(perm))
                        continue;
                }
            }

            String message = Bungeesystem.settings.getString("BanReasons");
            message = message.replace("%id%", banID + ".").replace("%reason%", Bungeesystem.ban.getString("BanIDs." + banID + ".Reason")).replace("%time%", (Bungeesystem.ban.getBoolean("BanIDs." + banID + ".Perma") ? "§4Permanent" : Bungeesystem.ban.getInt("BanIDs." + banID + ".Time") + " " + Bungeesystem.ban.getString("BanIDs." + banID + ".Format"))).replace("%status%", (Bungeesystem.ban.getBoolean("BanIDs." + banID + ".Ban") ? "Ban" : "Mute"));
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
        }

        if (Bungeesystem.ban.getSection("BanIDs").getKeys().size() == 0) {
            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Es wurden keine Ban-IDs gefunden!"));
            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutze: " + Bungeesystem.other + "/banadd <ID>"));
            return;
        }
        sender.sendMessage(new TextComponent(Bungeesystem.fehler + "Benutze: " + Bungeesystem.other + "/ban <Spieler> <Ban-ID>"));
    }


}