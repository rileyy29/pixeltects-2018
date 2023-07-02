package com.pixeltects.core.cosmetic.fun;

import com.pixeltects.core.Pixeltects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Vomit extends BukkitRunnable {

    private UUID uuid;
    private int ticks;

    public Vomit(Player player) {
        this.uuid = player.getUniqueId();
        this.ticks = 0;
    }

    public void run() {
        Player player = Bukkit.getPlayer(uuid);
        if(player != null) {
            Location playerLocation = player.getLocation();
            ticks++;
            if(ticks >= 100) {
                player.playSound(playerLocation, Sound.ENTITY_PLAYER_BURP, 5.0F, 0.5F);
                finish();
            }

            Location spawnParticleLocation = new Location(playerLocation.getWorld(), playerLocation.getX(), playerLocation.getY() + 1.5, playerLocation.getZ());
            player.getWorld().spawnParticle(Particle.SLIME, spawnParticleLocation, 50);
            player.playSound(playerLocation, Sound.ENTITY_PLAYER_BURP, 1.0F, 2.5F);

        }else{
            finish();
        }
    }

    public void finish() {
        Pixeltects.getPackageManager().getCosmeticManager().getCurrentlyVomiting().remove(this.uuid);
        cancel();
    }
}
