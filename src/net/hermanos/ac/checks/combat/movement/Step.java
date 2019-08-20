package net.hermanos.ac.checks.combat.movement;

import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.event.player.*;
import org.bukkit.potion.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import org.bukkit.block.*;
import java.util.*;
import org.bukkit.event.*;

public class Step extends Check
{
    double stepHeight;
    
    public Step(final Bernard Daedalus) {
        super("Step", "Step", Daedalus);
        this.setEnabled(true);
        this.setBannable(false);
        this.setMaxViolations(7);
        this.setViolationsToNotify(1);
        this.setViolationResetTime(90000L);
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
    public void onMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (!this.isOnGround(player)) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        if (UtilPlayer.isOnClimbable(player, 0)) {
            return;
        }
        if (UtilCheat.slabsNear(player.getLocation())) {
            return;
        }
        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            return;
        }
        if (this.getDaedalus().LastVelocity.containsKey(player.getUniqueId())) {
            return;
        }
        if (player.getLocation().getBlock().getType().equals((Object)Material.WATER) || player.getLocation().getBlock().getType().equals((Object)Material.STATIONARY_WATER)) {
            return;
        }
        final double yDist = event.getTo().getY() - event.getFrom().getY();
        if (yDist < 0.0) {
            return;
        }
        final double YSpeed = UtilMath.offset(UtilMath.getVerticalVector(event.getFrom().toVector()), UtilMath.getVerticalVector(event.getTo().toVector()));
        if (yDist > 0.95) {
            this.dumplog(player, "Height (Logged): " + yDist);
            this.getDaedalus().logCheat(this, player, String.valueOf(Math.round(yDist)) + " blocks", Chance.HIGH, "Type A");
            return;
        }
        if ((((YSpeed == 0.25 || (YSpeed >= 0.58 && YSpeed < 0.581)) && yDist > 0.0) || (YSpeed > 0.2457 && YSpeed < 0.24582) || (YSpeed > 0.329 && YSpeed < 0.33)) && !player.getLocation().clone().subtract(0.0, 0.1, 0.0).getBlock().getType().equals((Object)Material.SNOW)) {
            this.getDaedalus().logCheat(this, player, "Speed: " + YSpeed + " Block: " + player.getLocation().clone().subtract(0.0, 0.1, 0.0).getBlock().getType().toString(), Chance.LIKELY, "Type C");
            return;
        }
        final ArrayList<Block> blocks = UtilBlock.getBlocksAroundCenter(player.getLocation(), 1);
        for (final Block block : blocks) {
            if (block.getType().isSolid() && YSpeed >= 0.321 && YSpeed < 0.322) {
                this.getDaedalus().logCheat(this, player, "Speed: " + YSpeed, Chance.HIGH, "Type D");
            }
        }
    }
}
