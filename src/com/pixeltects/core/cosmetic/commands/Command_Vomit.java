package com.pixeltects.core.cosmetic.commands;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.cosmetic.CosmeticManager;
import com.pixeltects.core.cosmetic.fun.Vomit;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_Vomit implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vomit")) {
           if(sender instanceof Player) {
               CosmeticManager cosmeticManager = Pixeltects.getPackageManager().getCosmeticManager();
               Player player = (Player)sender;
               if(!cosmeticManager.getCurrentlyVomiting().containsKey(player.getUniqueId())) {
                   if(!player.isInsideVehicle()) {
                       Vomit vomit = new Vomit(player);
                       vomit.runTaskTimer(Pixeltects.getPackageManager(), 1L, 1L);
                       cosmeticManager.getCurrentlyVomiting().put(player.getUniqueId(), vomit);
                   }else{
                       player.sendMessage(ChatColor.RED + "To maintain the best experience for everyone, using /vomit is disabled during rides.");
                   }
               }
           }else{
               sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
           }
        }
        return true;
    }



}

