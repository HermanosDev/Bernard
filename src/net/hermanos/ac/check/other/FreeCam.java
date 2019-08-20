package net.hermanos.ac.check.other;

import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import org.bukkit.event.block.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import java.util.*;

public class FreeCam extends Check
{
    public FreeCam(final Bernard Daedalus) {
        super("BlockInteract", "Block Interact", Daedalus);
        this.setBannable(false);
        this.setEnabled(true);
        this.setMaxViolations(29);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void checkFreecam(final PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        boolean isValid = false;
        final Player player = e.getPlayer();
        final Location scanLocation = e.getClickedBlock().getRelative(e.getBlockFace()).getLocation();
        final double x = scanLocation.getX();
        final double y = scanLocation.getY();
        final double z = scanLocation.getZ();
        for (double sX = x; sX < x + 2.0; ++sX) {
            for (double sY = y; sY < y + 2.0; ++sY) {
                for (double sZ = z; sZ < z + 2.0; ++sZ) {
                    final Location relative = new Location(scanLocation.getWorld(), sX, sY, sZ);
                    final List<Location> blocks = this.rayTrace(player.getLocation(), relative);
                    boolean valid = true;
                    for (final Location l : blocks) {
                        if (!this.checkPhase(l.getBlock().getType())) {
                            valid = false;
                        }
                    }
                    if (valid) {
                        isValid = true;
                    }
                }
            }
        }
        if (!isValid && !player.getPlayer().getItemInHand().getType().equals((Object)Material.ENDER_PEARL)) {
            this.getDaedalus().logCheat(this, player, "Block Interact is invalid!", Chance.LIKELY, "Experimental");
            e.setCancelled(true);
        }
    }
    
    private List<Location> rayTrace(final Location from, final Location to) {
        final List<Location> a = new ArrayList<Location>();
        if (from == null || to == null) {
            return a;
        }
        if (!from.getWorld().equals(to.getWorld())) {
            return a;
        }
        if (from.distance(to) > 10.0) {
            return a;
        }
        double x1 = from.getX();
        double y1 = from.getY() + 1.62;
        double z1 = from.getZ();
        final double x2 = to.getX();
        final double y2 = to.getY();
        final double z2 = to.getZ();
        for (boolean scanning = true; scanning; scanning = false) {
            a.add(new Location(from.getWorld(), x1, y1, z1));
            x1 += (x2 - x1) / 10.0;
            y1 += (y2 - y1) / 10.0;
            z1 += (z2 - z1) / 10.0;
            if (Math.abs(x1 - x2) < 0.01 && Math.abs(y1 - y2) < 0.01 && Math.abs(z1 - z2) < 0.01) {}
        }
        return a;
    }
    
    @SuppressWarnings("deprecation")
	public boolean checkPhase(final Material m) {
        final int[] whitelist = { 355, 196, 194, 197, 195, 193, 64, 96, 187, 184, 186, 107, 185, 183, 192, 189, 139, 191, 85, 101, 190, 113, 188, 160, 102, 163, 157, 0, 145, 49, 77, 135, 108, 67, 164, 136, 114, 156, 180, 128, 143, 109, 134, 53, 126, 44, 416, 8, 425, 138, 26, 397, 372, 13, 135, 117, 108, 39, 81, 92, 71, 171, 141, 118, 144, 54, 139, 67, 127, 59, 115, 330, 164, 151, 178, 32, 28, 93, 94, 175, 122, 116, 130, 119, 120, 51, 140, 147, 154, 148, 136, 65, 10, 69, 31, 105, 114, 372, 33, 34, 36, 29, 90, 142, 27, 104, 156, 66, 40, 330, 38, 180, 149, 150, 75, 76, 55, 128, 6, 295, 323, 63, 109, 78, 88, 134, 176, 11, 9, 44, 70, 182, 83, 50, 146, 132, 131, 106, 177, 68, 8, 111, 30, 72, 53, 126, 37 };
        int[] array;
        for (int length = (array = whitelist).length, i = 0; i < length; ++i) {
            final int ids = array[i];
            if (m.getId() == ids) {
                return true;
            }
        }
        return false;
    }
}
