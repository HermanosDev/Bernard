package net.hermanos.ac.packets.events;

import com.comphenix.protocol.wrappers.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class PacketUseEntityEvent extends Event
{
    public EnumWrappers.EntityUseAction Action;
    public Player Attacker;
    public Entity Attacked;
    private static final HandlerList handlers;
    
    static {
        handlers = new HandlerList();
    }
    
    public PacketUseEntityEvent(final EnumWrappers.EntityUseAction Action, final Player Attacker, final Entity Attacked) {
        this.Action = Action;
        this.Attacker = Attacker;
        this.Attacked = Attacked;
    }
    
    public EnumWrappers.EntityUseAction getAction() {
        return this.Action;
    }
    
    public Player getAttacker() {
        return this.Attacker;
    }
    
    public Entity getAttacked() {
        return this.Attacked;
    }
    
    public HandlerList getHandlers() {
        return PacketUseEntityEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PacketUseEntityEvent.handlers;
    }
}
