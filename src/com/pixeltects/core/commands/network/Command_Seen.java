package com.pixeltects.core.commands.network;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.player.PlayerProfile;
import com.pixeltects.core.utils.messages.MessageOutput;
import com.pixeltects.core.utils.time.TimeUtils;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class Command_Seen implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("seen")) {
            if (sender.isOp() || sender.hasPermission("pixeltects.global.seen")) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.RED + "Correct usage: /seen <username>");
                } else if (args.length >= 1) {
                    String target = args[0];
                    OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(target);
                    PlayerProfile targetProfile;
                    if (targetOffline.isOnline()) {
                        targetProfile = Pixeltects.getPackageManager().getProfileManager().of(Bukkit.getPlayer(target));
                    } else {
                        targetProfile = Pixeltects.getPackageManager().getProfileManager().getPlayer(targetOffline.getUniqueId(), true);
                    }

                    if (targetProfile == null) {
                        sender.sendMessage(ChatColor.RED + "Failed to fetch that player's profile.");
                    } else {
                        long currTime = System.currentTimeMillis();
                        long lastJoin = targetProfile.getLastJoin();
                        long diff = currTime - lastJoin;
                        if(diff == 0 || lastJoin == 0 || lastJoin == -1 || diff == currTime || lastJoin == currTime) {
                            sender.sendMessage(ChatColor.RED + "That player has never joined the server.");
                        }else {
                            if (Pixeltects.getPackageManager().getPlayerTracker().isOnline(targetOffline.getUniqueId())) {
                                String serverName = Pixeltects.getPackageManager().getPlayerTracker().getServerAt(targetOffline.getUniqueId());
                                BaseComponent[] description = new ComponentBuilder(ChatColor.GREEN +""+ChatColor.BOLD+"Click to join this server.").create();
                                TextComponent message = new TextComponent(ChatColor.AQUA + targetOffline.getName() + " is currently online at " + ChatColor.UNDERLINE + serverName + ".");
                                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, description));
                                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join " + serverName));

                                if (!(sender instanceof Player)) {
                                    sender.sendMessage(ChatColor.AQUA + targetOffline.getName() + " is currently online at " + ChatColor.UNDERLINE + serverName + ".");
                                } else {
                                    sender.spigot().sendMessage(message);
                                }
                            } else {
                                sender.sendMessage(ChatColor.AQUA + targetOffline.getName() + " was last seen " + TimeUtils.calculateTime(diff) + " ago.");
                            }
                        }

                        if (!targetOffline.isOnline() || (targetOffline == null)) {
                            Pixeltects.getPackageManager().getProfileManager().removePlayer(targetOffline.getUniqueId());
                        }
                    }
                }
            } else {
                sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
            }
        }
        return true;
    }


}

