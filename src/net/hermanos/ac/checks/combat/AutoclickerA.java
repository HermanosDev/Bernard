package net.hermanos.ac.checks.combat;

import java.util.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.packets.events.*;
import net.hermanos.ac.utils.*;

import org.bukkit.entity.*;
import org.bukkit.event.*;

public class AutoclickerA extends Check
{
    public Map<UUID, Integer> clicks;
    private Map<UUID, Long> recording;
    
    public AutoclickerA(final Bernard Daedalus) {
        super("AutoclickerA", "Autoclicker (Type A)", Daedalus);
        this.setEnabled(true);
        this.setBannable(true);
        this.setViolationsToNotify(1);
        this.setMaxViolations(5);
        this.clicks = new HashMap<UUID, Integer>();
        this.recording = new HashMap<UUID, Long>();
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final UUID uuid = p.getUniqueId();
        if (this.clicks.containsKey(uuid)) {
            this.clicks.remove(uuid);
        }
        if (this.recording.containsKey(uuid)) {
            this.recording.remove(uuid);
        }
    }
    
    @EventHandler
    public void onSwing(final PacketSwingArmEvent e) {
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        final Player player = e.getPlayer();
        if (player.hasPermission("daedalus.bypass")) {
            return;
        }
        int clicks = this.clicks.getOrDefault(this, 0);
        final long time = this.recording.getOrDefault(player.getUniqueId(), System.currentTimeMillis());
        if (UtilTime.elapsed(time, 1000L)) {
            if (clicks > 30) {
                this.getDaedalus().logCheat(this, player, null, Chance.HIGH, String.valueOf(clicks) + " Clicks/Second");
            }
            clicks = 0;
            this.recording.remove(player.getUniqueId());
        }
        else {
            ++clicks;
        }
        this.clicks.put(player.getUniqueId(), clicks);
        this.recording.put(player.getUniqueId(), time);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(final PlayerQuitEvent e) {
        if (this.clicks.containsKey(e.getPlayer().getUniqueId())) {
            this.clicks.remove(e.getPlayer().getUniqueId());
        }
        if (this.recording.containsKey(e.getPlayer().getUniqueId())) {
            this.recording.remove(e.getPlayer().getUniqueId());
        }
    }
}
