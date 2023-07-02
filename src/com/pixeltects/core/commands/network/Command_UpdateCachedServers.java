package com.pixeltects.core.commands.network;

import com.pixeltects.core.utils.bungeecord.ServerFetchRequest;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Command_UpdateCachedServers implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("updatecachedservers")) {
            if(sender.isOp()) {
                ServerFetchRequest.getAllServers();
                sender.sendMessage(ChatColor.GREEN + "Refreshed and updated all the cached servers.");
            }else{
                sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
            }
        }
        return true;
    }



}

