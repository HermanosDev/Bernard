package net.hermanos.ac.check.other;

import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.packets.events.*;
import net.hermanos.ac.utils.*;

import org.bukkit.*;

import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class MorePackets extends Check
{
    public static Map<UUID, Map.Entry<Integer, Long>> packetTicks;
    public static Map<UUID, Long> lastPacket;
    public List<UUID> blacklist;
    
    public MorePackets(final Bernard Daedalus) {
        super("MorePackets", "MorePackets", Daedalus);
        this.setEnabled(true);
        this.setBannable(false);
        this.setMaxViolations(10);
        this.blacklist = new ArrayList<UUID>();
        MorePackets.lastPacket = new HashMap<UUID, Long>();
        MorePackets.packetTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    }
    
    @EventHandler
    public void PlayerJoin(final PlayerJoinEvent event) {
        this.blacklist.add(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onLogout(final PlayerQuitEvent e) {
        if (MorePackets.packetTicks.containsKey(e.getPlayer().getUniqueId())) {
            MorePackets.packetTicks.remove(e.getPlayer().getUniqueId());
        }
        if (MorePackets.lastPacket.containsKey(e.getPlayer().getUniqueId())) {
            MorePackets.lastPacket.remove(e.getPlayer().getUniqueId());
        }
        if (this.blacklist.contains(e.getPlayer().getUniqueId())) {
            this.blacklist.remove(e.getPlayer().getUniqueId());
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
        if (player.hasPermission("daedalus.bypass")) {
            return;
        }
        if (!this.getDaedalus().isEnabled()) {
            return;
        }
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        if (this.getDaedalus().lag.getTPS() > 21.0 || this.getDaedalus().lag.getTPS() < this.getDaedalus().getTPSCancel()) {
            return;
        }
        if (this.getDaedalus().lag.getPing(player) > 200) {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (MorePackets.packetTicks.containsKey(player.getUniqueId())) {
            Count = MorePackets.packetTicks.get(player.getUniqueId()).getKey();
            Time = MorePackets.packetTicks.get(player.getUniqueId()).getValue();
        }
        if (MorePackets.lastPacket.containsKey(player.getUniqueId())) {
            final long MS = System.currentTimeMillis() - MorePackets.lastPacket.get(player.getUniqueId());
            if (MS >= 100L) {
                this.blacklist.add(player.getUniqueId());
            }
            else if (MS > 1L && this.blacklist.contains(player.getUniqueId())) {
                this.blacklist.remove(player.getUniqueId());
            }
        }
        if (!this.blacklist.contains(player.getUniqueId())) {
            ++Count;
            if (MorePackets.packetTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 1000L)) {
                final int maxPackets = 50;
                if (Count > maxPackets && !UtilPlayer.isFullyStuck(player) && !UtilPlayer.isPartiallyStuck(player)) {
                    this.getDaedalus().logCheat(this, player, "Packets: " + Count, Chance.LIKELY, new String[0]);
                }
                if (Count > 400) {
                    this.getDaedalus().logCheat(this, player, null, Chance.HIGH, "Kicked");
                    player.kickPlayer("Too many packets.");
                }
                Count = 0;
                Time = UtilTime.nowlong();
            }
        }
        MorePackets.packetTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
        MorePackets.lastPacket.put(player.getUniqueId(), System.currentTimeMillis());
    }
}
