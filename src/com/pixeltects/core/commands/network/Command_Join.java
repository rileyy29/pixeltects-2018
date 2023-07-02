package com.pixeltects.core.commands.network;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.player.tracker.PlayerTracker;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_Join implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("join")) {
            if(sender instanceof Player) {
                Player player = (Player)sender;
                PlayerTracker playerTracker = Pixeltects.getPackageManager().getPlayerTracker();
                String currentServer = playerTracker.getServerAt(player.getUniqueId());
                if (args.length == 0) {
                    sender.sendMessage(" ");
                    sender.sendMessage(ChatColor.AQUA + "You are currently connected to " + ChatColor.UNDERLINE + currentServer + ".");
                    sender.sendMessage(ChatColor.AQUA + "Available servers to join: ");
                    StringBuilder sb = new StringBuilder();
                    for (String serverName : Pixeltects.getPackageManager().getNetworkedServers())
                        if(playerTracker.hasPermissionToJoin(player, serverName)) {
                            sb.append(ChatColor.WHITE + serverName + ChatColor.GRAY + ", ");
                        }
                    sender.sendMessage(sb.toString());
                    sender.sendMessage(" ");
                } else if (args.length >= 1) {
                    String targetServer = args[0];
                    if(ifNameIsServer(targetServer)) {
                        boolean canJoin = playerTracker.hasPermissionToJoin(player, targetServer);
                        if (canJoin) {
                            if (currentServer.equalsIgnoreCase(targetServer)) {
                                sender.sendMessage(ChatColor.RED + "You are already connected to this server!");
                            } else {
                                sender.sendMessage(ChatColor.AQUA + "Attempting to transfer you to that server..");
                                playerTracker.sendPlayerToServer(player, targetServer);
                            }
                        } else {
                            sender.sendMessage(ChatColor.AQUA + "Sorry! We could not transfer you to that server.");
                        }
                    } else {
                      sender.sendMessage(ChatColor.RED + "That server does not exist.");
                    }
                }
            }else{
                sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
            }
        }
        return true;
    }


    public boolean ifNameIsServer(String targetServer) {
        boolean exists = false;
        for (String serverName : Pixeltects.getPackageManager().getNetworkedServers()) {
            if(serverName.equalsIgnoreCase(targetServer)) {
                exists = true;
            }
        }
        return exists;
    }


}

