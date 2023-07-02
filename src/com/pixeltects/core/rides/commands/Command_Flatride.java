package com.pixeltects.core.rides.commands;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.rides.RideManager;
import com.pixeltects.core.rides.flatrides.Flatride;
import com.pixeltects.core.rides.flatrides.dlp.FlatrideDLP_Orbitron;
import com.pixeltects.core.rides.flatrides.util.ArmorStandGroup;
import com.pixeltects.core.rides.flatrides.util.FlatrideUtils;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class Command_Flatride implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("flatride")) {
            if(hasPermission(sender, false)) {
                RideManager rideManager = Pixeltects.getPackageManager().getRideManager();
                if(args.length == 0) {

                }else if(args.length == 2) {
                    if(args[0].equalsIgnoreCase("start")) {
                        if(args[1].equalsIgnoreCase("DLP_Orbitron")) {
                            if(rideManager.doesFlatrideExist(args[1])) {
                                if(!rideManager.isFlatrideRunning(args[1])) {
                                    rideManager.getSpawnedFlatrides().get(args[1].toLowerCase()).startRideSequence();
                                    sender.sendMessage("started");
                                }else{
                                    sender.sendMessage(ChatColor.RED + "already running");
                                }
                            }else{
                                sender.sendMessage(ChatColor.RED + "not spawned");
                            }
                        }
                    }
                    if(args[0].equalsIgnoreCase("spawn")) {
                        if(args[1].equalsIgnoreCase("DLP_Orbitron")) {
                            if(!rideManager.doesFlatrideExist(args[1])) {
                                FlatrideDLP_Orbitron flatride = new FlatrideDLP_Orbitron("Orbitron");
                                flatride.spawnRide();
                                rideManager.getSpawnedFlatrides().put("dlp_orbitron", flatride);
                                sender.sendMessage("spawned!");
                            }else{
                                sender.sendMessage("already exists!");
                            }
                        }
                    }
                    if(args[0].equalsIgnoreCase("despawn")) {
                        if(args[1].equalsIgnoreCase("DLP_Orbitron")) {
                            if(rideManager.doesFlatrideExist(args[1])) {
                                Flatride flatride = rideManager.getSpawnedFlatrides().get(args[1].toLowerCase());
                                flatride.despawnRide();
                                rideManager.getSpawnedFlatrides().remove("dlp_orbitron");
                                sender.sendMessage("despawned!");
                            }else{
                                sender.sendMessage("already exists!");
                            }
                        }
                    }
                    if(args[0].equalsIgnoreCase("stop")) {
                        if(args[1].equalsIgnoreCase("DLP_Orbitron")) {
                            if(rideManager.isFlatrideRunning(args[1])) {
                                Flatride flatride = rideManager.getSpawnedFlatrides().get(args[1].toLowerCase());
                                flatride.cancelRideTask();
                                sender.sendMessage("stopped");
                            }else{
                                sender.sendMessage("already exists!");
                            }
                        }
                    }
                    if(args[0].equalsIgnoreCase("pitchtest")) {
                        if(sender.getName().equalsIgnoreCase(Pixeltects.getPackageManager().RILEY_USERNAME)) {
                            ArmorStandGroup[] armorStandGroup = rideManager.getSpawnedFlatrides().get("dlp_orbitron").getArmorStandGroups();

                            for (ArmorStandGroup randomGroup : armorStandGroup) {
                                double toSetPitch = Double.parseDouble(args[1]);
                                randomGroup.getMainArmorStand().setPitch(toSetPitch);
                            }
                        }
                    }
                }
            }else{
                sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
            }
        }
        return false;
    }

    public boolean hasPermission(CommandSender sender, boolean admin) {
        if(sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
            return true;
        }

        if(admin) {
            if (sender instanceof Player) {
                if (sender.isOp() || sender.hasPermission("pixeltects.flatride.admin")) {
                    return true;
                }
            }
        }else {
            if (sender instanceof Player) {
                if (sender.isOp() || sender.hasPermission("pixeltects.flatride.manager")
                        || sender.hasPermission("pixeltects.flatride")) {
                    return true;
                }
            }
        }
        return false;
    }
}
