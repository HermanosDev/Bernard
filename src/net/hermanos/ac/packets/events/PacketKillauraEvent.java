package net.hermanos.ac.packets.events;

import org.bukkit.entity.*;
import org.bukkit.event.*;

public class PacketKillauraEvent extends Event
{
    private Player Player;
    private static final HandlerList handlers;
    private PacketPlayerType type;
    
    static {
        handlers = new HandlerList();
    }
    
    public PacketKillauraEvent(final Player Player, final PacketPlayerType type) {
        this.Player = Player;
        this.type = type;
    }
    
    public Player getPlayer() {
        return this.Player;
    }
    
    public PacketPlayerType getType() {
        return this.type;
    }
    
    public HandlerList getHandlers() {
        return PacketKillauraEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PacketKillauraEvent.handlers;
    }
}
