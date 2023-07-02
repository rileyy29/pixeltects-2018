package com.pixeltects.core.commands.entities;

import com.pixeltects.core.utils.math.MathArgUtils;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Command_RideEntity implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("setpassenger")) {
            if(sender.isOp() || sender.hasPermission("pixeltects.entity.ride")
                    || sender.hasPermission("pixeltects.technician")) {
                if(args.length == 0) {
                    sendHelp(sender);
                }else if(args.length >= 6) {
                    World world = Bukkit.getWorld(args[0]);
                    if(world != null) {
                        if (MathArgUtils.isDouble(args[1]) &&
                                MathArgUtils.isDouble(args[2]) &&
                                MathArgUtils.isDouble(args[3]) &&
                                MathArgUtils.isInt(args[4])) {
                            double x = Double.parseDouble(args[1]), y = Double.parseDouble(args[2]), z = Double.parseDouble(args[3]);
                            Location location = new Location(world, x,y,z);
                            int radius = Integer.parseInt(args[4]);
                            String name = args[5];

                            for (Player player : world.getPlayers()) {
                                if(!player.isInsideVehicle()) {
                                Location playerLoc = player.getLocation();
                                if(!player.getGameMode().equals(GameMode.CREATIVE)) {
                                    if (playerLoc.distance(location) <= radius) {
                                        for (Entity entity : world.getNearbyEntities(location, radius, radius, radius)) {
                                            if ((entity != null) && entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase(name)) {
                                                if (entity.getPassenger() == null) {
                                                    entity.addPassenger(player);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                }
                            }
                            sender.sendMessage(ChatColor.GREEN + "Players that are " + radius + " blocks close to " + x + ", " + y + ", " + z + " are now riding any entity (in a " + radius + " radius) called '" + name + ".'");
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
        sender.sendMessage(ChatColor.RED + "Correct usage: /setpassenger <world> <x> <y> <z> <radius> <entity custom name>");
    }

}


