package net.hermanos.ac.packets.events;

import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;
import org.bukkit.event.*;

public class PacketBlockPlacementEvent extends Event
{
    public Player Player;
    public PacketEvent Event;
    private static final HandlerList handlers;
    
    static {
        handlers = new HandlerList();
    }
    
    public PacketBlockPlacementEvent(final PacketEvent Event, final Player Player) {
        this.Player = Player;
        this.Event = Event;
    }
    
    public PacketEvent getPacketEvent() {
        return this.Event;
    }
    
    public Player getPlayer() {
        return this.Player;
    }
    
    public HandlerList getHandlers() {
        return PacketBlockPlacementEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PacketBlockPlacementEvent.handlers;
    }
}
