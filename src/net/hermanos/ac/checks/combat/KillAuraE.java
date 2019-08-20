package net.hermanos.ac.checks.combat;

import org.bukkit.entity.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import org.bukkit.event.entity.*;

import java.util.*;
import org.bukkit.event.*;

public class KillAuraE extends Check
{
    public static Map<Player, Map.Entry<Integer, Long>> lastAttack;
    
    public KillAuraE(final Bernard Daedalus) {
        super("KillAuraE", "Kill Aura (MultiAura)", Daedalus);
        KillAuraE.lastAttack = new HashMap<Player, Map.Entry<Integer, Long>>();
        this.setEnabled(true);
        this.setBannable(false);
        this.setViolationsToNotify(2);
        this.setMaxViolations(7);
        this.setViolationResetTime(1800000L);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        if (KillAuraE.lastAttack.containsKey(e.getPlayer())) {
            KillAuraE.lastAttack.remove(e.getPlayer());
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void Damage(final EntityDamageByEntityEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (this.getDaedalus().isSotwMode()) {
            return;
        }
        final Player player = (Player)e.getDamager();
        if (KillAuraE.lastAttack.containsKey(player)) {
            final Integer entityid = KillAuraE.lastAttack.get(player).getKey();
            final Long time = KillAuraE.lastAttack.get(player).getValue();
            if (entityid != e.getEntity().getEntityId() && System.currentTimeMillis() - time < 6L) {
                this.getDaedalus().logCheat(this, player, null, Chance.LIKELY, new String[0]);
            }
            KillAuraE.lastAttack.remove(player);
        }
        else {
            KillAuraE.lastAttack.put(player, new AbstractMap.SimpleEntry<Integer, Long>(e.getEntity().getEntityId(), System.currentTimeMillis()));
        }
    }
}
