package de.b33fb0n3.bungeesystem;

import de.b33fb0n3.bungeesystem.commands.Report;
import de.b33fb0n3.bungeesystem.commands.Reports;
import de.b33fb0n3.bungeesystem.listener.Login;
import de.b33fb0n3.bungeesystem.utils.ConnectionPoolFactory;
import de.b33fb0n3.bungeesystem.utils.ReportManager;
import de.b33fb0n3.bungeesystem.utils.Updater;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bstats.bungeecord.Metrics;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Plugin made by B33fb0n3YT
 * 29.12.2020
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

public class Bungeesystem extends Plugin {

    private static Bungeesystem plugin;
    public static String Prefix = "§bB33fb0n3§4.net §7| §a";
    public static String noPerm = Prefix + "§cDazu hast du keine Rechte!";
    public static String normal = "&a";
    public static String fehler = "&c";
    public static String herH = "&b";
    public static String other = "&e";
    public static String other2 = "&7";
    public static String helpMessage = "";
    public static Configuration mysqlConfig;
    public static Configuration ban;
    public static Configuration settings;
    public static Configuration cooldowns;
    public static Configuration blacklist;
    public static Configuration raenge;
    public static Configuration standardBans;
    public static File cooldownsFile;
    private DataSource dataSource;

    public static Logger logger() {
        return plugin.getLogger();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public static Bungeesystem getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        Metrics metrics = new Metrics(this, 9816);

        getLogger().info( "[]=======================[]");
        getLogger().info( "						 ");
        getLogger().info( "Coded by: B33fb0n3YT");

        loadConfig();
        ConnectionPoolFactory connectionPool = new ConnectionPoolFactory(mysqlConfig);

        // mysql connect
        try {
            dataSource = connectionPool.getPluginDataSource(this);
        } catch (SQLException e) {
            logger().log(Level.SEVERE, "Could not create data source.", e);
            getProxy().getPluginManager().unregisterListeners(this);
            getProxy().getPluginManager().unregisterCommands(this);
            onDisable();
            return;
        }

        // check update
        Updater updater = new Updater(this);
        if (updater.ckeckUpdate() == 0) {
            getLogger().info("§7Du bist auf der neusten Version!");
        } else if (updater.ckeckUpdate() == 1) {
            getLogger().info("§aEine neue Version hier verfügbar: \n§bhttps://www.spigotmc.org/resources/bungeesystem-%E2%98%85-ban-mute-report-warn-kick-%E2%98%85-mysql.67179/updates");
            updater.setUpdate(true);
        } else {
            getLogger().info("§cUpdater konnte keine Verbingung herstellen §7(§cmögl. Dev Build§7)");
        }

        // load color codes from config
        try {
            normal = settings.getString("ChatColor.normal").replace("&", "§");
            fehler = settings.getString("ChatColor.fehler").replace("&", "§");
            herH = settings.getString("ChatColor.hervorhebung").replace("&", "§");
            other = settings.getString("ChatColor.other").replace("&", "§");
            other2 = settings.getString("ChatColor.other2").replace("&", "§");

            Prefix = settings.getString("Prefix").replace("&", "§") + normal;
            noPerm = settings.getString("NoPerm").replace("&", "§");
            helpMessage = ChatColor.translateAlternateColorCodes('&', Prefix + fehler + "Benutze: " + other + "/bhelp %begriff% oder " + other + "/bhelp");
        } catch (NullPointerException e) {
            getLogger().log(Level.WARNING,"Some messages not found!", e);
        }

        getLogger().info(metrics.isEnabled() ? "Statistiken wurden aktiviert" : "Statistiken sind deaktiviert");
        getLogger().info( "Bungeesystem wurde aktiviert!");
        getLogger().info( "						 ");
        getLogger().info( "[]=======================[]");
        registerCommands();
        registerListener();
        initMySQL();
    }

