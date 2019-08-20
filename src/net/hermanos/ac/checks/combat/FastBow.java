package net.hermanos.ac.checks.combat;

import java.util.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import org.bukkit.event.entity.*;
import org.bukkit.entity.*;

public class FastBow extends Check
{
    public static Map<Player, Long> bowPull;
    public static Map<Player, Integer> count;
    
    public FastBow(final Bernard Daedalus) {
        super("FastBow", "FastBow", Daedalus);
        FastBow.bowPull = new HashMap<Player, Long>();
        FastBow.count = new HashMap<Player, Integer>();
        this.setViolationsToNotify(2);
        this.setMaxViolations(7);
        this.setEnabled(true);
        this.setBannable(true);
    }
    
    @EventHandler
    public void Interact(final PlayerInteractEvent e) {
        final Player Player = e.getPlayer();
        if (Player.getItemInHand() != null && Player.getItemInHand().getType().equals((Object)Material.BOW)) {
            FastBow.bowPull.put(Player, System.currentTimeMillis());
        }
    }
    
    @EventHandler
    public void onLogout(final PlayerQuitEvent e) {
        if (FastBow.bowPull.containsKey(e.getPlayer())) {
            FastBow.bowPull.remove(e.getPlayer());
        }
        if (FastBow.count.containsKey(e.getPlayer())) {
            FastBow.count.remove(e.getPlayer());
        }
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onShoot(final ProjectileLaunchEvent e) {
        if (!this.isEnabled()) {
            return;
        }
        if (e.getEntity() instanceof Arrow) {
            final Arrow arrow = (Arrow)e.getEntity();
            if (arrow.getShooter() != null && arrow.getShooter() instanceof Player) {
                final Player player = (Player)arrow.getShooter();
                if (FastBow.bowPull.containsKey(player)) {
                    final Long time = System.currentTimeMillis() - FastBow.bowPull.get(player);
                    final double power = arrow.getVelocity().length();
                    final Long timeLimit = 300L;
                    int Count = 0;
                    if (FastBow.count.containsKey(player)) {
                        Count = FastBow.count.get(player);
                    }
                    if (power > 2.5 && time < timeLimit) {
                        FastBow.count.put(player, Count + 1);
                    }
                    else {
                        FastBow.count.put(player, (Count > 0) ? (Count - 1) : Count);
                    }
                    if (player.hasPermission("daedalus.bypass")) {
                        return;
                    }
                    if (Count > 8) {
                        this.getDaedalus().logCheat(this, player, time + " ms", Chance.HIGH, new String[0]);
                        FastBow.count.remove(player);
                    }
                }
            }
        }
    }
}
