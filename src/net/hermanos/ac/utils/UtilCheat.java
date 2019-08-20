package net.hermanos.ac.utils;

import org.bukkit.block.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.util.*;
import org.bukkit.util.Vector;
import org.bukkit.*;
import java.util.*;
import java.util.regex.*;
import org.bukkit.entity.*;

public final class UtilCheat
{
    private static final List<Material> INSTANT_BREAK;
    private static final List<Material> FOOD;
    private static final List<Material> INTERACTABLE;
    private static final Map<Material, Material> COMBO;
    public static final String SPY_METADATA = "ac-spydata";
    @SuppressWarnings("unused")
	private static Set<UUID> teleported;
    
    static {
        INSTANT_BREAK = new ArrayList<Material>();
        FOOD = new ArrayList<Material>();
        INTERACTABLE = new ArrayList<Material>();
        COMBO = new HashMap<Material, Material>();
        UtilCheat.teleported = new HashSet<UUID>();
        UtilCheat.INSTANT_BREAK.add(Material.RED_MUSHROOM);
        UtilCheat.INSTANT_BREAK.add(Material.RED_ROSE);
        UtilCheat.INSTANT_BREAK.add(Material.BROWN_MUSHROOM);
        UtilCheat.INSTANT_BREAK.add(Material.YELLOW_FLOWER);
        UtilCheat.INSTANT_BREAK.add(Material.REDSTONE);
        UtilCheat.INSTANT_BREAK.add(Material.REDSTONE_TORCH_OFF);
        UtilCheat.INSTANT_BREAK.add(Material.REDSTONE_TORCH_ON);
        UtilCheat.INSTANT_BREAK.add(Material.REDSTONE_WIRE);
        UtilCheat.INSTANT_BREAK.add(Material.LONG_GRASS);
        UtilCheat.INSTANT_BREAK.add(Material.PAINTING);
        UtilCheat.INSTANT_BREAK.add(Material.WHEAT);
        UtilCheat.INSTANT_BREAK.add(Material.SUGAR_CANE);
        UtilCheat.INSTANT_BREAK.add(Material.SUGAR_CANE_BLOCK);
        UtilCheat.INSTANT_BREAK.add(Material.DIODE);
        UtilCheat.INSTANT_BREAK.add(Material.DIODE_BLOCK_OFF);
        UtilCheat.INSTANT_BREAK.add(Material.DIODE_BLOCK_ON);
        UtilCheat.INSTANT_BREAK.add(Material.SAPLING);
        UtilCheat.INSTANT_BREAK.add(Material.TORCH);
        UtilCheat.INSTANT_BREAK.add(Material.CROPS);
        UtilCheat.INSTANT_BREAK.add(Material.SNOW);
        UtilCheat.INSTANT_BREAK.add(Material.TNT);
        UtilCheat.INSTANT_BREAK.add(Material.POTATO);
        UtilCheat.INSTANT_BREAK.add(Material.CARROT);
        UtilCheat.INTERACTABLE.add(Material.STONE_BUTTON);
        UtilCheat.INTERACTABLE.add(Material.LEVER);
        UtilCheat.INTERACTABLE.add(Material.CHEST);
        UtilCheat.FOOD.add(Material.COOKED_BEEF);
        UtilCheat.FOOD.add(Material.COOKED_CHICKEN);
        UtilCheat.FOOD.add(Material.COOKED_FISH);
        UtilCheat.FOOD.add(Material.GRILLED_PORK);
        UtilCheat.FOOD.add(Material.PORK);
        UtilCheat.FOOD.add(Material.MUSHROOM_SOUP);
        UtilCheat.FOOD.add(Material.RAW_BEEF);
        UtilCheat.FOOD.add(Material.RAW_CHICKEN);
        UtilCheat.FOOD.add(Material.RAW_FISH);
        UtilCheat.FOOD.add(Material.APPLE);
        UtilCheat.FOOD.add(Material.GOLDEN_APPLE);
        UtilCheat.FOOD.add(Material.MELON);
        UtilCheat.FOOD.add(Material.COOKIE);
        UtilCheat.FOOD.add(Material.BREAD);
        UtilCheat.FOOD.add(Material.SPIDER_EYE);
        UtilCheat.FOOD.add(Material.ROTTEN_FLESH);
        UtilCheat.FOOD.add(Material.POTATO_ITEM);
        UtilCheat.COMBO.put(Material.SHEARS, Material.WOOL);
        UtilCheat.COMBO.put(Material.IRON_SWORD, Material.WEB);
        UtilCheat.COMBO.put(Material.DIAMOND_SWORD, Material.WEB);
        UtilCheat.COMBO.put(Material.STONE_SWORD, Material.WEB);
        UtilCheat.COMBO.put(Material.WOOD_SWORD, Material.WEB);
    }
    
