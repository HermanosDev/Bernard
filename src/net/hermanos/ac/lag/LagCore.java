package net.hermanos.ac.lag;

import org.bukkit.event.*;
import org.bukkit.scheduler.*;

import net.hermanos.ac.*;

import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import java.lang.reflect.*;
import org.bukkit.*;

public class LagCore implements Listener
{
    public Bernard Daedalus;
    private double tps;
    
    public LagCore(final Bernard Daedalus) {
        this.Daedalus = Daedalus;
        new BukkitRunnable() {
            long sec;
            long currentSec;
            int ticks;
            
            public void run() {
                this.sec = System.currentTimeMillis() / 1000L;
                if (this.currentSec == this.sec) {
                    ++this.ticks;
                }
                else {
                    this.currentSec = this.sec;
                    LagCore.access$1(LagCore.this, (LagCore.this.tps == 0.0) ? ((double)this.ticks) : ((LagCore.this.tps + this.ticks) / 2.0));
                    this.ticks = 0;
                }
            }
        }.runTaskTimerAsynchronously((Plugin)Daedalus, 1L, 1L);
        this.Daedalus.RegisterListener((Listener)this);
    }
    
    public double getTPS() {
        return (this.tps + 1.0 > 20.0) ? 20.0 : (this.tps + 1.0);
    }
    
    public double getLag() {
        return Math.round((1.0 - this.tps / 20.0) * 100.0);
    }
    
    public double getFreeRam() {
        return Math.round(Runtime.getRuntime().freeMemory() / 1000000L);
    }
    
    public double getMaxRam() {
        return Math.round(Runtime.getRuntime().maxMemory() / 1000000L);
    }
    
    public static Object getNmsPlayer(final Player p) throws Exception {
        final Method getHandle = p.getClass().getMethod("getHandle", (Class<?>[])new Class[0]);
        return getHandle.invoke(p, new Object[0]);
    }
    
    public static Object getFieldValue(final Object instance, final String fieldName) throws Exception {
        final Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }
    
    public int getPing(final Player who) {
        try {
            final String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            final Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + bukkitversion + ".entity.CraftPlayer");
            final Object handle = craftPlayer.getMethod("getHandle", (Class<?>[])new Class[0]).invoke(who, new Object[0]);
            final Integer ping = (Integer)handle.getClass().getDeclaredField("ping").get(handle);
            return ping;
        }
        catch (Exception e) {
            return 404;
        }
    }
    
    static /* synthetic */ void access$1(final LagCore lagCore, final double tps) {
        lagCore.tps = tps;
    }
}
