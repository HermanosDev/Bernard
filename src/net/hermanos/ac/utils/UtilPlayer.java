package net.hermanos.ac.utils;

import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import org.bukkit.block.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class UtilPlayer
{
    public static void clear(final Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setSprinting(false);
        player.setFoodLevel(20);
        player.setSaturation(3.0f);
        player.setExhaustion(0.0f);
        player.setMaxHealth(20.0);
        player.setHealth(((Damageable)player).getMaxHealth());
        player.setFireTicks(0);
        player.setFallDistance(0.0f);
        player.setLevel(0);
        player.setExp(0.0f);
        player.setWalkSpeed(0.2f);
        player.setFlySpeed(0.1f);
        player.getInventory().clear();
        player.getInventory().setHelmet((ItemStack)null);
        player.getInventory().setChestplate((ItemStack)null);
        player.getInventory().setLeggings((ItemStack)null);
        player.getInventory().setBoots((ItemStack)null);
        player.updateInventory();
        for (final PotionEffect potion : player.getActivePotionEffects()) {
            player.removePotionEffect(potion.getType());
        }
    }
    
    public static Location getEyeLocation(final Player player) {
        final Location eye = player.getLocation();
        eye.setY(eye.getY() + player.getEyeHeight());
        return eye;
    }
    
    public static boolean isInWater(final Player player) {
        final Material m = player.getLocation().getBlock().getType();
        return m == Material.STATIONARY_WATER || m == Material.WATER;
    }
    
    public static boolean isOnClimbable(final Player player, final int blocks) {
        if (blocks == 0) {
            for (final Block block : UtilBlock.getSurrounding(player.getLocation().getBlock(), false)) {
                if (block.getType() == Material.LADDER || block.getType() == Material.VINE) {
                    return true;
                }
            }
        }
        else {
            for (final Block block : UtilBlock.getSurrounding(player.getLocation().clone().add(0.0, 1.0, 0.0).getBlock(), false)) {
                if (block.getType() == Material.LADDER || block.getType() == Material.VINE) {
                    return true;
                }
            }
        }
        return player.getLocation().getBlock().getType() == Material.LADDER || player.getLocation().getBlock().getType() == Material.VINE;
    }
    
    public static boolean isInAir(final Player player) {
        for (final Block block : UtilBlock.getSurrounding(player.getLocation().getBlock(), false)) {
            if (block.getType() == Material.AIR) {
                return true;
            }
        }
        return player.getLocation().getBlock().getType() == Material.AIR;
    }
    
    public static boolean isPartiallyStuck(final Player player) {
        if (player.getLocation().clone().getBlock() == null) {
            return false;
        }
        final Block block = player.getLocation().clone().getBlock();
        return !UtilCheat.isSlab(block) && !UtilCheat.isStair(block) && (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid() || player.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid() || (player.getLocation().clone().add(0.0, 1.0, 0.0).getBlock().getRelative(BlockFace.DOWN).getType().isSolid() || player.getLocation().clone().add(0.0, 1.0, 0.0).getBlock().getRelative(BlockFace.UP).getType().isSolid()) || block.getType().isSolid());
    }
    
    public static boolean isFullyStuck(final Player player) {
        final Block block1 = player.getLocation().clone().getBlock();
        final Block block2 = player.getLocation().clone().add(0.0, 1.0, 0.0).getBlock();
        return (block1.getType().isSolid() && block2.getType().isSolid()) || (block1.getRelative(BlockFace.DOWN).getType().isSolid() || (block1.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid() && block2.getRelative(BlockFace.DOWN).getType().isSolid()) || block2.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid());
    }
    
    public static boolean isOnGround(final Player player) {
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
            return true;
        }
        Location a = player.getLocation().clone();
        a.setY(a.getY() - 0.5);
        if (a.getBlock().getType() != Material.AIR) {
            return true;
        }
        a = player.getLocation().clone();
        a.setY(a.getY() + 0.5);
        return a.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR || UtilCheat.isBlock(player.getLocation().getBlock().getRelative(BlockFace.DOWN), new Material[] { Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER });
    }
    
    public static List<Entity> getNearbyRidables(final Location loc, final double distance) {
        final List<Entity> entities = new ArrayList<Entity>();
        for (final Entity entity : new ArrayList<Entity>(loc.getWorld().getEntities())) {
            if (!entity.getType().equals((Object)EntityType.HORSE) && !entity.getType().equals((Object)EntityType.BOAT)) {
                continue;
            }
            Bukkit.getServer().broadcastMessage(new StringBuilder(String.valueOf(entity.getLocation().distance(loc))).toString());
            if (entity.getLocation().distance(loc) > distance) {
                continue;
            }
            entities.add(entity);
        }
        return entities;
    }
}
