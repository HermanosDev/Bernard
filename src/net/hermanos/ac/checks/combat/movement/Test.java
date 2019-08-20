package net.hermanos.ac.checks.combat.movement;

import java.util.*;

import org.bukkit.entity.*;
import org.bukkit.block.*;
import org.bukkit.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import org.bukkit.event.*;

public class Test extends Check
{
    ArrayList<Double> values;
    @SuppressWarnings("unused")
	private boolean testing;
    @SuppressWarnings("unused")
	private Map<UUID, Long> LastMS;
    @SuppressWarnings("unused")
	private Map<UUID, List<Long>> Clicks;
    @SuppressWarnings("unused")
	private Map<UUID, Map.Entry<Integer, Long>> ClickTicks;
    public static Map<UUID, Map.Entry<Integer, Long>> speedTicks;
    
    static {
        Test.speedTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    }
    
    public Test(final Bernard Daedalus) {
        super("Test", "Test", Daedalus);
        this.setEnabled(false);
        this.setBannable(false);
        this.setMaxViolations(5);
        this.values = new ArrayList<Double>();
        this.testing = false;
        this.LastMS = new HashMap<UUID, Long>();
        this.Clicks = new HashMap<UUID, List<Long>>();
        this.ClickTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    }
    
    public boolean isOnGround(final Player player) {
        if (UtilPlayer.isOnClimbable(player, 0)) {
            return false;
        }
        if (player.getVehicle() != null) {
            return false;
        }
        Material type = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
        if (type != Material.AIR && type.isBlock() && type.isSolid() && type != Material.LADDER && type != Material.VINE) {
            return true;
        }
        Location a = player.getLocation().clone();
        a.setY(a.getY() - 0.5);
        type = a.getBlock().getType();
        if (type != Material.AIR && type.isBlock() && type.isSolid() && type != Material.LADDER && type != Material.VINE) {
            return true;
        }
        a = player.getLocation().clone();
        a.setY(a.getY() + 0.5);
        type = a.getBlock().getRelative(BlockFace.DOWN).getType();
        return (type != Material.AIR && type.isBlock() && type.isSolid() && type != Material.LADDER && type != Material.VINE) || UtilCheat.isBlock(player.getLocation().getBlock().getRelative(BlockFace.DOWN), new Material[] { Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER });
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        if (e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ()) {
            return;
        }
        final Player player = e.getPlayer();
        final double YSpeed = UtilMath.offset(UtilMath.getHorizontalVector(e.getFrom().toVector()), UtilMath.getHorizontalVector(e.getTo().toVector()));
        this.getDaedalus().logCheat(this, player, null, null, String.valueOf(YSpeed) + " speed");
    }
}