    private void initMySQL() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS bannedPlayers (TargetUUID VARCHAR(64),TargetName VARCHAR(64),VonUUID VARCHAR(64),VonName VARCHAR(64),Grund VARCHAR(100),TimeStamp BIGINT(8),Bis VARCHAR(100),Perma TINYINT(1),Ban TINYINT(1), ip VARCHAR(100))");
             PreparedStatement ps1 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS history (TargetUUID VARCHAR(64), VonUUID VARCHAR(64), Type VARCHAR(50), Grund VARCHAR(100), Erstellt BIGINT(8), Bis BIGINT(8), Perma TINYINT(1), Ban TINYINT(1))");
             PreparedStatement ps2 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS playerdata (UUID VARCHAR(64) NOT NULL, Name VARCHAR(64) NOT NULL, lastIP VARCHAR(60), firstJoin BIGINT(8) NOT NULL, lastOnline BIGINT(8), bansMade INT(60) NOT NULL DEFAULT 0, warnsMade INT(60) NOT NULL DEFAULT 0, reportsMade INT(60) NOT NULL DEFAULT 0, bansReceive INT(60) NOT NULL DEFAULT 0, warnsReceive INT(60) NOT NULL DEFAULT 0, power BIGINT(8) NOT NULL DEFAULT 0, primary key(UUID))");
             PreparedStatement ps3 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS chat (message VARCHAR(255), uuid VARCHAR(100), timestamp BIGINT(8), server VARCHAR(50))");
             PreparedStatement ps4 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS onlinetime (UUID VARCHAR(255), Name VARCHAR(100), Datum VARCHAR(50), onlinezeit BIGINT(8))");
        ) {
            ps.executeUpdate();
            ps1.executeUpdate();
            ps2.executeUpdate();
            ps3.executeUpdate();
            ps4.executeUpdate();
        } catch (SQLException e) {
            logger().log(Level.WARNING, "Could not establish database connection.", e);
        }
    }

    public String formatTime(Long timestamp) {
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("Europe/Berlin"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm");
        return date.format(formatter) + " Uhr";
    }

    private void registerListener() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new Login(this, dataSource));
//        ProxyServer.getInstance().getPluginManager().registerListener(this, new Chat(this));
//        ProxyServer.getInstance().getPluginManager().registerListener(this, new BanAdd(this));
//        ProxyServer.getInstance().getPluginManager().registerListener(this, new TabComplete(this));
//        ProxyServer.getInstance().getPluginManager().registerListener(this, new Disconnect(this));
    }

    private void registerCommands() {
        if (settings.getBoolean("Toggler.report")) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new Report("report"));
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new Reports("reports"));
        }
