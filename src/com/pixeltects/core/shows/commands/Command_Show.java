package com.pixeltects.core.shows.commands;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.shows.Show;
import com.pixeltects.core.shows.ShowManager;
import com.pixeltects.core.shows.utils.HelpUtil;
import com.pixeltects.core.utils.messages.MessageOutput;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class Command_Show implements CommandExecutor {

    public static String USAGE = ChatColor.RED + "Correct usage: /show <start/stop/stopall/list/cmdlist/particlelist/soundlist> [showname]";
    public static String EFFECT_USAGE = ChatColor.RED + "For show effects and commands use: /show cmdlist";
    public static String RECENT_UPDATE = ChatColor.AQUA + "New Update! /show <startgroup/stopgroup> [show1,show2,show3]";
    public static String BASE_PATH = "/shows/";

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("show")) {
                if(sender.isOp() || sender.hasPermission("pixeltects.show.manager")
                        || sender.hasPermission("pixeltects.show")) {
                    if(args.length == 0) {
                        sender.sendMessage(USAGE);
                        sender.sendMessage(EFFECT_USAGE);
                        sender.sendMessage(RECENT_UPDATE);
                    }else if(args.length == 2) {
                        if (args[0].equalsIgnoreCase("start")) {
                            File file = new File(Pixeltects.getPackageManager().getDataFolder() + BASE_PATH, args[1] + ".show");
                            if (file.exists()) {
                                String startedBy;
                                if (sender instanceof Player) {
                                    startedBy = ((Player) sender).getName();
                                } else {
                                    startedBy = "Console";
                                }

                                Show showRunnable = new Show(args[1], file, startedBy);
                                showRunnable.runTaskTimer(Pixeltects.getPackageManager(), 1L, 1L);

                                if (sender instanceof Player) {
                                    sender.sendMessage(MessageOutput.COMMAND_SHOW_START.getOutput().replace("%show%", args[1]));
                                }

                            } else {
                                sender.sendMessage(MessageOutput.COMMAND_SHOW_NOTFOUND.getOutput().replace("%show%", args[1]));
                            }
                        } else if (args[0].equalsIgnoreCase("stop")) {
                            ShowManager showManager = Pixeltects.getPackageManager().getShowManager();
                            String showName = args[1].toLowerCase();
                            if (showManager.isShowRunning(showName)) {
                                showManager.stopShow(showName);

                                if (sender instanceof Player) {
                                    sender.sendMessage(MessageOutput.COMMAND_SHOW_STOPPED.getOutput().replace("%show%", args[1]));
                                }

                            } else {
                                sender.sendMessage(MessageOutput.COMMAND_SHOW_NOTRUNNING.getOutput().replace("%show%", args[1]));
                            }
                        } else if (args[0].equalsIgnoreCase("startgroup")) {
                            String[] showNames = args[1].split(",");

                            String startedBy;
                            if (sender instanceof Player) {
                                startedBy = ((Player) sender).getName();
                            } else {
                                startedBy = "Console";
                            }

                            for(String showName : showNames) {
                                File file = new File(Pixeltects.getPackageManager().getDataFolder() + BASE_PATH, showName + ".show");
                                if (file.exists()) {
                                    Show showRunnable = new Show(showName, file, startedBy);
                                    showRunnable.runTaskTimer(Pixeltects.getPackageManager(), 1L, 1L);

                                    if (sender instanceof Player) {
                                        sender.sendMessage(MessageOutput.COMMAND_SHOW_START.getOutput().replace("%show%", showName));
                                    }

                                } else {
                                    sender.sendMessage(MessageOutput.COMMAND_SHOW_NOTFOUND.getOutput().replace("%show%", showName));
                                }
                            }

                        } else if (args[0].equalsIgnoreCase("stopgroup")) {
                            String[] showNames = args[1].split(",");
                            ShowManager showManager = Pixeltects.getPackageManager().getShowManager();

                            for(String showName : showNames) {
                                String lowercaseName = showName.toLowerCase();
                                if(showManager.getNameToShowMap().containsKey(lowercaseName)) {
                                    Show showRunnable = showManager.getNameToShowMap().get(lowercaseName);
                                    showManager.getRunningShows().remove(showRunnable);
                                    showRunnable.cancel();
                                    showManager.getNameToShowMap().remove(lowercaseName);

                                    if (sender instanceof Player) {
                                        sender.sendMessage(MessageOutput.COMMAND_SHOW_STOPPED.getOutput().replace("%show%", showName));
                                    }
                                }
                            }

                        } else {
                            sender.sendMessage(USAGE);
                            sender.sendMessage(EFFECT_USAGE);
                            sender.sendMessage(RECENT_UPDATE);
                        }
                    }else{
                        if(args.length == 1) {
                            if (args[0].equalsIgnoreCase("stopall")) {
                                if (sender.isOp() || sender.hasPermission("pixeltects.show.admin")) {
                                    ShowManager showManager = Pixeltects.getPackageManager().getShowManager();
                                    showManager.stopAllShows();
                                    sender.sendMessage(MessageOutput.COMMAND_SHOW_STOPPEDALL.getOutput());
                                } else {
                                    sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                                }
                            } else if (args[0].equalsIgnoreCase("list")) {
                                ShowManager showManager = Pixeltects.getPackageManager().getShowManager();
                                StringBuilder stringBuilder = new StringBuilder();
                                File file = new File(Pixeltects.getPackageManager().getDataFolder() + BASE_PATH);

                                for (int i = 0; i < file.listFiles().length; i++) {
                                    File showFile = file.listFiles()[i];
                                    String showName = showFile.getName().replace(".show", "");
                                    if (showManager.isShowRunning(showName.toLowerCase())) {
                                        stringBuilder.append(ChatColor.GREEN + showName + ChatColor.GRAY + ", ");
                                    } else {
                                        stringBuilder.append(ChatColor.RED + showName + ChatColor.GRAY + ", ");
                                    }
                                }

                                sender.sendMessage(ChatColor.GRAY + stringBuilder.toString());
                            } else if (args[0].equalsIgnoreCase("cmdlist")) {
                                if(sender instanceof Player) {
                                    Player player = (Player)sender;
                                    player.sendMessage(" ");
                                    player.sendMessage(ChatColor.AQUA + "Commands for show files (hover for description): ");
                                    for(String cmds : HelpUtil.commands.keySet()) {
                                        String desc = HelpUtil.commands.get(cmds);
                                        BaseComponent[] description = new ComponentBuilder(desc).create();
                                        TextComponent message = new TextComponent(ChatColor.GRAY + " - " + ChatColor.WHITE + cmds);
                                        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, description));
                                        player.spigot().sendMessage(message);
                                    }
                                    player.sendMessage(" ");
                                    player.sendMessage(ChatColor.RED + "Most of these commands are exclusive to show files only. Hover over them to find out more information.");
                                    player.sendMessage(" ");
                                }else{
                                    sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
                                }
                            } else if (args[0].equalsIgnoreCase("particlelist")) {
                                sender.sendMessage(ChatColor.AQUA + "Minecraft Particles: ");
                                HelpUtil.sendParticleList(sender);
                                sender.sendMessage(" ");
                            } else if (args[0].equalsIgnoreCase("soundlist")) {
                                sender.sendMessage(ChatColor.AQUA + "Minecraft Sound Effects: ");
                                HelpUtil.sendSoundList(sender);
                                sender.sendMessage(" ");
                            } else {
                                sender.sendMessage(USAGE);
                                sender.sendMessage(EFFECT_USAGE);
                                sender.sendMessage(RECENT_UPDATE);
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
