package net.hermanos.ac.checks.combat.movement;

import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

public class FastLadder extends Check
{
    public static HashMap<Player, Integer> count;
    
    static {
        FastLadder.count = new HashMap<Player, Integer>();
    }
    
    public FastLadder(final Bernard Daedalus) {
        super("FastLadder", "FastLadder", Daedalus);
        this.setEnabled(true);
        this.setBannable(true);
        this.setMaxViolations(7);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        if (FastLadder.count.containsKey(p)) {
            FastLadder.count.remove(p);
        }
    }
    
    @EventHandler
    public void checkFastLadder(final PlayerMoveEvent e) {
        final double OffsetY = UtilMath.offset(UtilMath.getVerticalVector(e.getFrom().toVector()), UtilMath.getVerticalVector(e.getTo().toVector()));
        final double Limit = 0.13;
        final Player player = e.getPlayer();
        if (!FastLadder.count.containsKey(player)) {
            FastLadder.count.put(player, 0);
            return;
        }
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        if (this.getDaedalus().getLastVelocity().containsKey(player.getUniqueId())) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        if (player.hasPermission("daedalus.bypass")) {
            return;
        }
        final int Count = FastLadder.count.get(player);
        if (!UtilPlayer.isOnClimbable(player, 1) || !UtilPlayer.isOnClimbable(player, 0)) {
            return;
        }
        if (e.getFrom().getY() == e.getTo().getY()) {
            return;
        }
        if (this.getDaedalus().LastVelocity.containsKey(player.getUniqueId())) {
            return;
        }
        @SuppressWarnings("unused")
		final double yDist = UtilMath.offset(UtilMath.getVerticalVector(e.getFrom().toVector()), UtilMath.getVerticalVector(e.getTo().toVector()));
        final double updown = e.getTo().getY() - e.getFrom().getY();
        if (updown <= 0.0) {
            return;
        }
        if (OffsetY > Limit) {
            FastLadder.count.put(player, Count + 1);
            this.dumplog(player, "[Illegitmate] New Count: " + Count + " (+1); Speed: " + OffsetY + "; Max: " + Limit);
        }
        else {
            FastLadder.count.put(player, 0);
        }
        final long percent = Math.round((OffsetY - Limit) * 120.0);
        if (Count >= 12) {
            FastLadder.count.remove(player);
            this.dumplog(player, "Flagged for FastLadder; Speed:" + OffsetY + "; Max: " + Limit + "; New Count: " + Count);
            this.getDaedalus().logCheat(this, player, String.valueOf(percent) + "% faster than normal", Chance.HIGH, new String[0]);
        }
    }
}
