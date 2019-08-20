package net.hermanos.ac.packets.events;

import org.bukkit.entity.*;
import org.bukkit.event.*;

public class PacketKeepAliveEvent extends Event
{
    public Player Player;
    private static final HandlerList handlers;
    
    static {
        handlers = new HandlerList();
    }
    
    public PacketKeepAliveEvent(final Player Player) {
        this.Player = Player;
    }
    
    public Player getPlayer() {
        return this.Player;
    }
    
    public HandlerList getHandlers() {
        return PacketKeepAliveEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PacketKeepAliveEvent.handlers;
    }
}