    public static double getXDelta(final Location one, final Location two) {
        return Math.abs(one.getX() - two.getX());
    }
    
    public static boolean isDoor(final Block block) {
        return block.getType().equals((Object)Material.IRON_DOOR) || block.getType().equals((Object)Material.IRON_DOOR_BLOCK) || block.getType().equals((Object)Material.WOOD_DOOR) || block.getType().equals((Object)Material.WOODEN_DOOR);
    }
    
    public static boolean isFenceGate(final Block block) {
        return block.getType().equals((Object)Material.FENCE_GATE);
    }
    
    public static boolean isTrapDoor(final Block block) {
        return block.getType().equals((Object)Material.TRAP_DOOR);
    }
    
    public static double getZDelta(final Location one, final Location two) {
        return Math.abs(one.getZ() - two.getZ());
    }
    
    public static double getDistance3D(final Location one, final Location two) {
        double toReturn = 0.0;
        final double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
        final double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
        final double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
        final double sqrt = Math.sqrt(xSqr + ySqr + zSqr);
        toReturn = Math.abs(sqrt);
        return toReturn;
    }
    
    public static double getVerticalDistance(final Location one, final Location two) {
        double toReturn = 0.0;
        final double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
        final double sqrt = Math.sqrt(ySqr);
        toReturn = Math.abs(sqrt);
        return toReturn;
    }
    
    public static double getHorizontalDistance(final Location one, final Location two) {
        double toReturn = 0.0;
        final double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
        final double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
        final double sqrt = Math.sqrt(xSqr + zSqr);
        toReturn = Math.abs(sqrt);
        return toReturn;
    }
    
    public static boolean cantStandAtBetter(final Block block) {
        final Block otherBlock = block.getRelative(BlockFace.DOWN);
        final boolean center1 = otherBlock.getType() == Material.AIR;
        final boolean north1 = otherBlock.getRelative(BlockFace.NORTH).getType() == Material.AIR;
        final boolean east1 = otherBlock.getRelative(BlockFace.EAST).getType() == Material.AIR;
        final boolean south1 = otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.AIR;
        final boolean west1 = otherBlock.getRelative(BlockFace.WEST).getType() == Material.AIR;
        final boolean northeast1 = otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.AIR;
        final boolean northwest1 = otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.AIR;
        final boolean southeast1 = otherBlock.getRelative(BlockFace.SOUTH_EAST).getType() == Material.AIR;
        final boolean southwest1 = otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.AIR;
        final boolean overAir1 = otherBlock.getRelative(BlockFace.DOWN).getType() == Material.AIR || otherBlock.getRelative(BlockFace.DOWN).getType() == Material.WATER || otherBlock.getRelative(BlockFace.DOWN).getType() == Material.LAVA;
        return center1 && north1 && east1 && south1 && west1 && northeast1 && southeast1 && northwest1 && southwest1 && overAir1;
    }
    
