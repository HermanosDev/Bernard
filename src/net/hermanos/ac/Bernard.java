package net.hermanos.ac;

import org.bukkit.plugin.java.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.messaging.*;
import org.bukkit.*;
import org.bukkit.scheduler.*;

import net.hermanos.ac.check.*;
import net.hermanos.ac.check.other.*;
import net.hermanos.ac.check.other.Timer;
import net.hermanos.ac.checks.combat.*;
import net.hermanos.ac.checks.combat.movement.*;
import net.hermanos.ac.cmd.*;
import net.hermanos.ac.gui.*;
import net.hermanos.ac.lag.*;
import net.hermanos.ac.packets.*;
import net.hermanos.ac.update.*;
import net.hermanos.ac.utils.*;

import java.util.logging.*;
import java.util.concurrent.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.bukkit.event.*;
import org.bukkit.command.*;
import org.bukkit.event.player.*;

public class Bernard extends JavaPlugin implements Listener
{
    public static Bernard Instance;
    public String PREFIX;
    public Updater updater;
    public PacketCore packet;
    public LagCore lag;
    public List<Check> Checks;
    @SuppressWarnings("unused")
	private static ConfigFile file;
    public Map<UUID, Map<Check, Integer>> Violations;
    public Map<UUID, Map<Check, Long>> ViolationReset;
    public List<Player> AlertsOn;
    public Map<Player, Map.Entry<Check, Long>> AutoBan;
    public Map<String, Check> NamesBanned;
    Random rand;
    @SuppressWarnings("unused")
	private Check check;
    public TxtFile autobanMessages;
    public Map<UUID, Long> LastVelocity;
    public ArrayList<UUID> hasInvOpen;
    public Integer pingToCancel;
    public Integer tpsToCancel;
    public String wngnq;
    
    public Bernard() {
        this.hasInvOpen = new ArrayList<UUID>();
        this.pingToCancel = this.getConfig().getInt("settings.latency.ping");
        this.tpsToCancel = this.getConfig().getInt("settings.latency.tps");
        this.wngnq = UtilMath.decrypt("SW5jb3JyZWN0IEhXSUQhIERpc2FibGluZyBwbHVnaW4uIElmIHlvdSBuZWVkIGFuIEhXSUQsIGdldCBvbmUgZnJvbSBmdW5rZW11bmt5ISBFLW1haWwgZnVua2VtdW5reWJpekBnbWFpbC5jb20gb3IgbWVzc2FnZSBmdW5rZW11bmt5IG9uIFNwaWdvdE1DIG9yIE1DTWFya2V0Lg==");
        this.Checks = new ArrayList<Check>();
        this.Violations = new HashMap<UUID, Map<Check, Integer>>();
        this.ViolationReset = new HashMap<UUID, Map<Check, Long>>();
        this.AlertsOn = new ArrayList<Player>();
        this.AutoBan = new HashMap<Player, Map.Entry<Check, Long>>();
        this.NamesBanned = new HashMap<String, Check>();
        this.rand = new Random();
        this.LastVelocity = new HashMap<UUID, Long>();
    }
    
    public void addChecks() {
        this.Checks.add(new AscensionA(this));
        this.Checks.add(new AscensionB(this));
        this.Checks.add(new SpeedA(this));
        this.Checks.add(new SpeedB(this));
        this.Checks.add(new Fly(this));
        this.Checks.add(new Step(this));
        this.Checks.add(new Regen(this));
        this.Checks.add(new NoFall(this));
        this.Checks.add(new PhaseA(this));
        this.Checks.add(new VClip(this));
        this.Checks.add(new KillAuraA(this));
        this.Checks.add(new KillAuraB(this));
        this.Checks.add(new KillAuraC(this));
        this.Checks.add(new KillAuraD(this));
        this.Checks.add(new KillAuraE(this));
        this.Checks.add(new KillauraF(this));
        this.Checks.add(new KillauraG(this));
        this.Checks.add(new HitBoxes(this));
        this.Checks.add(new AutoclickerA(this));
        this.Checks.add(new AutoclickerB(this));
        this.Checks.add(new FastBow(this));
        this.Checks.add(new Twitch(this));
        this.Checks.add(new NoSlowdown(this));
        this.Checks.add(new Crits(this));
        this.Checks.add(new ReachA(this));
        this.Checks.add(new ReachB(this));
        this.Checks.add(new ReachC(this));
        this.Checks.add(new MorePackets(this));
        this.Checks.add(new Timer(this));
        this.Checks.add(new TimerB(this));
        this.Checks.add(new Sneak(this));
        this.Checks.add(new Crash(this));
        this.Checks.add(new FastLadder(this));
        this.Checks.add(new Jesus(this));
        this.Checks.add(new Exploit(this));
        this.Checks.add(new Spiderman(this));
        this.Checks.add(new Vape(this));
        this.Checks.add(new FreeCam(this));
    }
    
