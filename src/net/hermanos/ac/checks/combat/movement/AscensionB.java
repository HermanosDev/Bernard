package net.hermanos.ac.checks.combat.movement;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.potion.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.check.other.*;
import net.hermanos.ac.utils.*;

import org.bukkit.*;
import java.util.*;

public class AscensionB extends Check
{
    public static Map<UUID, Map.Entry<Integer, Long>> flyTicks;
    public static Map<UUID, Double> velocity;
    
    static {
        AscensionB.flyTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        AscensionB.velocity = new HashMap<UUID, Double>();
    }
    
    public AscensionB(final Bernard Daedalus) {
        super("AscensionB", "Ascension (Type B)", Daedalus);
        this.setBannable(true);
        this.setEnabled(true);
        this.setMaxViolations(5);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final UUID uuid = p.getUniqueId();
        if (AscensionB.flyTicks.containsKey(uuid)) {
            AscensionB.flyTicks.remove(uuid);
        }
    }
    
    @EventHandler
    public void CheckAscension(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        if (!this.getDaedalus().isEnabled()) {
            return;
        }
        if (this.getDaedalus().getLag().getTPS() < this.getDaedalus().getTPSCancel()) {
            return;
        }
        if (e.getFrom().getY() >= e.getTo().getY()) {
            return;
        }
        if (p.getAllowFlight()) {
            return;
        }
        if (!UtilTime.elapsed(this.getDaedalus().LastVelocity.getOrDefault(p.getUniqueId(), 0L), 4200L)) {
            return;
        }
        if (p.hasPermission("daedalus.bypass")) {
            return;
        }
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        if (Latency.getLag(p) > 75) {
            return;
        }
        if (this.getDaedalus().getLastVelocity().containsKey(p.getUniqueId())) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        if (p.getVehicle() != null) {
            return;
        }
        final Location to = e.getTo();
        final Location from = e.getFrom();
        int Count = 0;
        long Time = UtilTime.nowlong();
        if (AscensionB.flyTicks.containsKey(p.getUniqueId())) {
            Count = AscensionB.flyTicks.get(p.getUniqueId()).getKey();
            Time = AscensionB.flyTicks.get(p.getUniqueId()).getValue();
        }
        if (AscensionB.flyTicks.containsKey(p.getUniqueId())) {
            final double Offset = to.getY() - from.getY();
            double Limit = 0.5;
            double TotalBlocks = Offset;
            if (UtilCheat.blocksNear(p)) {
                TotalBlocks = 0.0;
            }
            final Location a = p.getLocation().subtract(0.0, 1.0, 0.0);
            if (UtilCheat.blocksNear(a)) {
                TotalBlocks = 0.0;
            }
            if (p.hasPotionEffect(PotionEffectType.JUMP)) {
                for (final PotionEffect effect : p.getActivePotionEffects()) {
                    if (effect.getType().equals((Object)PotionEffectType.JUMP)) {
                        final int level = effect.getAmplifier() + 1;
                        Limit += Math.pow(level + 4.1, 2.0) / 16.0;
                        break;
                    }
                }
            }
            if (TotalBlocks >= Limit) {
                Count += 2;
            }
            else if (Count > 0) {
                --Count;
            }
        }
        if (AscensionB.flyTicks.containsKey(p.getUniqueId()) && UtilTime.elapsed(Time, 30000L)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count >= 4) {
            Count = 0;
            this.dumplog(p, "Logged for Ascension Type B");
            this.getDaedalus().logCheat(this, p, null, Chance.HIGH, new String[0]);
        }
        AscensionB.flyTicks.put(p.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}
