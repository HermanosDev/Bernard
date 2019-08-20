package net.hermanos.ac.checks.combat.movement;

import java.util.*;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.check.other.*;
import net.hermanos.ac.utils.*;

public class Fly extends Check
{
    public static Map<UUID, Long> flyTicksA;
    
    static {
        Fly.flyTicksA = new HashMap<UUID, Long>();
    }
    
    public Fly(final Bernard Daedalus) {
        super("FlyA", "Fly (Type A)", Daedalus);
        this.setEnabled(true);
        this.setBannable(true);
        this.setMaxViolations(5);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final UUID uuid = p.getUniqueId();
        if (Fly.flyTicksA.containsKey(uuid)) {
            Fly.flyTicksA.remove(uuid);
        }
    }
    
    @EventHandler
    public void CheckFlyA(final PlayerMoveEvent event) {
        if (!this.getDaedalus().isEnabled()) {
            return;
        }
        final Player player = event.getPlayer();
        if (player.getAllowFlight()) {
            return;
        }
        if (player.getVehicle() != null) {
            return;
        }
        if (player.hasPermission("daedalus.bypass")) {
            return;
        }
        if (Latency.getLag(player) > 92) {
            return;
        }
        if (this.getDaedalus().getLag().getTPS() < this.getDaedalus().getTPSCancel()) {
            return;
        }
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        if (UtilPlayer.isInWater(player)) {
            return;
        }
        if (UtilCheat.isInWeb(player)) {
            return;
        }
        if (UtilCheat.blocksNear(player.getLocation())) {
            if (Fly.flyTicksA.containsKey(player.getUniqueId())) {
                Fly.flyTicksA.remove(player.getUniqueId());
            }
            return;
        }
        if (event.getTo().getX() == event.getFrom().getX() && event.getTo().getZ() == event.getFrom().getZ()) {
            return;
        }
        if (Math.abs(event.getTo().getY() - event.getFrom().getY()) > 0.06) {
            if (Fly.flyTicksA.containsKey(player.getUniqueId())) {
                Fly.flyTicksA.remove(player.getUniqueId());
            }
            return;
        }
        long Time = System.currentTimeMillis();
        if (Fly.flyTicksA.containsKey(player.getUniqueId())) {
            Time = Fly.flyTicksA.get(player.getUniqueId());
        }
        final long MS = System.currentTimeMillis() - Time;
        if (MS > 200L) {
            this.dumplog(player, "Logged Fly. MS: " + MS);
            this.getDaedalus().logCheat(this, player, "Hovering for " + UtilMath.trim(1, Double.valueOf(MS / 1000L)) + " second(s)", Chance.HIGH, new String[0]);
            Fly.flyTicksA.remove(player.getUniqueId());
            return;
        }
        Fly.flyTicksA.put(player.getUniqueId(), Time);
    }
}
