package net.hermanos.ac.checks.combat.movement;

import org.bukkit.entity.*;

import java.util.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.block.*;
import org.bukkit.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

public class Jesus extends Check
{
    public static Map<Player, Integer> onWater;
    public static List<Player> placedBlockOnWater;
    public static Map<Player, Integer> count;
    
    public Jesus(final Bernard Daedalus) {
        super("Jesus", "Jesus", Daedalus);
        this.setEnabled(true);
        this.setBannable(true);
        this.setViolationsToNotify(1);
        this.setMaxViolations(5);
        Jesus.count = new WeakHashMap<Player, Integer>();
        Jesus.placedBlockOnWater = new ArrayList<Player>();
        Jesus.onWater = new WeakHashMap<Player, Integer>();
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(final PlayerQuitEvent e) {
        if (Jesus.onWater.containsKey(e.getPlayer())) {
            Jesus.onWater.remove(e.getPlayer());
        }
        if (Jesus.placedBlockOnWater.contains(e.getPlayer())) {
            Jesus.placedBlockOnWater.remove(e.getPlayer());
        }
        if (Jesus.count.containsKey(e.getPlayer())) {
            Jesus.count.remove(e.getPlayer());
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(final PlayerDeathEvent e) {
        if (Jesus.onWater.containsKey(e.getEntity())) {
            Jesus.onWater.remove(e.getEntity());
        }
        if (Jesus.placedBlockOnWater.contains(e.getEntity())) {
            Jesus.placedBlockOnWater.remove(e.getEntity());
        }
        if (Jesus.count.containsKey(e.getEntity())) {
            Jesus.count.remove(e.getEntity());
        }
    }
    
    @EventHandler
    public void OnPlace(final BlockPlaceEvent e) {
        if (e.getBlockReplacedState().getBlock().getType() == Material.WATER) {
            Jesus.placedBlockOnWater.add(e.getPlayer());
        }
    }
    
    @EventHandler
    public void CheckJesus(final PlayerMoveEvent event) {
        if (event.getFrom().getX() == event.getTo().getX() && event.getFrom().getZ() == event.getTo().getZ()) {
            return;
        }
        final Player p = event.getPlayer();
        if (p.hasPermission("daedalus.bypass")) {
            return;
        }
        if (p.getAllowFlight()) {
            return;
        }
        if (!p.getNearbyEntities(1.0, 1.0, 1.0).isEmpty()) {
            return;
        }
        if (UtilCheat.isOnLilyPad(p)) {
            return;
        }
        if (Jesus.placedBlockOnWater.remove(p)) {
            return;
        }
        int Count = 0;
        if (Jesus.count.containsKey(p)) {
            Count = Jesus.count.get(p);
        }
        if (UtilCheat.cantStandAtWater(p.getWorld().getBlockAt(p.getLocation())) && UtilCheat.isHoveringOverWater(p.getLocation()) && !UtilCheat.isFullyInWater(p.getLocation())) {
            Jesus.count.put(p, Count + 2);
        }
        else {
            Jesus.count.put(p, (Count > 0) ? -1 : 0);
        }
        if (Count >= 20) {
            Jesus.count.remove(p);
            this.getDaedalus().logCheat(this, p, null, Chance.HIGH, new String[0]);
        }
    }
}
