package net.hermanos.ac.checks.combat;

import org.bukkit.event.player.*;
import org.bukkit.event.*;

import com.comphenix.protocol.wrappers.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.packets.events.*;
import net.hermanos.ac.utils.*;

import org.bukkit.entity.*;

import java.util.*;

public class KillAuraB extends Check
{
    public static Map<UUID, Map.Entry<Integer, Long>> AuraTicks;
    
    public KillAuraB(final Bernard Daedalus) {
        super("KillAuraB", "Kill Aura (Hit Miss Ratio)", Daedalus);
        KillAuraB.AuraTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.setEnabled(false);
        this.setBannable(true);
        this.setMaxViolations(150);
        this.setViolationsToNotify(140);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final UUID uuid = p.getUniqueId();
        if (KillAuraB.AuraTicks.containsKey(uuid)) {
            KillAuraB.AuraTicks.remove(uuid);
        }
    }
    
    @EventHandler
    public void UseEntity(final PacketUseEntityEvent e) {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK) {
            return;
        }
        if (!(e.getAttacked() instanceof Player)) {
            return;
        }
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        final Player damager = e.getAttacker();
        if (damager.hasPermission("daedalus.bypass")) {
            return;
        }
        final Player player = (Player)e.getAttacked();
        if (damager.getAllowFlight()) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (KillAuraB.AuraTicks.containsKey(damager.getUniqueId())) {
            Count = KillAuraB.AuraTicks.get(damager.getUniqueId()).getKey();
            Time = KillAuraB.AuraTicks.get(damager.getUniqueId()).getValue();
        }
        final double OffsetXZ = UtilCheat.getAimbotoffset(damager.getLocation(), damager.getEyeHeight(), (LivingEntity)player);
        double LimitOffset = 200.0;
        if (damager.getVelocity().length() > 0.08 || this.getDaedalus().LastVelocity.containsKey(damager.getUniqueId())) {
            LimitOffset += 200.0;
        }
        final int Ping = this.getDaedalus().getLag().getPing(damager);
        if (Ping >= 100 && Ping < 200) {
            LimitOffset += 50.0;
        }
        else if (Ping >= 200 && Ping < 250) {
            LimitOffset += 75.0;
        }
        else if (Ping >= 250 && Ping < 300) {
            LimitOffset += 150.0;
        }
        else if (Ping >= 300 && Ping < 350) {
            LimitOffset += 300.0;
        }
        else if (Ping >= 350 && Ping < 400) {
            LimitOffset += 400.0;
        }
        else if (Ping > 400) {
            return;
        }
        if (OffsetXZ > LimitOffset * 4.0) {
            Count += 12;
        }
        else if (OffsetXZ > LimitOffset * 3.0) {
            Count += 10;
        }
        else if (OffsetXZ > LimitOffset * 2.0) {
            Count += 8;
        }
        else if (OffsetXZ > LimitOffset) {
            Count += 4;
        }
        if (KillAuraB.AuraTicks.containsKey(damager.getUniqueId()) && UtilTime.elapsed(Time, 60000L)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count >= 16) {
            this.dumplog(damager, "Offset: " + OffsetXZ + ", Ping: " + Ping + ", Max Offset: " + LimitOffset);
            this.dumplog(damager, "Logged. Count: " + Count + ", Ping: " + Ping);
            Count = 0;
            this.getDaedalus().logCheat(this, damager, null, Chance.LIKELY, "Experimental");
        }
        KillAuraB.AuraTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}
