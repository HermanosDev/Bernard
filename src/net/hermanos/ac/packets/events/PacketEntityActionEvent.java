package net.hermanos.ac.packets.events;

import org.bukkit.entity.*;
import org.bukkit.event.*;

public class PacketEntityActionEvent extends Event
{
    public int Action;
    public Player Player;
    private static final HandlerList handlers;
    
    static {
        handlers = new HandlerList();
    }
    
    public PacketEntityActionEvent(final Player Player, final int Action) {
        this.Player = Player;
        this.Action = Action;
    }
    
    public Player getPlayer() {
        return this.Player;
    }
    
    public int getAction() {
        return this.Action;
    }
    
    public HandlerList getHandlers() {
        return PacketEntityActionEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PacketEntityActionEvent.handlers;
    }
    
    public class PlayerAction
    {
    }
}
