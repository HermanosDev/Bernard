package net.hermanos.ac.checks.combat;

import org.bukkit.event.player.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

import com.comphenix.protocol.wrappers.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.packets.events.*;
import net.hermanos.ac.utils.*;

import java.util.*;

public class KillAuraA extends Check
{
    public static Map<UUID, Long> LastMS;
    public static Map<UUID, List<Long>> Clicks;
    public static Map<UUID, Map.Entry<Integer, Long>> ClickTicks;
    
    public KillAuraA(final Bernard Daedalus) {
        super("KillAuraA", "Kill Aura (Click Pattern)", Daedalus);
        KillAuraA.LastMS = new HashMap<UUID, Long>();
        KillAuraA.Clicks = new HashMap<UUID, List<Long>>();
        KillAuraA.ClickTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.setEnabled(true);
        this.setBannable(true);
        this.setViolationResetTime(300000L);
        this.setMaxViolations(7);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final UUID uuid = p.getUniqueId();
        if (KillAuraA.LastMS.containsKey(uuid)) {
            KillAuraA.LastMS.remove(uuid);
        }
        if (KillAuraA.Clicks.containsKey(uuid)) {
            KillAuraA.Clicks.remove(uuid);
        }
        if (KillAuraA.ClickTicks.containsKey(uuid)) {
            KillAuraA.ClickTicks.remove(uuid);
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
        final Player damager = e.getAttacker();
        if (damager.hasPermission("daedalus.bypass")) {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (KillAuraA.ClickTicks.containsKey(damager.getUniqueId())) {
            Count = KillAuraA.ClickTicks.get(damager.getUniqueId()).getKey();
            Time = KillAuraA.ClickTicks.get(damager.getUniqueId()).getValue();
        }
        if (KillAuraA.LastMS.containsKey(damager.getUniqueId())) {
            final long MS = UtilTime.nowlong() - KillAuraA.LastMS.get(damager.getUniqueId());
            if (MS > 500L || MS < 5L) {
                KillAuraA.LastMS.put(damager.getUniqueId(), UtilTime.nowlong());
                return;
            }
            if (KillAuraA.Clicks.containsKey(damager.getUniqueId())) {
                final List<Long> Clicks = KillAuraA.Clicks.get(damager.getUniqueId());
                if (Clicks.size() == 10) {
                    KillAuraA.Clicks.remove(damager.getUniqueId());
                    Collections.sort(Clicks);
                    final long Range = Clicks.get(Clicks.size() - 1) - Clicks.get(0);
                    if (Range < 30L) {
                        ++Count;
                        Time = System.currentTimeMillis();
                        this.dumplog(damager, "New Range: " + Range);
                        this.dumplog(damager, "New Count: " + Count);
                    }
                }
                else {
                    Clicks.add(MS);
                    KillAuraA.Clicks.put(damager.getUniqueId(), Clicks);
                }
            }
            else {
                final List<Long> Clicks = new ArrayList<Long>();
                Clicks.add(MS);
                KillAuraA.Clicks.put(damager.getUniqueId(), Clicks);
            }
        }
        if (KillAuraA.ClickTicks.containsKey(damager.getUniqueId()) && UtilTime.elapsed(Time, 5000L)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if ((Count > 0 && this.getDaedalus().getLag().getPing(damager) < 100) || (Count > 2 && this.getDaedalus().getLag().getPing(damager) < 200)) {
            this.dumplog(damager, "Logged. Count: " + Count);
            Count = 0;
            this.getDaedalus().logCheat(this, damager, null, Chance.HIGH, new String[0]);
            KillAuraA.ClickTicks.remove(damager.getUniqueId());
        }
        else if (this.getDaedalus().getLag().getPing(damager) > 250) {
            this.dumplog(damager, "Would set off Killaura (Click Pattern) but latency is too high!");
        }
        KillAuraA.LastMS.put(damager.getUniqueId(), UtilTime.nowlong());
        KillAuraA.ClickTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}
