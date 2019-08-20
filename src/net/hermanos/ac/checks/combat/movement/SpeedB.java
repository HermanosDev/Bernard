package net.hermanos.ac.checks.combat.movement;

import java.util.concurrent.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.*;
import org.bukkit.potion.*;
import java.util.*;
import org.bukkit.util.Vector;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import org.bukkit.block.*;

public class SpeedB extends Check
{
    public Location location;
    public Map<UUID, Map.Entry<Integer, Long>> speedTicks;
    public Map<UUID, Map.Entry<Integer, Long>> tooFastTicks;
    public Map<UUID, Long> lastHit;
    
    public SpeedB(final Bernard Daedalus) {
        super("SpeedB", "Speed (Type B)", Daedalus);
        this.setEnabled(true);
        this.setMaxViolations(15);
        this.setViolationResetTime(TimeUnit.MINUTES.toMillis(2L));
        this.setBannable(true);
        this.setViolationsToNotify(4);
        this.lastHit = new HashMap<UUID, Long>();
        this.tooFastTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.speedTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
    public void onMove(final PlayerMoveEvent e) {
        final Location from = e.getFrom().clone();
        final Location to = e.getTo().clone();
        final Player p = e.getPlayer();
        final Location l = p.getLocation();
        final int x = l.getBlockX();
        final int y = l.getBlockY();
        final int z = l.getBlockZ();
        final Location blockLoc = new Location(p.getWorld(), (double)x, (double)(y - 1), (double)z);
        final Location loc = new Location(p.getWorld(), (double)x, (double)y, (double)z);
        final Location loc2 = new Location(p.getWorld(), (double)x, (double)(y + 1), (double)z);
        final Location above = new Location(p.getWorld(), (double)x, (double)(y + 2), (double)z);
        final Location above2 = new Location(p.getWorld(), (double)(x - 1), (double)(y + 2), (double)(z - 1));
        final long lastHitDiff = Math.abs(System.currentTimeMillis() - SpeedA.lastHit.getOrDefault(p.getUniqueId(), 0L));
        if (lastHitDiff < 1500L || p.getNoDamageTicks() != 0) {
            return;
        }
        if (p.getVehicle() != null) {
            return;
        }
        if (p.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        if (p.getAllowFlight()) {
            return;
        }
        if (e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ() && e.getTo().getY() == e.getFrom().getY()) {
            return;
        }
        double Airmaxspeed = 0.4;
        double maxSpeed = 0.42;
        double newmaxspeed = 0.75;
        if (this.isOnIce(p)) {
            newmaxspeed = 1.0;
        }
        double ig = 0.28;
        final double speed = UtilMath.offset(this.getHV(to.toVector()), this.getHV(from.toVector()));
        final double onGroundDiff = to.getY() - from.getY();
        if (p.hasPotionEffect(PotionEffectType.SPEED)) {
            final int level = this.getPotionEffectLevel(p, PotionEffectType.SPEED);
            if (level > 0) {
                newmaxspeed *= level * 20 * 0.011 + 1.0;
                Airmaxspeed *= level * 20 * 0.011 + 1.0;
                maxSpeed *= level * 20 * 0.011 + 1.0;
                ig *= level * 20 * 0.011 + 1.0;
            }
        }
        Airmaxspeed += ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
        maxSpeed += ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
        if (isReallyOnGround(p) && to.getY() == from.getY() && speed >= maxSpeed && p.isOnGround() && p.getFallDistance() < 0.15 && blockLoc.getBlock().getType() != Material.ICE && blockLoc.getBlock().getType() != Material.PACKED_ICE && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR) {
            this.getDaedalus().logCheat(this, p, null, Chance.HIGH, "onGround");
        }
        if (!isReallyOnGround(p) && speed >= Airmaxspeed && !this.isOnIce(p) && blockLoc.getBlock().getType() != Material.ICE && !blockLoc.getBlock().isLiquid() && !loc.getBlock().isLiquid() && blockLoc.getBlock().getType() != Material.PACKED_ICE && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && blockLoc.getBlock().getType() != Material.AIR) {
            this.getDaedalus().logCheat(this, p, null, Chance.HIGH, "MidAir");
        }
        if (speed >= newmaxspeed && this.isOnIce(p) && p.getFallDistance() < 0.6 && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR && loc2.getBlock().getType() == Material.AIR) {
            this.getDaedalus().logCheat(this, p, null, Chance.HIGH, "Limit");
        }
        if (speed > ig && !isAir(p) && onGroundDiff <= -0.4 && p.getFallDistance() <= 0.4 && !flaggyStuffNear(p.getLocation()) && blockLoc.getBlock().getType() != Material.ICE && e.getTo().getY() != e.getFrom().getY() && blockLoc.getBlock().getType() != Material.PACKED_ICE && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR) {
            this.getDaedalus().logCheat(this, p, null, Chance.HIGH, "Vanilla");
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent e) {
        if (this.speedTicks.containsKey(e.getPlayer().getUniqueId())) {
            this.speedTicks.remove(e.getPlayer().getUniqueId());
        }
        if (this.tooFastTicks.containsKey(e.getPlayer().getUniqueId())) {
            this.tooFastTicks.remove(e.getPlayer().getUniqueId());
        }
        if (this.lastHit.containsKey(e.getPlayer().getUniqueId())) {
            this.lastHit.remove(e.getPlayer().getUniqueId());
        }
    }
    
    public boolean isOnIce(final Player player) {
        final Location a = player.getLocation();
        a.setY(a.getY() - 1.0);
        if (a.getBlock().getType().equals((Object)Material.ICE)) {
            return true;
        }
        a.setY(a.getY() - 1.0);
        return a.getBlock().getType().equals((Object)Material.ICE);
    }
    
    private int getPotionEffectLevel(final Player p, final PotionEffectType pet) {
        for (final PotionEffect pe : p.getActivePotionEffects()) {
            if (pe.getType().getName().equals(pet.getName())) {
                return pe.getAmplifier() + 1;
            }
        }
        return 0;
    }
    
    private Vector getHV(final Vector V) {
        V.setY(0);
        return V;
    }
    
    @SuppressWarnings("deprecation")
	public static boolean isReallyOnGround(final Player p) {
        final Location l = p.getLocation();
        final int x = l.getBlockX();
        final int y = l.getBlockY();
        final int z = l.getBlockZ();
        final Location b = new Location(p.getWorld(), (double)x, (double)(y - 1), (double)z);
        return p.isOnGround() && b.getBlock().getType() != Material.AIR && b.getBlock().getType() != Material.WEB && !b.getBlock().isLiquid();
    }
    
    public static boolean flaggyStuffNear(final Location loc) {
        boolean nearBlocks = false;
        for (final Block bl : UtilBlock.getSurrounding(loc.getBlock(), true)) {
            if (bl.getType().equals((Object)Material.STEP) || bl.getType().equals((Object)Material.DOUBLE_STEP) || bl.getType().equals((Object)Material.BED) || bl.getType().equals((Object)Material.WOOD_DOUBLE_STEP) || bl.getType().equals((Object)Material.WOOD_STEP)) {
                nearBlocks = true;
                break;
            }
        }
        for (final Block bl : UtilBlock.getSurrounding(loc.getBlock(), false)) {
            if (bl.getType().equals((Object)Material.STEP) || bl.getType().equals((Object)Material.DOUBLE_STEP) || bl.getType().equals((Object)Material.BED) || bl.getType().equals((Object)Material.WOOD_DOUBLE_STEP) || bl.getType().equals((Object)Material.WOOD_STEP)) {
                nearBlocks = true;
                break;
            }
        }
        if (isBlock(loc.getBlock().getRelative(BlockFace.DOWN), new Material[] { Material.STEP, Material.BED, Material.DOUBLE_STEP, Material.WOOD_DOUBLE_STEP, Material.WOOD_STEP })) {
            nearBlocks = true;
        }
        return nearBlocks;
    }
    
    public static boolean isBlock(final Block block, final Material[] materials) {
        final Material type = block.getType();
        for (final Material m : materials) {
            if (m == type) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isAir(final Player player) {
        final Block b = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        return b.getType().equals((Object)Material.AIR) && b.getRelative(BlockFace.WEST).getType().equals((Object)Material.AIR) && b.getRelative(BlockFace.NORTH).getType().equals((Object)Material.AIR) && b.getRelative(BlockFace.EAST).getType().equals((Object)Material.AIR) && b.getRelative(BlockFace.SOUTH).getType().equals((Object)Material.AIR);
    }
}
