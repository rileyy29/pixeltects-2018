package com.pixeltects.core.rides.flatrides;

import com.pixeltects.core.rides.flatrides.util.ArmorStandGroup;
import org.bukkit.scheduler.BukkitTask;

public interface Flatride {

    String getName();
    boolean spawnOnLoad();
    void spawnRide();
    void startRideSequence();
    void cancelRideTask();
    void despawnRide();
    BukkitTask getRideTask();
    ArmorStandGroup[] getArmorStandGroups();
}
