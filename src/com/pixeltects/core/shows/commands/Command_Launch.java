package com.pixeltects.core.shows.commands;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.shows.effects.particle.LaunchObject;
import com.pixeltects.core.shows.utils.HelpUtil;
import com.pixeltects.core.utils.math.MathArgUtils;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class Command_Launch implements CommandExecutor {

    //TODO: Potentially recode to feature everything, for example /launch stop

    private static String USAGE = ChatColor.RED + "Usage: /launch <x> <y> <z> <world> <xmotion> <ymotion> <zmotion> <material> <data> <particle> <endEarly (true/false)>";

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("launch")) {
            if(sender.isOp() || sender.hasPermission("pixeltects.show.launch")
                    || sender.hasPermission("pixeltects.show")) {
                if(args.length >= 11) {
                    if (MathArgUtils.isDouble(args[0]) &&
                            MathArgUtils.isDouble(args[1]) &&
                            MathArgUtils.isDouble(args[2]) &&
                            MathArgUtils.isDouble(args[4]) &&
                            MathArgUtils.isDouble(args[5]) &&
                            MathArgUtils.isDouble(args[6]) &&
                            MathArgUtils.isInt(args[8])) {
                        World world = Bukkit.getWorld(args[3]);

                        if(world != null) {
                            double x = Double.parseDouble(args[0]);
                            double y = Double.parseDouble(args[1]);
                            double z = Double.parseDouble(args[2]);
                            double xmot = Double.parseDouble(args[4]);
                            double ymot = Double.parseDouble(args[5]);
                            double zmot = Double.parseDouble(args[6]);

                            try {
                                Material material = Material.getMaterial(args[7].toUpperCase());
                                int data = Integer.parseInt(args[8]);
                                Particle particle = Particle.valueOf(args[9].toUpperCase());
                                boolean endEarly = Boolean.parseBoolean(args[10]);
                                if (material != null) {
                                    if (particle != null) {
                                        FallingBlock fb = world.spawnFallingBlock(new Location(world, x, y, z), material, (byte)data);
                                        fb.setDropItem(false);
                                        fb.setMetadata("launch-block", new FixedMetadataValue(Pixeltects.getPackageManager(),true));
                                        fb.setVelocity(new Vector(xmot, ymot, zmot));
                                        LaunchObject lo = new LaunchObject(fb, particle, endEarly);
                                        lo.runTaskTimer(Pixeltects.getPackageManager(), 1L, 1L);
                                        if (sender instanceof Player) {
                                            sender.sendMessage(ChatColor.GREEN + "Launched an object from " + world.getName() + ", " + x + ", " + y + ", " + z + ".");
                                            sender.sendMessage(ChatColor.GRAY + "(Velocity: " + xmot + ", " + ymot + ", " + zmot + ")");
                                        }
                                    } else {
                                        sender.sendMessage(MessageOutput.INVALID_PARTICLE.getOutput());
                                        HelpUtil.sendParticleList(sender);
                                    }
                                } else {
                                    sender.sendMessage(MessageOutput.INVALID_MATERIAL.getOutput());
                                }
                            } catch (IllegalArgumentException e) {
                                sender.sendMessage(MessageOutput.INVALID_PARTICLE.getOutput());
                                HelpUtil.sendParticleList(sender);
                            }
                        }else{
                            sender.sendMessage(MessageOutput.INVALID_WORLD.getOutput());
                        }
                    }else{
                        sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
                    }
                }else{
                    sender.sendMessage(USAGE);
                }
            }else{
                sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
            }
        }
        return false;
    }

}



