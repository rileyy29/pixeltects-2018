package com.pixeltects.core.shows.commands;

import com.pixeltects.core.shows.utils.RepeatUtil;
import com.pixeltects.core.utils.math.MathArgUtils;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_Repeat implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("repeat")) {
            if(sender.isOp() || sender.hasPermission("pixeltects.show.repeat")
            || sender.hasPermission("pixeltects.show")) {
                if(args.length == 0) {
                    sender.sendMessage(ChatColor.RED + "Correct usage: /repeat <timesToRepeat> <command>");
                }
                if(args.length >= 1) {
                    if(MathArgUtils.isInt(args[0])) {
                        int time = Integer.parseInt(args[0]);
                        String command = "";

                        for(int i = 1; i < args.length; i++){
                            command = command + args[i] + " ";
                        }

                        String repeatString = time == 1 ? ChatColor.GREEN + "Now repeating '" + command + "' once." :
                                ChatColor.GREEN + "Now repeating '" + command + "' " + time + " times.";

                        if(sender instanceof Player) {
                            sender.sendMessage(repeatString);
                        }

                        RepeatUtil.runRepeat(time, command);

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