    public static boolean cantStandAtSingle(final Block block) {
        final Block otherBlock = block.getRelative(BlockFace.DOWN);
        final boolean center = otherBlock.getType() == Material.AIR;
        return center;
    }
    
    public static boolean cantStandAtWater(final Block block) {
        final Block otherBlock = block.getRelative(BlockFace.DOWN);
        final boolean isHover = block.getType() == Material.AIR;
        final boolean n = otherBlock.getRelative(BlockFace.NORTH).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER;
        final boolean s = otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.WATER || otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.STATIONARY_WATER;
        final boolean e = otherBlock.getRelative(BlockFace.EAST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.EAST).getType() == Material.STATIONARY_WATER;
        final boolean w = otherBlock.getRelative(BlockFace.WEST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.WEST).getType() == Material.STATIONARY_WATER;
        final boolean ne = otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.STATIONARY_WATER;
        final boolean nw = otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.STATIONARY_WATER;
        final boolean se = otherBlock.getRelative(BlockFace.SOUTH_EAST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER;
        final boolean sw = otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.STATIONARY_WATER;
        return n && s && e && w && ne && nw && se && sw && isHover;
    }
    
    public static boolean canStandWithin(final Block block) {
        final boolean isSand = block.getType() == Material.SAND;
        final boolean isGravel = block.getType() == Material.GRAVEL;
        final boolean solid = block.getType().isSolid() && !block.getType().name().toLowerCase().contains("door") && !block.getType().name().toLowerCase().contains("fence") && !block.getType().name().toLowerCase().contains("bars") && !block.getType().name().toLowerCase().contains("sign");
        return !isSand && !isGravel && !solid;
    }
    
    public static Vector getRotation(final Location one, final Location two) {
        final double dx = two.getX() - one.getX();
        final double dy = two.getY() - one.getY();
        final double dz = two.getZ() - one.getZ();
        final double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        final float yaw = (float)(Math.atan2(dz, dx) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(dy, distanceXZ) * 180.0 / 3.141592653589793));
        return new Vector(yaw, pitch, 0.0f);
    }
    
    public static double clamp180(double theta) {
        theta %= 360.0;
        if (theta >= 180.0) {
            theta -= 360.0;
        }
        if (theta < -180.0) {
            theta += 360.0;
        }
        return theta;
    }
    
    public static int getLevelForEnchantment(final Player player, final String enchantment) {
        try {
            final Enchantment theEnchantment = Enchantment.getByName(enchantment);
            ItemStack[] arrayOfItemStack;
            for (int j = (arrayOfItemStack = player.getInventory().getArmorContents()).length, i = 0; i < j; ++i) {
                final ItemStack item = arrayOfItemStack[i];
                if (item.containsEnchantment(theEnchantment)) {
                    return item.getEnchantmentLevel(theEnchantment);
                }
            }
        }
        catch (Exception e) {
            return -1;
        }
        return -1;
    }
    
    public static boolean cantStandAt(final Block block) {
        return !canStand(block) && cantStandClose(block) && cantStandFar(block);
    }
    
    public static boolean cantStandAtExp(final Location location) {
        return cantStandAt(new Location(location.getWorld(), fixXAxis(location.getX()), location.getY() - 0.01, (double)location.getBlockZ()).getBlock());
    }
    
    public static boolean cantStandClose(final Block block) {
        return !canStand(block.getRelative(BlockFace.NORTH)) && !canStand(block.getRelative(BlockFace.EAST)) && !canStand(block.getRelative(BlockFace.SOUTH)) && !canStand(block.getRelative(BlockFace.WEST));
    }
    
    public static boolean cantStandFar(final Block block) {
        return !canStand(block.getRelative(BlockFace.NORTH_WEST)) && !canStand(block.getRelative(BlockFace.NORTH_EAST)) && !canStand(block.getRelative(BlockFace.SOUTH_WEST)) && !canStand(block.getRelative(BlockFace.SOUTH_EAST));
    }
    
