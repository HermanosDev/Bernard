package net.hermanos.ac.checks.combat;

import java.util.*;

import org.bukkit.event.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.check.other.*;
import net.hermanos.ac.utils.*;

import org.bukkit.event.entity.*;
import org.bukkit.entity.*;

public class HitBoxes extends Check
{
    public static Map<UUID, Integer> count;
    public static Map<UUID, Player> lastHit;
    public static Map<UUID, Double> yawDif;
    
    static {
        HitBoxes.count = new HashMap<UUID, Integer>();
        HitBoxes.lastHit = new HashMap<UUID, Player>();
        HitBoxes.yawDif = new HashMap<UUID, Double>();
    }
    
    public HitBoxes(final Bernard Daedalus) {
        super("HitBoxes", "Hitboxes", Daedalus);
        this.setEnabled(true);
        this.setBannable(false);
        this.setMaxViolations(5);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(final PlayerQuitEvent e) {
        if (HitBoxes.count.containsKey(e.getPlayer().getUniqueId())) {
            HitBoxes.count.remove(e.getPlayer().getUniqueId());
        }
        if (HitBoxes.yawDif.containsKey(e.getPlayer().getUniqueId())) {
            HitBoxes.yawDif.remove(e.getPlayer().getUniqueId());
        }
        if (HitBoxes.lastHit.containsKey(e.getPlayer().getUniqueId())) {
            HitBoxes.lastHit.remove(e.getPlayer().getUniqueId());
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onMove(final PlayerMoveEvent e) {
        final double yawDif = Math.abs(e.getFrom().getYaw() - e.getTo().getYaw());
        HitBoxes.yawDif.put(e.getPlayer().getUniqueId(), yawDif);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onUse(final EntityDamageByEntityEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
            return;
        }
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        final Player player = (Player)e.getDamager();
        final Player attacked = (Player)e.getEntity();
        if (player.hasPermission("daedalus.bypass")) {
            return;
        }
        int Count = 0;
        double yawDif = 0.0;
        Player lastPlayer = attacked;
        if (HitBoxes.lastHit.containsKey(player.getUniqueId())) {
            lastPlayer = HitBoxes.lastHit.get(player.getUniqueId());
        }
        if (HitBoxes.count.containsKey(player.getUniqueId())) {
            Count = HitBoxes.count.get(player.getUniqueId());
        }
        if (HitBoxes.yawDif.containsKey(player.getUniqueId())) {
            yawDif = HitBoxes.yawDif.get(player.getUniqueId());
        }
        if (lastPlayer != attacked) {
            HitBoxes.lastHit.put(player.getUniqueId(), attacked);
            return;
        }
        final double offset = UtilCheat.getOffsetOffCursor(player, (LivingEntity)attacked);
        double Limit = 108.0;
        final double distance = UtilCheat.getHorizontalDistance(player.getLocation(), attacked.getLocation());
        Limit += distance * 57.0;
        Limit += (attacked.getVelocity().length() + player.getVelocity().length()) * 64.0;
        Limit += yawDif * 6.0;
        if (Latency.getLag(player) > 80 || Latency.getLag(attacked) > 80) {
            return;
        }
        if (offset > Limit) {
            ++Count;
        }
        else {
            Count = ((Count > 0) ? (Count - 1) : Count);
        }
        if (Count > 8) {
            this.getDaedalus().logCheat(this, player, String.valueOf(offset) + " > " + Limit, Chance.LIKELY, "Experimental");
            Count = 0;
        }
        HitBoxes.count.put(player.getUniqueId(), Count);
        HitBoxes.lastHit.put(player.getUniqueId(), attacked);
    }
}
