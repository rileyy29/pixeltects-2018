package com.pixeltects.core.shows.effects.particle;

import com.pixeltects.core.Pixeltects;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class ParticleLaser {

    private String id;
    private World world;
    private Particle particle;
    private Location startLocation;
    private Location endLocation;
    private int ticks;
    private boolean moving = false;
    private BukkitTask task;

    public void showLaser(String id, Particle particle, Location startLocation, Location endLocation, int ticks) {
        this.id = id;
        this.particle = particle;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.world = this.startLocation.getWorld();
        this.ticks = ticks;
        start();
    }

    public void showLaser(String id, Particle particle, Location startLocation, Location endLocation) {
        this.id = id;
        this.particle = particle;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.world = this.startLocation.getWorld();
        this.ticks = -1;
        start();
    }

    public void start() {
        this.task = new BukkitRunnable(){
            public void run() {
                if(!moving) {
                    if (ticks == -1) {
                        showParticle();
                    } else {
                        showParticle();
                        ticks = ticks - 1;
                        if (ticks <= 0) {
                            if (Pixeltects.getPackageManager().getShowManager().getRunningLasers().containsKey(id)) {
                                Pixeltects.getPackageManager().getShowManager().getRunningLasers().remove(id);
                                task.cancel();
                            }
                            task.cancel();
                        }
                    }
                }
            }
        }.runTaskTimer(Pixeltects.getPackageManager(), 0L, 1L);
    }

    public void moveParticle(Location newLocation, final int steps) {
        this.moving = true;
        Location newLoc = newLocation.clone();
        newLoc.setWorld(this.world);

        final Location current = this.endLocation.clone();
        final Vector adding = newLoc.toVector();
        adding.subtract(this.endLocation.toVector());
        adding.divide(new Vector(steps, steps, steps));

        new BukkitRunnable(){
            int ran = 0;

            public void run() {
                if(!task.isCancelled()) {
                    current.add(adding);
                    endLocation = current;
                    showParticle();
                    if(++ran >= steps) {
                        endLocation = current;
                        moving = false;
                        cancel();
                    }
                }else{
                    cancel();
                }
            }
        }.runTaskTimer(Pixeltects.getPackageManager(), 0L, 1L);

    }

    public void showParticle() {
        double step = 0.5D;
        Vector a = this.startLocation.toVector();
        Vector b = this.endLocation.toVector();
        Vector difference = b.subtract(a);
        double length = difference.length();
        difference.normalize().multiply(step);

        double steps = length / step;

        for(int i = 0; i < steps; i++) {
            Vector point = a.add(difference);
            if(this.moving) {
                moveParticleForAll(point.toLocation(this.startLocation.getWorld()));
            }else{
                sendParticleToAll(point.toLocation(this.startLocation.getWorld()));
            }
        }

    }

    public void sendParticleToAll(Location location) {
        location.getWorld().spawnParticle(this.particle, location, 4, 0.005D, 0.005D, 0.005D, 0.005D, null, true);
    }

    public void moveParticleForAll(Location location) {
        location.getWorld().spawnParticle(this.particle, location, 4, 0.0005D, 0.0005D, 0.0005D, 0.0005D, null, true);
    }

    public void end() {
        this.task.cancel();
    }

    public World getWorld() {
        return this.world;
    }

}