    public static boolean canStand(final Block block) {
        return !block.isLiquid() && block.getType() != Material.AIR;
    }
    
    public static boolean isFullyInWater(final Location player) {
        final double touchedX = fixXAxis(player.getX());
        return new Location(player.getWorld(), touchedX, player.getY(), (double)player.getBlockZ()).getBlock().isLiquid() && new Location(player.getWorld(), touchedX, (double)Math.round(player.getY()), (double)player.getBlockZ()).getBlock().isLiquid();
    }
    
    public static double fixXAxis(final double x) {
        double touchedX = x;
        final double rem = touchedX - Math.round(touchedX) + 0.01;
        if (rem < 0.3) {
            touchedX = NumberConversions.floor(x) - 1;
        }
        return touchedX;
    }
    
    public static boolean isOnGround(final Location location, final int down) {
        final double posX = location.getX();
        final double posZ = location.getZ();
        final double fracX = (UtilMath.getFraction(posX) > 0.0) ? Math.abs(UtilMath.getFraction(posX)) : (1.0 - Math.abs(UtilMath.getFraction(posX)));
        final double fracZ = (UtilMath.getFraction(posZ) > 0.0) ? Math.abs(UtilMath.getFraction(posZ)) : (1.0 - Math.abs(UtilMath.getFraction(posZ)));
        final int blockX = location.getBlockX();
        final int blockY = location.getBlockY() - down;
        final int blockZ = location.getBlockZ();
        final World world = location.getWorld();
        if (UtilBlock.isSolid(world.getBlockAt(blockX, blockY, blockZ))) {
            return true;
        }
        if (fracX < 0.3) {
            if (UtilBlock.isSolid(world.getBlockAt(blockX - 1, blockY, blockZ))) {
                return true;
            }
            if (fracZ < 0.3) {
                if (UtilBlock.isSolid(world.getBlockAt(blockX - 1, blockY, blockZ - 1))) {
                    return true;
                }
                if (UtilBlock.isSolid(world.getBlockAt(blockX, blockY, blockZ - 1))) {
                    return true;
                }
                if (UtilBlock.isSolid(world.getBlockAt(blockX + 1, blockY, blockZ - 1))) {
                    return true;
                }
            }
            else if (fracZ > 0.7) {
                if (UtilBlock.isSolid(world.getBlockAt(blockX - 1, blockY, blockZ + 1))) {
                    return true;
                }
                if (UtilBlock.isSolid(world.getBlockAt(blockX, blockY, blockZ + 1))) {
                    return true;
                }
                if (UtilBlock.isSolid(world.getBlockAt(blockX + 1, blockY, blockZ + 1))) {
                    return true;
                }
            }
        }
        else if (fracX > 0.7) {
            if (UtilBlock.isSolid(world.getBlockAt(blockX + 1, blockY, blockZ))) {
                return true;
            }
            if (fracZ < 0.3) {
                if (UtilBlock.isSolid(world.getBlockAt(blockX - 1, blockY, blockZ - 1))) {
                    return true;
                }
                if (UtilBlock.isSolid(world.getBlockAt(blockX, blockY, blockZ - 1))) {
                    return true;
                }
                if (UtilBlock.isSolid(world.getBlockAt(blockX + 1, blockY, blockZ - 1))) {
                    return true;
                }
            }
            else if (fracZ > 0.7) {
                if (UtilBlock.isSolid(world.getBlockAt(blockX - 1, blockY, blockZ + 1))) {
                    return true;
                }
                if (UtilBlock.isSolid(world.getBlockAt(blockX, blockY, blockZ + 1))) {
                    return true;
                }
                if (UtilBlock.isSolid(world.getBlockAt(blockX + 1, blockY, blockZ + 1))) {
                    return true;
                }
            }
        }
        else if (fracZ < 0.3) {
            if (UtilBlock.isSolid(world.getBlockAt(blockX, blockY, blockZ - 1))) {
                return true;
            }
        }
        else if (fracZ > 0.7 && UtilBlock.isSolid(world.getBlockAt(blockX, blockY, blockZ + 1))) {
            return true;
        }
        return false;
    }
    
