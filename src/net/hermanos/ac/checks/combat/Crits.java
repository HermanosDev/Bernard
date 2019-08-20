package net.hermanos.ac.checks.combat;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;

import java.util.*;
import org.bukkit.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

public class Crits extends Check
{
    public static Map<UUID, Map.Entry<Integer, Long>> CritTicks;
    public static Map<UUID, Double> FallDistance;
    
    static {
        Crits.CritTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        Crits.FallDistance = new HashMap<UUID, Double>();
    }
    
    public Crits(final Bernard Daedalus) {
        super("Criticals", "Criticals", Daedalus);
        this.setEnabled(true);
        this.setBannable(true);
        this.setMaxViolations(4);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final UUID uuid = p.getUniqueId();
        if (Crits.CritTicks.containsKey(uuid)) {
            Crits.CritTicks.remove(uuid);
        }
        if (Crits.FallDistance.containsKey(uuid)) {
            Crits.CritTicks.remove(uuid);
        }
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onDamage(final EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (!e.getCause().equals((Object)EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            return;
        }
        final Player player = (Player)e.getDamager();
        if (player.getAllowFlight()) {
            return;
        }
        if (this.getDaedalus().LastVelocity.containsKey(player.getUniqueId())) {
            return;
        }
        if (UtilCheat.slabsNear(player.getLocation())) {
            return;
        }
        if (player.hasPermission("daedalus.bypass")) {
            return;
        }
        final Location pL = player.getLocation().clone();
        pL.add(0.0, player.getEyeHeight() + 1.0, 0.0);
        if (UtilCheat.blocksNear(pL)) {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (Crits.CritTicks.containsKey(player.getUniqueId())) {
            Count = Crits.CritTicks.get(player.getUniqueId()).getKey();
            Time = Crits.CritTicks.get(player.getUniqueId()).getValue();
        }
        if (!Crits.FallDistance.containsKey(player.getUniqueId())) {
            return;
        }
        final double realFallDistance = Crits.FallDistance.get(player.getUniqueId());
        Count = ((player.getFallDistance() > 0.0 && !player.isOnGround() && realFallDistance == 0.0) ? (++Count) : 0);
        if (Crits.CritTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 10000L)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count >= 2) {
            Count = 0;
            this.getDaedalus().logCheat(this, player, null, Chance.HIGH, new String[0]);
        }
        Crits.CritTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void Move(final PlayerMoveEvent e) {
        final Player Player2 = e.getPlayer();
        double Falling = 0.0;
        if (!Player2.isOnGround() && e.getFrom().getY() > e.getTo().getY()) {
            if (Crits.FallDistance.containsKey(Player2.getUniqueId())) {
                Falling = Crits.FallDistance.get(Player2.getUniqueId());
            }
            Falling += e.getFrom().getY() - e.getTo().getY();
        }
        Crits.FallDistance.put(Player2.getUniqueId(), Falling);
    }
}
