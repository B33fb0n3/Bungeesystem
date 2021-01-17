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
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Bug extends Command {

    public Bug(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            final ProxiedPlayer pp = (ProxiedPlayer) sender;
            if(pp.hasPermission("bungeecord.bug") || pp.hasPermission("bungeecord.*")) {
                if(args.length == 0) {
                    TextComponent tc = new TextComponent();
                    tc.setText(Bungeesystem.Prefix + "Einen Bug kannst du hier melden "+Bungeesystem.other2+"("+Bungeesystem.fehler+"Click"+Bungeesystem.other2+")");
                    tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://forms.gle/fFsgUSMvKpWg6pYx9"));
                    tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.fehler + "Melde einen Bug")));
                    pp.sendMessage(tc);
                } else
                    pp.sendMessage(Bungeesystem.helpMessage.replace("%begriff%", "bug"));
            } else
                pp.sendMessage(Bungeesystem.noPerm);
        } else
            sender.sendMessage(Bungeesystem.Prefix + Bungeesystem.fehler+"Du bist kein Spieler!");
    }

}
