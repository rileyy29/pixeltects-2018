package com.pixeltects.core.shows.effects.armorstand;

import com.pixeltects.core.Pixeltects;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

public class RotationAnimator extends BukkitRunnable {

    public enum Element { HEAD, BODY, LEFT_ARM, RIGHT_ARM, LEFT_LEG, RIGHT_LEG; };
    private String id;
    private double[] inc;      // the calculated increment on each run
    private long n;            // the number of runs
    private ArmorStand target; // the ArmorStand to be used
    private Element part;      // the selected part

    public RotationAnimator(String id, double maxInc, EulerAngle from, EulerAngle to, ArmorStand target, Element part) {
        this.id = id;
        this.target = target;
        this.part = part;
        double dX = to.getX() - from.getX();
        double dY = to.getY() - from.getY();
        double dZ = to.getZ() - from.getZ();
        double max = Math.max(dX, Math.max(dY, dZ));
        this.n = Math.round(Math.ceil(max / maxInc));
        this.inc = new double[] { Math.toRadians(dX / this.n), Math.toRadians(dY / this.n), Math.toRadians(dZ / this.n) };
    }

    //public int start(long timeout) {
        //this.task = this.main.getServer().getScheduler().scheduleSyncRepeatingTask(this.main, this, 0l, timeout);
       // this.started = true;
        //return this.task;
    //}

    public void run() {
        if (this.n > 0) {
            switch (this.part) {
            case BODY:
                this.target.setBodyPose(this.target.getBodyPose().add(this.inc[0], this.inc[1], this.inc[2]));
                break;
            case HEAD:
                this.target.setHeadPose(this.target.getHeadPose().add(this.inc[0], this.inc[1], this.inc[2]));
                break;
            case LEFT_ARM:
                this.target.setLeftArmPose(this.target.getLeftArmPose().add(this.inc[0], this.inc[1], this.inc[2]));
                break;
            case LEFT_LEG:
                this.target.setLeftLegPose(this.target.getLeftLegPose().add(this.inc[0], this.inc[1], this.inc[2]));
                break;
            case RIGHT_ARM:
                this.target.setRightArmPose(this.target.getRightArmPose().add(this.inc[0], this.inc[1], this.inc[2]));
                break;
            case RIGHT_LEG:
                this.target.setRightLegPose(this.target.getRightLegPose().add(this.inc[0], this.inc[1], this.inc[2]));
                break;
            }
            this.n = this.n - 1;
        } else {
            cancel();
        }
    }

    public void endTask() {
        Pixeltects.getPackageManager().getShowManager().getRunningAnimations().remove(this.id);
        cancel();
    }
}