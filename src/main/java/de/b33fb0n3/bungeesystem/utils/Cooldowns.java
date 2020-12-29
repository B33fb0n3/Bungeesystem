package de.b33fb0n3.bungeesystem.utils;

import de.b33fb0n3.bungeesystem.Bungeesystem;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.IOException;

/**
 * Plugin made by B33fb0n3YT
 * 25.07.2019
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

public class Cooldowns {

    private String type;
    private ProxiedPlayer pp;

    public Cooldowns(String type, ProxiedPlayer pp) {
        this.type = type;
        this.pp = pp;
    }

    public void startCooldown() {
        DateUnit unit = null;
        try {
            unit = DateUnit.valueOf((Bungeesystem.settings.getString("Cooldown." + getType() + ".format")).toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            getPp().sendMessage(new ComponentBuilder(Bungeesystem.herH + (Bungeesystem.settings.getString("Cooldown." + getType() + ".format")) + Bungeesystem.fehler + " ist keine gültige Einheit!").create());
            getPp().sendMessage(new ComponentBuilder(Bungeesystem.Prefix + Bungeesystem.normal + "Gültige Einheiten: ").create());
            for (DateUnit date : DateUnit.values()) {
                getPp().sendMessage(new ComponentBuilder(Bungeesystem.herH + date).create());
            }
            return;
        }
        long current = System.currentTimeMillis();
        long millis = (long) Bungeesystem.settings.getInt("Cooldown." + getType() + ".time") * (unit != null ? unit.getToSec() : 0) * 1000;
        long bis = current + millis;
        Bungeesystem.cooldowns.set(getType() + "." + getPp().getUniqueId().toString() + ".Bis", bis);
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(Bungeesystem.cooldowns, Bungeesystem.cooldownsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isOnCooldown() {
        if (getPp().hasPermission("bungeecord.cooldown.bypass") || getPp().hasPermission("bungeecord.*"))
            return false;
        if (Bungeesystem.cooldowns.getSection(getType()).contains(getPp().getUniqueId().toString())) {
            long bis = Bungeesystem.cooldowns.getLong(getType() + "." + getPp().getUniqueId().toString() + ".Bis");
            return System.currentTimeMillis() < bis;
        }
        return false;
    }

    public String getType() {
        return getType();
    }

    public ProxiedPlayer getPp() {
        return pp;
    }
}
