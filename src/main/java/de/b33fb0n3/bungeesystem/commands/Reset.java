package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 17.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.UUIDFetcher;
import de.b33fb0n3.bungeesystem.utils.WarnManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class Reset extends Command {

    public Reset(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
            if (sender.hasPermission("bungeecord.reset") || sender.hasPermission("bungeecord.*")) {
                if (args.length == 1) {
                    try {
                        reset(UUIDFetcher.getUUID(args[0]).toString());
                        sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.herH + args[0] + Bungeesystem.normal+" wurde resetet! "+Bungeesystem.other2+"("+Bungeesystem.fehler+"nicht wiederherrstellbar"+Bungeesystem.other2+")"));
                    } catch (NullPointerException e) {
                        sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler+"Dieser Spieler existiert nicht!"));
                    }
                } else
                    sender.sendMessage(new TextComponent(Bungeesystem.helpMessage.replace("%begriff%", "reset")));
            } else
                sender.sendMessage(new TextComponent(Bungeesystem.noPerm));
    }

    private void reset(String uuid) {
        try (Connection conn = Bungeesystem.getPlugin().getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM history WHERE TargetUUID = ?");
            PreparedStatement ps1 = conn.prepareStatement("DELETE FROM bannedPlayers WHERE TargetUUID = ?");
            PreparedStatement ps2 = conn.prepareStatement("UPDATE playerdata SET bansMade = 0, warnsMade = 0, reportsMade = 0, bansReceive = 0, warnsReceive = 0 WHERE UUID = ?");
        ) {
            ps.setString(1, uuid);
            ps.executeUpdate();

            ps1.setString(1, uuid);
            ps1.executeUpdate();

            ps2.setString(1, uuid);
            ps2.executeUpdate();
        } catch (SQLException e) {
            Bungeesystem.logger().log(Level.WARNING, "cloud not reset player", e);
        }
    }

}