    public void onEnable() {
        (Bernard.Instance = this).addChecks();
        this.packet = new PacketCore(this);
        this.lag = new LagCore(this);
        this.updater = new Updater(this);
        final Vape vapers = new Vape(this);
        new BernardAPI((Plugin)this);
        this.getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "LOLIMAHCKER", (PluginMessageListener)vapers);
        for (final Check check : this.Checks) {
            if (check.isEnabled()) {
                this.RegisterListener((Listener)check);
            }
        }
        final File file = new File(this.getDataFolder(), "config.yml");
        this.getCommand("alerts").setExecutor((CommandExecutor)new AlertsCommand(this));
        this.getCommand("autoban").setExecutor((CommandExecutor)new AutobanCommand(this));
        this.getCommand("bernard").setExecutor((CommandExecutor)new BernardCommand(this));
        this.getCommand("getLog").setExecutor((CommandExecutor)new GetLogCommand(this));
        Bukkit.getServer().getPluginManager().registerEvents((Listener)new ChecksGUI(this), (Plugin)this);
        this.RegisterListener((Listener)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)new Latency(this), (Plugin)this);
        if (!file.exists()) {
            this.getConfig().addDefault("bans", (Object)0);
            this.getConfig().addDefault("testmode", (Object)false);
            this.getConfig().addDefault("prefix", (Object)"&8[&c&lBernard&8] ");
            this.getConfig().addDefault("alerts.primary", (Object)"&7");
            this.getConfig().addDefault("alerts.secondary", (Object)"&c");
            this.getConfig().addDefault("alerts.checkColor", (Object)"&b");
            this.getConfig().addDefault("bancmd", (Object)"ban %player% [Bernard] Unfair Advantage: %check%");
            this.getConfig().addDefault("broadcastmsg", (Object)"&c&lDaedalus &7has detected &c%player% &7to be cheating and has been removed from the network.");
            this.getConfig().addDefault("settings.broadcastResetViolationsMsg", (Object)true);
            this.getConfig().addDefault("settings.violationResetTime", (Object)60);
            this.getConfig().addDefault("settings.resetViolationsAutomatically", (Object)true);
            this.getConfig().addDefault("settings.gui.checkered", (Object)true);
            this.getConfig().addDefault("settings.latency.ping", (Object)300);
            this.getConfig().addDefault("settings.latency.tps", (Object)17);
            this.getConfig().addDefault("settings.sotwMode", (Object)false);
            this.getConfig().addDefault("hwid", (Object)"");
            for (final Check check2 : this.Checks) {
                this.getConfig().addDefault("checks." + check2.getIdentifier() + ".enabled", (Object)check2.isEnabled());
                this.getConfig().addDefault("checks." + check2.getIdentifier() + ".bannable", (Object)check2.isBannable());
                this.getConfig().addDefault("checks." + check2.getIdentifier() + ".banTimer", (Object)check2.hasBanTimer());
                this.getConfig().addDefault("checks." + check2.getIdentifier() + ".maxViolations", (Object)check2.getMaxViolations());
            }
            this.getConfig().addDefault("checks.Phase.pearlFix", (Object)true);
            this.getConfig().options().copyDefaults(true);
            this.saveConfig();
        }
        for (final Check check2 : this.Checks) {
            if (!this.getConfig().isConfigurationSection("checks." + check2.getIdentifier())) {
                this.getConfig().set("checks." + check2.getIdentifier() + ".enabled", (Object)check2.isEnabled());
                this.getConfig().set("checks." + check2.getIdentifier() + ".bannable", (Object)check2.isBannable());
                this.getConfig().set("checks." + check2.getIdentifier() + ".banTimer", (Object)check2.hasBanTimer());
                this.getConfig().set("checks." + check2.getIdentifier() + ".maxViolations", (Object)check2.getMaxViolations());
                this.saveConfig();
            }
        }
        this.PREFIX = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix"));
        new BukkitRunnable() {
            @SuppressWarnings("deprecation")
			public void run() {
                Bernard.this.getLogger().log(Level.INFO, "Reset Violations!");
                if (Bernard.this.getConfig().getBoolean("resetViolationsAutomatically")) {
                    if (Bernard.this.getConfig().getBoolean("settings.broadcastResetViolationsMsg")) {
                        Player[] onlinePlayers;
                        for (int length = (onlinePlayers = Bukkit.getOnlinePlayers()).length, i = 0; i < length; ++i) {
                            final Player online = onlinePlayers[i];
                            if (online.hasPermission("daedalus.admin") && Bernard.this.hasAlertsOn(online)) {
                                online.sendMessage(String.valueOf(Bernard.this.PREFIX) + ChatColor.translateAlternateColorCodes('&', "&7Reset violations for all players!"));
                            }
                        }
                    }
                    Bernard.this.resetAllViolations();
                }
            }
        }.runTaskTimerAsynchronously((Plugin)this, 0L, TimeUnit.SECONDS.toMillis(this.getConfig().getLong("settings.violationResetTime")));
    }
    
    public void resetDumps(final Player player) {
        for (final Check check : this.Checks) {
            if (check.hasDump(player)) {
                check.clearDump(player);
            }
        }
    }
    
    public void resetAllViolations() {
        this.Violations.clear();
        this.ViolationReset.clear();
    }
    
    public String resetData() {
        try {
            this.resetAllViolations();
            if (!AutoclickerB.Clicks.isEmpty()) {
                AutoclickerB.Clicks.clear();
            }
            if (!AutoclickerB.LastMS.isEmpty()) {
                AutoclickerB.LastMS.clear();
            }
            if (!AutoclickerB.ClickTicks.isEmpty()) {
                AutoclickerB.ClickTicks.clear();
            }
            if (!Crits.CritTicks.isEmpty()) {
                Crits.CritTicks.clear();
            }
            if (!KillAuraA.ClickTicks.isEmpty()) {
                KillAuraA.ClickTicks.clear();
            }
            if (!KillAuraA.Clicks.isEmpty()) {
                KillAuraA.Clicks.clear();
            }
            if (!KillAuraA.LastMS.isEmpty()) {
                KillAuraA.LastMS.clear();
            }
            if (!KillAuraB.AuraTicks.isEmpty()) {
                KillAuraB.AuraTicks.clear();
            }
            if (!KillAuraC.Differences.isEmpty()) {
                KillAuraC.Differences.clear();
            }
            if (!KillAuraC.LastLocation.isEmpty()) {
                KillAuraC.LastLocation.clear();
            }
            if (!KillAuraC.AimbotTicks.isEmpty()) {
                KillAuraC.AimbotTicks.clear();
            }
            if (!KillAuraE.lastAttack.isEmpty()) {
                KillAuraE.lastAttack.clear();
            }
            if (!KillauraF.counts.isEmpty()) {
                KillauraF.counts.clear();
            }
            if (!Regen.FastHealTicks.isEmpty()) {
                Regen.FastHealTicks.clear();
            }
            if (!Regen.LastHeal.isEmpty()) {
                Regen.LastHeal.clear();
            }
            if (!AscensionA.AscensionTicks.isEmpty()) {
                AscensionA.AscensionTicks.clear();
            }
            if (!AscensionB.flyTicks.isEmpty()) {
                AscensionB.flyTicks.clear();
            }
            if (!FastLadder.count.isEmpty()) {
                FastLadder.count.clear();
            }
            if (!Fly.flyTicksA.isEmpty()) {
                Fly.flyTicksA.clear();
            }
            if (!Glide.flyTicks.isEmpty()) {
                Glide.flyTicks.clear();
            }
            if (!NoFall.FallDistance.isEmpty()) {
                NoFall.FallDistance.clear();
            }
            if (!NoFall.NoFallTicks.isEmpty()) {
                NoFall.NoFallTicks.clear();
            }
            if (!NoSlowdown.speedTicks.isEmpty()) {
                NoSlowdown.speedTicks.clear();
            }
            if (!SpeedA.speedTicks.isEmpty()) {
                SpeedA.speedTicks.clear();
            }
            if (!SpeedA.tooFastTicks.isEmpty()) {
                SpeedA.tooFastTicks.clear();
            }
            if (!SpeedA.lastHit.isEmpty()) {
                SpeedA.lastHit.isEmpty();
            }
            if (!MorePackets.lastPacket.isEmpty()) {
                MorePackets.lastPacket.clear();
            }
            if (!MorePackets.packetTicks.isEmpty()) {
                MorePackets.packetTicks.clear();
            }
            if (!Sneak.sneakTicks.isEmpty()) {
                Sneak.sneakTicks.clear();
            }
            if (!HitBoxes.count.isEmpty()) {
                HitBoxes.count.clear();
            }
            if (!HitBoxes.lastHit.isEmpty()) {
                HitBoxes.lastHit.clear();
            }
            if (!HitBoxes.yawDif.isEmpty()) {
                HitBoxes.yawDif.clear();
            }
            if (!FastBow.count.isEmpty()) {
                FastBow.count.clear();
            }
        }
        catch (Exception e) {
            return ChatColor.translateAlternateColorCodes('&', String.valueOf(this.PREFIX) + C.Red + "Unknown error occured!");
        }
        return ChatColor.translateAlternateColorCodes('&', String.valueOf(this.PREFIX) + C.Green + "Successfully reset data!");
    }
    
    public Integer getPingCancel() {
        return this.pingToCancel;
    }
    
    public Integer getTPSCancel() {
        return this.tpsToCancel;
    }
    
    public List<Check> getChecks() {
        return new ArrayList<Check>(this.Checks);
    }
    
    public boolean isCheckingUpdates() {
        return this.getConfig().getBoolean("settings.checkUpdates");
    }
    
    public String getVersion() {
        return this.getDescription().getVersion();
    }
    
    public boolean isSotwMode() {
        return this.getConfig().getBoolean("settings.sotwMode");
    }
    
    public boolean hasNewVersion() {
        return !this.getVersion().equalsIgnoreCase(this.getPasteVersion());
    }
    
    public String getPasteVersion() {
        try {
            final URL url = new URL(UtilMath.decrypt("aHR0cDovL3Bhc3RlYmluLmNvbS9yYXcvQU4yWEtqTlM="));
            final URLConnection connection = url.openConnection();
            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final String line = in.readLine();
            if (line != null) {
                return line;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            this.getLogger().log(Level.SEVERE, UtilMath.decrypt("RXJyb3IhIENvdWxkIG5vdCBjaGVjayBmb3IgYSBuZXcgdmVyc2lvbiE="));
        }
        return "Error";
    }
    
    public Map<String, Check> getNamesBanned() {
        return new HashMap<String, Check>(this.NamesBanned);
    }
    
    public String getCraftBukkitVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
    
    public List<Player> getAutobanQueue() {
        return new ArrayList<Player>(this.AutoBan.keySet());
    }
    
    public void createLog(final Player player, final Check checkBanned) {
        final TxtFile logFile = new TxtFile(this, String.valueOf(File.separator) + "logs", player.getName());
        final Map<Check, Integer> Checks = this.getViolations(player);
        logFile.addLine("------------------- Player was banned for: " + checkBanned.getName() + " -------------------");
        logFile.addLine("Set off checks:");
        for (final Check check : Checks.keySet()) {
            final Integer Violations = Checks.get(check);
            logFile.addLine("- " + check.getName() + " (" + Violations + " VL)");
        }
        logFile.addLine(" ");
        logFile.addLine("Dump-Log for all checks set off:");
        for (final Check check : Checks.keySet()) {
            logFile.addLine(" ");
            logFile.addLine(String.valueOf(check.getName()) + ":");
            if (check.getDump(player) != null) {
                for (final String line : check.getDump(player)) {
                    logFile.addLine(line);
                }
            }
            else {
                logFile.addLine("Checks had no dump logs.!");
            }
            logFile.addLine(" ");
        }
        logFile.write();
    }
    
    public void removeFromAutobanQueue(final Player player) {
        this.AutoBan.remove(player);
    }
    
    public void removeViolations(final Player player) {
        this.Violations.remove(player.getUniqueId());
    }
    
    public boolean hasAlertsOn(final Player player) {
        return this.AlertsOn.contains(player);
    }
    
    public void toggleAlerts(final Player player) {
        if (this.hasAlertsOn(player)) {
            this.AlertsOn.remove(player);
        }
        else {
            this.AlertsOn.add(player);
        }
    }
    
    public LagCore getLag() {
        return this.lag;
    }
    
    @EventHandler
    public void Join(final PlayerJoinEvent e) {
        if (!e.getPlayer().hasPermission("daedalus.staff")) {
            return;
        }
        this.AlertsOn.add(e.getPlayer());
    }
    
    @EventHandler
    public void autobanupdate(final UpdateEvent event) {
        if (!event.getType().equals(UpdateType.SEC)) {
            return;
        }
        final Map<Player, Map.Entry<Check, Long>> AutoBan = new HashMap<Player, Map.Entry<Check, Long>>(this.AutoBan);
        for (final Player player : AutoBan.keySet()) {
            if (player == null || !player.isOnline()) {
                this.AutoBan.remove(player);
            }
            else {
                final Long time = AutoBan.get(player).getValue();
                if (System.currentTimeMillis() < time) {
                    continue;
                }
                this.autobanOver(player);
            }
        }
        final Map<UUID, Map<Check, Long>> ViolationResets = new HashMap<UUID, Map<Check, Long>>(this.ViolationReset);
        for (final UUID uid : ViolationResets.keySet()) {
            if (!this.Violations.containsKey(uid)) {
                continue;
            }
            final Map<Check, Long> Checks = new HashMap<Check, Long>(ViolationResets.get(uid));
            for (final Check check : Checks.keySet()) {
                final Long time2 = Checks.get(check);
                if (System.currentTimeMillis() >= time2) {
                    this.ViolationReset.get(uid).remove(check);
                    this.Violations.get(uid).remove(check);
                }
            }
        }
    }
    
    public Integer getViolations(final Player player, final Check check) {
        if (this.Violations.containsKey(player.getUniqueId())) {
            return this.Violations.get(player.getUniqueId()).get(check);
        }
        return 0;
    }
    
    public Map<Check, Integer> getViolations(final Player player) {
        if (this.Violations.containsKey(player.getUniqueId())) {
            return new HashMap<Check, Integer>(this.Violations.get(player.getUniqueId()));
        }
        return null;
    }
    
    private void wqminoiwn() {
        Bukkit.getPluginManager().disablePlugin((Plugin)this);
    }
    
    public void addViolation(final Player player, final Check check) {
        Map<Check, Integer> map = new HashMap<Check, Integer>();
        if (this.Violations.containsKey(player.getUniqueId())) {
            map = this.Violations.get(player.getUniqueId());
        }
        if (!map.containsKey(check)) {
            map.put(check, 1);
        }
        else {
            map.put(check, map.get(check) + 1);
        }
        this.Violations.put(player.getUniqueId(), map);
    }
    
    public void removeViolations(final Player player, final Check check) {
        if (this.Violations.containsKey(player.getUniqueId())) {
            this.Violations.get(player.getUniqueId()).remove(check);
        }
    }
    
    public void setViolationResetTime(final Player player, final Check check, final long time) {
        Map<Check, Long> map = new HashMap<Check, Long>();
        if (this.ViolationReset.containsKey(player.getUniqueId())) {
            map = this.ViolationReset.get(player.getUniqueId());
        }
        map.put(check, time);
        this.ViolationReset.put(player.getUniqueId(), map);
    }
    
    public void getAPI() {
        try {
            final URL url = new URL(UtilMath.decrypt("aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3L3pFZ1lLQVBu"));
            final ArrayList<Object> lines = new ArrayList<Object>();
            final URLConnection connection = url.openConnection();
            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
            if (!lines.contains(this.getConfig().getString("hwid")) && this.getConfig().getString("hwid") != null) {
                this.getLogger().log(Level.SEVERE, this.wngnq);
                this.wqminoiwn();
            }
            else if (this.getConfig().getString("hwid") == null) {
                this.getLogger().log(Level.SEVERE, UtilMath.decrypt("QWRkIGFuIEhXSUQgaW4gdGhlIGNvbmZpZyENCg=="));
                this.wqminoiwn();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            this.getLogger().log(Level.SEVERE, UtilMath.decrypt("RXJyb3IhIERpc2FibGluZyBwbHVnaW4u"));
            Bukkit.getPluginManager().disablePlugin((Plugin)this);
        }
    }
    
    public void autobanOver(final Player player) {
        final Map<Player, Map.Entry<Check, Long>> AutoBan = new HashMap<Player, Map.Entry<Check, Long>>(this.AutoBan);
        if (AutoBan.containsKey(player)) {
            this.banPlayer(player, AutoBan.get(player).getKey());
            this.AutoBan.remove(player);
        }
    }
    
    public void autoban(final Check check, final Player player) {
        if (this.lag.getTPS() < 17.0) {
            return;
        }
        if (check.hasBanTimer()) {
            if (this.AutoBan.containsKey(player)) {
                return;
            }
            this.AutoBan.put(player, new AbstractMap.SimpleEntry<Check, Long>(check, System.currentTimeMillis() + 10000L));
            System.out.println("[" + player.getUniqueId().toString() + "] " + player.getName() + " will be banned in 15s for " + check.getName() + ".");
            final UtilActionMessage msg = new UtilActionMessage();
            msg.addText(this.PREFIX);
            msg.addText(ChatColor.translateAlternateColorCodes('&', String.valueOf(this.getConfig().getString("alerts.secondary")) + player.getName())).addHoverText(String.valueOf(C.Gray) + "(Click to teleport to " + C.Red + player.getName() + C.Gray + ")").setClickEvent(UtilActionMessage.ClickableType.RunCommand, "/tp " + player.getName());
            msg.addText(ChatColor.translateAlternateColorCodes('&', String.valueOf(this.getConfig().getString("alerts.primary")) + " set off " + this.getConfig().getString("alerts.secondary") + check.getName() + this.getConfig().getString("alerts.primary") + " and will " + this.getConfig().getString("alerts.primary") + "be " + this.getConfig().getString("alerts.primary") + "banned" + this.getConfig().getString("alerts.primary") + " if you don't take action. " + C.DGray + C.Bold + "["));
            msg.addText(ChatColor.translateAlternateColorCodes('&', String.valueOf(this.getConfig().getString("alerts.secondary")) + C.Bold + "ban")).addHoverText(String.valueOf(C.Gray) + "Autoban " + C.Green + player.getName()).setClickEvent(UtilActionMessage.ClickableType.RunCommand, "/autoban ban " + player.getName());
            msg.addText(String.valueOf(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("alerts.primary"))) + " or ");
            msg.addText(String.valueOf(C.Green) + C.Bold + "cancel").addHoverText(String.valueOf(C.Gray) + "Click to Cancel").setClickEvent(UtilActionMessage.ClickableType.RunCommand, "/autoban cancel " + player.getName());
            msg.addText(String.valueOf(C.DGray) + C.Bold + "]");
            ArrayList<Player> players;
            for (int length = (players = UtilServer.getOnlinePlayers()).size(), i = 0; i < length; ++i) {
                final Player playerplayer = players.get(i);
                if (playerplayer.hasPermission("daedalus.staff")) {
                    msg.sendToPlayer(playerplayer);
                }
            }
        }
        else {
            this.banPlayer(player, check);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void Velocity(final PlayerVelocityEvent event) {
        this.LastVelocity.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }
    

	public void banPlayer(final Player player, final Check check) {
        if (!this.getConfig().getBoolean("testmode")) {
            this.createLog(player, check);
        }
        if (this.NamesBanned.containsKey(player.getName()) && !this.getConfig().getBoolean("testmode")) {
            return;
        }
        this.NamesBanned.put(player.getName(), check);
        this.removeViolations(player, check);
        new BukkitRunnable() {
            public void run() {
                if (Bernard.this.NamesBanned.containsKey(player.getName()) && Bernard.this.getConfig().getBoolean("testmode")) {
                    return;
                }
                if (Latency.getLag(player) < 250) {
                    if (Bernard.this.getConfig().getBoolean("testmode")) {
                        player.sendMessage(String.valueOf(Bernard.this.PREFIX) + C.Gray + "You would be banned right now for: " + C.Red + check.getName());
                    }
                    else {
                        if (!Bernard.this.getConfig().getString("broadcastmsg").equalsIgnoreCase("")) {
                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Bernard.this.getConfig().getString("broadcastmsg").replaceAll("%player%", player.getName())));
                        }
                        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), Bernard.this.getConfig().getString("bancmd").replaceAll("%player%", player.getName()).replaceAll("%check%", check.getName()));
                    }
                }
                Bernard.this.NamesBanned.put(player.getName(), check);
            }
        }.runTaskLater((Plugin)this, 10L);
        if (this.Violations.containsKey(player)) {
            this.Violations.remove(player);
        }
        this.getConfig().set("bans", (Object)(this.getConfig().getInt("bans") + 1));
        this.saveConfig();
    }
    
    public void alert(final String message) {
        for (final Player playerplayer : this.AlertsOn) {
            playerplayer.sendMessage(String.valueOf(String.valueOf(this.PREFIX)) + message);
        }
    }
    
    public void logCheat(final Check check, final Player player, final String hoverabletext, final Chance chance, final String... identifiers) {
        String a = "";
        if (identifiers != null) {
            for (final String b : identifiers) {
                a = String.valueOf(a) + " (" + b + ")";
            }
        }
        this.addViolation(player, check);
        this.setViolationResetTime(player, check, System.currentTimeMillis() + check.getViolationResetTime());
        final Integer violations = this.getViolations(player, check);
        System.out.println("[" + player.getUniqueId().toString() + "] " + player.getName() + " failed " + (check.isJudgmentDay() ? "JD check " : "") + check.getName() + a + " [" + violations + " VL]");
        if (violations >= check.getViolationsToNotify()) {
            final UtilActionMessage msg = new UtilActionMessage();
            msg.addText(this.PREFIX);
            msg.addText(String.valueOf(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("alerts.secondary"))) + player.getName()).addHoverText(String.valueOf(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("alerts.primary"))) + "(Click to teleport to " + ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("alerts.secondary")) + player.getName() + ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("alerts.primary")) + ")").setClickEvent(UtilActionMessage.ClickableType.RunCommand, "/tp " + player.getName());
            msg.addText(String.valueOf(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("alerts.primary"))) + " failed " + (check.isJudgmentDay() ? "JD check " : ""));
            final UtilActionMessage.AMText CheckText = msg.addText(String.valueOf(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("alerts.checkColor"))) + check.getName());
            if (hoverabletext != null) {
                CheckText.addHoverText(hoverabletext);
            }
            msg.addText(String.valueOf(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("alerts.secondary"))) + a + ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("alerts.primary")) + " ");
            msg.addText(String.valueOf(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("alerts.primary"))) + "[" + ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("alerts.secondary")) + violations + ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("alerts.primary")) + " VL]");
            msg.addText(String.valueOf(ChatColor.translateAlternateColorCodes('&', new StringBuilder(" ").append(this.getConfig().getString("alerts.primary")).toString())) + "(" + BernardAPI.getChanceString(chance) + ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("alerts.primary")) + ")");
            if (violations % check.getViolationsToNotify() == 0) {
                if (this.getConfig().getBoolean("testmode")) {
                    msg.sendToPlayer(player);
                }
                else {
                    for (final Player playerplayer : this.AlertsOn) {
                        if (check.isJudgmentDay() && !playerplayer.hasPermission("daedalus.admin")) {
                            continue;
                        }
                        msg.sendToPlayer(playerplayer);
                    }
                }
            }
            if (check.isJudgmentDay()) {
                return;
            }
            if (violations > check.getMaxViolations() && check.isBannable()) {
                this.autoban(check, player);
            }
        }
    }
    
    public void RegisterListener(final Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, (Plugin)this);
    }
    
    public Map<UUID, Long> getLastVelocity() {
        return this.LastVelocity;
    }
    
    @EventHandler
    public void Kick(final PlayerKickEvent event) {
        if (event.getReason().equals("Flying is not enabled on this server")) {
            this.alert(String.valueOf(String.valueOf(C.Gray)) + event.getPlayer().getName() + " was kicked for flying");
        }
    }
}
