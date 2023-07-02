package com.pixeltects.core.player.data;

import com.pixeltects.core.player.PlayerProfile;
import lombok.Getter;

import java.util.Date;

public class RideStreak {

    @Getter
    private int currentStreak;
    @Getter
    private long lastTimeSet;

    public RideStreak(PlayerProfile playerProfile) {
        this.currentStreak = 0;
        this.lastTimeSet = -1;
    }

    public RideStreak(PlayerProfile playerProfile, int currentStreak) {
        this.currentStreak = currentStreak;
        this.lastTimeSet = new Date().getTime();
    }

    public void resetStreak() {
        this.currentStreak = 0;
        this.lastTimeSet = -1;
    }

    public void increaseStreak() {
        this.currentStreak = this.currentStreak + 1;
        this.lastTimeSet = new Date().getTime();
    }

    public double getBonus() {
        double bonus = this.currentStreak * 2.0 / 5.0; //Custom method to work out bonus.
        return bonus;
    }

    public boolean hasTimedOut() {
        if(this.lastTimeSet == -1) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long diffMs = currentTime - this.lastTimeSet;
        long diffSec = diffMs / 1000;
        long min = diffSec / 60;

        if(min >= 5) {
            return true;
        }
        return false;
    }

}
