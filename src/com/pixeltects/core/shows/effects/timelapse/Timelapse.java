package com.pixeltects.core.shows.effects.timelapse;

import com.pixeltects.core.Pixeltects;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class Timelapse extends BukkitRunnable {

    private World world;
    private long startValue;
    private long endValue;
    private int ticks;
    private int interval;
    private int count = 0;

    public Timelapse(World world, long startValue, long endValue, int ticks) {
        this.startValue = startValue;
        this.endValue = endValue;
        this.world = world;
        this.ticks = ticks;

        double valueDifference = endValue - startValue; //TODO: May need to switch end and start. Test functionality ingame
        this.interval = ((int)(valueDifference / ticks));
    }

    public void start() {
        this.world.setTime(this.startValue);
        runTaskTimer(Pixeltects.getPackageManager(), 0L, 1L);
    }

    public void run() {
        if(this.count == this.ticks) {
            cancel();
        }
        this.world.setTime(this.world.getTime() + this.interval);
        this.count += 1;
    }

}
