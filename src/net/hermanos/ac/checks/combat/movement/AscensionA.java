package net.hermanos.ac.checks.combat.movement;

import org.bukkit.potion.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import org.bukkit.entity.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class AscensionA extends Check
{
    public static Map<UUID, Map.Entry<Long, Double>> AscensionTicks;
    public static Map<UUID, Double> velocity;
    
    public AscensionA(final Bernard Daedalus) {
        super("AscensionA", "Ascension (Type A)", Daedalus);
        this.setBannable(true);
        this.setEnabled(true);
        this.setMaxViolations(4);
        AscensionA.AscensionTicks = new HashMap<UUID, Map.Entry<Long, Double>>();
        AscensionA.velocity = new HashMap<UUID, Double>();
    }
    
    @EventHandler
    public void CheckAscension(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (event.getFrom().getY() >= event.getTo().getY()) {
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
        if (!UtilTime.elapsed(this.getDaedalus().LastVelocity.getOrDefault(player.getUniqueId(), 0L), 4200L)) {
            return;
        }
        long Time = System.currentTimeMillis();
        double TotalBlocks = 0.0;
        if (AscensionA.AscensionTicks.containsKey(player.getUniqueId())) {
            Time = AscensionA.AscensionTicks.get(player.getUniqueId()).getKey();
            TotalBlocks = AscensionA.AscensionTicks.get(player.getUniqueId()).getValue();
        }
        final long MS = System.currentTimeMillis() - Time;
        final double OffsetY = UtilMath.offset(UtilMath.getVerticalVector(event.getFrom().toVector()), UtilMath.getVerticalVector(event.getTo().toVector()));
        if (OffsetY > 0.0) {
            TotalBlocks += OffsetY;
        }
        final Location a = player.getLocation().subtract(0.0, 1.0, 0.0);
        if (UtilCheat.blocksNear(a)) {
            TotalBlocks = 0.0;
        }
        double Limit = 1.05;
        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            for (final PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().equals((Object)PotionEffectType.JUMP)) {
                    final int level = effect.getAmplifier() + 1;
                    Limit += Math.pow(level + 4.2, 2.0) / 16.0 + 0.3;
                    break;
                }
            }
        }
        if (TotalBlocks > Limit) {
            if (MS > 250L) {
                if (AscensionA.velocity.containsKey(player.getUniqueId())) {
                    this.getDaedalus().logCheat(this, player, "Flew up " + UtilMath.trim(1, TotalBlocks) + " blocks", Chance.HIGH, new String[0]);
                }
                Time = System.currentTimeMillis();
            }
        }
        else {
            Time = System.currentTimeMillis();
        }
        AscensionA.AscensionTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Long, Double>(Time, TotalBlocks));
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        if (AscensionA.AscensionTicks.containsKey(e.getPlayer().getUniqueId())) {
            AscensionA.AscensionTicks.remove(e.getPlayer().getUniqueId());
        }
        if (AscensionA.velocity.containsKey(e.getPlayer().getUniqueId())) {
            AscensionA.velocity.remove(e.getPlayer().getUniqueId());
        }
    }
}
