package com.pixeltects.core.shows.effects.particle;

import org.bukkit.Particle;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;

public class LaunchObject extends BukkitRunnable {

    private FallingBlock fallingBLock;
    private Particle particle;
    private boolean endEarly;

    public LaunchObject(FallingBlock fb, Particle p, boolean end) {
        this.fallingBLock = fb;
        this.particle = p;
        this.endEarly = end;
    }

    public void run() {
        if(this.fallingBLock.isDead()) {
            cancel();
        }
        if(this.fallingBLock.getVelocity().getY() < 0.0D && this.endEarly) {
            cancel();
            this.fallingBLock.remove();
        }
        this.fallingBLock.getWorld().spawnParticle(this.particle, this.fallingBLock.getLocation(),
                5, 0.005D, 0.005D, 0.005D, 0.005D, null, true);
    }
}
