package com.pixeltects.core.commands.entities;

import com.pixeltects.core.utils.math.MathArgUtils;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_MovePlayers implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("moveplayers")) {
            if(sender.isOp() || sender.hasPermission("pixeltects.entity.moveplayers")
                    || sender.hasPermission("pixeltects.technician")) {
                if(args.length == 0) {
                    sendHelp(sender);
                }else if(args.length >= 8) {
                    World world = Bukkit.getWorld(args[0]);
                    if(world != null) {
                        if (MathArgUtils.isDouble(args[1]) &&
                                MathArgUtils.isDouble(args[2]) &&
                                MathArgUtils.isDouble(args[3]) &&
                                MathArgUtils.isDouble(args[4]) &&
                                MathArgUtils.isDouble(args[5]) &&
                                MathArgUtils.isDouble(args[6]) &&
                                MathArgUtils.isInt(args[7])) {
                                double oldX = Double.parseDouble(args[1]);
                                double oldY = Double.parseDouble(args[2]);
                                double oldZ = Double.parseDouble(args[3]);
                                double newX = Double.parseDouble(args[4]);
                                double newY = Double.parseDouble(args[5]);
                                double newZ = Double.parseDouble(args[6]);
                                Location oldLoc = new Location(world, oldX, oldY, oldZ);
                                Location newLoc = new Location(world, newX, newY, newZ);
                                int radius = Integer.parseInt(args[7]);
                                for (Player player : world.getPlayers()) {
                                    Location playerLoc = player.getLocation();
                                    if (oldLoc.distance(playerLoc) <= radius)
                                        player.teleport(newLoc);
                                }
                                sender.sendMessage(ChatColor.GREEN + "Teleported players in a radius of " + radius + " from " + oldX + ", " + oldY + ", " + oldZ + " to " + newX + ", " + newY + ", " + newZ + ".");
                        } else {
                            sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
                        }
                    }else{
                        sender.sendMessage(MessageOutput.INVALID_WORLD.getOutput());
                    }
                }else{
                    sendHelp(sender);
                }
            }else{
                sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
            }
        }
        return true;
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Correct usage: /moveplayers <world> <x> <y> <z> <newx> <newy> <newz> <radius>");
    }

}

