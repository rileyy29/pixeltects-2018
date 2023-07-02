package com.pixeltects.core.shows;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class Command_Show implements CommandExecutor {

    public static String USAGE = ChatColor.RED + "Correct usage: /magic <start/stop/stopall/list> [showname]";
    public static String EFFECT_USAGE = ChatColor.RED + "For show effects, use: /magiceffect";
    public static String BASE_PATH = "/shows/";

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("magic")) {
                if(sender.isOp() || sender.hasPermission("pixeltects.show.manager")
                        || sender.hasPermission("pixeltects.show")) {
                    if(args.length == 0) {
                        sender.sendMessage(USAGE);
                        sender.sendMessage(EFFECT_USAGE);
                    }else if(args.length == 2) {
                        if(args[0].equalsIgnoreCase("start")) {
                            File file = new File(Pixeltects.getPackageManager().getDataFolder() + BASE_PATH, args[1] + ".show");
                            if(file.exists()) {
                                String startedBy;
                                if(sender instanceof Player) {
                                    startedBy = ((Player) sender).getName(); }else{
                                    startedBy = "Console"; }

                                Show showRunnable = new Show(args[1], file, startedBy);
                                showRunnable.runTaskTimer(Pixeltects.getPackageManager(), 1L, 1L);

                                if(sender instanceof Player) {
                                    sender.sendMessage(MessageOutput.COMMAND_SHOW_START.getOutput().replace("%show%", args[1]));
                                }

                            }else{
                                sender.sendMessage(MessageOutput.COMMAND_SHOW_NOTFOUND.getOutput().replace("%show%", args[1]));
                            }
                        }else if(args[0].equalsIgnoreCase("stop")) {
                            ShowManager showManager = Pixeltects.getPackageManager().getShowManager();
                            String showName = args[1].toLowerCase();
                            if(showManager.isShowRunning(showName)) {
                                showManager.stopShow(showName);

                                if(sender instanceof Player) {
                                    sender.sendMessage(MessageOutput.COMMAND_SHOW_STOPPED.getOutput().replace("%show%", args[1]));
                                }

                            }else{
                                sender.sendMessage(MessageOutput.COMMAND_SHOW_NOTRUNNING.getOutput().replace("%show%", args[1]));
                            }
                        }else{
                            sender.sendMessage(USAGE);
                            sender.sendMessage(EFFECT_USAGE);
                        }
                    }else{
                        if(args.length == 1) {
                            if(args[0].equalsIgnoreCase("stopall")) {
                                if(sender.isOp() || sender.hasPermission("pixeltects.show.stopall")) {
                                    ShowManager showManager = Pixeltects.getPackageManager().getShowManager();
                                    showManager.stopAllShows();
                                    sender.sendMessage(MessageOutput.COMMAND_SHOW_STOPEDALL.getOutput());
                                }else{
                                    sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                                }
                            }else if(args[0].equalsIgnoreCase("list")) {
                                ShowManager showManager = Pixeltects.getPackageManager().getShowManager();
                                StringBuilder stringBuilder = new StringBuilder();
                                File file = new File(Pixeltects.getPackageManager().getDataFolder() + BASE_PATH);

                                for(int i = 0; i < file.listFiles().length; i++) {
                                    File showFile = file.listFiles()[i];
                                    String showName = showFile.getName().replace(".show", "");
                                    if(showManager.isShowRunning(showName.toLowerCase())) {
                                        stringBuilder.append(ChatColor.GREEN + showName + ChatColor.GRAY + ", ");
                                    }else{
                                        stringBuilder.append(ChatColor.RED + showName + ChatColor.GRAY + ", ");
                                    }
                                }

                                sender.sendMessage(ChatColor.GRAY + stringBuilder.toString());
                            }else{
                                sender.sendMessage(USAGE);
                                sender.sendMessage(EFFECT_USAGE);
                            }
                        }
                    }
                }else{
                    sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                }
        }
        return true;
    }

}
