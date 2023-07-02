package com.pixeltects.core.rides.listeners;

import com.pixeltects.core.Pixeltects;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class RideListeners implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent entityEvent) {
        if ((entityEvent.getRightClicked() != null) && entityEvent.getRightClicked() instanceof ArmorStand) {
            if ((entityEvent.getRightClicked().getCustomName() != null) && entityEvent.getRightClicked().getCustomName().contains("ride")) {
                    entityEvent.setCancelled(true);
            }
        }
    }
}