package net.hermanos.ac.checks.combat.movement;

import java.util.*;

import com.google.common.collect.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.listeners.*;
import net.hermanos.ac.utils.*;

import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.block.*;
import org.bukkit.material.*;

public class PhaseA extends Check
{
    public static List<Material> allowed;
    public static List<Material> semi;
    public static Set<UUID> teleported;
    public static final Map<UUID, Location> lastLocation;
    private final ImmutableSet<Material> blockedPearlTypes;
    
    static {
        PhaseA.allowed = new ArrayList<Material>();
        PhaseA.semi = new ArrayList<Material>();
        PhaseA.teleported = new HashSet<UUID>();
        lastLocation = new HashMap<UUID, Location>();
        PhaseA.allowed.add(Material.SIGN);
        PhaseA.allowed.add(Material.SIGN_POST);
        PhaseA.allowed.add(Material.WALL_SIGN);
        PhaseA.allowed.add(Material.SUGAR_CANE_BLOCK);
        PhaseA.allowed.add(Material.WHEAT);
        PhaseA.allowed.add(Material.POTATO);
        PhaseA.allowed.add(Material.CARROT);
        PhaseA.allowed.add(Material.STEP);
        PhaseA.allowed.add(Material.AIR);
        PhaseA.allowed.add(Material.WOOD_STEP);
        PhaseA.allowed.add(Material.SOUL_SAND);
        PhaseA.allowed.add(Material.CARPET);
        PhaseA.allowed.add(Material.STONE_PLATE);
        PhaseA.allowed.add(Material.WOOD_PLATE);
        PhaseA.allowed.add(Material.LADDER);
        PhaseA.allowed.add(Material.CHEST);
        PhaseA.allowed.add(Material.WATER);
        PhaseA.allowed.add(Material.STATIONARY_WATER);
        PhaseA.allowed.add(Material.LAVA);
        PhaseA.allowed.add(Material.STATIONARY_LAVA);
        PhaseA.allowed.add(Material.REDSTONE_COMPARATOR);
        PhaseA.allowed.add(Material.REDSTONE_COMPARATOR_OFF);
        PhaseA.allowed.add(Material.REDSTONE_COMPARATOR_ON);
        PhaseA.allowed.add(Material.IRON_PLATE);
        PhaseA.allowed.add(Material.GOLD_PLATE);
        PhaseA.allowed.add(Material.DAYLIGHT_DETECTOR);
        PhaseA.allowed.add(Material.STONE_BUTTON);
        PhaseA.allowed.add(Material.WOOD_BUTTON);
        PhaseA.allowed.add(Material.HOPPER);
        PhaseA.allowed.add(Material.RAILS);
        PhaseA.allowed.add(Material.ACTIVATOR_RAIL);
        PhaseA.allowed.add(Material.DETECTOR_RAIL);
        PhaseA.allowed.add(Material.POWERED_RAIL);
        PhaseA.allowed.add(Material.TRIPWIRE_HOOK);
        PhaseA.allowed.add(Material.TRIPWIRE);
        PhaseA.allowed.add(Material.SNOW_BLOCK);
        PhaseA.allowed.add(Material.REDSTONE_TORCH_OFF);
        PhaseA.allowed.add(Material.REDSTONE_TORCH_ON);
        PhaseA.allowed.add(Material.DIODE_BLOCK_OFF);
        PhaseA.allowed.add(Material.DIODE_BLOCK_ON);
        PhaseA.allowed.add(Material.DIODE);
        PhaseA.allowed.add(Material.SEEDS);
        PhaseA.allowed.add(Material.MELON_SEEDS);
        PhaseA.allowed.add(Material.PUMPKIN_SEEDS);
        PhaseA.allowed.add(Material.DOUBLE_PLANT);
        PhaseA.allowed.add(Material.LONG_GRASS);
        PhaseA.allowed.add(Material.WEB);
        PhaseA.allowed.add(Material.SNOW);
        PhaseA.allowed.add(Material.FLOWER_POT);
        PhaseA.allowed.add(Material.BREWING_STAND);
        PhaseA.allowed.add(Material.CAULDRON);
        PhaseA.allowed.add(Material.CACTUS);
        PhaseA.allowed.add(Material.WATER_LILY);
        PhaseA.allowed.add(Material.RED_ROSE);
        PhaseA.allowed.add(Material.ENCHANTMENT_TABLE);
        PhaseA.allowed.add(Material.ENDER_PORTAL_FRAME);
        PhaseA.allowed.add(Material.PORTAL);
        PhaseA.allowed.add(Material.ENDER_PORTAL);
        PhaseA.allowed.add(Material.ENDER_CHEST);
        PhaseA.allowed.add(Material.NETHER_FENCE);
        PhaseA.allowed.add(Material.NETHER_WARTS);
        PhaseA.allowed.add(Material.REDSTONE_WIRE);
        PhaseA.allowed.add(Material.LEVER);
        PhaseA.allowed.add(Material.YELLOW_FLOWER);
        PhaseA.allowed.add(Material.CROPS);
        PhaseA.allowed.add(Material.WATER);
        PhaseA.allowed.add(Material.LAVA);
        PhaseA.allowed.add(Material.SKULL);
        PhaseA.allowed.add(Material.TRAPPED_CHEST);
        PhaseA.allowed.add(Material.FIRE);
        PhaseA.allowed.add(Material.BROWN_MUSHROOM);
        PhaseA.allowed.add(Material.RED_MUSHROOM);
        PhaseA.allowed.add(Material.DEAD_BUSH);
        PhaseA.allowed.add(Material.SAPLING);
        PhaseA.allowed.add(Material.TORCH);
        PhaseA.allowed.add(Material.MELON_STEM);
        PhaseA.allowed.add(Material.PUMPKIN_STEM);
        PhaseA.allowed.add(Material.COCOA);
        PhaseA.allowed.add(Material.BED);
        PhaseA.allowed.add(Material.BED_BLOCK);
        PhaseA.allowed.add(Material.PISTON_EXTENSION);
        PhaseA.allowed.add(Material.PISTON_MOVING_PIECE);
        PhaseA.semi.add(Material.IRON_FENCE);
        PhaseA.semi.add(Material.THIN_GLASS);
        PhaseA.semi.add(Material.STAINED_GLASS_PANE);
        PhaseA.semi.add(Material.COBBLE_WALL);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public PhaseA(final Bernard Daedalus) {
        super("Phase", "Phase", Daedalus);
        this.blockedPearlTypes = (ImmutableSet<Material>)Sets.immutableEnumSet((Enum)Material.THIN_GLASS, (Enum[])new Material[] { Material.IRON_FENCE, Material.FENCE, Material.NETHER_FENCE, Material.FENCE_GATE, Material.ACACIA_STAIRS, Material.BIRCH_WOOD_STAIRS, Material.BRICK_STAIRS, Material.COBBLESTONE_STAIRS, Material.DARK_OAK_STAIRS, Material.JUNGLE_WOOD_STAIRS, Material.NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.SANDSTONE_STAIRS, Material.SMOOTH_STAIRS, Material.SPRUCE_WOOD_STAIRS, Material.WOOD_STAIRS });
        this.setEnabled(true);
        this.setBannable(false);
        this.setMaxViolations(40);
        this.setViolationsToNotify(2);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void teleport(final PlayerTeleportEvent e) {
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            PhaseA.teleported.add(e.getPlayer().getUniqueId());
        }
    }
    
    @EventHandler
    public void death(final PlayerDeathEvent e) {
        PhaseA.teleported.add(e.getEntity().getUniqueId());
    }
    
    @EventHandler
    public void respawn(final PlayerRespawnEvent e) {
        PhaseA.teleported.add(e.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void update(final PlayerMoveEvent e) {
        final Player player = e.getPlayer();
        if (player.isDead()) {
            return;
        }
        final UUID playerId = player.getUniqueId();
        final Location loc1 = PhaseA.lastLocation.containsKey(playerId) ? PhaseA.lastLocation.get(playerId) : player.getLocation();
        final Location loc2 = player.getLocation();
        if (player.getAllowFlight()) {
            PhaseA.teleported.add(player.getUniqueId());
        }
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            PhaseA.teleported.add(player.getUniqueId());
        }
        if (loc1.getWorld() == loc2.getWorld() && !PhaseA.teleported.contains(playerId) && loc1.distance(loc2) > 10.0) {
            player.teleport((Location)PhaseA.lastLocation.get(playerId), PlayerTeleportEvent.TeleportCause.PLUGIN);
            if (player.getLocation().getBlock().getType().isSolid() || (player.getLocation().clone().add(0.0, 1.0, 0.0).getBlock().getType().isSolid() && player.getVehicle() == null && !player.hasPermission("daedalus.bypass"))) {
                player.teleport((Location)PhaseA.lastLocation.get(playerId), PlayerTeleportEvent.TeleportCause.PLUGIN);
                return;
            }
            this.getDaedalus().logCheat(this, player, null, Chance.LIKELY, new String[0]);
        }
        else if (this.isLegit(playerId, loc1, loc2)) {
            PhaseA.lastLocation.put(playerId, loc2);
        }
        else if (player.hasPermission("daedalus.admin") || PhaseA.lastLocation.containsKey(playerId)) {
            player.teleport((Location)PhaseA.lastLocation.get(playerId), PlayerTeleportEvent.TeleportCause.PLUGIN);
            if (player.getLocation().getBlock().getType().isSolid() || (player.getLocation().clone().add(0.0, 1.0, 0.0).getBlock().getType().isSolid() && player.getVehicle() == null && !player.hasPermission("daedalus.bypass"))) {
                player.teleport((Location)PhaseA.lastLocation.get(playerId), PlayerTeleportEvent.TeleportCause.PLUGIN);
                return;
            }
            this.getDaedalus().logCheat(this, player, null, Chance.LIKELY, new String[0]);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (!this.getDaedalus().getConfig().getBoolean("checks.Phase.pearlFix")) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasItem() && event.getItem().getType() == Material.ENDER_PEARL) {
            final Block block = event.getClickedBlock();
            if (block.getType().isSolid() && this.blockedPearlTypes.contains((Object)block.getType()) && !(block.getState() instanceof InventoryHolder)) {
                final PearlGlitchEvent event2 = new PearlGlitchEvent(event.getPlayer(), event.getPlayer().getLocation(), event.getPlayer().getLocation(), event.getPlayer().getItemInHand(), PearlGlitchType.INTERACT);
                Bukkit.getPluginManager().callEvent((Event)event2);
                if (!event2.isCancelled()) {
                    event.setCancelled(true);
                    final Player player = event.getPlayer();
                    player.setItemInHand(event.getItem());
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPearlClip(final PlayerTeleportEvent event) {
        if (!this.getDaedalus().getConfig().getBoolean("checks.Phase.pearlFix")) {
            return;
        }
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            final Location to = event.getTo();
            if (this.blockedPearlTypes.contains((Object)to.getBlock().getType()) && to.getBlock().getType() != Material.FENCE_GATE && to.getBlock().getType() != Material.TRAP_DOOR) {
                final PearlGlitchEvent event2 = new PearlGlitchEvent(event.getPlayer(), event.getFrom(), event.getTo(), event.getPlayer().getItemInHand(), PearlGlitchType.TELEPORT);
                Bukkit.getPluginManager().callEvent((Event)event2);
                if (!event2.isCancelled()) {
                    final Player player = event.getPlayer();
                    player.sendMessage(String.valueOf(this.getDaedalus().PREFIX) + C.Red + "You have been detected trying to pearl glitch, therefore your pearl was cancelled.");
                    event.setCancelled(true);
                }
                return;
            }
            to.setX(to.getBlockX() + 0.5);
            to.setZ(to.getBlockZ() + 0.5);
            if ((!PhaseA.allowed.contains(to.getBlock().getType()) || !PhaseA.allowed.contains(to.clone().add(0.0, 1.0, 0.0).getBlock().getType())) && (to.getBlock().getType().isSolid() || to.clone().add(0.0, 1.0, 0.0).getBlock().getType().isSolid()) && (to.clone().subtract(0.0, 1.0, 0.0).getBlock().getType().isSolid() & !UtilCheat.isSlab(to.getBlock()))) {
                final Player player2 = event.getPlayer();
                final PearlGlitchEvent event3 = new PearlGlitchEvent(player2, event.getFrom(), event.getTo(), event.getPlayer().getItemInHand(), PearlGlitchType.SAFE_LOCATION);
                Bukkit.getPluginManager().callEvent((Event)event3);
                if (!event3.isCancelled()) {
                    event.setCancelled(true);
                    player2.sendMessage(String.valueOf(this.getDaedalus().PREFIX) + C.Red + "Could not find a safe location, therefore your pearl was cancelled.");
                }
                return;
            }
            if (!PhaseA.allowed.contains(to.clone().add(0.0, 1.0, 0.0).getBlock().getType()) && to.clone().add(0.0, 1.0, 0.0).getBlock().getType().isSolid() && !to.getBlock().getType().isSolid()) {
                to.setY(to.getY() - 0.7);
            }
            event.setTo(to);
        }
    }
    
    public boolean isLegit(final UUID playerId, final Location loc1, final Location loc2) {
        if (loc1.getWorld() != loc2.getWorld()) {
            return true;
        }
        if (PhaseA.teleported.remove(playerId)) {
            return true;
        }
        int moveMaxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        final int moveMinX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        final int moveMaxY = Math.max(loc1.getBlockY(), loc2.getBlockY()) + 1;
        int moveMinY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        final int moveMaxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        final int moveMinZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        if (moveMaxY > 256) {
            moveMaxX = 256;
        }
        if (moveMinY > 256) {
            moveMinY = 256;
        }
        for (int x = moveMinX; x <= moveMaxX; ++x) {
            for (int z = moveMinZ; z <= moveMaxZ; ++z) {
                for (int y = moveMinY; y <= moveMaxY; ++y) {
                    final Block block = loc1.getWorld().getBlockAt(x, y, z);
                    if ((y != moveMinY || loc1.getBlockY() == loc2.getBlockY()) && this.hasPhased(block, loc1, loc2, Bukkit.getPlayer(playerId))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    @SuppressWarnings("deprecation")
	private boolean hasPhased(final Block block, final Location loc1, final Location loc2, final Player p) {
        if (PhaseA.allowed.contains(block.getType()) || UtilCheat.isStair(block) || UtilCheat.isSlab(block) || UtilCheat.isClimbableBlock(block) || block.isLiquid()) {
            return false;
        }
        final double moveMaxX = Math.max(loc1.getX(), loc2.getX());
        final double moveMinX = Math.min(loc1.getX(), loc2.getX());
        final double moveMaxY = Math.max(loc1.getY(), loc2.getY()) + 1.8;
        final double moveMinY = Math.min(loc1.getY(), loc2.getY());
        final double moveMaxZ = Math.max(loc1.getZ(), loc2.getZ());
        final double moveMinZ = Math.min(loc1.getZ(), loc2.getZ());
        double blockMaxX = block.getLocation().getBlockX() + 1;
        double blockMinX = block.getLocation().getBlockX();
        double blockMaxY = block.getLocation().getBlockY() + 2;
        double blockMinY = block.getLocation().getBlockY();
        double blockMaxZ = block.getLocation().getBlockZ() + 1;
        double blockMinZ = block.getLocation().getBlockZ();
        if (blockMinY > moveMinY) {
            --blockMaxY;
        }
        if (block.getType().equals((Object)Material.IRON_DOOR_BLOCK) || block.getType().equals((Object)Material.WOODEN_DOOR)) {
            final Door door = (Door)block.getType().getNewData(block.getData());
            if (door.isTopHalf()) {
                return false;
            }
            BlockFace facing = door.getFacing();
            if (door.isOpen()) {
                final Block up = block.getRelative(BlockFace.UP);
                if (!up.getType().equals((Object)Material.IRON_DOOR_BLOCK) && !up.getType().equals((Object)Material.WOODEN_DOOR)) {
                    return false;
                }
                final boolean hinge = (up.getData() & 0x1) == 0x1;
                if (facing == BlockFace.NORTH) {
                    facing = (hinge ? BlockFace.WEST : BlockFace.EAST);
                }
                else if (facing == BlockFace.EAST) {
                    facing = (hinge ? BlockFace.NORTH : BlockFace.SOUTH);
                }
                else if (facing == BlockFace.SOUTH) {
                    facing = (hinge ? BlockFace.EAST : BlockFace.WEST);
                }
                else {
                    facing = (hinge ? BlockFace.SOUTH : BlockFace.NORTH);
                }
            }
            if (facing == BlockFace.WEST) {
                blockMaxX -= 0.8;
            }
            if (facing == BlockFace.EAST) {
                blockMinX += 0.8;
            }
            if (facing == BlockFace.NORTH) {
                blockMaxZ -= 0.8;
            }
            if (facing == BlockFace.SOUTH) {
                blockMinZ += 0.8;
            }
        }
        else if (block.getType().equals((Object)Material.FENCE_GATE)) {
            if (((Gate)block.getType().getNewData(block.getData())).isOpen()) {
                return false;
            }
            final BlockFace face = ((Directional)block.getType().getNewData(block.getData())).getFacing();
            if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
                blockMaxX -= 0.2;
                blockMinX += 0.2;
            }
            else {
                blockMaxZ -= 0.2;
                blockMinZ += 0.2;
            }
        }
        else if (block.getType().equals((Object)Material.TRAP_DOOR)) {
            final TrapDoor door2 = (TrapDoor)block.getType().getNewData(block.getData());
            if (door2.isOpen()) {
                return false;
            }
            if (door2.isInverted()) {
                blockMinY += 0.85;
            }
            else {
                blockMaxY -= ((blockMinY > moveMinY) ? 0.85 : 1.85);
            }
        }
        else if (block.getType().equals((Object)Material.FENCE) || PhaseA.semi.contains(block.getType())) {
            blockMaxX -= 0.2;
            blockMinX += 0.2;
            blockMaxZ -= 0.2;
            blockMinZ += 0.2;
            if ((moveMaxX > blockMaxX && moveMinX > blockMaxX && moveMaxZ > blockMaxZ && moveMinZ > blockMaxZ) || (moveMaxX < blockMinX && moveMinX < blockMinX && moveMaxZ > blockMaxZ && moveMinZ > blockMaxZ) || (moveMaxX > blockMaxX && moveMinX > blockMaxX && moveMaxZ < blockMinZ && moveMinZ < blockMinZ) || (moveMaxX < blockMinX && moveMinX < blockMinX && moveMaxZ < blockMinZ && moveMinZ < blockMinZ)) {
                return false;
            }
            if (block.getRelative(BlockFace.EAST).getType() == block.getType()) {
                blockMaxX += 0.2;
            }
            if (block.getRelative(BlockFace.WEST).getType() == block.getType()) {
                blockMinX -= 0.2;
            }
            if (block.getRelative(BlockFace.SOUTH).getType() == block.getType()) {
                blockMaxZ += 0.2;
            }
            if (block.getRelative(BlockFace.NORTH).getType() == block.getType()) {
                blockMinZ -= 0.2;
            }
        }
        final boolean x = loc1.getX() < loc2.getX();
        final boolean y = loc1.getY() < loc2.getY();
        final boolean z = loc1.getZ() < loc2.getZ();
        final double distance = loc1.distance(loc2) - Math.abs(loc1.getY() - loc2.getY());
        return (distance > 0.5 && block.getType().isSolid()) || (moveMinX != moveMaxX && moveMinY <= blockMaxY && moveMaxY >= blockMinY && moveMinZ <= blockMaxZ && moveMaxZ >= blockMinZ && ((x && moveMinX <= blockMinX && moveMaxX >= blockMinX) || (!x && moveMinX <= blockMaxX && moveMaxX >= blockMaxX))) || (moveMinY != moveMaxY && moveMinX <= blockMaxX && moveMaxX >= blockMinX && moveMinZ <= blockMaxZ && moveMaxZ >= blockMinZ && ((y && moveMinY <= blockMinY && moveMaxY >= blockMinY) || (!y && moveMinY <= blockMaxY && moveMaxY >= blockMaxY))) || (moveMinZ != moveMaxZ && moveMinX <= blockMaxX && moveMaxX >= blockMinX && moveMinY <= blockMaxY && moveMaxY >= blockMinY && ((z && moveMinZ <= blockMinZ && moveMaxZ >= blockMinZ) || (!z && moveMinZ <= blockMaxZ && moveMaxZ >= blockMaxZ)));
    }
}
