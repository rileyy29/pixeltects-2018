package com.pixeltects.core.shows.effects.fountain;

import com.pixeltects.core.Pixeltects;
import org.bukkit.*;
import org.bukkit.entity.FallingBlock;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class Fountain {

    private String id;
    private Location location;
    private World world;
    private Vector motion;
    private Material material;
    private byte data;
    private int ticks;
    private boolean onMove = false;
    private BukkitTask task;

    public Fountain(String id, double x, double y, double z, String worldName, float xmotion, float ymotion, float zmotion, Material material, int data, int ticks) {
        this.id = id;
        this.world = Bukkit.getWorld(worldName);
        this.location = new Location(this.world, x, y, z);
        this.motion = new Vector(xmotion, ymotion, zmotion);
        this.material = material;
        this.data = (byte)data;
        this.ticks = ticks;
        start();
    }

    public Fountain(String id, double x, double y, double z, String worldName, float xmotion, float ymotion, float zmotion, Material material, int data) {
        this.id = id;
        this.world = Bukkit.getWorld(worldName);
        this.location = new Location(this.world, x, y, z);
        this.motion = new Vector(xmotion, ymotion, zmotion);
        this.material = material;
        this.data = (byte)data;
        this.ticks = -1;
        start();
    }

    public void start() {
        this.task = new BukkitRunnable() {
            public void run() {
                if (!onMove) {
                    if (ticks == -1) {
                        spawnFountainBlock(location, motion, material, data);
                    } else {
                        spawnFountainBlock(location, motion, material, data);
                        ticks = ticks - 1;
                        if (ticks <= 0) {
                            if (Pixeltects.getPackageManager().getShowManager().getRunningFountains().containsKey(id)) {
                                Pixeltects.getPackageManager().getShowManager().getRunningFountains().remove(id);
                                task.cancel();
                            }
                            task.cancel();
                        }
                    }
                }
            }
        }.runTaskTimer(Pixeltects.getPackageManager(), 0L, 1L);
    }

    public void end() {
        this.task.cancel();
    }

    public void moveFountain(Vector newMotion, final int steps) {
        this.onMove = true;
        final Vector current = this.motion.clone();
        final Vector adding = newMotion.clone();
        adding.subtract(current);
        adding.divide(new Vector(steps, steps, steps));
        new BukkitRunnable() {
            int ran = 0;

            public void run() {
                if (!task.isCancelled()) {
                    current.add(adding);
                    motion = current;
                    spawnFountainBlock(location, current, material, data);
                    if (++ran >= steps) {
                        motion = current;
                        onMove = false;
                        cancel();
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Pixeltects.getPackageManager(), 0L, 1L);
    }

    public void changeMaterial(Material material, byte data) {
        this.material = material;
        this.data = data;
    }

    private FallingBlock spawnFountainBlock(Location location, Vector velocity, Material material, int data) {
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, material, (byte)data);
        fallingBlock.setDropItem(false);
        fallingBlock.setMetadata("show-block",new FixedMetadataValue(Pixeltects.getPackageManager(), true));
        fallingBlock.setVelocity(velocity);

        return fallingBlock;
    }

}
