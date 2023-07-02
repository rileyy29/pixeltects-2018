package com.pixeltects.core.shows.commands;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.shows.ShowManager;
import com.pixeltects.core.shows.effects.particle.ParticleLaser;
import com.pixeltects.core.shows.utils.HelpUtil;
import com.pixeltects.core.utils.math.MathArgUtils;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_Laser implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("laser")) {
            if(hasPermission(sender, "ALL")) {
                ShowManager showManager = Pixeltects.getPackageManager().getShowManager();
                if(args.length == 1) {
                    if(args[0].equalsIgnoreCase("list")) {
                        if(showManager.getRunningLasers().isEmpty()) {
                            sender.sendMessage(MessageOutput.COMMAND_LASER_NONACTIVE.getOutput());
                        }else{
                            StringBuilder stringBuilder = new StringBuilder();
                            for(String ids : showManager.getRunningLasers().keySet()) {
                                stringBuilder.append(ChatColor.GREEN + ids + ChatColor.GRAY + ", ");
                            }
                            sender.sendMessage(ChatColor.GREEN + stringBuilder.toString());
                        }
                    }else if(args[0].equalsIgnoreCase("stopall")) {
                        if(hasPermission(sender, "stopall")) {
                            for(ParticleLaser laser : showManager.getRunningLasers().values()) {
                               laser.end();
                            }
                            showManager.getRunningLasers().clear();
                            sender.sendMessage(MessageOutput.COMMAND_LASER_STOPPEDALL.getOutput());
                        }else{
                            sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                        }
                    }else{
                        sendHelp(sender);
                    }
                }else if(args.length == 2) {
                    if(args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("remove")) {
                        String id = args[1].toLowerCase();
                        if(showManager.getRunningLasers().containsKey(id)) {
                            ParticleLaser laser = showManager.getRunningLasers().get(id);
                            laser.end();
                            showManager.getRunningLasers().remove(id);
                            if (sender instanceof Player) {
                                sender.sendMessage(MessageOutput.COMMAND_LASER_STOPPED.getOutput().replace("%laser%", id));
                            }
                        }else{
                            if (sender instanceof Player) {
                                sender.sendMessage(MessageOutput.COMMAND_LASER_NOTRUNNING.getOutput().replace("%laser%", id));
                            }
                        }
                    }else  if(args[0].equalsIgnoreCase("stopgroup")) {
                        String ids = args[1].toLowerCase();
                        String[] idSplit = ids.split(",");
                        for(String id : idSplit) {
                            if(showManager.getRunningLasers().containsKey(id)) {
                                ParticleLaser laser = showManager.getRunningLasers().get(id);
                                laser.end();
                                showManager.getRunningLasers().remove(id);

                                if (sender instanceof Player) {
                                    sender.sendMessage(MessageOutput.COMMAND_LASER_STOPPED.getOutput().replace("%laser%", id));
                                }
                            }
                        }
                    }else{
                        sendHelp(sender);
                    }
                }else if(args.length == 6) {
                    if(args[0].equalsIgnoreCase("move")) {
                        String id = args[1].toLowerCase();
                        if(showManager.getRunningLasers().containsKey(id)) {
                            if(MathArgUtils.isDouble(args[2]) && MathArgUtils.isDouble(args[3]) && MathArgUtils.isDouble(args[4])
                                    && MathArgUtils.isInt(args[5])) {
                                double x = Double.parseDouble(args[2]);
                                double y = Double.parseDouble(args[3]);
                                double z = Double.parseDouble(args[4]);
                                int steps = Integer.parseInt(args[5]);

                                ParticleLaser laser = showManager.getRunningLasers().get(id);
                                laser.moveParticle(new Location(laser.getWorld(),x,y,z), steps);

                                if(sender instanceof Player) {
                                    sender.sendMessage(ChatColor.GREEN + "Moved laser with id '" + id + "' in " + steps + " steps.");
                                    sender.sendMessage(ChatColor.GRAY + "(New Target Location: " + x + ", " + y + ", " + z+")");
                                }
                            }else{
                                sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
                            }
                        }else{
                            sender.sendMessage(MessageOutput.COMMAND_LASER_NOTRUNNING.getOutput().replace("%laser%", id));
                        }
                    }else if(args[0].equalsIgnoreCase("movegroup")) {
                        String ids = args[1].toLowerCase();
                        String[] idSplit = ids.split(",");
                        if(MathArgUtils.isDouble(args[2]) && MathArgUtils.isDouble(args[3]) && MathArgUtils.isDouble(args[4])
                                && MathArgUtils.isInt(args[5])) {
                            double x = Double.parseDouble(args[2]);
                            double y = Double.parseDouble(args[3]);
                            double z = Double.parseDouble(args[4]);
                            int steps = Integer.parseInt(args[5]);

                            for (String id : idSplit) {
                                if (showManager.getRunningLasers().containsKey(id)) {
                                    ParticleLaser laser = showManager.getRunningLasers().get(id);
                                    laser.moveParticle(new Location(laser.getWorld(),x,y,z), steps);
                                } else {
                                    sender.sendMessage(MessageOutput.COMMAND_LASER_NOTRUNNING.getOutput().replace("%laser%", id));
                                    break;
                                }
                            }

                            if(sender instanceof Player) {
                                sender.sendMessage(ChatColor.GREEN + "Moved lasers: " + ids);
                                sender.sendMessage(ChatColor.GRAY + "(New Target Location: " + x + ", " + y + ", " + z +")");
                            }
                        }else{
                            sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
                        }
                    }else{
                        sendHelp(sender);
                    }
                }else if(args.length >= 10) {
                    if(args[0].equalsIgnoreCase("start")) {
                        if (MathArgUtils.isDouble(args[3]) &&
                                MathArgUtils.isDouble(args[4]) &&
                                MathArgUtils.isDouble(args[5]) &&
                                MathArgUtils.isDouble(args[6]) &&
                                MathArgUtils.isDouble(args[7]) &&
                                MathArgUtils.isDouble(args[8])) {

                            World world = Bukkit.getWorld(args[2]);
                            if(world != null) {
                                String id = args[1].toLowerCase();
                                if(showManager.getRunningLasers().containsKey(id)) {
                                    sender.sendMessage(MessageOutput.COMMAND_LASER_ALREADYEXISTS.getOutput());
                                }else{
                                    double x = Double.parseDouble(args[3]);
                                    double y = Double.parseDouble(args[4]);
                                    double z = Double.parseDouble(args[5]);
                                    double x2 = Double.parseDouble(args[6]);
                                    double y2 = Double.parseDouble(args[7]);
                                    double z2 = Double.parseDouble(args[8]);

                                    String mat = args[9].toUpperCase();
                                    try {
                                        Particle particle = Particle.valueOf(args[9].toUpperCase());
                                        if (particle != null) {
                                            Location start = new Location(world, x, y, z);
                                            Location end = new Location(world, x2, y2, z2);

                                            String locString = ChatColor.GRAY + "(Location: " + x + ", " + y + ", " + z + ". Targeting: " + x2 + ", " + y2 + ", " + z2 + ")";

                                            if(args.length == 11) {
                                                int ticks = Integer.parseInt(args[10]);
                                                ParticleLaser laser = new ParticleLaser();
                                                laser.showLaser(id, particle, start, end, ticks);
                                                showManager.getRunningLasers().put(id, laser);
                                                if(sender instanceof Player) {
                                                    sender.sendMessage(ChatColor.GREEN + "Started laser with the id '" + id + "' that will run for " + ticks + " ticks.");
                                                    sender.sendMessage(locString);
                                                }
                                            }else{
                                                ParticleLaser laser = new ParticleLaser();
                                                laser.showLaser(id, particle, start, end);
                                                showManager.getRunningLasers().put(id, laser);
                                                if(sender instanceof Player) {
                                                    sender.sendMessage(ChatColor.GREEN + "Started laser with the id '" + id + "' that will run until it is stopped.");
                                                    sender.sendMessage(locString);
                                                }
                                            }
                                        }else{
                                            sender.sendMessage(MessageOutput.INVALID_PARTICLE.getOutput());
                                            HelpUtil.sendParticleList(sender);
                                        }
                                    } catch (IllegalArgumentException e) {
                                        sender.sendMessage(MessageOutput.INVALID_PARTICLE.getOutput());
                                        HelpUtil.sendParticleList(sender);
                                    }
                                }
                            }else{
                                sender.sendMessage(MessageOutput.INVALID_WORLD.getOutput());
                            }
                        }else{
                            sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
                        }
                    }else if(args[0].equalsIgnoreCase("startgroup")){
                        if (MathArgUtils.isDouble(args[3]) &&
                                MathArgUtils.isDouble(args[4]) &&
                                MathArgUtils.isDouble(args[5]) &&
                                MathArgUtils.isDouble(args[6]) &&
                                MathArgUtils.isDouble(args[7]) &&
                                MathArgUtils.isDouble(args[8])) {

                            World world = Bukkit.getWorld(args[2]);
                            if(world != null) {
                                String ids = args[1].toLowerCase();
                                String[] idSplit = ids.split(",");

                                double x = Double.parseDouble(args[3]);
                                double y = Double.parseDouble(args[4]);
                                double z = Double.parseDouble(args[5]);
                                double x2 = Double.parseDouble(args[6]);
                                double y2 = Double.parseDouble(args[7]);
                                double z2 = Double.parseDouble(args[8]);

                                String mat = args[9].toUpperCase();
                                try {
                                    Particle particle = Particle.valueOf(args[9].toUpperCase());
                                    if (particle != null) {
                                        Location start = new Location(world, x, y, z);
                                        Location end = new Location(world, x2, y2, z2);

                                        String locString = ChatColor.GRAY + "(Location: " + x + ", " + y + ", " + z + ". Targeting: " + x2 + ", " + y2 + ", " + z2 + ")";
                                        String tickString = ChatColor.GRAY + "The lasers will run until stopped.";

                                        int ticks = -1;
                                        if(args.length == 11) {
                                            ticks = Integer.parseInt(args[10]);
                                            tickString = ChatColor.GRAY + "The lasers will run for " + ticks + " ticks.";
                                        }

                                        for(String id : idSplit) {
                                            if(showManager.getRunningLasers().containsKey(id)) {
                                                sender.sendMessage(MessageOutput.COMMAND_LASER_ALREADYEXISTS_SPECIFIC.getOutput().replace("%laser%", id));
                                                break;
                                            }else{
                                                ParticleLaser laser = new ParticleLaser();
                                                laser.showLaser(id, particle, start, end, ticks);
                                                showManager.getRunningLasers().put(id, laser);
                                            }
                                        }

                                        if(sender instanceof Player) {
                                            sender.sendMessage(ChatColor.GREEN + "Started lasers: " + ids);
                                            sender.sendMessage(locString);
                                            sender.sendMessage(tickString);
                                        }

                                        }else{
                                            sender.sendMessage(MessageOutput.INVALID_PARTICLE.getOutput());
                                            HelpUtil.sendParticleList(sender);
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
        sender.sendMessage(ChatColor.RED + "Correct usage: /laser start <id> <world> <startx> <starty> <startz> <endx> <endy> <endz> <particle> [ticks]");
        sender.sendMessage(ChatColor.RED + "Correct usage: /laser startgroup <id1,id2,id3,etc> <world> <startx> <starty> <startz> <endx> <endy> <endz> <particle> [ticks]");
        sender.sendMessage(ChatColor.RED + "Correct usage: /laser move <id> <x> <y> <z> <steps>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /laser movegroup <id1,id2,id3,etc> <x> <y> <z> <steps>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /laser stop <id>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /laser stopgroup <id1,id2,id3,etc>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /laser list");

        if(sender.isOp() || sender.hasPermission("pixeltects.show.admin")) {
            sender.sendMessage(ChatColor.RED + "Correct usage: /laser stopall");
        }
    }

    public boolean hasPermission(CommandSender sender, String arg) {
        if(arg.equalsIgnoreCase("stopall")) {
            if(sender.isOp() || sender.hasPermission("pixeltects.show.admin")) {
                return true;
            }
            return false;
        }else{
            if (sender.isOp() || sender.hasPermission("pixeltects.show.laser")
                    || sender.hasPermission("pixeltects.show")) {
                return true;
            }
        }
        return false;
    }

}


