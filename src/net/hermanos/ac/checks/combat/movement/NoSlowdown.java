package net.hermanos.ac.checks.combat.movement;

import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import org.bukkit.event.block.*;
import java.util.*;

public class NoSlowdown extends Check
{
    public static Map<UUID, Map.Entry<Integer, Long>> speedTicks;
    
    public NoSlowdown(final Bernard Daedalus) {
        super("NoSlowdown", "NoSlowdown", Daedalus);
        this.setEnabled(true);
        this.setBannable(true);
        this.setMaxViolations(5);
        NoSlowdown.speedTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent e) {
        if (NoSlowdown.speedTicks.containsKey(e.getPlayer().getUniqueId())) {
            NoSlowdown.speedTicks.remove(e.getPlayer().getUniqueId());
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void BowShoot(final EntityShootBowEvent event) {
        if (!this.isEnabled()) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final Player player = (Player)event.getEntity();
        if (player.isInsideVehicle()) {
            return;
        }
        if (player.isSprinting()) {
            this.getDaedalus().logCheat(this, player, "Sprinting while bowing.", Chance.LIKELY, new String[0]);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(final PlayerMoveEvent e) {
        if (e.getTo().getX() == e.getFrom().getX() && e.getFrom().getY() == e.getTo().getY() && e.getTo().getZ() == e.getFrom().getZ()) {
            return;
        }
        final Player player = e.getPlayer();
        final double OffsetXZ = UtilMath.offset(UtilMath.getHorizontalVector(e.getFrom().toVector()), UtilMath.getHorizontalVector(e.getTo().toVector()));
        if (!player.getLocation().getBlock().getType().equals((Object)Material.WEB)) {
            return;
        }
        if (OffsetXZ < 0.2) {
            return;
        }
        this.getDaedalus().logCheat(this, player, null, Chance.LIKELY, new String[0]);
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null) {
            if (event.getItem().equals((Object)Material.EXP_BOTTLE) || event.getItem().getType().equals((Object)Material.GLASS_BOTTLE) || event.getItem().getType().equals((Object)Material.POTION)) {
                return;
            }
            final Player player = event.getPlayer();
            if (player.hasPermission("daedalus.bypass")) {
                return;
            }
            long Time = System.currentTimeMillis();
            int level = 0;
            if (NoSlowdown.speedTicks.containsKey(player.getUniqueId())) {
                level = NoSlowdown.speedTicks.get(player.getUniqueId()).getKey();
                Time = NoSlowdown.speedTicks.get(player.getUniqueId()).getValue();
            }
            final double diff = System.currentTimeMillis() - Time;
            int n;
            if (diff >= 2.0) {
                if (diff <= 51.0) {
                    level += 2;
                    n = level;
                }
                else if (diff <= 100.0) {
                    level += 0;
                    n = level;
                }
                else if (diff <= 500.0) {
                    level -= 6;
                    n = level;
                }
                else {
                    level -= 12;
                    n = level;
                }
            }
            else {
                n = ++level;
            }
            level = n;
            final int max = 50;
            if (level > max * 0.9 && diff <= 100.0) {
                this.getDaedalus().logCheat(this, player, "Level: " + level + " Ping: " + this.getDaedalus().lag.getPing(player), Chance.HIGH, new String[0]);
                if (level > max) {
                    level = max / 4;
                }
            }
            else if (level < 0) {
                level = 0;
            }
            NoSlowdown.speedTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(level, System.currentTimeMillis()));
        }
    }
}
