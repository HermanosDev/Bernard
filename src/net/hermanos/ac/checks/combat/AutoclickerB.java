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

public class AutoclickerB extends Check
{
    public static Map<UUID, Long> LastMS;
    public static Map<UUID, List<Long>> Clicks;
    public static Map<UUID, Map.Entry<Integer, Long>> ClickTicks;
    
    public AutoclickerB(final Bernard Daedalus) {
        super("AutoclickerB", "Autoclicker (Type B)", Daedalus);
        AutoclickerB.LastMS = new HashMap<UUID, Long>();
        AutoclickerB.Clicks = new HashMap<UUID, List<Long>>();
        AutoclickerB.ClickTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.setEnabled(true);
        this.setBannable(false);
        this.setMaxViolations(5);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final UUID uuid = p.getUniqueId();
        if (AutoclickerB.LastMS.containsKey(uuid)) {
            AutoclickerB.LastMS.remove(uuid);
        }
        if (AutoclickerB.Clicks.containsKey(uuid)) {
            AutoclickerB.Clicks.remove(uuid);
        }
        if (AutoclickerB.ClickTicks.containsKey(uuid)) {
            AutoclickerB.Clicks.remove(uuid);
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
        if (AutoclickerB.ClickTicks.containsKey(damager.getUniqueId())) {
            Count = AutoclickerB.ClickTicks.get(damager.getUniqueId()).getKey();
            Time = AutoclickerB.ClickTicks.get(damager.getUniqueId()).getValue();
        }
        if (AutoclickerB.LastMS.containsKey(damager.getUniqueId())) {
            final long MS = UtilTime.nowlong() - AutoclickerB.LastMS.get(damager.getUniqueId());
            if (MS > 500L || MS < 5L) {
                AutoclickerB.LastMS.put(damager.getUniqueId(), UtilTime.nowlong());
                return;
            }
            if (AutoclickerB.Clicks.containsKey(damager.getUniqueId())) {
                final List<Long> Clicks = AutoclickerB.Clicks.get(damager.getUniqueId());
                if (Clicks.size() == 3) {
                    AutoclickerB.Clicks.remove(damager.getUniqueId());
                    Collections.sort(Clicks);
                    final long Range = Clicks.get(Clicks.size() - 1) - Clicks.get(0);
                    if (Range >= 0L && Range <= 2L) {
                        ++Count;
                        Time = System.currentTimeMillis();
                        this.dumplog(damager, "New Count: " + Count + "; Range: " + Range + "; Ping: " + this.getDaedalus().getLag().getPing(damager) + "; TPS: " + this.getDaedalus().getLag().getTPS());
                    }
                }
                else {
                    Clicks.add(MS);
                    AutoclickerB.Clicks.put(damager.getUniqueId(), Clicks);
                }
            }
            else {
                final List<Long> Clicks = new ArrayList<Long>();
                Clicks.add(MS);
                AutoclickerB.Clicks.put(damager.getUniqueId(), Clicks);
            }
        }
        if (AutoclickerB.ClickTicks.containsKey(damager.getUniqueId()) && UtilTime.elapsed(Time, 5000L)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if ((Count > 4 && this.getDaedalus().getLag().getPing(damager) < 100) || (Count > 6 && this.getDaedalus().getLag().getPing(damager) < 200)) {
            this.dumplog(damager, "Logged. Count: " + Count);
            Count = 0;
            this.getDaedalus().logCheat(this, damager, "Continuous/Repeating Patterns", Chance.HIGH, new String[0]);
            AutoclickerB.ClickTicks.remove(damager.getUniqueId());
        }
        else if (this.getDaedalus().getLag().getPing(damager) > 250) {
            this.dumplog(damager, "Would set off Autoclicker (Constant) but latency is too high!");
        }
        AutoclickerB.LastMS.put(damager.getUniqueId(), UtilTime.nowlong());
        AutoclickerB.ClickTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}
