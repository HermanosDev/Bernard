package net.hermanos.ac.check.other;

import org.bukkit.plugin.messaging.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.utils.*;

import org.bukkit.event.player.*;
import org.bukkit.event.*;
import org.bukkit.entity.*;

public class Vape extends Check implements PluginMessageListener
{
    public Vape(final Bernard Daedalus) {
        super("Vape", "Vape", Daedalus);
        this.setEnabled(true);
        this.setBannable(true);
        this.setMaxViolations(0);
    }
    
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        event.getPlayer().sendMessage("§8 §8 §1 §3 §3 §7 §8 ");
    }
    
    public void onPluginMessageReceived(final String s, final Player player, final byte[] data) {
        try {
            @SuppressWarnings("unused")
			final String str = new String(data);
        }
        catch (Exception ex) {
            @SuppressWarnings("unused")
			final String str = "";
        }
        this.getDaedalus().logCheat(this, player, "Using Cracked Vape!", Chance.HIGH, "Banned");
    }
}
