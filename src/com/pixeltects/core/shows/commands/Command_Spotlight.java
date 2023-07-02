package com.pixeltects.core.shows.commands;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.shows.ShowManager;
import com.pixeltects.core.shows.effects.spotlight.Beam;
import com.pixeltects.core.utils.math.MathArgUtils;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_Spotlight implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spotlight")) {
            if(hasPermission(sender, "ALL")) {
                ShowManager showManager = Pixeltects.getPackageManager().getShowManager();
                if(args.length == 1) {
                    if(args[0].equalsIgnoreCase("list")) {
                        if(showManager.getRunningSpotlights().isEmpty()) {
                            sender.sendMessage(MessageOutput.COMMAND_SPOTLIGHT_NONACTIVE.getOutput());
                        }else{
                            StringBuilder stringBuilder = new StringBuilder();
                            for(String ids : showManager.getRunningSpotlights().keySet()) {
                                stringBuilder.append(ChatColor.GREEN + ids + ChatColor.GRAY + ", ");
                            }
                            sender.sendMessage(ChatColor.GREEN + stringBuilder.toString());
                        }
                    }else if(args[0].equalsIgnoreCase("stopall")) {
                        if(hasPermission(sender, "stopall")) {
                            for(Beam beam : showManager.getRunningSpotlights().values()) {
                                beam.kill();
                            }
                            showManager.getRunningSpotlights().clear();
                            sender.sendMessage(MessageOutput.COMMAND_SPOTLIGHT_STOPPEDALL.getOutput());
                        }else{
                            sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                        }
                    }else{
                        sendHelp(sender);
                    }
                }else if(args.length == 2) {
                    if(args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("remove")) {
                        String id = args[1].toLowerCase();
                        if(showManager.getRunningSpotlights().containsKey(id)) {
                            Beam beam = showManager.getRunningSpotlights().get(id);
                            beam.kill();
                            showManager.getRunningSpotlights().remove(id);
                            if (sender instanceof Player) {
                                sender.sendMessage(MessageOutput.COMMAND_SPOTLIGHT_STOPPED.getOutput().replace("%spotlight%", id));
                            }
                            }else{
                            if (sender instanceof Player) {
                                sender.sendMessage(MessageOutput.COMMAND_SPOTLIGHT_NOTRUNNING.getOutput().replace("%spotlight%", id));
                            }
                            }
                    }else  if(args[0].equalsIgnoreCase("stopgroup")) {
                        String ids = args[1].toLowerCase();
                        String[] idSplit = ids.split(",");
                        for(String id : idSplit) {
                            if(showManager.getRunningSpotlights().containsKey(id)) {
                                Beam beam = showManager.getRunningSpotlights().get(id);
                                beam.kill();
                                showManager.getRunningSpotlights().remove(id);

                                if (sender instanceof Player) {
                                    sender.sendMessage(MessageOutput.COMMAND_SPOTLIGHT_STOPPED.getOutput().replace("%spotlight%", id));
                                }
                            }
                        }
                    }else{
                        sendHelp(sender);
                    }
                }else if(args.length == 6) {
                    if(args[0].equalsIgnoreCase("move")) {
                        String id = args[1].toLowerCase();
                        if(showManager.getRunningSpotlights().containsKey(id)) {
                            if(MathArgUtils.isDouble(args[2]) && MathArgUtils.isDouble(args[3]) && MathArgUtils.isDouble(args[4])
                                    && MathArgUtils.isInt(args[5])) {
                                double x = Double.parseDouble(args[2]);
                                double y = Double.parseDouble(args[3]);
                                double z = Double.parseDouble(args[4]);
                                double steps = Double.parseDouble(args[5]);

                                Beam beam = showManager.getRunningSpotlights().get(id);
                                beam.moveBeam(new Location(beam.getWorld(), x,y,z), steps);

                                if(sender instanceof Player) {
                                    sender.sendMessage(ChatColor.GREEN + "Moved spotlight with id '" + id + "' in " + steps + " steps.");
                                    sender.sendMessage(ChatColor.GRAY + "(New Target Location: " + x + ", " + y + ", " + z +")");
                                }
                            }else{
                                sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
                            }
                        }else{
                            sender.sendMessage(MessageOutput.COMMAND_SPOTLIGHT_NOTRUNNING.getOutput().replace("%spotlight%", id));
                        }
                    }else if(args[0].equalsIgnoreCase("movegroup")) {
                        String ids = args[1].toLowerCase();
                        String[] idSplit = ids.split(",");
                        if(MathArgUtils.isDouble(args[2]) && MathArgUtils.isDouble(args[3]) && MathArgUtils.isDouble(args[4])
                                && MathArgUtils.isInt(args[5])) {
                            double x = Double.parseDouble(args[2]);
                            double y = Double.parseDouble(args[3]);
                            double z = Double.parseDouble(args[4]);
                            double steps = Double.parseDouble(args[5]);

                            for (String id : idSplit) {
                                if (showManager.getRunningSpotlights().containsKey(id)) {
                                    Beam beam = showManager.getRunningSpotlights().get(id);
                                    beam.moveBeam(new Location(beam.getWorld(), x,y,z), steps);
                                } else {
                                    sender.sendMessage(MessageOutput.COMMAND_SPOTLIGHT_NOTRUNNING.getOutput().replace("%spotlight%", id));
                                    break;
                                }
                            }

                            if(sender instanceof Player) {
                                sender.sendMessage(ChatColor.GREEN + "Moved spotlights: " + ids);
                                sender.sendMessage(ChatColor.GRAY + "(New Target Location: " + x + ", " + y + ", " + z+")");
                            }
                        }else{
                            sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
                        }
                    }else{
                        sendHelp(sender);
                    }
                }else if(args.length >= 9) {
                    if(args[0].equalsIgnoreCase("create")) {
                        if (MathArgUtils.isDouble(args[3]) &&
                                MathArgUtils.isDouble(args[4]) &&
                                MathArgUtils.isDouble(args[5]) &&
                                MathArgUtils.isDouble(args[6]) &&
                                MathArgUtils.isDouble(args[7]) &&
                                MathArgUtils.isDouble(args[8])) {

                            World world = Bukkit.getWorld(args[2]);
                            if(world != null) {
                                String id = args[1].toLowerCase();
                                if(showManager.getRunningSpotlights().containsKey(id)) {
                                    sender.sendMessage(MessageOutput.COMMAND_SPOTLIGHT_ALREADYEXISTS.getOutput());
                                }else{
                                    double x = Double.parseDouble(args[3]);
                                    double y = Double.parseDouble(args[4]);
                                    double z = Double.parseDouble(args[5]);
                                    double x2 = Double.parseDouble(args[6]);
                                    double y2 = Double.parseDouble(args[7]);
                                    double z2 = Double.parseDouble(args[8]);

                                    Beam beam = new Beam(id,new Location(world,x,y,z), new Location(world,x2,y2,z2));
                                    beam.spawnCrystal();
                                    showManager.getRunningSpotlights().put(id, beam);
                                    if(sender instanceof Player) {
                                        sender.sendMessage(ChatColor.GREEN + "Created spotlight with the id '" + id + "' that will run until it is stopped.");
                                        sender.sendMessage(ChatColor.GRAY + "(Crystal Location: " + x + ", " + y + ", " + z + ")");
                                        sender.sendMessage(ChatColor.GRAY + "(Target Location: " + x2 + ", " + y2 + ", " + z2 + ")");
                                    }

                                }
                            }else{
                                sender.sendMessage(MessageOutput.INVALID_WORLD.getOutput());
                            }
                        }else{
                            sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
                        }
                    }else if(args[0].equalsIgnoreCase("creategroup")){
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

                                for(String id : idSplit) {
                                    if (showManager.getRunningSpotlights().containsKey(id)) {
                                        sender.sendMessage(MessageOutput.COMMAND_SPOTLIGHT_ALREADYEXISTS_SPECIFIC.getOutput().replace("%spotlight%", id));
                                        break;
                                    }
                                    Beam beam = new Beam(id,new Location(world,x,y,z), new Location(world,x2,y2,z2));
                                    beam.spawnCrystal();
                                    showManager.getRunningSpotlights().put(id, beam);
                                }

                                if(sender instanceof Player) {
                                    sender.sendMessage(ChatColor.GREEN + "Created spotlights: " + ids);
                                    sender.sendMessage(ChatColor.GRAY + "The spotlight will run until stopped.");
                                    sender.sendMessage(ChatColor.GRAY + "(Crystal Location: " + x + ", " + y + ", " + z + ")");
                                    sender.sendMessage(ChatColor.GRAY + "(Target Location: " + x2 + ", " + y2 + ", " + z2 + ")");
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
        sender.sendMessage(ChatColor.RED + "Correct usage: /spotlight create <id> <world> <x> <y> <z> <beamx> <beamy> <beamz>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /spotlight creategroup <id> <world> <x> <y> <z> <beamx> <beamy> <beamz>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /spotlight move <id> <x> <y> <z> <steps>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /spotlight movegroup <id1,id2,id3,etc> <x> <y> <z> <steps>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /spotlight stop <id>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /spotlight stopgroup <id1,id2,id3,etc>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /spotlight list");

        if(sender.isOp() || sender.hasPermission("pixeltects.show.admin")) {
            sender.sendMessage(ChatColor.RED + "Correct usage: /spotlight stopall");
        }
    }

    public boolean hasPermission(CommandSender sender, String arg) {
        if(arg.equalsIgnoreCase("stopall")) {
            if(sender.isOp() || sender.hasPermission("pixeltects.show.admin")) {
                return true;
            }
            return false;
        }else{
            if (sender.isOp() || sender.hasPermission("pixeltects.show.spotlight")
                    || sender.hasPermission("pixeltects.show")) {
                return true;
            }
        }
        return false;
    }


}

