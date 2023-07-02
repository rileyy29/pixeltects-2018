package com.pixeltects.core.shows.commands;

import com.google.common.collect.Lists;
import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.shows.effects.armorstand.ArmorStandAnimator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.util.EulerAngle;

import java.io.File;
import java.util.List;

public class Command_Animate implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("animate")) {
           if(args.length == 1) {
               String id = args[0];

               List<ArmorStand> standTest = Lists.newArrayList();
               for (World world : Bukkit.getWorlds()) {
                   for (Entity e : world.getEntities()) {
                       if(e != null) {
                           if(e instanceof ArmorStand) {
                               if (e.getCustomName() != null) {
                                   if (e.getCustomName().startsWith(id)) {
                                       standTest.add((ArmorStand)e);
                                   }
                               }
                           }
                       }
                   }
               }

               File file = new File(Pixeltects.getPackageManager().getDataFolder() + "/animatronics/", id + ".animatronic");

               for(ArmorStand armorStand : standTest) {
                   if (armorStand != null) {
                       new ArmorStandAnimator(file, armorStand).setStartLocation(armorStand.getLocation());
                   }
               }

           }
        }
        return true;
    }



}