    public static boolean isHoveringOverWater(final Location player, final int blocks) {
        for (int i = player.getBlockY(); i > player.getBlockY() - blocks; --i) {
            final Block newloc = new Location(player.getWorld(), (double)player.getBlockX(), (double)i, (double)player.getBlockZ()).getBlock();
            if (newloc.getType() != Material.AIR) {
                return newloc.isLiquid();
            }
        }
        return false;
    }
    
    public static boolean isHoveringOverWater(final Location player) {
        return isHoveringOverWater(player, 25);
    }
    
    public static boolean isInstantBreak(final Material m) {
        return UtilCheat.INSTANT_BREAK.contains(m);
    }
    
    public static boolean isFood(final Material m) {
        return UtilCheat.FOOD.contains(m);
    }
    
    public static String getCardinalDirection(final Player player) {
        double rotation = (player.getLocation().getYaw() - 90.0f) % 360.0f;
        if (rotation < 0.0) {
            rotation += 360.0;
        }
        if (0.0 <= rotation && rotation < 22.5) {
            return "N";
        }
        if (22.5 <= rotation && rotation < 67.5) {
            return "NE";
        }
        if (67.5 <= rotation && rotation < 112.5) {
            return "E";
        }
        if (112.5 <= rotation && rotation < 157.5) {
            return "SE";
        }
        if (157.5 <= rotation && rotation < 202.5) {
            return "S";
        }
        if (202.5 <= rotation && rotation < 247.5) {
            return "SW";
        }
        if (247.5 <= rotation && rotation < 292.5) {
            return "W";
        }
        if (292.5 <= rotation && rotation < 337.5) {
            return "NW";
        }
        if (337.5 <= rotation && rotation < 360.0) {
            return "N";
        }
        return null;
    }
    
