package com.pixeltects.core.commands;

import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Command_DebugStick implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("debugstick")) {
            if(sender instanceof Player) {
                if(sender.isOp() || sender.hasPermission("pixeltects.build.debugstick")
                        || sender.hasPermission("pixeltects.build")) {

                    Player player = (Player)sender;
                    if(player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(MessageOutput.STAFF_INVENTORY_FULL.getOutput());
                    }else{
                        player.getInventory().addItem(new ItemStack(Material.DEBUG_STICK, 1));
                        player.sendMessage(ChatColor.GREEN + "A debug stick was added to your inventory.");
                    }
                }else{
                    sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                }
            }else{
                sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
            }
        }
        return true;
    }

}
