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
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Feedback extends Command {

    public Feedback(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            final ProxiedPlayer pp = (ProxiedPlayer) sender;
            if(pp.hasPermission("bungeecord.feedback") || pp.hasPermission("bungeecord.*")) {
                if(args.length == 0) {
                    TextComponent tc = new TextComponent();
                    TextComponent tc1 = new TextComponent();
                    TextComponent tc2 = new TextComponent();
                    tc.setText(Bungeesystem.Prefix + Bungeesystem.herH + "2. "+Bungeesystem.normal+"Hinterlasse "+Bungeesystem.herH+"B33fb0n3"+Bungeesystem.normal+" ein Review "+Bungeesystem.other2+"("+Bungeesystem.fehler+"Click"+Bungeesystem.other2+")");
                    tc1.setText(Bungeesystem.Prefix + Bungeesystem.herH + "3. "+Bungeesystem.normal+"Schau dir an, was "+Bungeesystem.herH+"B33fb0n3"+Bungeesystem.normal+" geplant hat "+Bungeesystem.other2+"("+Bungeesystem.fehler+"Click"+Bungeesystem.other2+")");
                    tc2.setText(Bungeesystem.Prefix + Bungeesystem.herH + "1. "+Bungeesystem.normal+"Direktes Feedback geben "+Bungeesystem.other2+"("+Bungeesystem.fehler+"Click"+Bungeesystem.other2+")");
                    tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/advanced-bungeesystem-%E2%98%85-ban-mute-report-warn-kick-%E2%98%85.67179/reviews"));
                    tc1.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://trello.com/b/1xYTQRj2/bungeesystem"));
                    tc2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://forms.gle/8Sd7f3qfdrPnSwsz7"));
                    tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.fehler + "Öffne die spigotmc Seite")));
                    tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.fehler + "Öffne die Trello Seite von B33fb0n3")));
                    tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Bungeesystem.fehler + "Gib B33fb0n3 direktes Feedback")));
                    pp.sendMessage(tc2);
                    pp.sendMessage(tc);
                    pp.sendMessage(tc1);
                } else
                    pp.sendMessage(new TextComponent(Bungeesystem.helpMessage.replace("%begriff%", "feedback")));
            } else
                pp.sendMessage(new TextComponent(Bungeesystem.noPerm));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler+"Du bist kein Spieler!"));
    }

}
