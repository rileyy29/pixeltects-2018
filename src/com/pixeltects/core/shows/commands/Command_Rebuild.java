package com.pixeltects.core.shows.commands;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.shows.utils.RebuildUtil;
import com.pixeltects.core.utils.math.MathArgUtils;
import com.pixeltects.core.utils.messages.MessageOutput;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;

public class Command_Rebuild implements CommandExecutor {

    //TODO: Eventually create a new rebuild program to handle schematics separately to WorldEdit schems

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rebuild")) {
            if(sender.isOp() || sender.hasPermission("pixeltects.show.rebuild")
                    || sender.hasPermission("pixeltects.show")) {
                if(args.length >= 5) {
                    if(args[0].equalsIgnoreCase("paste")) {
                        WorldEditPlugin worldEditPlugin = Pixeltects.getWorldEditPlugin();
                        File schematicFile = new File(worldEditPlugin.getDataFolder() + File.separator + "schematics", args[1] + ".schem");

                        boolean copyAir = false;
                        if(argsContain(args, "-copyair")) {
                            copyAir = true;
                        }

                        if(schematicFile.exists()) {
                            if(MathArgUtils.isInt(args[2]) && MathArgUtils.isInt(args[3]) && MathArgUtils.isInt(args[4])
                            && Bukkit.getWorld(args[5]) != null) {
                                int x = Integer.parseInt(args[2]);
                                int y = Integer.parseInt(args[3]);
                                int z = Integer.parseInt(args[4]);

                                World world = Bukkit.getWorld(args[5]);
                                Vector origin = new Vector(x,y,z);

                                if(sender instanceof Player) {
                                    sender.sendMessage(ChatColor.GREEN + "Attempting to paste schematic '" + args[1] + "' using rebuild.");
                                }
                                RebuildUtil.pasteSchematic(world,schematicFile,origin,copyAir,sender);
                            }else{
                                sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
                            }
                        }else {
                            sender.sendMessage(ChatColor.RED + "That schematic does not exist.");
                        }
                    }
                }else{
                    sender.sendMessage(ChatColor.RED + "Correct usage: /rebuild paste <schematicFileName> <x> <y> <z> <world> [-copyair]");
                }
            }else{
                sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
            }
        }
        return false;
    }

    public boolean argsContain(String[] args, String arg) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase(arg) || args[i].startsWith(arg))
                return true;
        }
        return false;
    }

}