    public static boolean isSlab(final Block block) {
        final Material type = block.getType();
        switch (type) {
            case STEP:
            case WOOD_STEP: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isStair(final Block block) {
        final Material type = block.getType();
        switch (type) {
            case WOOD_STAIRS:
            case COBBLESTONE_STAIRS:
            case BRICK_STAIRS:
            case SMOOTH_STAIRS:
            case NETHER_BRICK_STAIRS:
            case SANDSTONE_STAIRS:
            case SPRUCE_WOOD_STAIRS:
            case BIRCH_WOOD_STAIRS:
            case JUNGLE_WOOD_STAIRS:
            case COMMAND:
            case QUARTZ_STAIRS:
            case ACACIA_STAIRS:
            case DARK_OAK_STAIRS: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isInteractable(final Material m) {
        return UtilCheat.INTERACTABLE.contains(m);
    }
    
    public static boolean sprintFly(final Player player) {
        return player.isSprinting() || player.isFlying();
    }
    
    public static boolean isOnLilyPad(final Player player) {
        final Block block = player.getLocation().getBlock();
        final Material lily = Material.WATER_LILY;
        return block.getType() == lily || block.getRelative(BlockFace.NORTH).getType() == lily || block.getRelative(BlockFace.SOUTH).getType() == lily || block.getRelative(BlockFace.EAST).getType() == lily || block.getRelative(BlockFace.WEST).getType() == lily;
    }
    
    public static boolean isSubmersed(final Player player) {
        return player.getLocation().getBlock().isLiquid() && player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid();
    }
    
    public static boolean isInWater(final Player player) {
        return player.getLocation().getBlock().isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid();
    }
    
	public static boolean isInWeb(final Player player) {
        return UtilBlock.getBlocksAroundCenter(player.getLocation(), 1).contains(Material.WEB) || player.getLocation().getBlock().getType() == Material.WEB || player.getLocation().clone().add(0.0, 1.0, 0.0).getBlock().getType() == Material.WEB || player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WEB || player.getLocation().getBlock().getRelative(BlockFace.UP).getType() == Material.WEB;
    }
    
    public static boolean isClimbableBlock(final Block block) {
        return block.getType() == Material.VINE || block.getType() == Material.LADDER || block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER;
    }
    
    public static boolean isOnVine(final Player player) {
        return player.getLocation().getBlock().getType() == Material.VINE;
    }
    
    public static boolean isInt(final String string) {
        try {
            Integer.parseInt(string);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public static boolean isDouble(final String string) {
        try {
            Double.parseDouble(string);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public static boolean blocksNear(final Player player) {
        return blocksNear(player.getLocation());
    }
    
    public static boolean blocksNear(final Location loc) {
        boolean nearBlocks = false;
        for (final Block block : UtilBlock.getSurrounding(loc.getBlock(), true)) {
            if (block.getType() != Material.AIR) {
                nearBlocks = true;
                break;
            }
        }
        for (final Block block : UtilBlock.getSurrounding(loc.getBlock(), false)) {
            if (block.getType() != Material.AIR) {
                nearBlocks = true;
                break;
            }
        }
        loc.setY(loc.getY() - 0.5);
        if (loc.getBlock().getType() != Material.AIR) {
            nearBlocks = true;
        }
        if (isBlock(loc.getBlock().getRelative(BlockFace.DOWN), new Material[] { Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER })) {
            nearBlocks = true;
        }
        return nearBlocks;
    }
    
    public static boolean blocksNearB(final Location loc) {
        boolean nearBlocks = false;
        for (final Block block : UtilBlock.getSurroundingB(loc.getBlock())) {
            if (block.getType() != Material.AIR) {
                nearBlocks = true;
                break;
            }
        }
        loc.setY(loc.getY() - 0.5);
        if (loc.getBlock().getType() != Material.AIR) {
            nearBlocks = true;
        }
        if (isBlock(loc.getBlock().getRelative(BlockFace.DOWN), new Material[] { Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER })) {
            nearBlocks = true;
        }
        return nearBlocks;
    }
    
    public static boolean slabsNear(final Location loc) {
        boolean nearBlocks = false;
        for (final Block bl : UtilBlock.getSurrounding(loc.getBlock(), true)) {
            if (bl.getType().equals((Object)Material.STEP) || bl.getType().equals((Object)Material.DOUBLE_STEP) || bl.getType().equals((Object)Material.WOOD_DOUBLE_STEP) || bl.getType().equals((Object)Material.WOOD_STEP)) {
                nearBlocks = true;
                break;
            }
        }
        for (final Block bl : UtilBlock.getSurrounding(loc.getBlock(), false)) {
            if (bl.getType().equals((Object)Material.STEP) || bl.getType().equals((Object)Material.DOUBLE_STEP) || bl.getType().equals((Object)Material.WOOD_DOUBLE_STEP) || bl.getType().equals((Object)Material.WOOD_STEP)) {
                nearBlocks = true;
                break;
            }
        }
        if (isBlock(loc.getBlock().getRelative(BlockFace.DOWN), new Material[] { Material.STEP, Material.DOUBLE_STEP, Material.WOOD_DOUBLE_STEP, Material.WOOD_STEP })) {
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
    
    public static String[] getCommands(final String command) {
        return command.replaceAll("COMMAND\\[", "").replaceAll("]", "").split(";");
    }
    
    public static String removeWhitespace(final String string) {
        return string.replaceAll(" ", "");
    }
    
    public static boolean hasArmorEnchantment(final Player player, final Enchantment e) {
        ItemStack[] arrayOfItemStack;
        for (int j = (arrayOfItemStack = player.getInventory().getArmorContents()).length, i = 0; i < j; ++i) {
            final ItemStack is = arrayOfItemStack[i];
            if (is != null && is.containsEnchantment(e)) {
                return true;
            }
        }
        return false;
    }
    
    public static String listToCommaString(final List<String> list) {
        final StringBuilder b = new StringBuilder();
        for (int i = 0; i < list.size(); ++i) {
            b.append(list.get(i));
            if (i < list.size() - 1) {
                b.append(",");
            }
        }
        return b.toString();
    }
    
    public static long lifeToSeconds(final String string) {
        if (string.equals("0") || string.equals("")) {
            return 0L;
        }
        final String[] lifeMatch = { "d", "h", "m", "s" };
        final int[] lifeInterval = { 86400, 3600, 60, 1 };
        long seconds = 0L;
        for (int i = 0; i < lifeMatch.length; ++i) {
            final Matcher matcher = Pattern.compile("([0-9]*)" + lifeMatch[i]).matcher(string);
            while (matcher.find()) {
                seconds += Integer.parseInt(matcher.group(1)) * lifeInterval[i];
            }
        }
        return seconds;
    }
    
    public static double[] cursor(final Player player, final LivingEntity entity) {
        final Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        final Location playerLoc = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        final Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        final Vector expectedRotation = getRotation(playerLoc, entityLoc);
        final double deltaYaw = clamp180(playerRotation.getX() - expectedRotation.getX());
        final double deltaPitch = clamp180(playerRotation.getY() - expectedRotation.getY());
        final double horizontalDistance = getHorizontalDistance(playerLoc, entityLoc);
        final double distance = getDistance3D(playerLoc, entityLoc);
        final double offsetX = deltaYaw * horizontalDistance * distance;
        final double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;
        return new double[] { Math.abs(offsetX), Math.abs(offsetY) };
    }
    
    public static double getAimbotoffset(final Location playerLocLoc, final double playerEyeHeight, final LivingEntity entity) {
        final Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        final Location playerLoc = playerLocLoc.add(0.0, playerEyeHeight, 0.0);
        final Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        final Vector expectedRotation = getRotation(playerLoc, entityLoc);
        final double deltaYaw = clamp180(playerRotation.getX() - expectedRotation.getX());
        final double horizontalDistance = getHorizontalDistance(playerLoc, entityLoc);
        final double distance = getDistance3D(playerLoc, entityLoc);
        final double offsetX = deltaYaw * horizontalDistance * distance;
        return offsetX;
    }
    
    public static double getAimbotoffset2(final Location playerLocLoc, final double playerEyeHeight, final LivingEntity entity) {
        final Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        final Location playerLoc = playerLocLoc.add(0.0, playerEyeHeight, 0.0);
        final Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        final Vector expectedRotation = getRotation(playerLoc, entityLoc);
        final double deltaPitch = clamp180(playerRotation.getY() - expectedRotation.getY());
        final double distance = getDistance3D(playerLoc, entityLoc);
        final double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;
        return offsetY;
    }
    
    public static double[] getOffsetsOffCursor(final Player player, final LivingEntity entity) {
        final Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        final Location playerLoc = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        final Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        final Vector expectedRotation = getRotation(playerLoc, entityLoc);
        final double deltaYaw = clamp180(playerRotation.getX() - expectedRotation.getX());
        final double deltaPitch = clamp180(playerRotation.getY() - expectedRotation.getY());
        final double horizontalDistance = getHorizontalDistance(playerLoc, entityLoc);
        final double distance = getDistance3D(playerLoc, entityLoc);
        final double offsetX = deltaYaw * horizontalDistance * distance;
        final double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;
        return new double[] { Math.abs(offsetX), Math.abs(offsetY) };
    }
    
    public static double getOffsetOffCursor(final Player player, final LivingEntity entity) {
        double offset = 0.0;
        final double[] offsets = getOffsetsOffCursor(player, entity);
        offset += offsets[0];
        offset += offsets[1];
        return offset;
    }
}
