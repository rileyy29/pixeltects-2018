package com.pixeltects.core.security;

import com.mojang.brigadier.Message;
import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class AntiListeners implements Listener, PluginMessageListener {

    public void onPluginMessageReceived(String channel, Player player, byte[] data) {
        if (channel.equals("WDL|INIT")) {
            player.kickPlayer(ChatColor.RED + "We have detected unauthorised modifications. Please disable them immediately.");

            //TODO: Add to security log when class done
        }
    }

    @EventHandler
    public void onCommandEvent(PlayerCommandPreprocessEvent event) {
        if(event.isCancelled()) {
            return;
        }

        if(Pixeltects.getPackageManager().getPlayerTracker() == null) {
            event.getPlayer().sendMessage(ChatColor.RED + "The player module is currently offline therefore all commands have been disabled.");
            return;
        }

        if(!Pixeltects.getPackageManager().getPlayerTracker().getServerAt(event.getPlayer().getUniqueId()).equalsIgnoreCase("Build")) {
            if (event.getMessage().startsWith("/reload") || event.getMessage().startsWith("/bukkit:reload")
                    || event.getMessage().startsWith("/rl") || event.getMessage().startsWith("/bukkit:rl")) {
                if (event.getPlayer().isOp()) {
                    event.getPlayer().sendMessage(ChatColor.RED + "This command has been disabled.");
                    if (event.getPlayer().getName().equalsIgnoreCase(Pixeltects.getPackageManager().RILEY_USERNAME)) {
                        event.getPlayer().sendMessage(ChatColor.GOLD + "Overriding command and reloading the server.");
                    } else {
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                }
            } else if (event.getMessage().startsWith("/ver") || event.getMessage().startsWith("/version")
                    || event.getMessage().startsWith("/bukkit:ver") || event.getMessage().startsWith("/bukkit:version")) {
                event.getPlayer().sendMessage(ChatColor.AQUA + "This server is running PixieDust version 1.0");
                event.setCancelled(true);
            } else {
            }
        }
    }

}
