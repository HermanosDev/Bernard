package net.hermanos.ac.checks.combat;

import java.util.*;
import org.bukkit.event.entity.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import org.bukkit.entity.*;
import org.bukkit.event.*;

public class KillauraG extends Check
{
    private Map<UUID, Integer> verbose;
    
    public KillauraG(final Bernard Daedalus) {
        super("KillauraG", "Killaura (Type G)", Daedalus);
        this.setEnabled(true);
        this.setBannable(true);
        this.verbose = new HashMap<UUID, Integer>();
    }
    
    @EventHandler
    public void onHit(final EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        final Player player = (Player)e.getDamager();
        int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
        if (player.isDead()) {
            ++verbose;
        }
        else if (this.verbose.containsKey(player.getUniqueId())) {
            this.verbose.remove(player.getUniqueId());
            return;
        }
        if (verbose > 1) {
            verbose = 0;
            this.getDaedalus().logCheat(this, player, "Hit another player while dead.", Chance.HIGH, new String[0]);
        }
        this.verbose.put(player.getUniqueId(), verbose);
    }
}
