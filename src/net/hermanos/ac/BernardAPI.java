package net.hermanos.ac;

import org.bukkit.plugin.*;

import net.hermanos.ac.check.*;
import net.hermanos.ac.check.other.*;
import net.hermanos.ac.utils.*;

import java.util.*;

import org.bukkit.entity.*;

public class BernardAPI
{
    private static Bernard daedalus;
    @SuppressWarnings("unused")
	private Plugin plugin;
    
    public BernardAPI(final Plugin plugin) {
        this.plugin = plugin;
        BernardAPI.daedalus = (Bernard)plugin;
    }
    
    public static List<Check> getChecks() {
        return BernardAPI.daedalus.getChecks();
    }
    
    public static Integer getPing(final Player player) {
        return Math.round(Latency.getLag(player) / 2 * 6);
    }
    
    public static String getChanceString(final Chance chance) {
        if (chance == Chance.HIGH) {
            return String.valueOf(C.Red) + "HIGH";
        }
        if (chance == Chance.LIKELY) {
            return String.valueOf(C.Gold) + "LIKELY";
        }
        return String.valueOf(C.Gray) + "UNKNOWN";
    }
}
