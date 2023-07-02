package com.pixeltects.core.shows.effects.particle;

import com.google.common.collect.Lists;
import com.pixeltects.core.Pixeltects;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.channels.Pipe;
import java.util.List;

public class ParticleBlock extends BukkitRunnable {

    private String id;
    private Particle particle;
    private Location location;
    private double particleDistance = 0.25;

    private int ticks;
    private String type;

    public ParticleBlock(String id, Particle particle, Location location, int ticks, String type) {
        this.id = id.toLowerCase();
        this.location = location.add(0.5, 0, 0.5); //Center?
        this.particle = particle;
        this.ticks = ticks;
        this.type = type;
    }

    public void run() {
        createCubeParticles();
        ticks = ticks - 1;
        if (ticks <= 0) {
            end();
        }
    }

    public void end() {
        if(Pixeltects.getPackageManager().getShowManager().getRunningBlockParticles().containsKey(this.id)) {
            Pixeltects.getPackageManager().getShowManager().getRunningBlockParticles().remove(this.id);
        }
        cancel();
    }

    private void createCubeParticles() {
        Location loc = this.location.clone();
        if(this.type.equalsIgnoreCase("Outline")) {
            for (Location cubeLoc : getHollowCube(loc, this.particleDistance)) {
                sendParticleToAll(cubeLoc);
            }
        }else{
            for (Location cubeLoc : getFullCube(loc, this.particleDistance)) {
                sendParticleToAll(cubeLoc);
            }
        }
    }

    public void sendParticleToAll(Location location) {
        location.getWorld().spawnParticle(this.particle, location, 4, 0.005D, 0.005D, 0.005D, 0.005D, null, true);
    }

    public List<Location> getHollowCube(Location loc, double particleDistance) {
        List<Location> result = Lists.newArrayList();
        World world = loc.getWorld();
        double minX = loc.getBlockX();
        double minY = loc.getBlockY();
        double minZ = loc.getBlockZ();
        double maxX = loc.getBlockX()+1;
        double maxY = loc.getBlockY()+1;
        double maxZ = loc.getBlockZ()+1;

        for (double x = minX; x <= maxX; x = Math.round((x + particleDistance) * 1e2) / 1e2) {
            for (double y = minY; y <= maxY; y = Math.round((y + particleDistance) * 1e2) / 1e2) {
                for (double z = minZ; z <= maxZ; z = Math.round((z + particleDistance) * 1e2) / 1e2) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (z == minZ || z == maxZ) components++;
                    if (components >= 2) {
                        result.add(new Location(world, x, y, z));
                    }
                }
            }
        }
        return result;
    }

    public List<Location> getFullCube(Location loc, double particleDistance) {
        List<Location> result = Lists.newArrayList();
        World world = loc.getWorld();
        double minX = loc.getBlockX();
        double minY = loc.getBlockY();
        double minZ = loc.getBlockZ();
        double maxX = loc.getBlockX()+1;
        double maxY = loc.getBlockY()+1;
        double maxZ = loc.getBlockZ()+1;

        for (double x = minX; x <= maxX; x = Math.round((x + particleDistance) * 1e2) / 1e2) {
            for (double y = minY; y <= maxY; y = Math.round((y + particleDistance) * 1e2) / 1e2) {
                for (double z = minZ; z <= maxZ; z = Math.round((z + particleDistance) * 1e2) / 1e2) {
                    result.add(new Location(world, x, y, z));
                }
            }
        }
        return result;
    }

}
