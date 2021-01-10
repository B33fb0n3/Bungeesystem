package de.b33fb0n3.bungeesystem.listener;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

/**
 * Plugin made by B33fb0n3YT
 * 10.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

public class TabComplete implements Listener {

    public TabComplete(Plugin plugin) {
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onTabComplete(final TabCompleteEvent e) {
        final String cursor = e.getCursor().toLowerCase();
        if (!cursor.startsWith("/ban ") &&
                !cursor.startsWith("/unban ") &&
                !cursor.startsWith("/chatlog ") &&
                !cursor.startsWith("/reset ") &&
                !cursor.startsWith("/accounts ") &&
                !cursor.startsWith("/warns ") &&
                !cursor.startsWith("/bans ") &&
                !cursor.startsWith("/report ") &&
                !cursor.startsWith("/reports ") &&
                !cursor.startsWith("/ip ") &&
                !cursor.startsWith("/check ") &&
                !cursor.startsWith("/history ") &&
                !cursor.startsWith("/kick ") &&
                !cursor.startsWith("/warn ") &&
                !cursor.startsWith("/banadd ") &&
                !cursor.startsWith("/banremove ") &&
                !cursor.startsWith("/changeid ") &&
                !cursor.startsWith("/blacklist ") &&
                !cursor.startsWith("/chatlog ") &&
                !cursor.startsWith("/chatlogs ") &&
                !cursor.startsWith("/support ") &&
                !cursor.startsWith("/testlag ") &&
                !cursor.startsWith("/bhelp ") &&
                !cursor.startsWith("/testperm ") &&
                !cursor.startsWith("/teamchat ") &&
                !cursor.startsWith("/onlinezeit ") &&
                !cursor.startsWith("/editban ")) {
            return;
        }

        ProxiedPlayer pp = (ProxiedPlayer) e.getSender();
        if (pp.hasPermission("bungeecord.tabcomplete") || pp.hasPermission("bungeecord.*")) {
            final String[] split = cursor.split(" ");
            final String partialPlayerName = split[split.length - 1];
            for (final ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                if (p.getName().toLowerCase().startsWith(partialPlayerName)) {
                    e.getSuggestions().add(p.getName());
                }
            }
        } else {
            e.setCancelled(true);
        }

    }

}
