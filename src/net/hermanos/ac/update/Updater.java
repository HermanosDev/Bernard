package net.hermanos.ac.update;

import org.bukkit.*;
import org.bukkit.plugin.*;

import net.hermanos.ac.*;

import org.bukkit.event.*;

public class Updater implements Runnable
{
    private Bernard Daedalus;
    private int updater;
    
    public Updater(final Bernard Daedalus) {
        this.Daedalus = Daedalus;
        this.updater = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.Daedalus, (Runnable)this, 0L, 1L);
    }
    
    public void Disable() {
        Bukkit.getScheduler().cancelTask(this.updater);
    }
    
    @Override
    public void run() {
        for (final UpdateType updateType : UpdateType.values()) {
            if (updateType != null && updateType.Elapsed()) {
                try {
                    final UpdateEvent event = new UpdateEvent(updateType);
                    Bukkit.getPluginManager().callEvent((Event)event);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
