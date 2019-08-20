package net.hermanos.ac.checks.combat.movement;

import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

public class Glide extends Check
{
    public static Map<UUID, Long> flyTicks;
    
    public Glide(final Bernard Daedalus) {
        super("FlyB", "Fly (Type B)", Daedalus);
        Glide.flyTicks = new HashMap<UUID, Long>();
        this.setEnabled(false);
        this.setBannable(true);
        this.setMaxViolations(5);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final UUID uuid = p.getUniqueId();
        if (Glide.flyTicks.containsKey(uuid)) {
            Glide.flyTicks.remove(uuid);
        }
    }
    
    @EventHandler
    public void CheckGlide(final PlayerMoveEvent event) {
        if (!this.getDaedalus().isEnabled()) {
            return;
        }
        final Player player = event.getPlayer();
        if (player.hasPermission("daedalus.bypass")) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        if (UtilCheat.isInWeb(player)) {
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
        if (player.getVehicle() != null) {
            return;
        }
        if (UtilCheat.blocksNear(player)) {
            if (Glide.flyTicks.containsKey(player.getUniqueId())) {
                Glide.flyTicks.remove(player.getUniqueId());
            }
            return;
        }
        if (event.getTo().getX() == event.getFrom().getX() && event.getTo().getZ() == event.getFrom().getZ()) {
            return;
        }
        final double OffsetY = event.getFrom().getY() - event.getTo().getY();
        if (OffsetY <= 0.0 || OffsetY > 0.16) {
            if (Glide.flyTicks.containsKey(player.getUniqueId())) {
                Glide.flyTicks.remove(player.getUniqueId());
            }
            return;
        }
        long Time = System.currentTimeMillis();
        if (Glide.flyTicks.containsKey(player.getUniqueId())) {
            Time = Glide.flyTicks.get(player.getUniqueId());
        }
        final long MS = System.currentTimeMillis() - Time;
        if (MS > 1000L) {
            this.dumplog(player, "Logged. MS: " + MS);
            Glide.flyTicks.remove(player.getUniqueId());
            if (this.getDaedalus().getLag().getPing(player) < 275) {
                this.getDaedalus().logCheat(this, player, null, Chance.LIKELY, new String[0]);
            }
            return;
        }
        Glide.flyTicks.put(player.getUniqueId(), Time);
    }
}
