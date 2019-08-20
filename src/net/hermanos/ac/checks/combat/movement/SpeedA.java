package net.hermanos.ac.checks.combat.movement;

import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.*;
import org.bukkit.event.player.*;
import org.bukkit.potion.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import java.util.*;

public class SpeedA extends Check
{
    public static Map<UUID, Map.Entry<Integer, Long>> speedTicks;
    public static Map<UUID, Map.Entry<Integer, Long>> tooFastTicks;
    public static Map<UUID, Long> lastHit;
    public static Map<UUID, Double> velocity;
    
    static {
        SpeedA.speedTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        SpeedA.tooFastTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        SpeedA.lastHit = new HashMap<UUID, Long>();
        SpeedA.velocity = new HashMap<UUID, Double>();
    }
    
    public SpeedA(final Bernard Daedalus) {
        super("SpeedA", "Speed (Type A)", Daedalus);
        this.setEnabled(true);
        this.setBannable(true);
        this.setMaxViolations(3);
    }
    
    @EventHandler
    public void onHit(final EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player player = (Player)e.getEntity();
            SpeedA.lastHit.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }
    
    public boolean isOnIce(final Player player) {
        final Location a = player.getLocation();
        a.setY(a.getY() - 1.0);
        if (a.getBlock().getType().equals((Object)Material.ICE)) {
            return true;
        }
        a.setY(a.getY() - 1.0);
        return a.getBlock().getType().equals((Object)Material.ICE);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        if (SpeedA.speedTicks.containsKey(e.getPlayer().getUniqueId())) {
            SpeedA.speedTicks.remove(e.getPlayer().getUniqueId());
        }
        if (SpeedA.tooFastTicks.containsKey(e.getPlayer().getUniqueId())) {
            SpeedA.tooFastTicks.remove(e.getPlayer().getUniqueId());
        }
        if (SpeedA.lastHit.containsKey(e.getPlayer().getUniqueId())) {
            SpeedA.lastHit.remove(e.getPlayer().getUniqueId());
        }
        if (SpeedA.velocity.containsKey(e.getPlayer().getUniqueId())) {
            SpeedA.velocity.remove(e.getPlayer().getUniqueId());
        }
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void CheckSpeed(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (event.getFrom().getX() == event.getTo().getX() && event.getFrom().getY() == event.getTo().getY() && event.getFrom().getZ() == event.getFrom().getZ()) {
            return;
        }
        if (!this.getDaedalus().isEnabled()) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        if (player.getVehicle() != null) {
            return;
        }
        if (player.getVelocity().length() + 0.1 < SpeedA.velocity.getOrDefault(player.getUniqueId(), -1.0)) {
            return;
        }
        if (this.getDaedalus().LastVelocity.containsKey(player.getUniqueId()) && !player.hasPotionEffect(PotionEffectType.POISON) && !player.hasPotionEffect(PotionEffectType.WITHER) && player.getFireTicks() == 0) {
            return;
        }
        final long lastHitDiff = SpeedA.lastHit.containsKey(player.getUniqueId()) ? (SpeedA.lastHit.get(player.getUniqueId()) - System.currentTimeMillis()) : 2001L;
        int Count = 0;
        long Time = UtilTime.nowlong();
        if (SpeedA.speedTicks.containsKey(player.getUniqueId())) {
            Count = SpeedA.speedTicks.get(player.getUniqueId()).getKey();
            Time = SpeedA.speedTicks.get(player.getUniqueId()).getValue();
        }
        int TooFastCount = 0;
        double percent = 0.0;
        if (SpeedA.tooFastTicks.containsKey(player.getUniqueId())) {
            final double OffsetXZ = UtilMath.offset(UtilMath.getHorizontalVector(event.getFrom().toVector()), UtilMath.getHorizontalVector(event.getTo().toVector()));
            double LimitXZ = 0.0;
            if (UtilPlayer.isOnGround(player) && player.getVehicle() == null) {
                LimitXZ = 0.34;
            }
            else {
                LimitXZ = 0.39;
            }
            if (lastHitDiff < 800L) {
                ++LimitXZ;
            }
            else if (lastHitDiff < 1600L) {
                LimitXZ += 0.4;
            }
            else if (lastHitDiff < 2000L) {
                LimitXZ += 0.1;
            }
            if (UtilCheat.slabsNear(player.getLocation())) {
                LimitXZ += 0.05;
            }
            final Location b = UtilPlayer.getEyeLocation(player);
            b.add(0.0, 1.0, 0.0);
            if (b.getBlock().getType() != Material.AIR && !UtilCheat.canStandWithin(b.getBlock())) {
                LimitXZ = 0.69;
            }
            final Location below = event.getPlayer().getLocation().clone().add(0.0, -1.0, 0.0);
            if (UtilCheat.isStair(below.getBlock())) {
                LimitXZ += 0.6;
            }
            if (this.isOnIce(player)) {
                if (b.getBlock().getType() != Material.AIR && !UtilCheat.canStandWithin(b.getBlock())) {
                    LimitXZ = 1.0;
                }
                else {
                    LimitXZ = 0.75;
                }
            }
            final float speed = player.getWalkSpeed();
            LimitXZ += ((speed > 0.2f) ? (speed * 10.0f * 0.33f) : 0.0f);
            for (final PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().equals((Object)PotionEffectType.SPEED)) {
                    if (player.isOnGround()) {
                        LimitXZ += 0.061 * (effect.getAmplifier() + 1);
                    }
                    else {
                        LimitXZ += 0.031 * (effect.getAmplifier() + 1);
                    }
                }
            }
            if (OffsetXZ > LimitXZ && !UtilTime.elapsed(SpeedA.tooFastTicks.get(player.getUniqueId()).getValue(), 150L)) {
                percent = (OffsetXZ - LimitXZ) * 100.0;
                TooFastCount = SpeedA.tooFastTicks.get(player.getUniqueId()).getKey() + 3;
            }
            else {
                TooFastCount = ((TooFastCount > -150) ? TooFastCount-- : -150);
            }
        }
        if (TooFastCount >= 11) {
            TooFastCount = 0;
            ++Count;
            this.dumplog(player, "New Count: " + Count);
        }
        if (SpeedA.speedTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 30000L)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        Chance prob = Chance.LIKELY;
        if (Count >= 3) {
            prob = Chance.HIGH;
            this.dumplog(player, "Logged for Speed. Count: " + Count);
            Count = 0;
            this.getDaedalus().logCheat(this, player, String.valueOf(Math.round(percent)) + "% faster than normal", prob, new String[0]);
        }
        if (!player.isOnGround()) {
            SpeedA.velocity.put(player.getUniqueId(), player.getVelocity().length());
        }
        else {
            SpeedA.velocity.put(player.getUniqueId(), -1.0);
        }
        SpeedA.tooFastTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(TooFastCount, System.currentTimeMillis()));
        SpeedA.speedTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}
