package net.hermanos.ac.checks.combat;

import org.bukkit.entity.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.check.other.*;
import net.hermanos.ac.utils.*;

import java.util.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.*;

public class ReachB extends Check
{
    public Map<Player, Integer> count;
    public Map<Player, Map.Entry<Double, Double>> offsets;
    
    public ReachB(final Bernard Daedalus) {
        super("ReachB", "Reach (Type B)", Daedalus);
        this.setEnabled(true);
        this.setMaxViolations(7);
        this.setBannable(true);
        this.setViolationsToNotify(1);
        this.offsets = new WeakHashMap<Player, Map.Entry<Double, Double>>();
        this.count = new WeakHashMap<Player, Integer>();
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        if (event.getFrom().getX() == event.getTo().getX() && event.getFrom().getZ() == event.getTo().getZ()) {
            return;
        }
        final double OffsetXZ = UtilMath.offset(UtilMath.getHorizontalVector(event.getFrom().toVector()), UtilMath.getHorizontalVector(event.getTo().toVector()));
        final double horizontal = Math.sqrt(Math.pow(event.getTo().getX() - event.getFrom().getX(), 2.0) + Math.pow(event.getTo().getZ() - event.getFrom().getZ(), 2.0));
        this.offsets.put(event.getPlayer(), new AbstractMap.SimpleEntry<Double, Double>(OffsetXZ, horizontal));
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(final EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        final Player damager = (Player)e.getDamager();
        final Player player = (Player)e.getEntity();
        final double Reach = UtilMath.trim(2, UtilPlayer.getEyeLocation(damager).distance(player.getEyeLocation()) - 0.32);
        final double Reach2 = UtilMath.trim(2, UtilPlayer.getEyeLocation(damager).distance(player.getEyeLocation()) - 0.32);
        if (this.getDaedalus().getLag().getTPS() < this.getDaedalus().getTPSCancel()) {
            return;
        }
        if (damager.getAllowFlight()) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        if (!this.count.containsKey(damager)) {
            this.count.put(damager, 0);
        }
        final int Count = this.count.get(damager);
        long Time = System.currentTimeMillis();
        double MaxReach = 3.1;
        final double YawDifference = Math.abs(damager.getEyeLocation().getYaw() - player.getEyeLocation().getYaw());
        double speedToVelocityDif = 0.0;
        double offsets = 0.0;
        double lastHorizontal = 0.0;
        if (this.offsets.containsKey(damager)) {
            offsets = this.offsets.get(damager).getKey();
            lastHorizontal = this.offsets.get(damager).getValue();
        }
        if (Latency.getLag(damager) > 92 || Latency.getLag(player) > 92) {
            return;
        }
        speedToVelocityDif = Math.abs(offsets - player.getVelocity().length());
        MaxReach += YawDifference * 0.001;
        MaxReach += lastHorizontal * 1.5;
        MaxReach += speedToVelocityDif * 0.08;
        if (damager.getLocation().getY() > player.getLocation().getY()) {
            final double Difference = damager.getLocation().getY() - player.getLocation().getY();
            MaxReach += Difference / 2.5;
        }
        else if (player.getLocation().getY() > damager.getLocation().getY()) {
            final double Difference = player.getLocation().getY() - damager.getLocation().getY();
            MaxReach += Difference / 2.5;
        }
        MaxReach += ((damager.getWalkSpeed() <= 0.2) ? 0.0 : (damager.getWalkSpeed() - 0.2));
        final int PingD = this.getDaedalus().getLag().getPing(damager);
        final int PingP = this.getDaedalus().getLag().getPing(player);
        MaxReach += (PingD + PingP) / 2 * 0.0024;
        if (UtilTime.elapsed(Time, 10000L)) {
            this.count.remove(damager);
            Time = System.currentTimeMillis();
        }
        if (Reach > MaxReach) {
            this.dumplog(damager, "Count Increase (+1); Reach: " + Reach2 + ", MaxReach: " + MaxReach + ", Damager Velocity: " + damager.getVelocity().length() + ", " + "Player Velocity: " + player.getVelocity().length() + "; New Count: " + Count);
            this.count.put(damager, Count + 1);
        }
        else if (Count >= -2) {
            this.count.put(damager, Count - 1);
        }
        if (Reach2 > 6.0) {
            e.setCancelled(true);
        }
        if (Count >= 2 && Reach > MaxReach && Reach < 20.0) {
            this.count.remove(damager);
            if (Latency.getLag(player) < 115) {
                this.getDaedalus().logCheat(this, damager, String.valueOf(Reach) + " > " + MaxReach + " MS: " + PingD + " Velocity Difference: " + speedToVelocityDif, Chance.HIGH, new String[0]);
            }
            this.dumplog(damager, "Logged for Reach" + Reach2 + " > " + MaxReach);
        }
    }
}
