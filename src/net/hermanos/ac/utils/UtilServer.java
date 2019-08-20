package net.hermanos.ac.utils;

import org.bukkit.*;
import java.util.*;
import org.bukkit.entity.*;

public class UtilServer
{
    @SuppressWarnings("deprecation")
	public static ArrayList<Player> getOnlinePlayers() {
        final ArrayList<Player> list = new ArrayList<Player>();
        Player[] onlinePlayers;
        for (int length = (onlinePlayers = Bukkit.getOnlinePlayers()).length, i = 0; i < length; ++i) {
            final Player player = onlinePlayers[i];
            list.add(player);
        }
        return list;
    }
    
    public static List<Entity> getEntities(final World world) {
        return (List<Entity>)world.getEntities();
    }
}
