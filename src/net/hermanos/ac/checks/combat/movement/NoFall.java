package net.hermanos.ac.checks.combat.movement;

import org.bukkit.event.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import org.bukkit.*;

import java.util.*;
import org.bukkit.entity.*;

public class NoFall extends Check
{
    public static Map<UUID, Map.Entry<Long, Integer>> NoFallTicks;
    public static Map<UUID, Double> FallDistance;
    public static ArrayList<Player> cancel;
    
    public NoFall(final Bernard Daedalus) {
        super("NoFall", "NoFall", Daedalus);
        this.setEnabled(true);
        this.setBannable(true);
        NoFall.NoFallTicks = new HashMap<UUID, Map.Entry<Long, Integer>>();
        NoFall.FallDistance = new HashMap<UUID, Double>();
        NoFall.cancel = new ArrayList<Player>();
        this.setViolationResetTime(120000L);
        this.setMaxViolations(9);
    }
    
    @EventHandler
    public void onDeath(final PlayerDeathEvent e) {
        NoFall.cancel.add(e.getEntity());
    }
    
    @EventHandler
    public void onLogout(final PlayerQuitEvent e) {
        if (NoFall.FallDistance.containsKey(e.getPlayer().getUniqueId())) {
            NoFall.FallDistance.remove(e.getPlayer().getUniqueId());
        }
        if (NoFall.FallDistance.containsKey(e.getPlayer().getUniqueId())) {
            NoFall.FallDistance.containsKey(e.getPlayer().getUniqueId());
        }
    }
    
    @EventHandler
    public void onTeleport(final PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            NoFall.cancel.add(e.getPlayer());
        }
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void Move(final PlayerMoveEvent e) {
        final Player player = e.getPlayer();
        if (player.hasPermission("daedalus.bypass")) {
            return;
        }
        final Damageable dplayer = (Damageable)e.getPlayer();
        if (NoFall.cancel.contains(player)) {
            NoFall.cancel.remove(player);
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        if (player.getVehicle() != null) {
            return;
        }
        if (dplayer.getHealth() <= 0.0) {
            return;
        }
        if (UtilPlayer.isOnClimbable(player, 0)) {
            return;
        }
        if (UtilPlayer.isInWater(player)) {
            return;
        }
        double Falling = 0.0;
        if (!UtilPlayer.isOnGround(player) && e.getFrom().getY() > e.getTo().getY()) {
            if (NoFall.FallDistance.containsKey(player.getUniqueId())) {
                Falling = NoFall.FallDistance.get(player.getUniqueId());
            }
            Falling += e.getFrom().getY() - e.getTo().getY();
        }
        NoFall.FallDistance.put(player.getUniqueId(), Falling);
        if (Falling < 3.0) {
            return;
        }
        long Time = System.currentTimeMillis();
        int Count = 0;
        if (NoFall.NoFallTicks.containsKey(player.getUniqueId())) {
            Time = NoFall.NoFallTicks.get(player.getUniqueId()).getKey();
            Count = NoFall.NoFallTicks.get(player.getUniqueId()).getValue();
        }
        if (player.isOnGround() || player.getFallDistance() == 0.0f) {
            this.dumplog(player, "NoFall. Real Fall Distance: " + Falling);
            player.damage(5);
            Count += 2;
        }
        else {
            --Count;
        }
        if (NoFall.NoFallTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 10000L)) {
            Count = 0;
            Time = System.currentTimeMillis();
        }
        if (Count >= 4) {
            this.dumplog(player, "Logged. Count: " + Count);
            Count = 0;
            NoFall.FallDistance.put(player.getUniqueId(), 0.0);
            this.getDaedalus().logCheat(this, player, "Packet NoFall", Chance.HIGH, new String[0]);
        }
        NoFall.NoFallTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Long, Integer>(Time, Count));
    }
}
