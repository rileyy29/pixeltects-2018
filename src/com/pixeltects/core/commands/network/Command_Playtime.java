package com.pixeltects.core.commands.network;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.player.PlayerProfile;
import com.pixeltects.core.utils.messages.MessageOutput;
import com.pixeltects.core.utils.time.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_Playtime implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("playtime")) {
            if(args.length == 0) {
                if(sender instanceof Player) {
                    sender.sendMessage(printPlaytime(sender.getName()));
                }else{
                    sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
                }
            }else{
                if(sender.hasPermission("pixeltects.global.playtime.others") || sender.isOp()) {
                    String playerName = args[0];
                    sender.sendMessage(printPlaytime(playerName));
                }else{
                    sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                }
            }

        }
        return true;
    }

    public String printPlaytime(String playerName) {
        OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(playerName);
        PlayerProfile targetProfile;
        if (targetOffline.isOnline()) {
            targetProfile = Pixeltects.getPackageManager().getProfileManager().of(Bukkit.getPlayer(playerName));
        } else {
            targetProfile = Pixeltects.getPackageManager().getProfileManager().getPlayer(targetOffline.getUniqueId(), true);
        }

        String text;

        if (targetProfile == null) {
            text = ChatColor.RED + "Failed to fetch that player's profile.";
        } else {
            long firstTime = targetProfile.getFirstJoin();
            long curr = targetProfile.getLastJoin();

            if(targetOffline.isOnline()) {
                long diff = System.currentTimeMillis() - curr;
                curr += (diff);
            }

            long diff = curr - firstTime;
            if (diff == 0 || firstTime == 0 || firstTime == -1 || diff == firstTime || firstTime == curr || diff == curr) {
                text = ChatColor.RED + "That player has never joined the server.";
            } else {
                text = ChatColor.AQUA + "" + targetOffline.getName() + "'s playtime: " + TimeUtils.calculateTime(diff) + ".";
            }

            if (!targetOffline.isOnline() || (targetOffline == null)) {
                Pixeltects.getPackageManager().getProfileManager().removePlayer(targetOffline.getUniqueId());
            }
        }
        return text;
    }

}

