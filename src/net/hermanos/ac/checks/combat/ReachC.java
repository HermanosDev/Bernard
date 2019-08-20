package net.hermanos.ac.checks.combat;

import org.bukkit.entity.*;

import java.util.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.*;

import com.comphenix.protocol.wrappers.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.check.other.*;
import net.hermanos.ac.packets.events.*;
import net.hermanos.ac.utils.*;

public class ReachC extends Check
{
    private Map<Player, Map.Entry<Double, Double>> offsets;
    private Map<Player, Long> reachTicks;
    private ArrayList<Player> projectileHit;
    
    public ReachC(final Bernard Daedalus) {
        super("ReachC", "Reach (Type C)", Daedalus);
        this.offsets = new HashMap<Player, Map.Entry<Double, Double>>();
        this.reachTicks = new HashMap<Player, Long>();
        this.projectileHit = new ArrayList<Player>();
        this.setEnabled(true);
        this.setBannable(true);
        this.setMaxViolations(5);
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        if (event.getFrom().getX() == event.getTo().getX() && event.getFrom().getZ() == event.getTo().getZ()) {
            return;
        }
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        final double OffsetXZ = UtilMath.offset(UtilMath.getHorizontalVector(event.getFrom().toVector()), UtilMath.getHorizontalVector(event.getTo().toVector()));
        final double horizontal = Math.sqrt(Math.pow(event.getTo().getX() - event.getFrom().getX(), 2.0) + Math.pow(event.getTo().getZ() - event.getFrom().getZ(), 2.0));
        this.offsets.put(event.getPlayer(), new AbstractMap.SimpleEntry<Double, Double>(OffsetXZ, horizontal));
    }
    
    @EventHandler
    public void onDmg(final EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (e.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
            return;
        }
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        final Player player = (Player)e.getDamager();
        this.projectileHit.add(player);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogout(final PlayerQuitEvent e) {
        if (this.offsets.containsKey(e.getPlayer())) {
            this.offsets.remove(e.getPlayer());
        }
        if (this.reachTicks.containsKey(e.getPlayer())) {
            this.reachTicks.remove(e.getPlayer());
        }
        if (this.projectileHit.contains(e.getPlayer())) {
            this.projectileHit.remove(e.getPlayer());
        }
    }
    
    @EventHandler
    public void onDamage(final PacketUseEntityEvent e) {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK) {
            return;
        }
        if (!(e.getAttacked() instanceof Player)) {
            return;
        }
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        if (e.getAttacker().getAllowFlight()) {
            return;
        }
        if (this.getDaedalus().getLag().getTPS() < this.getDaedalus().getTPSCancel()) {
            return;
        }
        final Player damager = e.getAttacker();
        final Player player = (Player)e.getAttacked();
        final double ydist = Math.abs(damager.getEyeLocation().getY() - player.getEyeLocation().getY());
        double Reach = UtilMath.trim(2, UtilPlayer.getEyeLocation(damager).distance(player.getEyeLocation()) - ydist - 0.32);
        final int PingD = this.getDaedalus().getLag().getPing(damager);
        final int PingP = this.getDaedalus().getLag().getPing(player);
        long attackTime = System.currentTimeMillis();
        if (this.reachTicks.containsKey(damager)) {
            attackTime = this.reachTicks.get(damager);
        }
        final double yawdif = Math.abs(180.0f - Math.abs(damager.getLocation().getYaw() - player.getLocation().getYaw()));
        if (Latency.getLag(damager) > 92 || Latency.getLag(player) > 92) {
            return;
        }
        double offsetsp = 0.0;
        double lastHorizontal = 0.0;
        double offsetsd = 0.0;
        if (this.offsets.containsKey(damager)) {
            offsetsd = this.offsets.get(damager).getKey();
            lastHorizontal = this.offsets.get(damager).getValue();
        }
        if (this.offsets.containsKey(player)) {
            offsetsp = this.offsets.get(player).getKey();
            lastHorizontal = this.offsets.get(player).getValue();
        }
        Reach -= UtilMath.trim(2, offsetsd);
        Reach -= UtilMath.trim(2, offsetsp);
        double maxReach2 = 3.1;
        if (yawdif > 90.0) {
            maxReach2 += 0.38;
        }
        maxReach2 += lastHorizontal * 0.87;
        maxReach2 += (PingD + PingP) / 2 * 0.0024;
        if (Reach > maxReach2 && UtilTime.elapsed(attackTime, 1100L) && !this.projectileHit.contains(player)) {
            Chance chance = Chance.LIKELY;
            if (Reach - maxReach2 > 0.4) {
                chance = Chance.HIGH;
            }
            this.dumplog(damager, "Logged for Reach Type C (First Hit Reach) " + Reach + " > " + maxReach2 + " blocks. Ping: " + this.getDaedalus().getLag().getPing(damager) + " TPS: " + this.getDaedalus().getLag().getTPS() + " Elapsed: " + UtilTime.elapsed(attackTime));
            this.getDaedalus().logCheat(this, damager, "(First Hit Reach) Range: " + Reach + " > " + maxReach2 + " Ping: " + this.getDaedalus().getLag().getPing(damager), chance, new String[0]);
        }
        this.reachTicks.put(damager, UtilTime.nowlong());
        this.projectileHit.remove(player);
    }
}
