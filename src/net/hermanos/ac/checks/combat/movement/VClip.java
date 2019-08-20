package net.hermanos.ac.checks.combat.movement;

import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

public class VClip extends Check
{
    public static List<Material> allowed;
    public static ArrayList<Player> teleported;
    public static HashMap<Player, Location> lastLocation;
    
    static {
        VClip.allowed = new ArrayList<Material>();
        VClip.teleported = new ArrayList<Player>();
        VClip.lastLocation = new HashMap<Player, Location>();
        VClip.allowed.add(Material.PISTON_EXTENSION);
        VClip.allowed.add(Material.PISTON_STICKY_BASE);
        VClip.allowed.add(Material.PISTON_BASE);
        VClip.allowed.add(Material.SIGN_POST);
        VClip.allowed.add(Material.WALL_SIGN);
        VClip.allowed.add(Material.STRING);
        VClip.allowed.add(Material.AIR);
        VClip.allowed.add(Material.FENCE_GATE);
        VClip.allowed.add(Material.CHEST);
    }
    
    public VClip(final Bernard Daedalus) {
        super("VClip", "VClip", Daedalus);
        this.setBannable(false);
        this.setEnabled(true);
        this.setMaxViolations(19);
        this.setViolationResetTime(10000L);
    }
    
    @EventHandler
    public void onTeleport(final PlayerTeleportEvent e) {
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            return;
        }
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        if (p.hasPermission("daedalus.bypass")) {
            return;
        }
        final Location to = e.getTo().clone();
        final Location from = e.getFrom().clone();
        if (from.getY() == to.getY()) {
            return;
        }
        if (VClip.teleported.contains(e.getPlayer())) {
            VClip.teleported.remove(e.getPlayer());
            return;
        }
        if (!this.getDaedalus().isEnabled()) {
            return;
        }
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        if (p.getAllowFlight()) {
            return;
        }
        if (p.getVehicle() != null) {
            return;
        }
        if (e.getTo().getY() <= 0.0 || e.getTo().getY() >= p.getWorld().getMaxHeight()) {
            return;
        }
        if (!UtilCheat.blocksNear(p)) {
            return;
        }
        if (p.getLocation().getY() < 0.0 || p.getLocation().getY() > p.getWorld().getMaxHeight()) {
            return;
        }
        for (double yDist = from.getBlockY() - to.getBlockY(), y = 0.0; y < Math.abs(yDist); ++y) {
            final Location l = (yDist < -0.2) ? from.getBlock().getLocation().clone().add(0.0, y, 0.0) : to.getBlock().getLocation().clone().add(0.0, y, 0.0);
            if ((yDist > 20.0 || yDist < -20.0) && l.getBlock().getType() != Material.AIR && l.getBlock().getType().isSolid() && !VClip.allowed.contains(l.getBlock().getType())) {
                p.kickPlayer("No");
                this.getDaedalus().logCheat(this, p, "More than 20 blocks.", Chance.HIGH, new String[0]);
                p.teleport(from);
                return;
            }
            if (l.getBlock().getType() != Material.AIR && Math.abs(yDist) > 1.0 && l.getBlock().getType().isSolid() && !VClip.allowed.contains(l.getBlock().getType())) {
                this.getDaedalus().logCheat(this, p, String.valueOf(y) + " blocks", Chance.LIKELY, new String[0]);
                p.teleport((Location)VClip.lastLocation.get(p));
            }
            else {
                VClip.lastLocation.put(p, p.getLocation());
            }
        }
    }
}