//
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Ban("ban"));
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Editban("editban"));
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Bans("bans"));
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanAddRECODE("banadd"));
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanRemove("banremove"));
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Unban("unban"));
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Check("check"));
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new TestLag("testlag"));
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BHelp("bhelp"));
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new TestPerm("testperm"));
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Feedback("feedback"));
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Bug("bug"));
//
//        if (settings.getBoolean("Toggler.onlinezeit"))
//            ProxyServer.getInstance().getPluginManager().registerCommand(this, new Onlinezeit("onlinezeit"));
//
//        if (settings.getBoolean("Toggler.warn")) {
//            ProxyServer.getInstance().getPluginManager().registerCommand(this, new Warn("warn"));
//            ProxyServer.getInstance().getPluginManager().registerCommand(this, new Warns("warns"));
//        }
//
//        if (settings.getBoolean("Toggler.kick"))
//            ProxyServer.getInstance().getPluginManager().registerCommand(this, new Kick("kick"));
//
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ChangeID("changeid"));
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new History("history"));
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new IP("ip"));
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Accounts("accounts"));
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Reset("reset"));
//
//        if(settings.getBoolean("Toggler.chat.teamchat"))
//            ProxyServer.getInstance().getPluginManager().registerCommand(this, new Teamchat("teamchat"));
//
//        if (settings.getBoolean("Toggler.chat.blacklist"))
//            ProxyServer.getInstance().getPluginManager().registerCommand(this, new Blacklist("blacklist"));
//
//        if (settings.getBoolean("Toggler.chatlog")) {
//            ProxyServer.getInstance().getPluginManager().registerCommand(this, new ChatLog("chatlog"));
//        }
//
//        if (settings.getBoolean("Toggler.support"))
//            ProxyServer.getInstance().getPluginManager().registerCommand(this, new Support("support"));
    }

    @Override
    public void onDisable() {
        getLogger().info( "[]=======================[]");
        getLogger().info( "						 ");
        getLogger().info( "Coded by: B33fb0n3YT");
        getLogger().info( "Bungeesystem wurde deaktiviert!");
        getLogger().info( "						 ");
        getLogger().info( "[]=======================[]");
    }

    private void loadConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }
            File settingsFile = new File(getDataFolder().getPath(), "settings.yml");
            File banFile = new File(getDataFolder().getPath(), "reasons.yml");
            File mysqlFile = new File(getDataFolder().getPath(), "mysql.yml");
            cooldownsFile = new File(getDataFolder().getPath(), "cooldowns.yml");
            File blacklistFile = new File(getDataFolder().getPath(), "blacklist.yml");
            File raengeFile = new File(getDataFolder().getPath(), "raenge.yml");
            File standardBansFile = new File(getDataFolder().getPath(), "standardbans.yml");
            if (!mysqlFile.exists()) {
                mysqlFile.createNewFile();
                mysqlConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(mysqlFile);

                mysqlConfig.set("host", "localhost");
                mysqlConfig.set("port", 3306);
                mysqlConfig.set("datenbank", "DEINEDATENBANK");
                mysqlConfig.set("username", "DEINBENUTZERNAME");
                mysqlConfig.set("passwort", "DEINPASSWORT");
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(mysqlConfig, mysqlFile);
            }
            mysqlConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(mysqlFile);

            if (!standardBansFile.exists()) {
                standardBansFile.createNewFile();
                standardBans = ConfigurationProvider.getProvider(YamlConfiguration.class).load(standardBansFile);

                standardBans.set("BanIDs.1.Reason", "Alt-Account");
                standardBans.set("BanIDs.1.Time", 10);
                standardBans.set("BanIDs.1.Format", "HOUR");
                standardBans.set("BanIDs.1.Ban", true);
                standardBans.set("BanIDs.1.Perma", true);

                standardBans.set("BanIDs.2.Reason", "Chatverhalten");
                standardBans.set("BanIDs.2.Time", 1);
                standardBans.set("BanIDs.2.Format", "MON");
                standardBans.set("BanIDs.2.Ban", false);
                standardBans.set("BanIDs.2.Perma", false);

                standardBans.set("BanIDs.3.Reason", "Warnungen");
                standardBans.set("BanIDs.3.Time", 3);
                standardBans.set("BanIDs.3.Format", "MON");
                standardBans.set("BanIDs.3.Ban", true);
                standardBans.set("BanIDs.3.Perma", false);
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(standardBans, standardBansFile);
            }
            standardBans = ConfigurationProvider.getProvider(YamlConfiguration.class).load(standardBansFile);

            if (!banFile.exists() || banFile == null) {
                banFile.createNewFile();
                ban = ConfigurationProvider.getProvider(YamlConfiguration.class).load(banFile);

                ban.set("BanIDs.1.Reason", "Clientmodifikation");
                ban.set("BanIDs.1.Time", 6);
                ban.set("BanIDs.1.Format", "HOUR");
                ban.set("BanIDs.1.Ban", true);
                ban.set("BanIDs.1.Perma", true);
                ban.set("BanIDs.1.Reportable", true);

                ban.set("BanIDs.2.Reason", "Chatverhalten");
                ban.set("BanIDs.2.Time", 3);
                ban.set("BanIDs.2.Format", "HOUR");
                ban.set("BanIDs.2.Ban", false);
                ban.set("BanIDs.2.Perma", false);
                ban.set("BanIDs.2.Reportable", true);

                ConfigurationProvider.getProvider(YamlConfiguration.class).save(ban, banFile);
            }
            ban = ConfigurationProvider.getProvider(YamlConfiguration.class).load(banFile);
            if (!settingsFile.exists() || settingsFile == null) {
                settingsFile.createNewFile();
                settings = ConfigurationProvider.getProvider(YamlConfiguration.class).load(settingsFile);

                settings.set("Prefix", "&bB33fb0n3&4.net &7| &a");
                settings.set("NoPerm", "&cDazu hast du keine Rechte!");
                settings.set("WarnInfo", "&b%player% &ahat &b%target% &afür &b%reason% &agewarnt!");
                settings.set("BanReasons", "&a%id% &f» &c%reason% &8- &b%time% &8(&6%status%&8)");
                settings.set("AntiAd", "&cBitte mache keine Werbung!");
                settings.set("Onlinezeit", "&a%player% &f» %onlinezeit%");
                settings.set("TeamchatPrefix", "&6&lTC &f● &b%sender% &f» &7%msg%");

                settings.set("Toggler.support", true);
                settings.set("Toggler.report", true);
                settings.set("Toggler.warn", true);
                settings.set("Toggler.kick", true);
                settings.set("Toggler.power", true);
                settings.set("Toggler.onlinezeit", true);
                settings.set("Toggler.chatlog", true);
                settings.set("Toggler.chat.blacklist", true);
                settings.set("Toggler.chat.doublemessage", true);
                settings.set("Toggler.chat.caps", true);
                settings.set("Toggler.chat.ads", true);
                settings.set("Toggler.chat.spam", true);
                settings.set("Toggler.chat.teamchat", true);

                settings.set("ChatColor.normal", "&a");
                settings.set("ChatColor.fehler", "&4");
                settings.set("ChatColor.hervorhebung", "&b");
                settings.set("ChatColor.other", "&e");
                settings.set("ChatColor.other2", "&7");

                settings.set("Ban.Baninfo", "&b%player% &ahat &b%target% &afür &b%reason% &agebannt!");
                settings.set("Ban.Unbaninfo", "&b%player% &ahat &b%target% &aentbannt!");
                settings.set("Ban.Editinfo", "&aDer Ban von &b%target% &awurde von &b%player% &aeditiert!");
                settings.set("Ban.Disconnectmessage", "&cDu wurdest gebannt!%absatz% &aGrund: &b%reason%");
                settings.set("Ban.Usermessage", "&aDer Spieler &b%target% &awurde für &b%reason% &agebannt!");
                settings.set("Ban.Extrainfohover.1", "&dUUID: &3%uuid%");
                settings.set("Ban.Extrainfohover.2", "&dVon: &3%name%");
                settings.set("Ban.Extrainfohover.3", "&dGrund: &3%reason%");
                settings.set("Ban.Extrainfohover.4", "&dBis: &3%bis%");
                settings.set("Ban.Extrainfohover.5", "&dErstellt: &3%erstellt%");

                settings.set("Chatlog.Oncooldown", "§4Du kannst erst bald einen weiteren Chatlog erstellen!");
                settings.set("Chatlog.created", "§aDer Chatlog wird nun weitergeleitet!");
                settings.set("Chatlog.userGetLink", false);
                settings.set("Chatlog.userGetLinkMessage", "§aDein Chatlog wird unter folgender URL &ageführt: &b%url%");

                settings.set("Check.status", "&aStatus: &b%status%");
                settings.set("Check.hover.1", "&dVon: &3%name%");
                settings.set("Check.hover.2", "&dGrund: &3%reason%");
                settings.set("Check.hover.3", "&dBis: &3%bis%");
                settings.set("Check.hover.4", "&dEditiert von: &3%editby%");
                settings.set("Check.reports", "&aReports: &b%reportCount%");
                settings.set("Check.warns", "&aWarns: &b%warnsCount%");
                settings.set("Check.bans", "&aBans: &b%bansCount%");
                settings.set("Check.history", "&aHistory: &b%historyCount%");
                settings.set("Check.stats", "&aStats: ");
                settings.set("Check.hover2.1", "&dIP: &3%ip%");
                settings.set("Check.hover2.2", "&dPower: &3%power%");
                settings.set("Check.hover2.3", "&dErstes mal Online: &3%firstJoin%");
                settings.set("Check.hover2.4", "&dLetztes mal Online: &3%lastOnline%");
                settings.set("Check.hover2.5", "&dReports Erstellt: &3%reportsMade%");
                settings.set("Check.hover2.6", "&dWarns Erhalten: &3%warnsReceive%");
                settings.set("Check.hover2.7", "&dBans Erhalten: &3%bansReceive%");
                settings.set("Check.hover2.8", "&dWarns Erstellt: &3%warnsMade%");
                settings.set("Check.hover2.9", "&dBans Erstellt: &3%bansMade%");
                settings.set("Check.onlinezeit", "&aOnlinezeit: ");

                settings.set("History.Message", "&8(&6%type%&8) &f» &d%grund% &8{&a%time%&8} &f» ");
                settings.set("History.hover.1", "&7Von: &b%von%");
                settings.set("History.hover.2", "&7Bis: &b%bis%");
                settings.set("History.hover.3", "&7Zeit: &b%zeit%");
                settings.set("History.hover.4", "&7Status: &b%status%");
                settings.set("History.hover.5", "&7Aktiv: &b%aktiv%");
                settings.set("History.hover.6", "&7Entbannt von: &b%entbanner%");

                settings.set("Cooldown.Report.aktive", false);
                settings.set("Cooldown.Report.time", 10);
                settings.set("Cooldown.Report.format", "MIN");
                settings.set("Cooldown.Chatlog.aktive", true);
                settings.set("Cooldown.Chatlog.time", 1);
                settings.set("Cooldown.Chatlog.format", "HOUR");
                settings.set("Cooldown.Support.aktive", false);
                settings.set("Cooldown.Support.time", 10);
                settings.set("Cooldown.Support.format", "MIN");

                settings.set("Reports.Message", "%grund% &f» ");
                settings.set("Reports.hover.1", "&7Wer: &b%wer%");
                settings.set("Reports.hover.2", "&7Von: &b%von%");
                settings.set("Reports.hover.3", "&7Datum: &b%time%");

                settings.set("Bans.Message", "%grund% &f» ");
                settings.set("Bans.hover.1", "&7Wer: &b%wer%");
                settings.set("Bans.hover.2", "&7Von: &b%von%");
                settings.set("Bans.hover.3", "&7Datum: &b%time%");
                settings.set("Bans.hover.4", "&7Bis: &b%bis%");
                settings.set("Bans.hover.5", "&7Status: &b%status%");
                settings.set("Bans.hover.6", "&7Aktiv: &b%aktiv%");
                settings.set("Bans.hover.7", "&7Entbannt von: &b%entbanner%");

                settings.set("Warns.MaxWarns", 3);
                settings.set("Warns.Message", "%grund% &f» ");
                settings.set("Warns.hover.1", "&7Wer: &b%wer%");
                settings.set("Warns.hover.2", "&7Von: &b%von%");
                settings.set("Warns.hover.3", "&7Datum: &b%time%");
                settings.set("MutedMessage", "&cDu wurdest aus dem Chat verbannt!%absatz%&cGrund: &b%grund%%absatz%&cBis: &b%bis%");

                settings.set("BanPlaceholder.aktive", false);
                settings.set("BanPlaceholder.line1", "&7&m----------- &bMUTES &7&m---------------");
                settings.set("BanPlaceholder.line2", "%mutes%");
                settings.set("BanPlaceholder.line3", "&7&m----------- &bBANS &7&m----------------");
                settings.set("BanPlaceholder.line4", "%bans%");
                settings.set("BanPlaceholder.line5", "&7&m----------- &bPERMA &7&m---------------");
                settings.set("BanPlaceholder.line6", "%permas%");

                settings.set("KickMessage.lines", 8);
                settings.set("KickMessage.line1", "&e[&m===============================&e]");
                settings.set("KickMessage.line2", "");
                settings.set("KickMessage.line3", "&cDu wurdest vom Netzwerk gekickt!");
                settings.set("KickMessage.line4", "");
                settings.set("KickMessage.line5", "&6Grund:&b %grund%");
                settings.set("KickMessage.line6", "&6Von:&b %von%");
                settings.set("KickMessage.line7", "");
                settings.set("KickMessage.line8", "&e[&m===============================&e]");

                settings.set("BanMessage.lines", 13);
                settings.set("BanMessage.line1", "&e[&m===============================&e]");
                settings.set("BanMessage.line2", "");
                settings.set("BanMessage.line3", "&cDu wurdest vom Netzwerk gebannt!");
                settings.set("BanMessage.line4", "");
                settings.set("BanMessage.line5", "&6Grund:&b %grund%");
                settings.set("BanMessage.line6", "&6Von:&b %von%");
                settings.set("BanMessage.line7", "&6Bis:&b %bis%");
                settings.set("BanMessage.line8", "&6Beweis:&b %beweis%");
                settings.set("BanMessage.line9", "");
                settings.set("BanMessage.line10", "&aDu kannst einen Entbannungsantrag");
                settings.set("BanMessage.line11", "&aauf dem Teamspeak/Discord stellen!");
                settings.set("BanMessage.line12", "");
                settings.set("BanMessage.line13", "&e[&m===============================&e]");

                settings.set("ReportMessage.lines", 8);
                settings.set("ReportMessage.line1", "&8---------- &aReport &8----------");
                settings.set("ReportMessage.line2", "");
                settings.set("ReportMessage.line3", "&aWer &8- &b%target%");
                settings.set("ReportMessage.line4", "&aVon &8- &b%von%");
                settings.set("ReportMessage.line5", "&aGrund &8- &b%grund%");
                settings.set("ReportMessage.line6", "%teleport%");
                settings.set("ReportMessage.line7", "");
                settings.set("ReportMessage.line8", "&8---------- &aReport &8----------");

                settings.set("WarnMessage.lines", 6);
                settings.set("WarnMessage.line1", "&e[&m===============================&e]");
                settings.set("WarnMessage.line2", "");
                settings.set("WarnMessage.line3", "&c&lWARNUNG! &7(&b%warnCount%&7/&b%maxWarns%&7)");
                settings.set("WarnMessage.line4", "&aGrund: &b%grund%");
                settings.set("WarnMessage.line5", "");
                settings.set("WarnMessage.line6", "&e[&m===============================&e]");
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(settings, settingsFile);
            }
            settings = ConfigurationProvider.getProvider(YamlConfiguration.class).load(settingsFile);
            if (!cooldownsFile.exists() || cooldownsFile == null) {
                cooldownsFile.createNewFile();
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(cooldowns, cooldownsFile);
            }
            cooldowns = ConfigurationProvider.getProvider(YamlConfiguration.class).load(cooldownsFile);
            if (!blacklistFile.exists() || blacklistFile == null) {
                blacklistFile.createNewFile();
                blacklist = ConfigurationProvider.getProvider(YamlConfiguration.class).load(blacklistFile);
                blacklist.set("Blacklist.direkterBan", false);
                blacklist.set("Blacklist.hardMode", false);
                blacklist.set("Blacklist.Words", Arrays.asList("bastard", "fuck", "ficker", "fiker", "hitler", "huhrensohn", "fick", "wixxer", "hs", "leicht", "laicht", "ez", "e²", "mühelos", "clap", "clapz", "bot", "bodt", "bod", "bastard", "hitler", "huhrensohn", "fick", "wixxer", "Hure", "Wichser", "Huan", "Hurensohn", "Arsch", "Arschloch", "Aloch", "Ntte", "Nutte", "Arschkriecher", "Nuttensohn", "Kahbar", "Kachbar", "Drogendealer", "Ficker", "Fickfehler", "Fehlgeburt", "wixer", "Milf", "wihcser", "wiechser", "wiehser", "Hundesohn", "Idiot", "Schlampe", "Pipimann", "Muschi", "Mumu", "Vagina", "Blasen", "Blowjob", "Anal", "Sex", "Blowjop", "Pedo", "Pedophil", "Pedopil", "Pedopiel", "Doggy", "Porno", "GAy", "GAylord", "Nazi", "Natzi", "Nazie"));
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(blacklist, blacklistFile);
            }
            blacklist = ConfigurationProvider.getProvider(YamlConfiguration.class).load(blacklistFile);
            if (!raengeFile.exists()) {
                raengeFile.createNewFile();
                raenge = ConfigurationProvider.getProvider(YamlConfiguration.class).load(raengeFile);

                raenge.set("Raenge.Owner.Power", 100);
                raenge.set("Raenge.Owner.Permission", "bungeecord.banreport.owner");
                raenge.set("Raenge.Admin.Power", 70);
                raenge.set("Raenge.Admin.Permission", "bungeecord.banreport.admin");
                raenge.set("Raenge.Dev.Power", 60);
                raenge.set("Raenge.Dev.Permission", "bungeecord.banreport.dev");
                raenge.set("Raenge.Mod.Power", 50);
                raenge.set("Raenge.Mod.Permission", "bungeecord.banreport.mod");
                raenge.set("Raenge.Sup.Power", 40);
                raenge.set("Raenge.Sup.Permission", "bungeecord.banreport.sup");
                raenge.set("Raenge.Builder.Power", 30);
                raenge.set("Raenge.Builder.Permission", "bungeecord.banreport.builder");
                raenge.set("Raenge.Youtuber.Power", 20);
                raenge.set("Raenge.Youtuber.Permission", "bungeecord.banreport.youtuber");
                raenge.set("Raenge.Premium.Power", 10);
                raenge.set("Raenge.Premium.Permission", "bungeecord.banreport.premium");
                raenge.set("Raenge.default.Power", 0);
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(raenge, raengeFile);
            }
            raenge = ConfigurationProvider.getProvider(YamlConfiguration.class).load(raengeFile);
        } catch (IOException | NullPointerException e) {
            getLogger().log(Level.WARNING,"failed to create config", e);
        }

        getLogger().info("Configs geladen!");
    }
}
