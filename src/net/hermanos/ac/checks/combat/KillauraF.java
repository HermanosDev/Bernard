package net.hermanos.ac.checks.combat;

import java.util.*;

import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.checks.combat.movement.*;
import net.hermanos.ac.utils.*;

import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class KillauraF extends Check
{
    public static HashMap<Player, Integer> counts;
    private ArrayList<Player> blockGlitched;
    
    static {
        KillauraF.counts = new HashMap<Player, Integer>();
    }
    
    public KillauraF(final Bernard Daedalus) {
        super("KillAuraF", "KillAura (Wall)", Daedalus);
        this.blockGlitched = new ArrayList<Player>();
        this.setEnabled(true);
        this.setBannable(false);
        this.setMaxViolations(7);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogout(final PlayerQuitEvent e) {
        if (KillauraF.counts.containsKey(e.getPlayer())) {
            KillauraF.counts.remove(e.getPlayer());
        }
        if (this.blockGlitched.contains(e.getPlayer())) {
            this.blockGlitched.remove(e.getPlayer());
        }
    }
    
    @EventHandler
    public void onBreak(final BlockBreakEvent e) {
        if (e.isCancelled()) {
            this.blockGlitched.add(e.getPlayer());
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void checkKillaura(final EntityDamageByEntityEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        if (!this.getDaedalus().isEnabled()) {
            return;
        }
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) {
            return;
        }
        final Player p = (Player)e.getDamager();
        if (p.hasPermission("daedalus.bypass")) {
            return;
        }
        if (UtilCheat.slabsNear(p.getEyeLocation()) || UtilCheat.slabsNear(p.getEyeLocation().clone().add(0.0, 0.5, 0.0))) {
            return;
        }
        int Count = 0;
        if (KillauraF.counts.containsKey(p)) {
            Count = KillauraF.counts.get(p);
        }
        final Player attacked = (Player)e.getEntity();
        final Location dloc = p.getLocation();
        final Location aloc = attacked.getLocation();
        final double zdif = Math.abs(dloc.getZ() - aloc.getZ());
        final double xdif = Math.abs(dloc.getX() - aloc.getX());
        if (xdif == 0.0 || zdif == 0.0) {
            return;
        }
        for (int y = 0; y < 1; ++y) {
            final Location zBlock = (zdif < -0.2) ? dloc.clone().add(0.0, (double)y, zdif) : aloc.clone().add(0.0, (double)y, zdif);
            if (!PhaseA.allowed.contains(zBlock.getBlock().getType()) && zBlock.getBlock().getType().isSolid() && !p.hasLineOfSight((Entity)attacked) && !UtilCheat.isSlab(zBlock.getBlock())) {
                ++Count;
            }
            final Location xBlock = (xdif < -0.2) ? dloc.clone().add(xdif, (double)y, 0.0) : aloc.clone().add(xdif, (double)y, 0.0);
            if (!PhaseA.allowed.contains(xBlock.getBlock().getType()) && xBlock.getBlock().getType().isSolid() && !p.hasLineOfSight((Entity)attacked) && !UtilCheat.isSlab(xBlock.getBlock())) {
                ++Count;
            }
        }
        if (Count > 3) {
            this.getDaedalus().logCheat(this, p, null, Chance.LIKELY, "Experimental");
            Count = 0;
        }
        KillauraF.counts.put(p, Count);
    }
}
