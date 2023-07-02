package com.pixeltects.core.shows.commands;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.shows.ShowManager;
import com.pixeltects.core.shows.effects.fountain.Fountain;
import com.pixeltects.core.utils.math.MathArgUtils;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Command_Fountain implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fountain")) {
            if(hasPermission(sender, "ALL")) {
                ShowManager showManager = Pixeltects.getPackageManager().getShowManager();
                if(args.length == 1) {
                    if(args[0].equalsIgnoreCase("list")) {
                        if(showManager.getRunningFountains().isEmpty()) {
                            sender.sendMessage(MessageOutput.COMMAND_FOUNTAIN_NONACTIVE.getOutput());
                        }else{
                            StringBuilder stringBuilder = new StringBuilder();
                            for(String ids : showManager.getRunningFountains().keySet()) {
                                stringBuilder.append(ChatColor.GREEN + ids + ChatColor.GRAY + ", ");
                            }
                            sender.sendMessage(ChatColor.GREEN + stringBuilder.toString());
                        }
                    }else if(args[0].equalsIgnoreCase("stopall")) {
                        if(hasPermission(sender, "stopall")) {
                            for(Fountain fountain : showManager.getRunningFountains().values()) {
                                fountain.end();
                            }
                            showManager.getRunningFountains().clear();
                            sender.sendMessage(MessageOutput.COMMAND_FOUNTAIN_STOPPEDALL.getOutput());
                        }else{
                            sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                        }
                    }else{
                        sendHelp(sender);
                    }
                }else if(args.length == 2) {
                    if(args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("remove")) {
                        String id = args[1].toLowerCase();
                        if(showManager.getRunningFountains().containsKey(id)) {
                            Fountain fountain = showManager.getRunningFountains().get(id);
                            fountain.end();
                            showManager.getRunningFountains().remove(id);
                            if (sender instanceof Player) {
                                sender.sendMessage(MessageOutput.COMMAND_FOUNTAIN_STOPPED.getOutput().replace("%fountain%", id));
                            }
                        }else{
                            if (sender instanceof Player) {
                                sender.sendMessage(MessageOutput.COMMAND_FOUNTAIN_NOTRUNNING.getOutput().replace("%fountain%", id));
                            }
                        }
                    }else  if(args[0].equalsIgnoreCase("stopgroup")) {
                        String ids = args[1].toLowerCase();
                        String[] idSplit = ids.split(",");
                        for(String id : idSplit) {
                            if(showManager.getRunningFountains().containsKey(id)) {
                                Fountain fountain = showManager.getRunningFountains().get(id);
                                fountain.end();
                                showManager.getRunningFountains().remove(id);

                                if (sender instanceof Player) {
                                    sender.sendMessage(MessageOutput.COMMAND_FOUNTAIN_STOPPED.getOutput().replace("%fountain%", id));
                                }
                            }else{
                                sender.sendMessage(MessageOutput.COMMAND_FOUNTAIN_NOTRUNNING.getOutput().replace("%fountain%", id));
                                break;
                            }
                        }
                    }else{
                        sendHelp(sender);
                    }
                }else if (args.length == 4) {
                    if(args[0].equalsIgnoreCase("changemat")) {
                        String id = args[1].toLowerCase();
                        if(showManager.getRunningFountains().containsKey(id)) {
                            Fountain fountain = showManager.getRunningFountains().get(id);

                            String mat = args[2].toUpperCase();
                            try{
                            Material material = Material.getMaterial(mat);

                            if((material != null) && material != Material.AIR) {
                                byte data = Byte.parseByte(args[3]);
                                fountain.changeMaterial(material,data);

                                if (sender instanceof Player) {
                                    sender.sendMessage(MessageOutput.COMMAND_FOUNTAIN_CHANGEDMATERIAL.getOutput().replace("%fountain%", id));
                                }
                            }else{
                                sender.sendMessage(MessageOutput.INVALID_MATERIAL.getOutput());
                            }
                            } catch (IllegalArgumentException e) {
                                sender.sendMessage(MessageOutput.INVALID_MATERIAL.getOutput());
                            }
                        }else{
                            if (sender instanceof Player) {
                                sender.sendMessage(MessageOutput.COMMAND_FOUNTAIN_NOTRUNNING.getOutput().replace("%fountain%", id));
                            }
                        }
                    }else if(args[0].equalsIgnoreCase("changematgroup")) {
                        String ids = args[1].toLowerCase();
                        String[] idSplit = ids.split(",");

                        String mat = args[2].toUpperCase();
                        try{
                        Material material = Material.getMaterial(mat);
                        byte data = Byte.parseByte(args[3]);
                        if((material != null) && material != Material.AIR) {
                            for (String id : idSplit) {
                                if(showManager.getRunningFountains().containsKey(id)) {
                                    Fountain fountain = showManager.getRunningFountains().get(id);
                                    fountain.changeMaterial(material,data);
                                }else{
                                    sender.sendMessage(MessageOutput.COMMAND_FOUNTAIN_NOTRUNNING.getOutput().replace("%fountain%", id));
                                    break;
                                }
                            }
                        }else{
                            sender.sendMessage(MessageOutput.INVALID_MATERIAL.getOutput());
                        }
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(MessageOutput.INVALID_MATERIAL.getOutput());
                    }
                    }else{
                        sendHelp(sender);
                    }
                }else if(args.length == 6) {
                    if(args[0].equalsIgnoreCase("move")) {
                        String id = args[1].toLowerCase();
                        if(showManager.getRunningFountains().containsKey(id)) {
                            if(MathArgUtils.isFloat(args[2]) && MathArgUtils.isFloat(args[3]) && MathArgUtils.isFloat(args[4])
                            && MathArgUtils.isInt(args[5])) {
                                float xmot = Float.parseFloat(args[2]);
                                float ymot = Float.parseFloat(args[3]);
                                float zmot = Float.parseFloat(args[4]);
                                int steps = Integer.parseInt(args[5]);

                                Fountain fountain = showManager.getRunningFountains().get(id);
                                fountain.moveFountain(new Vector(xmot,ymot,zmot), steps);

                                if(sender instanceof Player) {
                                    sender.sendMessage(ChatColor.GREEN + "Moved fountain with id '" + id + "' in " + steps + " steps.");
                                    sender.sendMessage(ChatColor.GRAY + "(New Velocity: " + xmot + ", " + ymot + ", " + zmot+")");
                                }
                            }else{
                                sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
                            }
                        }else{
                            sender.sendMessage(MessageOutput.COMMAND_FOUNTAIN_NOTRUNNING.getOutput().replace("%fountain%", id));
                        }
                    }else if(args[0].equalsIgnoreCase("movegroup")) {
                        String ids = args[1].toLowerCase();
                        String[] idSplit = ids.split(",");
                        if(MathArgUtils.isFloat(args[2]) && MathArgUtils.isFloat(args[3]) && MathArgUtils.isFloat(args[4])
                                && MathArgUtils.isInt(args[5])) {
                            float xmot = Float.parseFloat(args[2]);
                            float ymot = Float.parseFloat(args[3]);
                            float zmot = Float.parseFloat(args[4]);
                            int steps = Integer.parseInt(args[5]);

                            for (String id : idSplit) {
                                if (showManager.getRunningFountains().containsKey(id)) {
                                    Fountain fountain = showManager.getRunningFountains().get(id);
                                    fountain.moveFountain(new Vector(xmot, ymot, zmot), steps);
                                } else {
                                    sender.sendMessage(MessageOutput.COMMAND_FOUNTAIN_NOTRUNNING.getOutput().replace("%fountain%", id));
                                    break;
                                }
                            }

                            if(sender instanceof Player) {
                                sender.sendMessage(ChatColor.GREEN + "Moved fountains: " + ids);
                                sender.sendMessage(ChatColor.GRAY + "(New Velocity: " + xmot + ", " + ymot + ", " + zmot+")");
                            }
                        }else{
                            sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
                        }
                    }else{
                        sendHelp(sender);
                    }
                }else if(args.length >= 11) {
                    if(args[0].equalsIgnoreCase("create")) {
                        if (MathArgUtils.isDouble(args[3]) &&
                                MathArgUtils.isDouble(args[4]) &&
                                MathArgUtils.isDouble(args[5]) &&
                                MathArgUtils.isFloat(args[6]) &&
                                MathArgUtils.isFloat(args[7]) &&
                                MathArgUtils.isFloat(args[8]) &&
                                MathArgUtils.isInt(args[10])) {

                            World world = Bukkit.getWorld(args[2]);
                            if(world != null) {
                                String id = args[1].toLowerCase();
                                if(showManager.getRunningFountains().containsKey(id)) {
                                    sender.sendMessage(MessageOutput.COMMAND_FOUNTAIN_ALREADYEXISTS.getOutput());
                                }else{
                                    double x = Double.parseDouble(args[3]);
                                    double y = Double.parseDouble(args[4]);
                                    double z = Double.parseDouble(args[5]);
                                    float xmot = Float.parseFloat(args[6]);
                                    float ymot = Float.parseFloat(args[7]);
                                    float zmot = Float.parseFloat(args[8]);
                                    String mat = args[9].toUpperCase();
                                    try {
                                        Material material = Material.getMaterial(mat.toUpperCase());
                                        if (material != null) {
                                            int data = Integer.parseInt(args[10]);

                                            if(args.length == 12) {
                                                int ticks = Integer.parseInt(args[11]);
                                                Fountain fountain = new Fountain(id,x,y,z,world.getName(),xmot,ymot,zmot,material,data, ticks);
                                                showManager.getRunningFountains().put(id, fountain);
                                                if(sender instanceof Player) {
                                                    sender.sendMessage(ChatColor.GREEN + "Created fountain with the id '" + id + "' that will run for " + ticks + " ticks.");
                                                    sender.sendMessage(ChatColor.GRAY + "(Velocity: " + xmot + ", " + ymot + ", " + zmot + ")");
                                                }
                                            }else{
                                                Fountain fountain = new Fountain(id,x,y,z,world.getName(),xmot,ymot,zmot,material,data);
                                                showManager.getRunningFountains().put(id, fountain);
                                                if(sender instanceof Player) {
                                                    sender.sendMessage(ChatColor.GREEN + "Created fountain with the id '" + id + "' that will run until it is stopped.");
                                                    sender.sendMessage(ChatColor.GRAY + "(Velocity: " + xmot + ", " + ymot + ", " + zmot + ")");
                                                }
                                            }

                                        }else{
                                            sender.sendMessage(MessageOutput.INVALID_MATERIAL.getOutput());
                                        }
                                    } catch (IllegalArgumentException e) {
                                        sender.sendMessage(MessageOutput.INVALID_MATERIAL.getOutput());
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
                                MathArgUtils.isFloat(args[6]) &&
                                MathArgUtils.isFloat(args[7]) &&
                                MathArgUtils.isFloat(args[8]) &&
                                MathArgUtils.isInt(args[10])) {

                            World world = Bukkit.getWorld(args[2]);
                            if (world != null) {
                                String ids = args[1].toLowerCase();
                                String[] idSplit = ids.split(",");

                                double x = Double.parseDouble(args[3]);
                                double y = Double.parseDouble(args[4]);
                                double z = Double.parseDouble(args[5]);
                                float xmot = Float.parseFloat(args[6]);
                                float ymot = Float.parseFloat(args[7]);
                                float zmot = Float.parseFloat(args[8]);
                                String mat = args[9].toUpperCase();
                                try {
                                    Material material = Material.getMaterial(mat.toUpperCase());
                                    if (material != null) {
                                        int data = Integer.parseInt(args[10]);

                                        String tickString = ChatColor.GRAY + "The lasers will run until stopped.";

                                        int ticks = -1;
                                        if(args.length == 12) {
                                            ticks = Integer.parseInt(args[11]);
                                            tickString = ChatColor.GRAY + "The lasers will run for " + ticks + " ticks.";
                                        }

                                        for(String id : idSplit) {
                                            if(showManager.getRunningFountains().containsKey(id)) {
                                                sender.sendMessage(MessageOutput.COMMAND_FOUNTAIN_ALREADYEXISTS_SPECIFIC.getOutput().replace("%fountain%", id));
                                                break;
                                            }else{
                                                Fountain fountain = new Fountain(id,x,y,z,world.getName(),xmot,ymot,zmot,material,data, ticks);
                                                showManager.getRunningFountains().put(id, fountain);
                                            }
                                        }

                                        if(sender instanceof Player) {
                                            sender.sendMessage(ChatColor.GREEN + "Created fountains: " + ids);
                                            sender.sendMessage(ChatColor.GRAY + "(Velocity: " + xmot + ", " + ymot + ", " + zmot + ")");
                                            sender.sendMessage(tickString);
                                        }
                                    }else{
                                        sender.sendMessage(MessageOutput.INVALID_MATERIAL.getOutput());
                                    }
                                } catch (IllegalArgumentException e) {
                                    sender.sendMessage(MessageOutput.INVALID_MATERIAL.getOutput());
                                }
                            } else {
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
        sender.sendMessage(ChatColor.RED + "Correct usage: /fountain create <id> <world> <x> <y> <z> <xmotion> <ymotion> <zmotion> <material> <data> [ticks]");
        sender.sendMessage(ChatColor.RED + "Correct usage: /fountain creategroup <id1,id2,id3,etc> <world> <x> <y> <z> <xmotion> <ymotion> <zmotion> <material> <data> [ticks]");
        sender.sendMessage(ChatColor.RED + "Correct usage: /fountain move <id> <xmotion> <ymotion> <zmotion> <steps>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /fountain movegroup <id1,id2,id3,etc> <xmotion> <ymotion> <zmotion> <steps>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /fountain changemat <id> <material> <data>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /fountain changematgroup <id1,id2,id3,etc> <material> <data>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /fountain stop <id>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /fountain stopgroup <id1,id2,id3,etc>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /fountain list");

        if(sender.isOp() || sender.hasPermission("pixeltects.show.admin")) {
            sender.sendMessage(ChatColor.RED + "Correct usage: /fountain stopall");
        }
    }

    public boolean hasPermission(CommandSender sender, String arg) {
        if(arg.equalsIgnoreCase("stopall")) {
            if(sender.isOp() || sender.hasPermission("pixeltects.show.admin")) {
                return true;
            }
            return false;
        }else{
            if (sender.isOp() || sender.hasPermission("pixeltects.show.fountain")
                    || sender.hasPermission("pixeltects.show")) {
                return true;
            }
        }
        return false;
    }

}


