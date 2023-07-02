package com.pixeltects.core.shows.effects.spotlight;

import com.pixeltects.core.Pixeltects;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Beam {

    private String id;
    private Location crystalLocation;
    private Location beamLocation;
    private EnderCrystal enderCrystal;

    public Beam(String id, Location crystalLocation, Location beamLocation) {
        this.id = id;
        this.crystalLocation = crystalLocation;
        this.beamLocation = beamLocation;
    }

    public void spawnCrystal() {
        this.crystalLocation = this.crystalLocation.add(0.5D, 0.0D, 0.5D);
        EnderCrystal enderCrystal = (EnderCrystal)this.crystalLocation.getWorld().spawnEntity(this.crystalLocation, EntityType.ENDER_CRYSTAL);
        enderCrystal.setBeamTarget(this.beamLocation);
        enderCrystal.setShowingBottom(false);
        enderCrystal.setSilent(true);
        enderCrystal.setInvulnerable(true);
        enderCrystal.setCustomName(this.id);
        enderCrystal.setCustomNameVisible(false);
        this.enderCrystal = enderCrystal;
    }

    public void moveBeam(Location newLocation, final double steps) {
        Location newLoc = newLocation.clone();
        newLoc.setWorld(this.crystalLocation.getWorld());

        final Location current = this.beamLocation.clone();
        final Vector adding = newLoc.toVector();
        adding.subtract(this.beamLocation.toVector());
        adding.divide(new Vector(steps, steps, steps));

        new BukkitRunnable() {
            int ran = 0;

            public void run() {
                if(enderCrystal != null) {
                    current.add(adding);
                    enderCrystal.setBeamTarget(current);
                    beamLocation = current;
                    if(++ran >= steps) {
                        beamLocation = current;
                        cancel();
                    }
                }else{
                    cancel();
                }
            }
        }.runTaskTimer(Pixeltects.getPackageManager(), 0L, 1L);
    }

    public void removeBeam() {
        if(this.enderCrystal.getBeamTarget() != null) {
            this.enderCrystal.setBeamTarget(null); //Should remove, if not, set to location of crystal
        }
    }

    public void kill() {
        if (this.enderCrystal != null)
            this.enderCrystal.remove();
    }

    public World getWorld() {
        return this.crystalLocation.getWorld();
    }

}
