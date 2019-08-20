package net.hermanos.ac.cmd;

import org.bukkit.command.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import net.hermanos.ac.*;
import net.hermanos.ac.utils.*;

public class AutobanCommand implements CommandExecutor
{
    private Bernard Daedalus;
    
    public AutobanCommand(final Bernard Daedalus) {
        this.Daedalus = Daedalus;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String alias, final String[] args) {
        if (!sender.hasPermission("daedalus.staff")) {
            sender.sendMessage(String.valueOf(C.Red) + "No permission.");
            return true;
        }
        if (args.length == 2) {
            final String type = args[0];
            final String playerName = args[1];
            final Player player = Bukkit.getServer().getPlayer(playerName);
            if (player == null || !player.isOnline()) {
                sender.sendMessage(String.valueOf(C.Red) + "This player does not exist.");
                return true;
            }
            if (this.Daedalus.getAutobanQueue().contains(player)) {
                @SuppressWarnings("unused")
				final String lowerCase;
                @SuppressWarnings("unused")
				final String s;
                switch (s = (lowerCase = type.toLowerCase())) {
                    case "cancel": {
                        System.out.println("[" + player.getUniqueId().toString() + "] " + sender.getName() + "'s auto-ban has been cancelled by " + sender.getName());
                        Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', String.valueOf(this.Daedalus.PREFIX) + this.Daedalus.getConfig().getString("alerts.secondary") + player.getName() + this.Daedalus.getConfig().getString("alerts.primary") + "'s auto-ban has been cancelled by " + this.Daedalus.getConfig().getString("alerts.secondary") + sender.getName()), "daedalus.staff");
                        break;
                    }
                    case "ban": {
                        if (this.Daedalus.getConfig().getBoolean("testmode")) {
                            sender.sendMessage(ChatColor.RED + "Test mode is enabled therefore this is disabled!");
                            break;
                        }
                        System.out.println("[" + player.getUniqueId().toString() + "] " + sender.getName() + "'s auto-ban has been forced by " + sender.getName());
                        Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', String.valueOf(this.Daedalus.PREFIX) + this.Daedalus.getConfig().getString("alerts.secondary") + player.getName() + this.Daedalus.getConfig().getString("alerts.primary") + "'s auto-ban has been forced by " + this.Daedalus.getConfig().getString("alerts.secondary") + sender.getName()), "daedalus.staff");
                        this.Daedalus.autobanOver(player);
                        break;
                    }
                    default:
                        break;
                }
                this.Daedalus.removeFromAutobanQueue(player);
                this.Daedalus.removeViolations(player);
            }
            else {
                sender.sendMessage(String.valueOf(String.valueOf(C.Red)) + "This player is not in the autoban queue!");
            }
        }
        return true;
    }
}
