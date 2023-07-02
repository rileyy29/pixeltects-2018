package com.pixeltects.core.shows.commands;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.utils.math.MathArgUtils;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Command_SetBlock implements CommandExecutor {

    public static String USAGE = ChatColor.RED + "Correct usage: /setblock <world> <x> <y> <z> <block_type> [ticksToSetFor]";
    public static String RECENT_UPDATE = ChatColor.AQUA + "New Update! Add *ticks* for how long the block should be set for. If you do not add any number of ticks, the block will be permanent.";

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { //REMOVED BLOCK DATA BECAUSE OF 1.14 UPDATE. ONLY NEED FOR BLOCK DATA IS SIGNS.
        if (cmd.getName().equalsIgnoreCase("setblock")) {
            if(sender.isOp() || sender.hasPermission("pixeltects.show.setblock")
                    || sender.hasPermission("pixeltects.show")) {
                if(args.length >= 5) {
                    if(MathArgUtils.isDouble(args[1]) && MathArgUtils.isDouble(args[2])
                    && MathArgUtils.isDouble(args[3])) {

                        World world = Bukkit.getWorld(args[0]);
                        if(world != null) {

                            double x = Double.parseDouble(args[1]);
                            double y = Double.parseDouble(args[2]);
                            double z = Double.parseDouble(args[3]);

                            Material material = Material.getMaterial(args[4].toUpperCase());
                            if(material == null) {
                                sender.sendMessage(MessageOutput.INVALID_MATERIAL.getOutput());
                            }else {
                                String completed = ChatColor.GREEN + "Setblock at " + x + ", " + y + ", " + z + " in " + world.getName() + " to "
                                        + material.name() + ".";

                                if (args.length >= 6) { //Temp
                                    if (MathArgUtils.isInt(args[5])) {
                                        int ticks = Integer.parseInt(args[5]);
                                        Block block = world.getBlockAt(new Location(world, x, y, z));

                                        Material oldType = Material.AIR;
                                        if ((block != null) && !block.getType().equals(Material.AIR)) {
                                            oldType = block.getType();
                                        }

                                        completed = ChatColor.GREEN + "Setblock at " + x + ", " + y + ", " + z + " in " + world.getName() + " to "
                                                + material.name() + " for " + ticks + " ticks.";

                                        final Block newBlock = world.getBlockAt(new Location(world, x, y, z));
                                        newBlock.setType(material);

                                        final Material resetMat = oldType;

                                        new BukkitRunnable() {
                                            public void run() {
                                                newBlock.setType(resetMat);
                                            }
                                        }.runTaskLater(Pixeltects.getPackageManager(), ticks);

                                        if (sender instanceof Player) {
                                            sender.sendMessage(completed);
                                        }

                                    } else {
                                        sender.sendMessage(ChatColor.RED + "You must enter a valid parameter for the ticks.");
                                    }
                                } else { //Perm
                                    Block block = world.getBlockAt(new Location(world, x, y, z));
                                    block.setType(material);

                                    if (sender instanceof Player) {
                                        sender.sendMessage(completed);
                                    }
                                }
                            }
                        }else{
                            sender.sendMessage(MessageOutput.INVALID_WORLD.getOutput());
                        }
                    }else{
                        sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
                    }
                }else{
                    sender.sendMessage(USAGE);
                    sender.sendMessage(RECENT_UPDATE);
                }
            }else{
                sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
            }
        }
        return false;
    }

}


