package net.hermanos.ac.check.other;

import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.packets.events.*;
import net.hermanos.ac.utils.*;

import org.bukkit.event.*;

import java.util.*;
import org.bukkit.entity.*;

public class Sneak extends Check
{
    public static Map<UUID, Map.Entry<Integer, Long>> sneakTicks;
    
    public Sneak(final Bernard daedalus) {
        super("Sneak", "Sneak", daedalus);
        this.setEnabled(true);
        this.setBannable(true);
        this.setMaxViolations(5);
        Sneak.sneakTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        if (Sneak.sneakTicks.containsKey(e.getPlayer().getUniqueId())) {
            Sneak.sneakTicks.remove(e.getPlayer().getUniqueId());
        }
    }
    
    @EventHandler
    public void EntityAction(final PacketEntityActionEvent event) {
        if (event.getAction() != 1) {
            return;
        }
        final Player player = event.getPlayer();
        if (player.hasPermission("daedalus.bypass")) {
            return;
        }
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        int Count = 0;
        long Time = -1L;
        if (Sneak.sneakTicks.containsKey(player.getUniqueId())) {
            Count = Sneak.sneakTicks.get(player.getUniqueId()).getKey();
            Time = Sneak.sneakTicks.get(player.getUniqueId()).getValue();
        }
        ++Count;
        if (Sneak.sneakTicks.containsKey(player.getUniqueId())) {
            if (UtilTime.elapsed(Time, 100L)) {
                Count = 0;
                Time = System.currentTimeMillis();
            }
            else {
                Time = System.currentTimeMillis();
            }
        }
        if (Count > 50) {
            Count = 0;
            this.getDaedalus().logCheat(this, player, null, Chance.HIGH, new String[0]);
        }
        Sneak.sneakTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}
