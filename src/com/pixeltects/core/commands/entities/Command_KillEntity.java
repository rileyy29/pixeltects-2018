package com.pixeltects.core.commands.entities;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.utils.math.MathArgUtils;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Command_KillEntity implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("killentity")) {
            if(sender.isOp() || sender.hasPermission("pixeltects.entity.kill")
                    || sender.hasPermission("pixeltects.technician")) {
                    if(args.length == 0) {
                        sendHelp(sender);
                    }else if(args.length == 2) {
                        String name = args[0];
                        if(MathArgUtils.isInt(args[1])) {
                            if(sender instanceof Player || sender instanceof BlockCommandSender) {
                                Location location;
                                World world;

                                if(sender instanceof Player) {
                                    Player player = (Player)sender;

                                    location = player.getLocation();
                                    world = location.getWorld();
                                }else{
                                    location = ((BlockCommandSender)sender).getBlock().getLocation();
                                    world = location.getWorld();
                                }

                                int radius = Integer.parseInt(args[1]);
                                int count = 0;

                                for(Entity entity : world.getEntities()) {
                                    if((entity != null) && entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase(name)) {
                                        if(entity.getLocation().distance(location) <= radius) {
                                            entity.remove();
                                            count++;
                                        }
                                    }
                                }

                                if(count == 0) {
                                    sender.sendMessage(ChatColor.RED + "No entities were killed.");
                                }else{
                                    sender.sendMessage(ChatColor.GREEN + "" + count + " entities were killed.");
                                }

                            }else{
                                sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
                            }
                        }else{
                            String worldName = args[1];
                            World world = Bukkit.getWorld(worldName);
                            int count = 0;
                            if(world != null) {
                                for(Entity entity : world.getEntities()) {
                                    if((entity != null) && entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase(name)) {
                                        entity.remove();
                                        count++;
                                    }
                                }

                                if(count == 0) {
                                    sender.sendMessage(ChatColor.RED + "No entities were killed.");
                                }else{
                                    sender.sendMessage(ChatColor.GREEN + "" + count + " entities were killed.");
                                }
                            }else{
                                sender.sendMessage(MessageOutput.INVALID_WORLD.getOutput());
                            }
                        }
                    }else if(args.length >= 6) {
                        String name = args[0];
                        if(MathArgUtils.isDouble(args[2]) && MathArgUtils.isDouble(args[3]) && MathArgUtils.isDouble(args[4]) &&
                                MathArgUtils.isInt(args[5])) {

                            World world = Bukkit.getWorld(args[1]);
                            if(world != null) {
                                double x = Double.parseDouble(args[2]), y = Double.parseDouble(args[3]), z = Double.parseDouble(args[4]);
                                Location location = new Location(world, x,y,z);
                                int radius = Integer.parseInt(args[5]);
                                int count = 0;

                                for(Entity entity : world.getEntities()) {
                                    if((entity != null) && entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase(name)) {
                                        if(entity.getLocation().distance(location) <= radius) {
                                            entity.remove();
                                            count++;
                                        }
                                    }
                                }

                                if(count == 0) {
                                    sender.sendMessage(ChatColor.RED + "No entities were killed.");
                                }else{
                                    sender.sendMessage(ChatColor.GREEN + "" + count + " entities were killed.");
                                }
                            }else{
                             sender.sendMessage(MessageOutput.INVALID_WORLD.getOutput());
                            }
                        }else{
                            sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
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
        sender.sendMessage(ChatColor.RED + "Correct usage: /killentity <name> <world/radius>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /killentity <name> <world> <x> <y> <z> <radius>");
    }

}

