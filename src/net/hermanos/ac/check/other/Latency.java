package net.hermanos.ac.check.other;

import org.bukkit.entity.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.packets.events.*;
import net.hermanos.ac.utils.*;

import org.bukkit.*;

import java.util.*;
import org.bukkit.event.*;

public class Latency implements Listener
{
    public static Map<UUID, Map.Entry<Integer, Long>> packetTicks;
    public static Map<UUID, Long> lastPacket;
    public List<UUID> blacklist;
    private static Map<UUID, Integer> packets;
    private Bernard Daedalus;
    
    public Latency(final Bernard Daedalus) {
        this.Daedalus = Daedalus;
        Latency.packetTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        Latency.lastPacket = new HashMap<UUID, Long>();
        this.blacklist = new ArrayList<UUID>();
        Latency.packets = new HashMap<UUID, Integer>();
    }
    
    public static Integer getLag(final Player player) {
        if (Latency.packets.containsKey(player.getUniqueId())) {
            return Latency.packets.get(player.getUniqueId());
        }
        return 0;
    }
    
    @EventHandler
    public void PlayerJoin(final PlayerJoinEvent event) {
        this.blacklist.add(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onLogout(final PlayerQuitEvent e) {
        if (Latency.packetTicks.containsKey(e.getPlayer().getUniqueId())) {
            Latency.packetTicks.remove(e.getPlayer().getUniqueId());
        }
        if (Latency.lastPacket.containsKey(e.getPlayer().getUniqueId())) {
            Latency.lastPacket.remove(e.getPlayer().getUniqueId());
        }
        if (this.blacklist.contains(e.getPlayer().getUniqueId())) {
            this.blacklist.remove(e.getPlayer().getUniqueId());
        }
        if (Latency.packets.containsKey(e.getPlayer().getUniqueId())) {
            Latency.packets.remove(e.getPlayer().getUniqueId());
        }
    }
    
    @EventHandler
    public void PlayerChangedWorld(final PlayerChangedWorldEvent event) {
        this.blacklist.add(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void PlayerRespawn(final PlayerRespawnEvent event) {
        this.blacklist.add(event.getPlayer().getUniqueId());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void PacketPlayer(final PacketPlayerEvent event) {
        final Player player = event.getPlayer();
        if (!this.Daedalus.isEnabled()) {
            return;
        }
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        if (this.Daedalus.lag.getTPS() > 21.0 || this.Daedalus.lag.getTPS() < this.Daedalus.getTPSCancel()) {
            return;
        }
        if (event.getType() != PacketPlayerType.FLYING) {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (Latency.packetTicks.containsKey(player.getUniqueId())) {
            Count = Latency.packetTicks.get(player.getUniqueId()).getKey();
            Time = Latency.packetTicks.get(player.getUniqueId()).getValue();
        }
        if (Latency.lastPacket.containsKey(player.getUniqueId())) {
            final long MS = System.currentTimeMillis() - Latency.lastPacket.get(player.getUniqueId());
            if (MS >= 100L) {
                this.blacklist.add(player.getUniqueId());
            }
            else if (MS > 1L && this.blacklist.contains(player.getUniqueId())) {
                this.blacklist.remove(player.getUniqueId());
            }
        }
        if (!this.blacklist.contains(player.getUniqueId())) {
            ++Count;
            if (Latency.packetTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 1000L)) {
                Latency.packets.put(player.getUniqueId(), Count);
                Count = 0;
                Time = UtilTime.nowlong();
            }
        }
        Latency.packetTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
        Latency.lastPacket.put(player.getUniqueId(), System.currentTimeMillis());
    }
}
