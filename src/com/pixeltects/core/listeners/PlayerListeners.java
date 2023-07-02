package com.pixeltects.core.listeners;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.commands.Command_Essentials;
import com.pixeltects.core.player.PlayerProfile;
import com.pixeltects.core.security.type.UUIDSpoofCheck;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerListeners implements Listener {

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        if(event.isCancelled()) {
            return;
        }

        if (event.getMessage().startsWith("/tps")) {
            event.setCancelled(true);
            Command_Essentials.printTPS(event.getPlayer());
        }

        //Extra security / precautions
        if(event.getMessage().startsWith("/me") || event.getMessage().startsWith("/say")) {
            if(!event.getPlayer().isOp()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onAsyncJoin(AsyncPlayerPreLoginEvent event) {
        String playerName = event.getName();
        if(UUIDSpoofCheck.isUUIDSpoofed(playerName, event.getUniqueId())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Failed to validate your player identity.");
            return;
        }

        PlayerProfile profile = Pixeltects.getPackageManager().getProfileManager().getPlayer(event.getUniqueId(), true);
        if(profile == null || !profile.isLoaded()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, MessageOutput.MODULE_FAILED_LOGIN.getOutput());

            if(profile == null) {
                System.out.print("[PlayerManager] AsyncPlayerPreLoginEvent failed to load player data. [1] Data is null.");
            }else {
                System.out.print("[PlayerManager] AsyncPlayerPreLoginEvent failed to load player data. [2] Data cant load.");
            }
            return;
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();

        if(player != null) {
            if(player.getName().equalsIgnoreCase(Pixeltects.getPackageManager().RILEY_USERNAME)) {
                Date loadedPlugin = Pixeltects.getPackageManager().getTimeLoaded();
                player.sendMessage(ChatColor.AQUA + "[PIXELTECTS] Plugin was loaded at: " + new SimpleDateFormat("HH:mm:ss z").format(loadedPlugin) +
                        " on " + new SimpleDateFormat("EEE, MMM d").format(loadedPlugin) + ".");
            }

            PlayerProfile profile = Pixeltects.getPackageManager().getProfileManager().of(player);

            if(profile == null) {
                event.getPlayer().kickPlayer(MessageOutput.MODULE_FAILED_JOIN.getOutput());
                return;
            }

            profile.setPlayer(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        if(player.isInsideVehicle()) {
            Entity entity = player.getVehicle();
            if(entity.getPassengers().contains(player)) {
                entity.removePassenger(player);
            }
        }

        Pixeltects.getPackageManager().getProfileManager().removePlayer(player);

    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

}
