package net.hermanos.ac.listeners;

import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

import net.hermanos.ac.utils.*;

import org.bukkit.event.*;

public class PearlGlitchEvent extends Event implements Cancellable
{
    private Player player;
    private Location from;
    private Location to;
    private ItemStack pearls;
    private PearlGlitchType type;
    private static HandlerList handlers;
    private boolean cancelled;
    
    static {
        PearlGlitchEvent.handlers = new HandlerList();
    }
    
    public PearlGlitchEvent(final Player player, final Location from, final Location to, final ItemStack pearls, final PearlGlitchType type) {
        this.cancelled = false;
        this.player = player;
        this.from = from;
        this.to = to;
        this.pearls = pearls;
        this.type = type;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Location getFrom() {
        return this.from;
    }
    
    public Location getTo() {
        return this.to;
    }
    
    public ItemStack getItems() {
        return this.pearls;
    }
    
    public PearlGlitchType getType() {
        return this.type;
    }
    
    public boolean isType(final PearlGlitchType type) {
        return type == this.type;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public static HandlerList getHandlerList() {
        return PearlGlitchEvent.handlers;
    }
    
    public HandlerList getHandlers() {
        return PearlGlitchEvent.handlers;
    }
}
