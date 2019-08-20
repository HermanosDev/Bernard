package net.hermanos.ac.checks.combat;

import java.util.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.packets.events.*;
import net.hermanos.ac.utils.*;

public class KillAuraD extends Check
{
    public static Map<UUID, Map.Entry<Double, Double>> packetTicks;
    
    public KillAuraD(final Bernard Daedalus) {
        super("KillAuraD", "KillAura (Packet)", Daedalus);
        this.setEnabled(true);
        this.setBannable(false);
        this.setMaxViolations(5);
        this.setViolationResetTime(60000L);
        KillAuraD.packetTicks = new HashMap<UUID, Map.Entry<Double, Double>>();
    }
    
    @EventHandler
    public void packet(final PacketKillauraEvent e) {
        if (!this.getDaedalus().isEnabled()) {
            return;
        }
        if (e.getPlayer().hasPermission("daedalus.bypass")) {
            return;
        }
        double Count = 0.0;
        double Other = 0.0;
        if (KillAuraD.packetTicks.containsKey(e.getPlayer().getUniqueId())) {
            Count = KillAuraD.packetTicks.get(e.getPlayer().getUniqueId()).getKey();
            Other = KillAuraD.packetTicks.get(e.getPlayer().getUniqueId()).getValue();
        }
        if (e.getType() == PacketPlayerType.ARM_SWING) {
            ++Other;
        }
        if (e.getType() == PacketPlayerType.USE) {
            ++Count;
        }
        if (Count > Other && Other == 2.0) {
            this.getDaedalus().logCheat(this, e.getPlayer(), null, Chance.HIGH, new String[0]);
        }
        if (Count > 3.0 || Other > 3.0) {
            Count = 0.0;
            Other = 0.0;
        }
        KillAuraD.packetTicks.put(e.getPlayer().getUniqueId(), new AbstractMap.SimpleEntry<Double, Double>(Count, Other));
    }
    
    @EventHandler
    public void logout(final PlayerQuitEvent e) {
        if (KillAuraD.packetTicks.containsKey(e.getPlayer().getUniqueId())) {
            KillAuraD.packetTicks.remove(e.getPlayer().getUniqueId());
        }
    }
}
