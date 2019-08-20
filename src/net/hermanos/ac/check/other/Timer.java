package net.hermanos.ac.check.other;

import org.bukkit.entity.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.packets.events.*;
import net.hermanos.ac.utils.*;

import org.bukkit.event.*;

import java.util.*;

public class Timer extends Check
{
    private Map<UUID, Map.Entry<Integer, Long>> packets;
    private Map<UUID, Integer> verbose;
    private Map<UUID, Long> lastPacket;
    private List<Player> toCancel;
    
    public Timer(final Bernard Daedalus) {
        super("TimerA", "Timer (Type A)", Daedalus);
        this.packets = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.verbose = new HashMap<UUID, Integer>();
        this.toCancel = new ArrayList<Player>();
        this.lastPacket = new HashMap<UUID, Long>();
        this.setEnabled(true);
        this.setBannable(false);
        this.setMaxViolations(5);
    }
    
    @EventHandler
    public void onLogout(final PlayerQuitEvent e) {
        if (this.packets.containsKey(e.getPlayer().getUniqueId())) {
            this.packets.remove(e.getPlayer().getUniqueId());
        }
        if (this.verbose.containsKey(e.getPlayer().getUniqueId())) {
            this.verbose.remove(e.getPlayer().getUniqueId());
        }
        if (this.lastPacket.containsKey(e.getPlayer().getUniqueId())) {
            this.lastPacket.remove(e.getPlayer().getUniqueId());
        }
        if (this.toCancel.contains(e.getPlayer())) {
            this.toCancel.remove(e.getPlayer());
        }
    }
    
    @EventHandler
    public void PacketPlayer(final PacketPlayerEvent event) {
        final Player player = event.getPlayer();
        if (!this.getDaedalus().isEnabled()) {
            return;
        }
        if (player.hasPermission("daedalus.bypass")) {
            return;
        }
        if (this.getDaedalus().getLag().getTPS() < this.getDaedalus().getTPSCancel()) {
            return;
        }
        final long lastPacket = this.lastPacket.getOrDefault(player.getUniqueId(), System.currentTimeMillis());
        int packets = 0;
        long Time = System.currentTimeMillis();
        int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
        if (this.packets.containsKey(player.getUniqueId())) {
            packets = this.packets.get(player.getUniqueId()).getKey();
            Time = this.packets.get(player.getUniqueId()).getValue();
        }
        if (System.currentTimeMillis() - lastPacket > 100L) {
            this.toCancel.add(player);
        }
        final double threshold = 23.0;
        if (UtilTime.elapsed(Time, 1000L)) {
            if (this.toCancel.remove(player) && packets <= 13) {
                return;
            }
            if (packets > threshold + this.getDaedalus().packet.movePackets.getOrDefault(player.getUniqueId(), 0) && this.getDaedalus().packet.movePackets.getOrDefault(player.getUniqueId(), 0) < 5) {
                verbose = ((packets - threshold > 10.0) ? (verbose + 2) : (verbose + 1));
            }
            else {
                verbose = 0;
            }
            if (verbose > 2) {
                this.getDaedalus().logCheat(this, player, "Packets: " + packets, Chance.HIGH, new String[0]);
            }
            packets = 0;
            Time = UtilTime.nowlong();
            this.getDaedalus().packet.movePackets.remove(player.getUniqueId());
        }
        ++packets;
        this.lastPacket.put(player.getUniqueId(), System.currentTimeMillis());
        this.packets.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(packets, Time));
        this.verbose.put(player.getUniqueId(), verbose);
    }
}
