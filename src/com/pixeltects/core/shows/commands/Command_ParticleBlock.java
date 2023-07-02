package com.pixeltects.core.shows.commands;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.shows.ShowManager;
import com.pixeltects.core.shows.effects.particle.ParticleBlock;
import com.pixeltects.core.shows.utils.HelpUtil;
import com.pixeltects.core.utils.math.MathArgUtils;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_ParticleBlock implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("particleblock")) {
            if(sender.isOp() || sender.hasPermission("pixeltects.show.particleblock")
            || sender.hasPermission("pixeltects.show")) {
                ShowManager showManager = Pixeltects.getPackageManager().getShowManager();
                if(args.length == 1) {
                   if(args[0].equalsIgnoreCase("stopall")) {
                        if(sender.isOp() || sender.hasPermission("pixeltects.show.admin")) {
                            for(ParticleBlock block : showManager.getRunningBlockParticles().values()) {
                                block.cancel();
                            }
                            showManager.getRunningBlockParticles().clear();
                            sender.sendMessage(MessageOutput.COMMAND_PARTICLEBLOCK_STOPPEDALL.getOutput());
                        }else{
                            sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                        }
                   }else if(args[0].equalsIgnoreCase("list")) {
                       if(showManager.getRunningBlockParticles().isEmpty()) {
                           sender.sendMessage(MessageOutput.COMMAND_PARTICLEBLOCK_NONACTIVE.getOutput());
                       }else{
                           StringBuilder stringBuilder = new StringBuilder();
                           for(String ids : showManager.getRunningBlockParticles().keySet()) {
                               stringBuilder.append(ChatColor.GREEN + ids + ChatColor.GRAY + ", ");
                           }
                           sender.sendMessage(ChatColor.GREEN + stringBuilder.toString());
                       }
                   }else{
                       sendHelp(sender);
                   }
                }else if(args.length == 2){
                    if(args[0].equalsIgnoreCase("stop")) {
                        String id = args[1].toLowerCase();
                        if(showManager.getRunningBlockParticles().containsKey(id)) {
                            ParticleBlock particleBlock = showManager.getRunningBlockParticles().get(id);
                            particleBlock.cancel();
                            showManager.getRunningBlockParticles().remove(id);

                            if(sender instanceof Player) {
                                sender.sendMessage(MessageOutput.COMMAND_PARTICLEBLOCK_STOPPED.getOutput().replace("%block%", id));
                            }
                        }else{
                            sender.sendMessage(MessageOutput.COMMAND_PARTICLEBLOCK_NOTRUNNING.getOutput().replace("%block%", id));
                        }
                    }else{
                        sendHelp(sender);
                    }
                }else if(args.length >= 9) {
                    if(args[0].equalsIgnoreCase("start")) {
                        String id = args[1].toLowerCase();
                        if(showManager.getRunningBlockParticles().containsKey(id)) {
                            sender.sendMessage(MessageOutput.COMMAND_PARTICLEBLOCK_ALREADYEXISTS.getOutput());
                        }else{
                            World world = Bukkit.getWorld(args[2]);
                            if (world != null) {
                                if (MathArgUtils.isDouble(args[3]) && MathArgUtils.isDouble(args[4]) && MathArgUtils.isDouble(args[5])
                                        && MathArgUtils.isInt(args[8])) {

                                    double x = Double.parseDouble(args[3]);
                                    double y = Double.parseDouble(args[4]);
                                    double z = Double.parseDouble(args[5]);

                                    Location blockLocation = new Location(world, x, y, z);
                                    int ticks = Integer.parseInt(args[8]);

                                    String type = args[7]; //Full or Outline

                                    try {
                                        Particle particle = Particle.valueOf(args[6].toUpperCase());
                                        if (particle != null) {
                                            ParticleBlock particleBlock = new ParticleBlock(id, particle, blockLocation, ticks, type);
                                            particleBlock.runTaskTimer(Pixeltects.getPackageManager(), 0L, 1L);

                                            showManager.getRunningBlockParticles().put(id, particleBlock);
                                            if (sender instanceof Player) {
                                                sender.sendMessage(ChatColor.GREEN + "You have set a particle block at the location: w: " + world.getName() + ", x: " + x + ", y: " + y + ", z: " + z);
                                                sender.sendMessage(ChatColor.GRAY + "Type: " + StringUtils.capitalize(type));
                                                sender.sendMessage(ChatColor.GRAY + "Ticks: " + ticks);
                                                sender.sendMessage(ChatColor.GRAY + "Particle: " + particle.name());
                                            }
                                        } else {
                                            sender.sendMessage(MessageOutput.INVALID_PARTICLE.getOutput());
                                            HelpUtil.sendParticleList(sender);
                                        }
                                    } catch (IllegalArgumentException e) {
                                        sender.sendMessage(MessageOutput.INVALID_PARTICLE.getOutput());
                                        HelpUtil.sendParticleList(sender);
                                    }
                                } else {
                                    sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
                                }
                            } else {
                                sender.sendMessage(MessageOutput.INVALID_WORLD.getOutput());
                            }
                        }
                    }else{
                        sendHelp(sender);
                    }
                }else{
                    sendHelp(sender);
                }
            }else{
                sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
            }
        }
        return false;
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Correct usage: /particleblock start <id> <world> <x> <y> <z> <particle> <full/outline> <ticks>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /particleblock stop <id>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /particleblock list");

        if(sender.isOp() || sender.hasPermission("pixeltects.show.admin")) {
            sender.sendMessage(ChatColor.RED + "Correct usage: /particleblock stopall");
        }
    }
}
