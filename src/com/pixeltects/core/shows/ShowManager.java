package com.pixeltects.core.shows;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.shows.effects.armorstand.RotationAnimator;
import com.pixeltects.core.shows.effects.firework.FireworkGenerator;
import com.pixeltects.core.shows.effects.fountain.Fountain;
import com.pixeltects.core.shows.effects.particle.ParticleBlock;
import com.pixeltects.core.shows.effects.particle.ParticleLaser;
import com.pixeltects.core.shows.effects.spotlight.Beam;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowManager {

    private Pixeltects packageManager;

    @Getter
    private HashMap<String, Show> nameToShowMap;
    @Getter
    private ArrayList<Show> runningShows;

    @Getter
    private HashMap<String, Fountain> runningFountains;
    @Getter
    private HashMap<String, ParticleLaser> runningLasers;
    @Getter
    private HashMap<String, ParticleBlock> runningBlockParticles;
    @Getter
    private HashMap<String, FireworkGenerator> runningFireworks;
    @Getter
    private HashMap<String, Beam> runningSpotlights;
    @Getter
    private HashMap<String, RotationAnimator> runningAnimations;

    public ShowManager(Pixeltects mainClass) {
        this.packageManager = mainClass;
        System.out.print("[PixeltectsCore] 'ShowManager' class initialized");

        this.runningShows = new ArrayList<>();
        this.nameToShowMap = new HashMap<>();
        this.runningFountains = new HashMap<>();
        this.runningLasers = new HashMap<>();
        this.runningSpotlights = new HashMap<>();
        this.runningBlockParticles = new HashMap<>();
        this.runningFireworks = new HashMap<>();
        this.runningAnimations = new HashMap<>();
        System.out.print("[ShowManager] Created array and hash to manage show data.");

    }

    public boolean isShowRunning(String showName) {
        String correctName = showName.toLowerCase();
        if(nameToShowMap.containsKey(correctName)) {
            return true;
        }
        return false;
    }

    public void stopShow(String showName) {
        String correctName = showName.toLowerCase();
        if(isShowRunning(correctName)) {
            Show showRunnable = nameToShowMap.get(correctName);
            runningShows.remove(showRunnable);
            showRunnable.cancel();
            nameToShowMap.remove(correctName);
        }
    }

    public void stopAllShows() {
        for(Show showRunnable : runningShows) {
            showRunnable.cancel();
        }

        runningShows.clear();
        nameToShowMap.clear();

        System.out.print("[ShowManager] All shows were requested to be stopped.");
    }

    public void stopAllFountains() {
        for(Fountain fountain : runningFountains.values()) {
            fountain.end();
        }
        runningFountains.clear();
    }

    public void stopAllLasers() {
        for(ParticleLaser laser : runningLasers.values()) {
            laser.end();
        }
        runningLasers.clear();
    }

    public void stopAllSpotlights() {
        for(Beam beam : runningSpotlights.values()) {
            beam.kill();
        }
        runningSpotlights.clear();
    }

    public void stopAllParticleBlocks() {
        for(ParticleBlock block : runningBlockParticles.values()) {
            block.end();
        }
        runningBlockParticles.clear();
    }

    public int getTotalEffectsRunning() {
        int count = runningBlockParticles.size() + runningFireworks.size() + runningSpotlights.size() + runningLasers.size()
                 + runningFountains.size();
        return count;
    }

}
