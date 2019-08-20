package net.hermanos.ac.update;

import org.bukkit.event.*;

public class UpdateEvent extends Event
{
    private static final HandlerList handlers;
    private UpdateType Type;
    
    static {
        handlers = new HandlerList();
    }
    
    public UpdateEvent(final UpdateType Type) {
        this.Type = Type;
    }
    
    public UpdateType getType() {
        return this.Type;
    }
    
    public HandlerList getHandlers() {
        return UpdateEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return UpdateEvent.handlers;
    }
}
