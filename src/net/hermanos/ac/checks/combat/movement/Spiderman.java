package net.hermanos.ac.checks.combat.movement;

import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.potion.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.event.*;

public class Spiderman extends Check
{
    private Map<UUID, Map.Entry<Long, Double>> AscensionTicks;
    
    public Spiderman(final Bernard Daedalus) {
        super("WallClimb", "WallClimb", Daedalus);
        this.AscensionTicks = new HashMap<UUID, Map.Entry<Long, Double>>();
        this.setBannable(false);
        this.setEnabled(true);
        this.setMaxViolations(5);
    }
    
    @EventHandler
    public void CheckSpider(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (event.getFrom().getY() >= event.getTo().getY()) {
            return;
        }
        if (!this.getDaedalus().isEnabled()) {
            return;
        }
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        if (player.getVehicle() != null) {
            return;
        }
        if (!UtilBlock.isInAir(player)) {
            return;
        }
        if (this.getDaedalus().LastVelocity.containsKey(player.getUniqueId())) {
            return;
        }
        long Time = System.currentTimeMillis();
        double TotalBlocks = 0.0;
        if (this.AscensionTicks.containsKey(player.getUniqueId())) {
            Time = this.AscensionTicks.get(player.getUniqueId()).getKey();
            TotalBlocks = this.AscensionTicks.get(player.getUniqueId()).getValue();
        }
        final long MS = System.currentTimeMillis() - Time;
        final double OffsetY = UtilMath.offset(UtilMath.getVerticalVector(event.getFrom().toVector()), UtilMath.getVerticalVector(event.getTo().toVector()));
        boolean ya = false;
        final List<Material> Types = new ArrayList<Material>();
        Types.add(player.getLocation().getBlock().getRelative(BlockFace.SOUTH).getType());
        Types.add(player.getLocation().getBlock().getRelative(BlockFace.NORTH).getType());
        Types.add(player.getLocation().getBlock().getRelative(BlockFace.WEST).getType());
        Types.add(player.getLocation().getBlock().getRelative(BlockFace.EAST).getType());
        for (final Material Type : Types) {
            if (Type.isSolid() && Type != Material.LADDER && Type != Material.VINE) {
                ya = true;
                break;
            }
        }
        if (OffsetY > 0.0) {
            TotalBlocks += OffsetY;
        }
        if (!ya || !UtilCheat.blocksNear(player)) {
            TotalBlocks = 0.0;
        }
        if (ya && (event.getFrom().getY() > event.getTo().getY() || UtilPlayer.isOnGround(player))) {
            TotalBlocks = 0.0;
        }
        double Limit = 0.5;
        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            for (final PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().equals((Object)PotionEffectType.JUMP)) {
                    final int level = effect.getAmplifier() + 1;
                    Limit += Math.pow(level + 4.2, 2.0) / 16.0;
                    break;
                }
            }
        }
        if (ya && TotalBlocks > Limit) {
            if (MS > 500L) {
                this.getDaedalus().logCheat(this, player, null, Chance.LIKELY, new String[0]);
                Time = System.currentTimeMillis();
            }
        }
        else {
            Time = System.currentTimeMillis();
        }
        this.AscensionTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Long, Double>(Time, TotalBlocks));
    }
}
