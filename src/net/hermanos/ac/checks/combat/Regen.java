package net.hermanos.ac.checks.combat;

import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.*;

import java.util.*;
import org.bukkit.event.*;

public class Regen extends Check
{
    public static Map<UUID, Long> LastHeal;
    public static Map<UUID, Map.Entry<Integer, Long>> FastHealTicks;
    
    static {
        Regen.LastHeal = new HashMap<UUID, Long>();
        Regen.FastHealTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    }
    
    public Regen(final Bernard Daedalus) {
        super("Regen", "Regen", Daedalus);
        this.setEnabled(true);
        this.setBannable(true);
        this.setViolationsToNotify(3);
        this.setMaxViolations(12);
        this.setViolationResetTime(60000L);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final UUID uuid = p.getUniqueId();
        if (Regen.LastHeal.containsKey(uuid)) {
            Regen.LastHeal.remove(uuid);
        }
        if (Regen.FastHealTicks.containsKey(uuid)) {
            Regen.FastHealTicks.remove(uuid);
        }
    }
    
    public boolean checkFastHeal(final Player player) {
        if (Regen.LastHeal.containsKey(player.getUniqueId())) {
            final long l = Regen.LastHeal.get(player.getUniqueId());
            Regen.LastHeal.remove(player.getUniqueId());
            if (System.currentTimeMillis() - l < 3000L) {
                return true;
            }
        }
        return false;
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onHeal(final EntityRegainHealthEvent event) {
        if (!event.getRegainReason().equals((Object)EntityRegainHealthEvent.RegainReason.SATIATED)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (this.getDaedalus().getLag().getTPS() < this.getDaedalus().getTPSCancel()) {
            return;
        }
        final Player player = (Player)event.getEntity();
        if (player.hasPermission("daedalus.bypass")) {
            return;
        }
        if (player.getWorld().getDifficulty().equals((Object)Difficulty.PEACEFUL)) {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (Regen.FastHealTicks.containsKey(player.getUniqueId())) {
            Count = Regen.FastHealTicks.get(player.getUniqueId()).getKey();
            Time = Regen.FastHealTicks.get(player.getUniqueId()).getValue();
        }
        if (this.checkFastHeal(player) && !UtilPlayer.isFullyStuck(player) && !UtilPlayer.isPartiallyStuck(player)) {
            ++Count;
        }
        else {
            Count = ((Count > 0) ? (Count - 1) : Count);
        }
        if (Count > 2) {
            this.getDaedalus().logCheat(this, player, null, Chance.HIGH, new String[0]);
        }
        if (Regen.FastHealTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 60000L)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        Regen.LastHeal.put(player.getUniqueId(), System.currentTimeMillis());
        Regen.FastHealTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}
