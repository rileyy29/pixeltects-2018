package com.pixeltects.core.shows.utils;

import com.pixeltects.core.Pixeltects;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class RepeatUtil {

    public static void runRepeat(final int time, final String command) {
        new BukkitRunnable() {
            int counter = 1;

            public void run() {
                if(this.counter >= time) {
                    cancel();
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                this.counter++;
            }
        }.runTaskTimer(Pixeltects.getPackageManager(), 0L, 1L);
    }

}
