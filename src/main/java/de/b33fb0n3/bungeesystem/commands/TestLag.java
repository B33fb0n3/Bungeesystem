package de.b33fb0n3.bungeesystem.commands;

/**
 * Plugin made by B33fb0n3YT
 * 17.01.2021
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

import de.b33fb0n3.bungeesystem.Bungeesystem;
import de.b33fb0n3.bungeesystem.utils.HistoryElemt;
import de.b33fb0n3.bungeesystem.utils.HistoryManager;
import de.b33fb0n3.bungeesystem.utils.PasteUtils;
import de.b33fb0n3.bungeesystem.utils.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class TestLag extends Command {

    public TestLag(String name) {
        super(name);
    }

    private HashMap<String, Long> timers = new HashMap<>();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer pp = (ProxiedPlayer) sender;
            if (pp.hasPermission("bungeecord.testlag") || pp.hasPermission("bungeecord.*")) {
                if (args.length == 1) {
                    String message = Bungeesystem.Prefix + Bungeesystem.normal + "Wenn du diesen Command nochmal ausführst,\nwerden für den Spieler " + Bungeesystem.herH + args[0] + Bungeesystem.normal + " willkürlich Bans/Reports\nund Warns in deine MySQL History eingetragen!\n§c§lB33fb0n3YT übernimmt keine Verwantwortung!\n§c§lDiese Einträge müssen manuell gelöscht werden!\n" + Bungeesystem.normal + "Führe in den nächsten 3 Sekunden diesen Befehl nochmal aus,\ndamit der Test startet.";
                    if (!timers.containsKey(pp.getName())) {
                        pp.sendMessage(new TextComponent(message));
                        timers.put(pp.getName(), System.currentTimeMillis() + 3000);
                        return;
                    }
                    if (timers.get(pp.getName()) > System.currentTimeMillis()) {
                        UUID ut = UUIDFetcher.getUUID(args[0]);
                        if (ut == null) {
                            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Gebe einen richtigen Spieler an!"));
                            return;
                        }
                        start(args[0], ut, pp);
                    } else {
                        timers.remove(pp.getName());
                        pp.sendMessage(new TextComponent(message));
                        timers.put(pp.getName(), System.currentTimeMillis() + 3000);
                    }
                } else
                    pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Benutze: " + Bungeesystem.other + "/testlag <Spieler>"));
            } else
                pp.sendMessage(new TextComponent(Bungeesystem.noPerm));
        } else
            sender.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.fehler + "Du bist kein Spieler!"));
    }

    private void start(String target, UUID ut, ProxiedPlayer pp) {
        long startTime = 0L;
        long startTime2 = System.currentTimeMillis();
        long endTime = 0L;
        long millis = 0L;
        float sec = 0;
        int count = random(100, 500);
        ArrayList<String> messages = new ArrayList<>();
        String[] types = new String[]{"warn", "ban", "report"};
        HistoryManager historyManager = new HistoryManager();
        for (int i = 0; i < count; i++) {
            String type = types[new Random().nextInt(types.length)];
            char[] chars = "abcdefghijklmnopqrstuvwxyz123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
            int length = 7;
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int a = 0; a < length; a++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }
            String reason = sb.toString();
            startTime = System.currentTimeMillis();
            boolean ban = type.equalsIgnoreCase("ban");
            historyManager.insertInDB(ut, pp.getUniqueId(), type, reason, startTime, ban ? startTime + 10000 : -2, ban ? (new Random().nextBoolean() ? 1 : 0) : -2, ban ? (new Random().nextBoolean() ? 1 : 0) : -2);
            endTime = System.currentTimeMillis();
            millis = endTime - startTime;
            sec = millis / 1000.0f;
            messages.add((Character.toUpperCase(type.charAt(0)) + type.substring(1)) + " in " + millis + "ms oder " + String.format("%.3f", sec) + " Sekunden erstellt!");
        }
        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + Bungeesystem.normal + "Es wurden " + Bungeesystem.herH + count + " Einträge " + Bungeesystem.normal + "erstellt!"));
        String url = PasteUtils.paste(String.join("\n", messages));
        TextComponent tc = new TextComponent();
        tc.setText(Bungeesystem.Prefix + Bungeesystem.normal + "Hier kannst du dir alles anzeigen lassen: ");

        TextComponent tc1 = new TextComponent();
        tc1.setText(url);
        tc1.setColor(ChatColor.AQUA);
        tc1.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        tc.addExtra(tc1);

        pp.sendMessage(tc);
        long endTime2 = System.currentTimeMillis();
        long millis2 = endTime2 - startTime2;
        float seconds = millis2 / 1000.0f;
        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "Dieser Prozess hat " + Bungeesystem.herH + seconds + Bungeesystem.normal + " Sekunden gedauert!"));

        float durchschnitt = seconds / count;

        pp.sendMessage(new TextComponent(Bungeesystem.Prefix + "Im Durchschnitt hat eine Eintragung " + Bungeesystem.herH + String.format("%.3f", durchschnitt) + Bungeesystem.normal + " Sekunden gebraucht."));
        timers.remove(pp.getName());
    }

    private int random(double min, double max) {
        Random rdm = new Random();
        return (int) (rdm.nextInt((int) (max - min + 1)) + min);
    }
}
