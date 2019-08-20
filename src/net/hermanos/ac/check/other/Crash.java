package net.hermanos.ac.check.other;

import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.packets.events.*;
import net.hermanos.ac.utils.*;

public class Crash extends Check
{
    public static Map<UUID, Map.Entry<Integer, Long>> faggotTicks;
    public static Map<UUID, Map.Entry<Integer, Long>> faggot2Ticks;
    public static Map<UUID, Map.Entry<Integer, Long>> faggot3Ticks;
    public List<UUID> faggots;
    
    public Crash(final Bernard Daedalus) {
        super("Crash", "Crash", Daedalus);
        Crash.faggotTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        Crash.faggot2Ticks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        Crash.faggot3Ticks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.faggots = new ArrayList<UUID>();
        this.setMaxViolations(0);
        this.setEnabled(true);
        this.setBannable(true);
    }
    
    @EventHandler
    public void Swing(final PacketSwingArmEvent e) {
        final Player faggot = e.getPlayer();
        if (this.faggots.contains(faggot.getUniqueId())) {
            e.getPacketEvent().setCancelled(true);
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (Crash.faggotTicks.containsKey(faggot.getUniqueId())) {
            Count = Crash.faggotTicks.get(faggot.getUniqueId()).getKey();
            Time = Crash.faggotTicks.get(faggot.getUniqueId()).getValue();
        }
        ++Count;
        if (Crash.faggotTicks.containsKey(faggot.getUniqueId()) && UtilTime.elapsed(Time, 100L)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count > 2000) {
            this.getDaedalus().logCheat(this, faggot, null, Chance.HIGH, new String[0]);
            this.faggots.add(faggot.getUniqueId());
        }
        Crash.faggotTicks.put(faggot.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
    
    @EventHandler
    public void Switch(final PacketHeldItemChangeEvent e) {
        final Player faggot = e.getPlayer();
        if (this.faggots.contains(faggot.getUniqueId())) {
            e.getPacketEvent().setCancelled(true);
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (Crash.faggot2Ticks.containsKey(faggot.getUniqueId())) {
            Count = Crash.faggot2Ticks.get(faggot.getUniqueId()).getKey();
            Time = Crash.faggot2Ticks.get(faggot.getUniqueId()).getValue();
        }
        ++Count;
        if (Crash.faggot2Ticks.containsKey(faggot.getUniqueId()) && UtilTime.elapsed(Time, 100L)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count > 2000) {
            this.getDaedalus().logCheat(this, faggot, null, Chance.HIGH, new String[0]);
            this.faggots.add(faggot.getUniqueId());
        }
        Crash.faggot2Ticks.put(faggot.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
    
    @EventHandler
    public void BlockPlace(final PacketBlockPlacementEvent e) {
        final Player faggot = e.getPlayer();
        if (this.faggots.contains(faggot.getUniqueId())) {
            e.getPacketEvent().setCancelled(true);
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (Crash.faggot3Ticks.containsKey(faggot.getUniqueId())) {
            Count = Crash.faggot3Ticks.get(faggot.getUniqueId()).getKey();
            Time = Crash.faggot3Ticks.get(faggot.getUniqueId()).getValue();
        }
        ++Count;
        if (Crash.faggot3Ticks.containsKey(faggot.getUniqueId()) && UtilTime.elapsed(Time, 100L)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count > 2000) {
            this.getDaedalus().logCheat(this, faggot, null, Chance.HIGH, new String[0]);
            this.faggots.add(faggot.getUniqueId());
        }
        Crash.faggot3Ticks.put(faggot.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}
