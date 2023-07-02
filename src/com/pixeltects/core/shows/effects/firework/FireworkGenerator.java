package com.pixeltects.core.shows.effects.firework;

import com.pixeltects.core.Pixeltects;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FireworkGenerator {

    private Location location;
    private int amount;

    private int fwPower;
    private FireworkEffect.Type fwType;
    private boolean fwFlicker;
    private boolean fwTrail;
    private Color fwPrimaryColor;
    private Color fwSecondaryColor;

    private Vector fwVelocity;

    private int ticks;
    private Firework firework;

    public FireworkGenerator(Location location, int amount, int fwPower, Color fwPrimaryColor,
                             Color fwSecondaryColor, boolean flicker, boolean trail, String type,
                             float motX, float motY, float motZ, int ticks) {

        this.location = location;
        this.amount = amount;
        this.fwPower = fwPower;
        this.fwPrimaryColor = fwPrimaryColor;
        this.fwSecondaryColor = fwSecondaryColor;
        this.fwVelocity = new Vector(motX,motY,motZ);
        this.fwFlicker = flicker;
        this.fwTrail = trail;
        this.fwType = FireworkEffect.Type.valueOf(type.toUpperCase());

        if(this.fwType == null) {
            this.fwType = FireworkEffect.Type.BALL;
        }

        this.ticks = ticks;
    }

    public Firework createFirework() {
        Location spawnLocation = location.clone();
        Firework firework = (Firework) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        fireworkMeta.setPower(this.fwPower);
        fireworkMeta.addEffect(FireworkEffect.builder()
                .with(this.fwType)
                .withColor(this.fwPrimaryColor)
                .withFade(this.fwSecondaryColor)
                .flicker(this.fwFlicker)
                .trail(this.fwTrail).build());

        firework.setFireworkMeta(fireworkMeta);
        firework.setVelocity(this.fwVelocity);
        return firework;
    }

    public void spawnFirework() {
        if(this.ticks == -1) {
            for (int i = 0; i < this.amount; i++) {
                this.firework = createFirework();
                if (this.firework == null) {
                    return;
                }
            }
        }else{
            for (int i = 0; i < this.amount; i++) {
                this.firework = createFirework();
                if (this.firework == null) {
                    return;
                }
                new BukkitRunnable() {
                    public void run() {
                        if(firework != null) {
                            firework.detonate();
                        }
                    }
                }.runTaskLater(Pixeltects.getPackageManager(), ticks);
            }
        }
    }
}
