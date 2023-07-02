package com.pixeltects.core.shows.commands;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.utils.math.MathArgUtils;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Command_Wait implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("wait")) {
            if(sender.isOp() || sender.hasPermission("pixeltects.show.wait")
            || sender.hasPermission("pixeltects.show")) {
                if(args.length == 0) {
                    sender.sendMessage(ChatColor.RED + "Correct usage: /wait <ticksToWait> <command>");
                    sender.sendMessage(ChatColor.GRAY + "(Note: 20 ticks equals 1 second)");
                }
                if(args.length >= 1) {
                    if(MathArgUtils.isInt(args[0])) {
                        int time = Integer.parseInt(args[0]);
                        String command = "";

                        for(int i = 1; i < args.length; i++){
                            command = command + args[i] + " ";
                        }

                        String waitString = ChatColor.GREEN + "Waiting " + time + " ticks to execute '" + command + ".'";

                        if(sender instanceof Player) {
                            sender.sendMessage(waitString);
                        }

                        final String commandString = command;

                        new BukkitRunnable() {
                            public void run() {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandString);
                            }
                        }.runTaskLater(Pixeltects.getPackageManager(), time);

                    }else{
                        sender.sendMessage(ChatColor.RED + "You must enter a valid parameter for 'timesToRepeat.'");
                    }
                }
            }else{
                sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
            }
        }
        return false;
    }

}
