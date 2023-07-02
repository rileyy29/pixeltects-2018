package com.pixeltects.core.listeners;

import com.pixeltects.core.utils.blocks.TrapdoorUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;

public class PhysicalListeners implements Listener {

    @EventHandler(priority= EventPriority.HIGHEST)
    public void onSignPlace(SignChangeEvent e) { //Color coding on signs
        String[] line = e.getLines();
        Player player = e.getPlayer();
        for (int i = 0; i < line.length; i++) {
            String l = line[i];
            String c = color(l);
            e.setLine(i, c);
        }
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        if(event.isCancelled()) {
            return;
        }

        event.setCancelled(true); //Cancel no matter what
    }

    @EventHandler
    public void onBlockGrowEvent(BlockGrowEvent event) {
        if(event.isCancelled()) {
            return;
        }

        if(event.getBlock().getType().equals(Material.VINE) ||
        event.getBlock().getType().equals(Material.SUGAR_CANE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFireSpread(BlockSpreadEvent event) {
        if(event.isCancelled()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onSaplingGrowth(StructureGrowEvent event) {
        if(event.isCancelled()) {
            return;
        }
        if(event.isFromBonemeal()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBedInteract(PlayerInteractEvent event) {
        if(event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        if((event.getAction() != null) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if((event.getClickedBlock() != null) && event.getClickedBlock().getType().equals(Material.LEGACY_BED_BLOCK)) {
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);

                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void onIronDoorInteract(PlayerInteractEvent event) {
        if(event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        if((event.getAction() != null) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if((event.getClickedBlock() != null)) {
                if(event.getClickedBlock().getType().equals(Material.IRON_TRAPDOOR)
                || event.getClickedBlock().getType().equals(Material.IRON_DOOR)) {
                    if (player.hasPermission("pixeltects.build.interact")) {
                        if(!player.isSneaking()) {
                            TrapdoorUtil.setOpen(event.getClickedBlock());
                            event.setUseItemInHand(Event.Result.DENY);
                            player.updateInventory();
                        }
                    } else {
                        event.setCancelled(true);
                        event.setUseItemInHand(Event.Result.DENY);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        player.updateInventory();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) { //Prevent players entering beds
        event.setCancelled(true);
        event.setUseBed(Event.Result.DENY);
    }

    @EventHandler
    public void onItemFrameInteract(PlayerInteractEntityEvent event) { //Item Frame rotation and editing
        if(event.isCancelled()) {
           return;
        }
        if(event.getRightClicked() != null && event.getRightClicked() instanceof ItemFrame) {
            Player player = event.getPlayer();

            if (player.isOp() || player.hasPermission("pixeltects.build.interact")
                    || player.hasPermission("pixeltects.build")
                    || player.hasPermission("pixeltects.technician")) {
                event.setCancelled(false);
            }else{
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onFallingBlockLand(EntityChangeBlockEvent event) { //Shows, prevent 'fountains' from placing blocks
        Entity ent = event.getEntity();
        if (event.getEntityType() == EntityType.FALLING_BLOCK) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCrystalDamage(EntityDamageEvent event) { //Preventing ender crystals explosion/damage
        Entity entity = event.getEntity();
        if (entity != null && entity.getType() == EntityType.ENDER_CRYSTAL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onStandDamage(EntityDamageByEntityEvent event) { //Prevent accidental deletion and guest deletion
        Entity entity = event.getEntity();
        if (entity != null &&
                entity.getType() == EntityType.ARMOR_STAND) {
            Entity damager = event.getDamager();
            if (damager instanceof Player) { //TODO: REPLACE RANK CHECK WITH PERMISSION NODE
                Player player = (Player)damager;
                    if (player.isOp() || player.hasPermission("pixeltects.build.interact")
                            || player.hasPermission("pixeltects.build")
                            || player.hasPermission("pixeltects.technician")) {
                        if (player.getItemInHand() != null && player.getItemInHand().getType().equals(Material.STICK)) {
                            ArmorStand armorStand = (ArmorStand)entity;
                           if(armorStand.isInvulnerable()) {
                               if(player.isSneaking()) {
                                    entity.remove();
                               }else {
                                   event.setCancelled(true);
                                   player.sendMessage(ChatColor.RED + "This armorstand has the invulnerability attribute.");
                                   player.sendMessage(ChatColor.RED + "Sneak whilst hitting the armorstand (with a stick) to confirm the removal.");
                               }
                           }else{
                               entity.remove();
                           }
                        } else {
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "You must be holding a stick whilst hitting the armorstand to destroy it.");
                        }
                    } else {
                        event.setCancelled(true);
                    }
            }else{
                event.setCancelled(true);
            }
        }
    }

    public String color(String o) {
        char color = '&';
        String c = ChatColor.translateAlternateColorCodes(color, o);
        return c;
    }

}
