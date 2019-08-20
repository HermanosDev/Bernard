package net.hermanos.ac.checks.combat;

import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import java.util.*;
import org.bukkit.event.*;

public class ReachA extends Check
{
    public static HashMap<UUID, Integer> toBan;
    
    static {
        ReachA.toBan = new HashMap<UUID, Integer>();
    }
    
    public ReachA(final Bernard Daedalus) {
        super("ReachA", "Reach (Type A)", Daedalus);
        this.setEnabled(true);
        this.setBannable(false);
        this.setViolationsToNotify(1);
        this.setMaxViolations(9);
    }
    
    @EventHandler
    public void onATTACK(final EntityDamageByEntityEvent e) {
        if (!e.getCause().equals((Object)EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            return;
        }
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
            return;
        }
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        if (this.getDaedalus().getLag().getTPS() < this.getDaedalus().getTPSCancel()) {
            return;
        }
        final Player player = (Player)e.getDamager();
        final Player damaged = (Player)e.getEntity();
        if (player.hasPermission("daedalus.bypass")) {
            return;
        }
        final double YawDifference = Math.abs(damaged.getLocation().getYaw() - player.getLocation().getYaw());
        if (player.getAllowFlight()) {
            return;
        }
        double Difference = UtilPlayer.getEyeLocation(player).distance(damaged.getEyeLocation()) - 0.35;
        final int Ping = this.getDaedalus().getLag().getPing(player);
        final double TPS = this.getDaedalus().getLag().getTPS();
        double MaxReach = 3.8 + damaged.getVelocity().length();
        if (player.isSprinting()) {
            MaxReach += 0.1;
        }
        if (player.getLocation().getY() > damaged.getLocation().getY()) {
            Difference = player.getLocation().getY() - player.getLocation().getY();
            MaxReach += Difference / 2.5;
        }
        else if (player.getLocation().getY() > player.getLocation().getY()) {
            Difference = player.getLocation().getY() - player.getLocation().getY();
            MaxReach += Difference / 2.5;
        }
        for (final PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().equals((Object)PotionEffectType.SPEED)) {
                MaxReach += 0.2 * (effect.getAmplifier() + 1);
            }
        }
        MaxReach += ((Ping < 150) ? (Ping * 0.00212) : (Ping * 0.0031));
        MaxReach += YawDifference / 1000.0;
        double ChanceVal = Math.round(Math.abs((Difference - MaxReach) * 100.0));
        if (ChanceVal > 100.0) {
            ChanceVal = 100.0;
        }
        if (MaxReach < Difference) {
            this.dumplog(player, "Logged for Reach Type A; Check is Bannable (so no special bans); Reach: " + Difference + "; MaxReach; " + MaxReach + "; Chance: " + ChanceVal + "%" + "; Ping: " + Ping + "; TPS: " + TPS);
            Chance chance = Chance.LIKELY;
            if (ChanceVal >= 60.0) {
                chance = Chance.HIGH;
            }
            this.getDaedalus().logCheat(this, player, String.valueOf(UtilMath.trim(4, Difference)) + " > " + MaxReach + " Ping:" + Ping + " Yaw Difference: " + YawDifference, chance, "Experimental");
        }
    }
}
