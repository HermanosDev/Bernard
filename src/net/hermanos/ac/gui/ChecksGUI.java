package net.hermanos.ac.gui;

import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.scheduler.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import org.bukkit.plugin.*;
import org.bukkit.event.*;
import java.util.*;

public class ChecksGUI implements Listener
{
    public static Inventory Daedalusmain;
    public static Inventory Daedaluschecks;
    public static Inventory Daedalusbannable;
    public static Inventory DaedalusTimer;
    public static Inventory Daedalusbans;
    public static Inventory Daedalusstatus;
    private static ItemStack back;
    private static Bernard Daedalus;
    
    static {
        ChecksGUI.Daedalusmain = Bukkit.createInventory((InventoryHolder)null, 36, String.valueOf(C.Gold) + "Home");
        ChecksGUI.Daedaluschecks = Bukkit.createInventory((InventoryHolder)null, 45, String.valueOf(C.Gold) + "Checks: Toggle");
        ChecksGUI.Daedalusbannable = Bukkit.createInventory((InventoryHolder)null, 45, String.valueOf(C.Gold) + "Checks: Bannable");
        ChecksGUI.DaedalusTimer = Bukkit.createInventory((InventoryHolder)null, 45, String.valueOf(C.Gold) + "Checks: BanTimer");
        ChecksGUI.Daedalusbans = Bukkit.createInventory((InventoryHolder)null, 54, String.valueOf(C.Gold) + "Recent Bans");
        ChecksGUI.Daedalusstatus = Bukkit.createInventory((InventoryHolder)null, 27, String.valueOf(C.Gold) + "Status");
        ChecksGUI.back = createItem(Material.REDSTONE, 1, "&6Back", new String[0]);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public ChecksGUI(final Bernard Daedalus) {
        ChecksGUI.Daedalus = Daedalus;
        final ItemStack checks = createItem(Material.COMPASS, 1, "&cChecks", new String[0]);
        final ItemStack bannable = createItem(Material.REDSTONE, 1, "&cAuto Bans", new String[0]);
        final ItemStack timers = createItem(Material.WATCH, 1, "&cTimers", new String[0]);
        final ItemStack resetVio = createItem(Material.PAPER, 1, "&cReset Violations", new String[0]);
        final ItemStack reload = createItem(Material.LAVA_BUCKET, 1, "&cReload", new String[0]);
        final ItemStack info = createItem(Material.BOOK, 1, "&aInfo", new String[0]);
        final ItemStack checkered = createItem(Material.COAL_BLOCK, 1, Daedalus.getConfig().getBoolean("settings.gui.checkered") ? "&aCheckered" : "&cCheckered", new String[0]);
        final ItemStack sotwMode = createItem(Daedalus.getConfig().getBoolean("settings.sotwMode") ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "&cSoTW Mode", new String[0]);
        final ItemMeta infom = info.getItemMeta();
        infom.setLore((List)infoLore());
        info.setItemMeta(infom);
        ChecksGUI.Daedalusmain.setItem(9, checks);
        ChecksGUI.Daedalusmain.setItem(13, timers);
        ChecksGUI.Daedalusmain.setItem(11, bannable);
        ChecksGUI.Daedalusmain.setItem(15, reload);
        ChecksGUI.Daedalusmain.setItem(17, resetVio);
        ChecksGUI.Daedalusmain.setItem(1, grayGlass());
        ChecksGUI.Daedalusmain.setItem(3, grayGlass());
        ChecksGUI.Daedalusmain.setItem(5, grayGlass());
        ChecksGUI.Daedalusmain.setItem(7, grayGlass());
        ChecksGUI.Daedalusmain.setItem(19, grayGlass());
        ChecksGUI.Daedalusmain.setItem(21, grayGlass());
        ChecksGUI.Daedalusmain.setItem(23, grayGlass());
        ChecksGUI.Daedalusmain.setItem(25, grayGlass());
        ChecksGUI.Daedalusmain.setItem(27, sotwMode);
        ChecksGUI.Daedalusmain.setItem(29, grayGlass());
        ChecksGUI.Daedalusmain.setItem(31, grayGlass());
        ChecksGUI.Daedalusmain.setItem(33, grayGlass());
        ChecksGUI.Daedalusmain.setItem(35, checkered);
        ChecksGUI.Daedalusmain.setItem(31, info);
        if (Daedalus.getConfig().contains("settings.gui.checkered")) {
            if (Daedalus.getConfig().getBoolean("settings.gui.checkered")) {
                ChecksGUI.Daedalusmain.setItem(0, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(2, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(4, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(6, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(8, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(10, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(12, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(14, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(16, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(18, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(20, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(22, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(24, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(26, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(28, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(30, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(32, whiteGlass());
                ChecksGUI.Daedalusmain.setItem(34, whiteGlass());
            }
            else {
                ChecksGUI.Daedalusmain.setItem(0, grayGlass());
                ChecksGUI.Daedalusmain.setItem(2, grayGlass());
                ChecksGUI.Daedalusmain.setItem(4, grayGlass());
                ChecksGUI.Daedalusmain.setItem(6, grayGlass());
                ChecksGUI.Daedalusmain.setItem(8, grayGlass());
                ChecksGUI.Daedalusmain.setItem(10, grayGlass());
                ChecksGUI.Daedalusmain.setItem(12, grayGlass());
                ChecksGUI.Daedalusmain.setItem(14, grayGlass());
                ChecksGUI.Daedalusmain.setItem(16, grayGlass());
                ChecksGUI.Daedalusmain.setItem(18, grayGlass());
                ChecksGUI.Daedalusmain.setItem(20, grayGlass());
                ChecksGUI.Daedalusmain.setItem(22, grayGlass());
                ChecksGUI.Daedalusmain.setItem(24, grayGlass());
                ChecksGUI.Daedalusmain.setItem(26, grayGlass());
                ChecksGUI.Daedalusmain.setItem(28, grayGlass());
                ChecksGUI.Daedalusmain.setItem(30, grayGlass());
                ChecksGUI.Daedalusmain.setItem(32, grayGlass());
                ChecksGUI.Daedalusmain.setItem(34, grayGlass());
            }
        }
        else {
            Daedalus.getConfig().set("settings.gui.checkered", (Object)true);
            ChecksGUI.Daedalusmain.setItem(0, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(2, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(4, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(6, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(8, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(10, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(12, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(14, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(16, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(18, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(20, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(22, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(24, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(26, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(28, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(30, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(32, whiteGlass());
            ChecksGUI.Daedalusmain.setItem(34, whiteGlass());
        }
    }
    
    private static ArrayList<String> infoLore() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add(" ");
        list.add(ChatColor.translateAlternateColorCodes('&', "&7You can do &f/daedalus help &7to see your"));
        list.add(ChatColor.translateAlternateColorCodes('&', "&7options for other &fcommands&7/&ffunctions&7!"));
        list.add(" ");
        list.add(ChatColor.translateAlternateColorCodes('&', "&7Current Version: &fb" + ChecksGUI.Daedalus.getDescription().getVersion()));
        if (ChecksGUI.Daedalus.hasNewVersion()) {
            list.add(String.valueOf(C.Gold) + C.Italics + "New Update: " + C.White + "b" + ChecksGUI.Daedalus.getPasteVersion());
        }
        return list;
    }
    
    public static void openDaedalusMain(final Player player) {
        player.openInventory(ChecksGUI.Daedalusmain);
    }
    
    public static void openBans(final Player player) {
        final List<Map.Entry<String, Check>> entrybans = new ArrayList<Map.Entry<String, Check>>(ChecksGUI.Daedalus.getNamesBanned().entrySet());
        for (int i = 0; i < entrybans.size(); ++i) {
            final Map.Entry<String, Check> entry = entrybans.get(i);
            if (i <= 54) {
                final ItemStack offender = createItem(Material.PAPER, 1, String.valueOf(C.Red) + entry.getKey(), String.valueOf(C.Gray) + entry.getValue().getName());
                ChecksGUI.Daedalusbans.setItem(i, offender);
            }
        }
    }
    
    public static void openStatus(final Player player, final Player target) {
        ChecksGUI.Daedalusstatus = Bukkit.createInventory((InventoryHolder)player, 27, String.valueOf(C.Gold) + "Status");
        final Map<Check, Integer> Checks = ChecksGUI.Daedalus.getViolations(target);
        if (Checks == null || Checks.isEmpty()) {
            player.sendMessage(String.valueOf(C.Gray) + "This player set off 0 checks. Yay!");
        }
        else {
            int slot = 0;
            for (final Check Check : Checks.keySet()) {
                final Integer Violations = Checks.get(Check);
                final ItemStack vl = createItem(Material.PAPER, 1, String.valueOf(C.Aqua) + Check.getName() + C.DGray + " [" + C.Red + Violations + C.DGray + "]", new String[0]);
                ChecksGUI.Daedalusstatus.setItem(slot, vl);
                ++slot;
            }
        }
        player.openInventory(ChecksGUI.Daedalusstatus);
    }
    
    public void openChecks(final Player player) {
        int slot = 0;
        for (final Check check : ChecksGUI.Daedalus.getChecks()) {
            if (ChecksGUI.Daedalus.getConfig().getBoolean("checks." + check.getIdentifier() + ".enabled")) {
                final ItemStack c = createGlass(Material.STAINED_GLASS_PANE, 5, 1, String.valueOf(C.Green) + check.getName(), new String[0]);
                ChecksGUI.Daedaluschecks.setItem(slot, c);
            }
            else {
                final ItemStack c = createGlass(Material.STAINED_GLASS_PANE, 14, 1, String.valueOf(C.Red) + check.getName(), new String[0]);
                ChecksGUI.Daedaluschecks.setItem(slot, c);
            }
            ++slot;
        }
        for (int i = slot; i < 44; ++i) {
            final ItemStack c2 = createGlass(Material.STAINED_GLASS_PANE, 15, 1, String.valueOf(C.Gray) + "N/A", new String[0]);
            ChecksGUI.Daedaluschecks.setItem(i, c2);
        }
        ChecksGUI.Daedaluschecks.setItem(44, ChecksGUI.back);
        player.openInventory(ChecksGUI.Daedaluschecks);
    }
    
    public void openAutoBans(final Player player) {
        int slot = 0;
        for (final Check check : ChecksGUI.Daedalus.getChecks()) {
            if (ChecksGUI.Daedalus.getConfig().getBoolean("checks." + check.getIdentifier() + ".bannable")) {
                final ItemStack c = createGlass(Material.STAINED_GLASS_PANE, 5, 1, String.valueOf(C.Green) + check.getName(), new String[0]);
                ChecksGUI.Daedalusbannable.setItem(slot, c);
            }
            else {
                final ItemStack c = createGlass(Material.STAINED_GLASS_PANE, 14, 1, String.valueOf(C.Red) + check.getName(), new String[0]);
                ChecksGUI.Daedalusbannable.setItem(slot, c);
            }
            ++slot;
        }
        for (int i = slot; i < 44; ++i) {
            final ItemStack c2 = createGlass(Material.STAINED_GLASS_PANE, 15, 1, String.valueOf(C.Gray) + "N/A", new String[0]);
            ChecksGUI.Daedalusbannable.setItem(i, c2);
        }
        ChecksGUI.Daedalusbannable.setItem(44, ChecksGUI.back);
        player.openInventory(ChecksGUI.Daedalusbannable);
    }
    
    public void openTimer(final Player player) {
        int slot = 0;
        for (final Check check : ChecksGUI.Daedalus.getChecks()) {
            if (ChecksGUI.Daedalus.getConfig().getBoolean("checks." + check.getIdentifier() + ".banTimer")) {
                final ItemStack c = createGlass(Material.STAINED_GLASS_PANE, 5, 1, String.valueOf(C.Green) + check.getName(), new String[0]);
                ChecksGUI.DaedalusTimer.setItem(slot, c);
            }
            else {
                final ItemStack c = createGlass(Material.STAINED_GLASS_PANE, 14, 1, String.valueOf(C.Red) + check.getName(), new String[0]);
                ChecksGUI.DaedalusTimer.setItem(slot, c);
            }
            ++slot;
        }
        for (int i = slot; i < 44; ++i) {
            final ItemStack c2 = createGlass(Material.STAINED_GLASS_PANE, 15, 1, String.valueOf(C.Gray) + "N/A", new String[0]);
            ChecksGUI.DaedalusTimer.setItem(i, c2);
        }
        ChecksGUI.DaedalusTimer.setItem(44, ChecksGUI.back);
        player.openInventory(ChecksGUI.DaedalusTimer);
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory().getName().equals(String.valueOf(C.Gold) + "Home")) {
            final Player player = (Player)e.getWhoClicked();
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            if (e.getCurrentItem() == null) {
                return;
            }
            if (!e.getCurrentItem().hasItemMeta()) {
                return;
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', "&cChecks"))) {
                this.openChecks(player);
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&cAuto Bans"))) {
                this.openAutoBans(player);
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&cTimers"))) {
                this.openTimer(player);
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&cSoTW Mode"))) {
                if (ChecksGUI.Daedalus.getConfig().getBoolean("settings.sotwMode")) {
                    ChecksGUI.Daedalus.getConfig().set("settings.sotwMode", (Object)false);
                    ChecksGUI.Daedalus.saveConfig();
                    final ItemStack sotwMode = createItem(ChecksGUI.Daedalus.getConfig().getBoolean("settings.sotwMode") ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "&cSoTW Mode", new String[0]);
                    ChecksGUI.Daedalusmain.setItem(27, sotwMode);
                }
                else {
                    ChecksGUI.Daedalus.getConfig().set("settings.sotwMode", (Object)true);
                    ChecksGUI.Daedalus.saveConfig();
                    final ItemStack sotwMode = createItem(ChecksGUI.Daedalus.getConfig().getBoolean("settings.sotwMode") ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "&cSoTW Mode", new String[0]);
                    ChecksGUI.Daedalusmain.setItem(27, sotwMode);
                }
            }
            if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equals("Checkered")) {
                ChecksGUI.Daedalus.getConfig().set("settings.gui.checkered", (Object)!ChecksGUI.Daedalus.getConfig().getBoolean("settings.gui.checkered"));
                ChecksGUI.Daedalus.saveConfig();
                @SuppressWarnings("unused")
				final ItemStack sotwMode = createItem(ChecksGUI.Daedalus.getConfig().getBoolean("settings.sotwMode") ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "&cSoTW Mode", new String[0]);
                if (ChecksGUI.Daedalus.getConfig().contains("settings.gui.checkered")) {
                    if (ChecksGUI.Daedalus.getConfig().getBoolean("settings.gui.checkered")) {
                        ChecksGUI.Daedalusmain.setItem(0, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(2, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(4, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(6, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(8, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(10, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(12, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(14, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(16, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(18, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(20, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(22, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(24, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(26, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(28, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(30, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(32, whiteGlass());
                        ChecksGUI.Daedalusmain.setItem(34, whiteGlass());
                    }
                    else {
                        ChecksGUI.Daedalusmain.setItem(0, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(2, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(4, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(6, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(8, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(10, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(12, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(14, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(16, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(18, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(20, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(22, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(24, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(26, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(28, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(30, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(32, grayGlass());
                        ChecksGUI.Daedalusmain.setItem(34, grayGlass());
                    }
                }
                else {
                    ChecksGUI.Daedalus.getConfig().set("settings.gui.checkered", (Object)true);
                    ChecksGUI.Daedalusmain.setItem(0, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(2, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(4, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(6, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(8, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(10, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(12, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(14, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(16, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(18, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(20, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(22, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(24, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(26, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(28, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(30, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(32, whiteGlass());
                    ChecksGUI.Daedalusmain.setItem(34, whiteGlass());
                }
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&cReset Violations"))) {
                ChecksGUI.Daedalus.resetAllViolations();
                final ItemMeta meta = e.getCurrentItem().getItemMeta();
                meta.setDisplayName(String.valueOf(C.Green) + C.Italics + "Success!");
                e.getCurrentItem().setItemMeta(meta);
                new BukkitRunnable() {
                    public void run() {
                        final ItemMeta meta = e.getCurrentItem().getItemMeta();
                        meta.setDisplayName(String.valueOf(C.Red) + "Reset Violations");
                        e.getCurrentItem().setItemMeta(meta);
                    }
                }.runTaskLater((Plugin)ChecksGUI.Daedalus, 40L);
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&cReload"))) {
                final ItemMeta meta = e.getCurrentItem().getItemMeta();
                meta.setDisplayName(String.valueOf(C.Red) + C.Italics + "Reloading...");
                e.getCurrentItem().setItemMeta(meta);
                ChecksGUI.Daedalus.reloadConfig();
                meta.setDisplayName(String.valueOf(C.Green) + C.Italics + "Success!");
                e.getCurrentItem().setItemMeta(meta);
                new BukkitRunnable() {
                    public void run() {
                        final ItemMeta meta = e.getCurrentItem().getItemMeta();
                        meta.setDisplayName(String.valueOf(C.Red) + "Reload");
                        e.getCurrentItem().setItemMeta(meta);
                        ChecksGUI.openDaedalusMain(player);
                    }
                }.runTaskLater((Plugin)ChecksGUI.Daedalus, 40L);
            }
        }
        else if (e.getInventory().getName().equals(String.valueOf(C.Gold) + "Checks: Toggle")) {
            final Player player = (Player)e.getWhoClicked();
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getCurrentItem().hasItemMeta()) {
                final String check_name = e.getCurrentItem().getItemMeta().getDisplayName();
                for (final Check check : ChecksGUI.Daedalus.getChecks()) {
                    if (check.getName().equals(ChatColor.stripColor(check_name))) {
                        if (ChecksGUI.Daedalus.getConfig().getBoolean("checks." + check.getIdentifier() + ".enabled")) {
                            ChecksGUI.Daedalus.getConfig().set("checks." + check.getIdentifier() + ".enabled", (Object)false);
                            ChecksGUI.Daedalus.saveConfig();
                            ChecksGUI.Daedalus.reloadConfig();
                            check.setEnabled(false);
                            this.openChecks(player);
                            return;
                        }
                        ChecksGUI.Daedalus.getConfig().set("checks." + check.getIdentifier() + ".enabled", (Object)true);
                        ChecksGUI.Daedalus.saveConfig();
                        ChecksGUI.Daedalus.reloadConfig();
                        check.setEnabled(true);
                        this.openChecks(player);
                        return;
                    }
                }
                if (ChatColor.stripColor(check_name).equals("Back")) {
                    openDaedalusMain(player);
                }
            }
        }
        else if (e.getInventory().getName().equals(String.valueOf(C.Gold) + "Checks: Bannable")) {
            final Player player = (Player)e.getWhoClicked();
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getCurrentItem().hasItemMeta()) {
                final String check_name = e.getCurrentItem().getItemMeta().getDisplayName();
                for (final Check check : ChecksGUI.Daedalus.getChecks()) {
                    if (check.getName().contains(ChatColor.stripColor(check_name))) {
                        if (ChecksGUI.Daedalus.getConfig().getBoolean("checks." + check.getIdentifier() + ".bannable")) {
                            ChecksGUI.Daedalus.getConfig().set("checks." + check.getIdentifier() + ".banTimer", (Object)false);
                            ChecksGUI.Daedalus.getConfig().set("checks." + check.getIdentifier() + ".bannable", (Object)false);
                            ChecksGUI.Daedalus.saveConfig();
                            ChecksGUI.Daedalus.reloadConfig();
                            check.setBannable(false);
                            player.closeInventory();
                            this.openAutoBans(player);
                            return;
                        }
                        ChecksGUI.Daedalus.getConfig().set("checks." + check.getIdentifier() + ".bannable", (Object)true);
                        ChecksGUI.Daedalus.saveConfig();
                        ChecksGUI.Daedalus.reloadConfig();
                        check.setBannable(true);
                        player.closeInventory();
                        this.openAutoBans(player);
                        return;
                    }
                }
                if (ChatColor.stripColor(check_name).equals("Back")) {
                    openDaedalusMain(player);
                }
            }
        }
        else if (e.getInventory().getName().equals(String.valueOf(C.Gold) + "Checks: BanTimer")) {
            final Player player = (Player)e.getWhoClicked();
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getCurrentItem().hasItemMeta()) {
                final String check_name = e.getCurrentItem().getItemMeta().getDisplayName();
                for (final Check check : ChecksGUI.Daedalus.getChecks()) {
                    if (check.getName().equals(ChatColor.stripColor(check_name))) {
                        if (ChecksGUI.Daedalus.getConfig().getBoolean("checks." + check.getIdentifier() + ".bannable")) {
                            ChecksGUI.Daedalus.getConfig().set("checks." + check.getIdentifier() + ".banTimer", (Object)false);
                            ChecksGUI.Daedalus.getConfig().set("checks." + check.getIdentifier() + ".bannable", (Object)false);
                            ChecksGUI.Daedalus.saveConfig();
                            ChecksGUI.Daedalus.reloadConfig();
                            check.setAutobanTimer(false);
                            check.setBannable(false);
                            this.openTimer(player);
                            return;
                        }
                        ChecksGUI.Daedalus.getConfig().set("checks." + check.getIdentifier() + ".bannable", (Object)true);
                        ChecksGUI.Daedalus.getConfig().set("checks." + check.getIdentifier() + ".banTimer", (Object)true);
                        ChecksGUI.Daedalus.saveConfig();
                        ChecksGUI.Daedalus.reloadConfig();
                        check.setAutobanTimer(true);
                        check.setBannable(true);
                        this.openTimer(player);
                        return;
                    }
                }
                if (ChatColor.stripColor(check_name).equals("Back")) {
                    openDaedalusMain(player);
                }
            }
        }
        else if (e.getInventory().getName().equals(String.valueOf(C.Gold) + "Recent Bans")) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
        }
        else if (e.getInventory().getName().equals(String.valueOf(C.Gold) + "Status")) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static ItemStack createItem(final Material material, final int amount, final String name, final String... lore) {
        final ItemStack thing = new ItemStack(material, amount);
        final ItemMeta thingm = thing.getItemMeta();
        thingm.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        thingm.setLore((List)Arrays.asList(lore));
        thing.setItemMeta(thingm);
        return thing;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static ItemStack createGlass(final Material material, final int color, final int amount, final String name, final String... lore) {
        final ItemStack thing = new ItemStack(material, amount, (short)color);
        final ItemMeta thingm = thing.getItemMeta();
        thingm.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        thingm.setLore((List)Arrays.asList(lore));
        thing.setItemMeta(thingm);
        return thing;
    }
    
    public static ItemStack grayGlass() {
        final ItemStack thing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);
        final ItemMeta thingm = thing.getItemMeta();
        thingm.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b"));
        thing.setItemMeta(thingm);
        return thing;
    }
    
    public static ItemStack whiteGlass() {
        final ItemStack thing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)0);
        final ItemMeta thingm = thing.getItemMeta();
        thingm.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b"));
        thing.setItemMeta(thingm);
        return thing;
    }
    
    public String c(final String str) {
        return str.replaceAll("&", "\ufffd\ufffd");
    }
}
