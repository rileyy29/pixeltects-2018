package com.pixeltects.core.commands;

import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_Outline implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("outline")) {
            if(sender instanceof Player) {
                if(sender.isOp() || sender.hasPermission("pixeltects.build.outline")
                        || sender.hasPermission("pixeltects.build")) {
                    if(args.length == 0) {
                        sender.sendMessage(ChatColor.RED + "Correct usage: /outline <x> <z> <angle> <length>");
                    }else if(args.length == 4) {
                        Player player = (Player)sender;
                        double angle = Double.parseDouble(args[2]);

                        double radAngle = angle / 180.0D * Math.PI; //More precise than using a set of numbers e.g. 3.14
                        double hypoLength = Double.parseDouble(args[3]);

                        double preRoundX = hypoLength * Math.sin(radAngle);
                        double preRoundZ = hypoLength * Math.cos(radAngle);

                        double postRoundX = Math.round(preRoundX);
                        double postRoundZ = Math.round(preRoundZ);

                        int changeX = (int)postRoundX;
                        int changeZ = (int)postRoundZ;

                        int x = Integer.parseInt(args[0]) + changeX;
                        int z = Integer.parseInt(args[1]) - changeZ;

                        double newX = x + 0.5D;
                        double newZ = z + 0.5D;

                        player.teleport(new Location(player.getWorld(), newX, player.getLocation().getY(), newZ));
                    }else{
                        sender.sendMessage(ChatColor.RED + "Invalid or missing parameters.");
                        sender.sendMessage(ChatColor.RED + "Correct usage: /outline <x> <z> <angle> <length>");
                    }
                }else{
                    sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                }
            }else{
                sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
            }
        }
        return true;
    }

}
