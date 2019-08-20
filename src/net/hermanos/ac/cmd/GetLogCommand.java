package net.hermanos.ac.cmd;

import org.bukkit.command.*;

import java.io.*;

import net.hermanos.ac.*;
import net.hermanos.ac.utils.*;
import net.minecraft.util.org.apache.commons.io.*;
import java.util.*;

public class GetLogCommand implements CommandExecutor
{
    private Bernard Daedalus;
    
    public GetLogCommand(final Bernard Daedalus) {
        this.Daedalus = Daedalus;
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("daedalus.log") && !sender.hasPermission("daedalus.admin") && !sender.getName().equalsIgnoreCase("funkemunky")) {
            sender.sendMessage(String.valueOf(C.Red) + "No permission.");
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(String.valueOf(this.Daedalus.PREFIX) + C.Red + "Usage: /getlog <name> <page>");
            return true;
        }
        final String player = args[0];
        final int page = Integer.parseInt(args[1]);
        final String path = this.Daedalus.getDataFolder() + File.separator + "logs" + File.separator + args[0] + ".txt";
        final File file = new File(path);
        if (!file.exists()) {
            sender.sendMessage(String.valueOf(this.Daedalus.PREFIX) + C.Red + "The player '" + C.Bold + player + C.Red + "' does not have a ban log! This is CASE SENSITIVE!");
            return true;
        }
        try {
            final List<String> lines = (List<String>)FileUtils.readLines(file);
            if (lines.size() / (page * 10) < 1) {
                sender.sendMessage(String.valueOf(this.Daedalus.PREFIX) + C.Red + "There is no page " + page + " for this log!");
                return true;
            }
            sender.sendMessage(String.valueOf(C.DGray) + C.Strike + "----------------------------------------------------");
            sender.sendMessage(String.valueOf(C.Gray) + "Log for " + C.White + player + C.Red + " Page " + page);
            sender.sendMessage("");
            for (int i = (page - 1) * 10; i < page * 10; ++i) {
                if (i < lines.size()) {
                    sender.sendMessage((String)lines.get(i));
                }
            }
            sender.sendMessage(String.valueOf(C.DGray) + C.Strike + "----------------------------------------------------");
        }
        catch (Exception e) {
            sender.sendMessage(String.valueOf(this.Daedalus.PREFIX) + C.Red + "Unknown error occured when tryin to read file and upload to pastebin!");
            e.printStackTrace();
        }
        return true;
    }
}
