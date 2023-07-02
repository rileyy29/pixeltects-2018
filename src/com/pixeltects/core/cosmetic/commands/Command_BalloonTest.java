package com.pixeltects.core.cosmetic.commands;

import com.pixeltects.core.cosmetic.balloon.Balloon;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_BalloonTest implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("balloontest")) {
            if(sender.isOp()) {
                Balloon balloon = new Balloon((Player)sender, "TestBalloon");
                sender.sendMessage(ChatColor.AQUA + "Spawning a test balloon.");
            }else{
                sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
            }
        }
        return true;
    }



}


