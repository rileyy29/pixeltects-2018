package com.pixeltects.core.rides;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.rides.flatrides.Flatride;
import com.pixeltects.core.rides.flatrides.dlp.FlatrideDLP_Orbitron;
import com.pixeltects.core.utils.messages.MessageOutput;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class RideManager {

   private Pixeltects packageManager;

   @Getter
   private HashMap<String, Flatride> spawnedFlatrides;

   public RideManager(Pixeltects mainClass) {
       this.packageManager = mainClass;
       System.out.print("[PixeltectsCore] 'RideManager' class initialized");

       this.spawnedFlatrides = new HashMap<>();
   }

   //-----------------------------FLATRIDE STUFF-----------------------------
   public boolean isFlatrideRunning(String rideID) {
       if(spawnedFlatrides.containsKey(rideID.toLowerCase())) {
           if(spawnedFlatrides.get(rideID.toLowerCase()).getRideTask() != null) {
               return true;
           }
       }
       return false;
   }

   public boolean doesFlatrideExist(String id) {
       if(spawnedFlatrides.containsKey(id.toLowerCase())) {
           return true;
       }
       return false;
   }

}
