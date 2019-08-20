package net.hermanos.ac.check.other;

import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class TimerB extends Check
{
    public static Map<UUID, Map.Entry<Integer, Long>> timerTicks;
    
    public TimerB(final Bernard Daedalus) {
        super("TimerB", "Timer (Type B)", Daedalus);
        this.setViolationsToNotify(1);
        this.setMaxViolations(9);
        this.setEnabled(true);
        this.setBannable(false);
        TimerB.timerTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onMove(final PlayerMoveEvent e) {
        if (!this.getDaedalus().isEnabled()) {
            return;
        }
        final Player player = e.getPlayer();
        if (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ() && e.getFrom().getY() == e.getTo().getY()) {
            return;
        }
        if (this.getDaedalus().isSotwMode() || player.hasPermission("daedalus.bypass") || Latency.getLag(player) > 500) {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (TimerB.timerTicks.containsKey(player.getUniqueId())) {
            Count = TimerB.timerTicks.get(player.getUniqueId()).getKey();
            Time = TimerB.timerTicks.get(player.getUniqueId()).getValue();
        }
        ++Count;
        if (TimerB.timerTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 1000L)) {
            if (Count > 35) {
                this.getDaedalus().logCheat(this, player, null, Chance.LIKELY, "Experimental");
            }
            Count = 0;
            Time = UtilTime.nowlong();
        }
        TimerB.timerTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}
