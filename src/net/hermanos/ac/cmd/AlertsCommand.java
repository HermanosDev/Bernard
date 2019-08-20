package net.hermanos.ac.cmd;

import org.bukkit.command.*;
import org.bukkit.entity.*;

import net.hermanos.ac.*;
import net.hermanos.ac.utils.*;

import org.bukkit.*;

public class AlertsCommand implements CommandExecutor
{
    private Bernard Daedalus;
    
    public AlertsCommand(final Bernard Daedalus) {
        this.Daedalus = Daedalus;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String alias, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You have to be a player to run this command!");
            return true;
        }
        final Player player = (Player)sender;
        if (!player.hasPermission("daedalus.staff")) {
            sender.sendMessage(String.valueOf(C.Red) + "No permission.");
            return true;
        }
        if (this.Daedalus.hasAlertsOn(player)) {
            this.Daedalus.toggleAlerts(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(this.Daedalus.PREFIX) + this.Daedalus.getConfig().getString("alerts.primary") + "Alerts toggled " + C.Red + "off" + this.Daedalus.getConfig().getString("alerts.primary") + "!"));
        }
        else {
            this.Daedalus.toggleAlerts(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(this.Daedalus.PREFIX) + this.Daedalus.getConfig().getString("alerts.primary") + "Alerts toggled " + C.Green + "on" + this.Daedalus.getConfig().getString("alerts.primary") + "!"));
        }
        return true;
    }
}
