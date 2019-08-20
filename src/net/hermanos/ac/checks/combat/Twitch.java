package net.hermanos.ac.checks.combat;

import org.bukkit.event.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.packets.events.*;
import net.hermanos.ac.utils.*;

public class Twitch extends Check
{
    public Twitch(final Bernard Daedalus) {
        super("Twitch", "Twitch", Daedalus);
        this.setEnabled(true);
        this.setBannable(true);
        this.setMaxViolations(5);
    }
    
    @EventHandler
    public void Player(final PacketPlayerEvent e) {
        if (e.getType() != PacketPlayerType.LOOK) {
            return;
        }
        if (e.getPitch() > 90.1f || e.getPitch() < -90.1f) {
            this.getDaedalus().logCheat(this, e.getPlayer(), null, Chance.HIGH, new String[0]);
        }
    }
}
