package net.hermanos.ac.checks.combat;

import org.bukkit.*;
import org.bukkit.event.player.*;
import org.bukkit.event.*;

import com.comphenix.protocol.wrappers.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.packets.events.*;
import net.hermanos.ac.utils.*;

import org.bukkit.entity.*;

import java.util.*;

public class KillAuraC extends Check
{
    public static Map<UUID, Map.Entry<Integer, Long>> AimbotTicks;
    public static Map<UUID, Double> Differences;
    public static Map<UUID, Location> LastLocation;
    
    public KillAuraC(final Bernard Daedalus) {
        super("KillAuraC", "Kill Aura (Aimbot)", Daedalus);
        KillAuraC.AimbotTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        KillAuraC.Differences = new HashMap<UUID, Double>();
        KillAuraC.LastLocation = new HashMap<UUID, Location>();
        this.setEnabled(true);
        this.setBannable(true);
        this.setMaxViolations(11);
        this.setViolationResetTime(120000L);
        this.setViolationsToNotify(2);
    }
    
    @EventHandler
    public void onLogout(final PlayerQuitEvent e) {
        if (KillAuraC.AimbotTicks.containsKey(e.getPlayer().getUniqueId())) {
            KillAuraC.AimbotTicks.remove(e.getPlayer().getUniqueId());
        }
        if (KillAuraC.Differences.containsKey(e.getPlayer().getUniqueId())) {
            KillAuraC.Differences.remove(e.getPlayer().getUniqueId());
        }
        if (KillAuraC.LastLocation.containsKey(e.getPlayer().getUniqueId())) {
            KillAuraC.LastLocation.remove(e.getPlayer().getUniqueId());
        }
    }
    
    @EventHandler
    public void UseEntity(final PacketUseEntityEvent e) {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK) {
            return;
        }
        final Player damager = e.getAttacker();
        if (damager.hasPermission("daedalus.bypass")) {
            return;
        }
        if (damager.getAllowFlight()) {
            return;
        }
        if (!(e.getAttacked() instanceof Player)) {
            return;
        }
        Location from = null;
        final Location to = damager.getLocation();
        if (KillAuraC.LastLocation.containsKey(damager.getUniqueId())) {
            from = KillAuraC.LastLocation.get(damager.getUniqueId());
        }
        KillAuraC.LastLocation.put(damager.getUniqueId(), damager.getLocation());
        double Count = 0.0;
        long Time = System.currentTimeMillis();
        double LastDifference = -111111.0;
        if (KillAuraC.Differences.containsKey(damager.getUniqueId())) {
            LastDifference = KillAuraC.Differences.get(damager.getUniqueId());
        }
        if (KillAuraC.AimbotTicks.containsKey(damager.getUniqueId())) {
            Count = KillAuraC.AimbotTicks.get(damager.getUniqueId()).getKey();
            Time = KillAuraC.AimbotTicks.get(damager.getUniqueId()).getValue();
        }
        if (from == null || (to.getX() == from.getX() && to.getZ() == from.getZ())) {
            return;
        }
        final double Difference = Math.abs(to.getYaw() - from.getYaw());
        if (Difference == 0.0) {
            return;
        }
        if (Difference > 2.4) {
            this.dumplog(damager, "Difference: " + Difference);
            final double diff = Math.abs(LastDifference - Difference);
            if (e.getAttacked().getVelocity().length() < 0.1) {
                if (diff < 1.4) {
                    ++Count;
                }
                else {
                    Count = 0.0;
                }
            }
            else if (diff < 1.8) {
                ++Count;
            }
            else {
                Count = 0.0;
            }
        }
        KillAuraC.Differences.put(damager.getUniqueId(), Difference);
        if (KillAuraC.AimbotTicks.containsKey(damager.getUniqueId()) && UtilTime.elapsed(Time, 5000L)) {
            this.dumplog(damager, "Count Reset");
            Count = 0.0;
            Time = UtilTime.nowlong();
        }
        if (Count >= 4.0) {
            Count = 0.0;
            this.dumplog(damager, "Logged. Last Difference: " + Math.abs(to.getYaw() - from.getYaw()) + ", Count: " + Count);
            this.getDaedalus().logCheat(this, damager, null, Chance.LIKELY, new String[0]);
        }
        KillAuraC.AimbotTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>((int)Math.round(Count), Time));
    }
}
