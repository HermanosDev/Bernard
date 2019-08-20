package net.hermanos.ac.cmd;

import org.bukkit.command.*;
import org.bukkit.entity.*;

import net.hermanos.ac.*;
import net.hermanos.ac.check.*;
import net.hermanos.ac.gui.*;
import net.hermanos.ac.utils.*;

import org.bukkit.*;

public class BernardCommand implements CommandExecutor
{
    private Bernard Daedalus;
    
    public BernardCommand(final Bernard Daedalus) {
        this.Daedalus = Daedalus;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String alias, final String[] args) {
        if (!sender.hasPermission("bernard.admin")) {
            sender.sendMessage(String.valueOf(C.Red) + "This server is using Bernard " + this.Daedalus.getVersion() + " by MrWiZoX.");
            return true;
        }
        if (args.length == 0) {
            if (sender instanceof Player) {
                final Player p = (Player)sender;
                ChecksGUI.openDaedalusMain(p);
            }
            else {
                sender.sendMessage(String.valueOf(C.Red) + "This is for players only! Do /bernard help to find a command you can do here.");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("violations")) {
            if (sender instanceof Player) {
                final String playerName2 = args[1];
                final Player player = this.Daedalus.getServer().getPlayer(playerName2);
                final Player p2 = (Player)sender;
                if (player == null || !player.isOnline()) {
                    sender.sendMessage(String.valueOf(C.Red) + "This player is not online!");
                    return true;
                }
                ChecksGUI.openStatus(p2, player);
            }
            else {
                sender.sendMessage(String.valueOf(C.Red) + "This is for players only!");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("dump")) {
            final String playerName3 = args[1];
            final String checkName = args[2];
            Check check = null;
            for (final Check checkcheck : this.Daedalus.getChecks()) {
                if (checkcheck.getIdentifier().equalsIgnoreCase(checkName)) {
                    check = checkcheck;
                }
            }
            if (check == null) {
                sender.sendMessage(String.valueOf(C.Red) + "This check does not exist!");
                return true;
            }
            final String result = check.dump(playerName3);
            if (result == null) {
                sender.sendMessage(String.valueOf(C.Red) + "Error creating dump file for player " + playerName3 + ".");
            }
            sender.sendMessage(String.valueOf(this.Daedalus.PREFIX) + C.Gray + "Dropped dump thread at " + C.Yellow + "/dumps/" + result + ".txt");
            return true;
        }
        else {
            if (args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage(String.valueOf(this.Daedalus.PREFIX) + C.Gray + "Reloading Bernard...");
                this.Daedalus.reloadConfig();
                sender.sendMessage(String.valueOf(this.Daedalus.PREFIX) + C.Green + "Successfully reloaded Bernard!");
                return true;
            }
            if (args[0].equalsIgnoreCase("clean") || args[0].equalsIgnoreCase("gc")) {
                sender.sendMessage(String.valueOf(this.Daedalus.PREFIX) + C.Gray + "Forcing garbage collector..." + C.Gray + "[" + C.Aqua + this.Daedalus.getLag().getFreeRam() + C.Gray + "/" + C.Red + this.Daedalus.getLag().getMaxRam() + C.Gray + "]");
                System.gc();
                sender.sendMessage(String.valueOf(this.Daedalus.PREFIX) + C.Green + "Completed garbage collector! " + C.Gray + "[" + C.Aqua + UtilMath.trim(3, this.Daedalus.getLag().getFreeRam()) + C.Gray + "/" + C.Red + UtilMath.trim(3, this.Daedalus.getLag().getMaxRam()) + C.Gray + "]");
                return true;
            }
            if (args[0].equalsIgnoreCase("lag") || args[0].equalsIgnoreCase("performance")) {
                sender.sendMessage(String.valueOf(C.DGray) + C.Strike + "----------------------------------------------------");
                sender.sendMessage(String.valueOf(C.Red) + C.Bold + "Performance Usage:");
                sender.sendMessage("");
                sender.sendMessage(String.valueOf(C.Gray) + "TPS: " + C.White + UtilMath.trim(2, this.Daedalus.getLag().getTPS()));
                sender.sendMessage(String.valueOf(C.Gray) + "Free Ram: " + C.White + this.Daedalus.getLag().getFreeRam() + "MB");
                sender.sendMessage(String.valueOf(C.Gray) + "Max Ram: " + C.White + this.Daedalus.getLag().getMaxRam() + "MB");
                sender.sendMessage(String.valueOf(C.Gray) + "Used Ram: " + C.White + Math.abs(this.Daedalus.getLag().getMaxRam() - this.Daedalus.getLag().getFreeRam()) + "MB");
                if (Math.abs(this.Daedalus.getLag().getMaxRam() - this.Daedalus.getLag().getFreeRam()) > this.Daedalus.getLag().getMaxRam() / 2.1) {
                    sender.sendMessage(String.valueOf(C.Aqua) + C.Italics + "It is recommended you do /bernard clean to clear up some RAM.");
                }
                sender.sendMessage((this.Daedalus.getLag().getLag() > 20.0) ? (String.valueOf(C.Red) + "Server Usage: " + this.Daedalus.getLag().getLag() + "%") : (String.valueOf(C.Green) + "Server Usage: " + this.Daedalus.getLag().getLag() + "%"));
                sender.sendMessage(String.valueOf(C.DGray) + C.Strike + "----------------------------------------------------");
                return true;
            }
            if (args[0].equalsIgnoreCase("test")) {
                sender.sendMessage(String.valueOf(3));
                return true;
            }
            if (args[0].equalsIgnoreCase("bans")) {
                if (sender instanceof Player) {
                    final Player p = (Player)sender;
                    ChecksGUI.openBans(p);
                }
                else {
                    sender.sendMessage(String.valueOf(C.Red) + "This is for players only!");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(String.valueOf(C.DGray) + C.Strike + "----------------------------------------------------");
                sender.sendMessage(String.valueOf(C.Red) + C.Bold + "Bernard Help:");
                sender.sendMessage(" ");
                sender.sendMessage(String.valueOf(C.Gray) + "/bernard" + C.Reset + " help" + C.Gray + "  - View the help page.");
                sender.sendMessage(String.valueOf(C.Gray) + "/bernard" + C.Reset + " dump" + C.Gray + " - Dump a check log of a player.");
                sender.sendMessage(String.valueOf(C.Gray) + "/bernard" + C.Reset + " ping" + C.Gray + "  - Get your ping.");
                sender.sendMessage(String.valueOf(C.Gray) + "/bernard" + C.Reset + " bans" + C.Gray + " - Lists bans this restart.");
                sender.sendMessage(String.valueOf(C.Gray) + "/bernard" + C.Reset + " clean" + C.Gray + " - Run Java Garbage-Collector.");
                sender.sendMessage(String.valueOf(C.Gray) + "/bernard" + C.Reset + " reload" + C.Gray + "   - Reload Daedalus.");
                sender.sendMessage(String.valueOf(C.Gray) + "/bernard" + C.Reset + " violations <player>" + C.Gray + " - Gets the violations of a player.");
                sender.sendMessage(String.valueOf(C.Gray) + "/bernard" + C.Reset + " lag" + C.Gray + "  - Get server performance info.");
                sender.sendMessage(String.valueOf(C.DGray) + C.Strike + "----------------------------------------------------");
                return true;
            }
            if (args[0].equalsIgnoreCase("ping")) {
                if (sender instanceof Player) {
                    final Player p = (Player)sender;
                    if (args.length == 1) {
                        sender.sendMessage(String.valueOf(this.Daedalus.PREFIX) + C.DGray + "[Vanilla] " + C.Gray + "Your ping: " + C.Red + this.Daedalus.getLag().getPing(p));
                        sender.sendMessage(String.valueOf(this.Daedalus.PREFIX) + C.DGray + "[Daedalus] " + C.Gray + "Your ping: " + C.Red + BernardAPI.getPing(p));
                        return true;
                    }
                    if (args.length == 2) {
                        final Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) {
                            sender.sendMessage(String.valueOf(this.Daedalus.PREFIX) + C.Red + "That player is not online!");
                            return true;
                        }
                        sender.sendMessage(String.valueOf(this.Daedalus.PREFIX) + C.White + target.getName() + "'s " + C.Gray + " ping: " + C.Red + this.Daedalus.getLag().getPing(target));
                        sender.sendMessage(String.valueOf(this.Daedalus.PREFIX) + C.White + target.getName() + "'s " + C.Gray + " ping: " + C.Red + BernardAPI.getPing(target));
                        return true;
                    }
                    else {
                        sender.sendMessage(String.valueOf(this.Daedalus.PREFIX) + C.Red + "Incorrect arguments. Usage: /bernard ping [player]");
                    }
                }
                else {
                    sender.sendMessage(String.valueOf(C.Red) + "This is for players only!");
                }
                return true;
            }
            sender.sendMessage(String.valueOf(C.Red) + "Unknown argument '/" + alias + " " + args[0] + "'! Do " + C.Italics + "/bernard help " + C.Red + "for more info!");
            return true;
        }
    }
}
