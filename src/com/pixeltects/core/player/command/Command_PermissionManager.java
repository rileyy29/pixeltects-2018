package com.pixeltects.core.player.command;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.player.PlayerProfile;
import com.pixeltects.core.player.permissions.PermissionManager;
import com.pixeltects.core.player.rank.Rank;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Command_PermissionManager implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("perms")) {
            if(sender instanceof Player) {
                PlayerProfile profile = Pixeltects.getPackageManager().getProfileManager().of((Player)sender);
                PermissionManager permissionManager = Pixeltects.getPackageManager().getPermissionManager();
                if(profile == null) {

                }else {
                    if (sender.isOp() || profile.getRank().has(Rank.CAST_LEADER)) { //Temp?
                        if(args.length == 0) {
                            sendHelp(sender);
                        }else if(args.length == 1) {
                            if(args[0].equalsIgnoreCase("resetdb")) {
                                if(sender.getName().equalsIgnoreCase(Pixeltects.getPackageManager().RILEY_USERNAME)) {
                                    permissionManager.resetDatabase();
                                    sender.sendMessage(ChatColor.GREEN + "Dropped (2) database collections.");
                                }else{
                                    sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                                }
                            }else if(args[0].equalsIgnoreCase("groups")) {
                                sendRankList(sender);
                            }else{
                                sendHelp(sender);
                            }
                        }else if(args.length == 3) {
                            if(args[0].equalsIgnoreCase("group")) {
                                if (args[1].equalsIgnoreCase("list")) {
                                    String groupId = args[2];
                                    Rank rank = Rank.byGroupID(groupId);
                                    List<String> permissions = permissionManager.getAllGroupPermissions(rank);
                                    StringBuilder sb = new StringBuilder();
                                    for (String perm : permissions)
                                        sb.append(ChatColor.WHITE + perm + ChatColor.GRAY + ", ");
                                    sender.sendMessage(ChatColor.AQUA + rank.getColoredTag() + ChatColor.AQUA + " - " + rank.getGroupID() + ":");
                                    sender.sendMessage(sb.toString());
                                }
                            }
                        }else if(args.length == 4) {
                            if(args[0].equalsIgnoreCase("group")) {
                                if (args[1].equalsIgnoreCase("add")) {
                                    String groupId = args[2];
                                    String permNode = args[3];
                                    permissionManager.addGroupPermission(sender, groupId, permNode);
                                } else if (args[1].equalsIgnoreCase("remove")) {

                                }
                            }
                        }

                    } else {
                        sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                    }
                }
            }else{
                sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
            }
        }
        return true;
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Correct usage: /perms groups");
        sender.sendMessage(ChatColor.RED + "Correct usage: /perms group add <group-id> <perm-node>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /perms group remove <group-id> <perm-node>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /perms group list <group-id>");

        sender.sendMessage(ChatColor.RED + "Correct usage: /perms player add <player-id> <perm-node>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /perms player remove <player-id> <perm-node>");
        sender.sendMessage(ChatColor.RED + "Correct usage: /perms player list <player-id>");

        if(sender.getName().equalsIgnoreCase(Pixeltects.getPackageManager().RILEY_USERNAME)) {
            sender.sendMessage(ChatColor.RED + "Correct usage: /perms resetdb");
        }
    }

    public void sendRankList(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for (Rank rank : Rank.values())
            sb.append(ChatColor.WHITE + rank.getGroupID() + ChatColor.GRAY + ", ");
        sender.sendMessage(sb.toString());
    }

}

