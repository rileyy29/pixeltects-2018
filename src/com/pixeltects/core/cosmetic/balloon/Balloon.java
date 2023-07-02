package com.pixeltects.core.cosmetic.balloon;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.utils.armorstand.ArmorStandUtils;
import com.pixeltects.core.utils.math.MathUtil;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_14_R1.entity.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Balloon {

    private UUID uuid;
    private World world;
    private String balloonType;

    private ArmorStand balStand;
    private Chicken bal;

    public Balloon(Player player, String balloonType) {
        this.uuid = player.getUniqueId();
        this.world = player.getWorld();
        this.balloonType = balloonType;

        spawnBalloon();
        runBalloon();
    }

    public void spawnBalloon() {
        Location spawnLocation = getPlayer().getLocation().clone().add(0,2.0,0);
        ArmorStand balloon = (ArmorStand)this.world.spawnEntity(spawnLocation, EntityType.ARMOR_STAND);
        CraftArmorStand craftArmorStand = (CraftArmorStand)balloon;
        ItemStack modelHelmet = new ItemStack(Material.DIAMOND_HOE, 1, (short)15);
        ItemMeta modelMeta = modelHelmet.getItemMeta();
        modelMeta.setUnbreakable(true);
        modelHelmet.setItemMeta(modelMeta);
        balloon.setHelmet(modelHelmet);

        Chicken leash = (Chicken) this.world.spawnEntity(spawnLocation, EntityType.CHICKEN);
        leash.setRemoveWhenFarAway(false);
        leash.setSilent(true);
        leash.setAdult();
        leash.setBreed(false);
        leash.setAgeLock(true);
        leash.setAI(false);
        leash.setPersistent(true);
        leash.setGravity(false);
        leash.setCanPickupItems(false);
        leash.setInvulnerable(true);
        leash.setCollidable(false);
        leash.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 255, false, false));
        leash.setLeashHolder(getPlayer());

        balloon.setRemoveWhenFarAway(false);
        balloon.setCustomNameVisible(false);
        balloon.setMarker(true);
        balloon.setGravity(false);
        balloon.setPersistent(true);
        balloon.setVisible(false);
        balloon.setCollidable(false);
        balloon.setInvulnerable(true);
        balloon.setCanPickupItems(false);
        balloon.setSmall(true);


        this.balStand = balloon;
        this.bal = leash;
    }

    public void runBalloon() {
        new BukkitRunnable() {
            private boolean yaw = true;
            private int i = 0;
            CraftChicken craftC = (CraftChicken)bal;
            CraftArmorStand craftArmorStand = (CraftArmorStand)balStand;

            Location location;
            public void run() {
                if(getPlayer() != null) {
                    if (bal != null && balStand != null) {
                        if (!bal.isLeashed()) {
                            despawnBalloon(location);
                            cancel();
                            return;
                        }

                        if(getPlayer().isInsideVehicle()) {
                            getPlayer().sendMessage("message_balloons_vehicle");
                            despawnBalloon(location);
                            cancel();
                            return;
                        }

                        Location currentLocation = getPlayer().getLocation().clone();

                        if (i == 90) {
                            yaw = false;
                        } else if (i == 0) {
                            yaw = true;
                        }

                        if (yaw) {
                            i--;
                        } else {
                            i++;
                        }

                        currentLocation.add(currentLocation.getDirection().multiply(-0.5D).getX(), currentLocation.clone().multiply(0.1).getY(),
                                currentLocation.getDirection().multiply(-0.6D).getZ());
                        //TODO: Move ^ up to spawning so it stays in the same place for looks

                        craftC.teleport(currentLocation.add(0.5,0.5,0.5));

                        Location standLocation = craftC.getLocation().subtract(0,0.5,0); //currentLocation.clone().add(0.5,0,0.5);
                        standLocation.setYaw(i);

                        balStand.teleport(standLocation);

                        this.location = standLocation;

                    } else {
                        despawnBalloon(location);
                        cancel();
                    }
                }else{
                    despawnBalloon(location);
                    cancel();
                }
            }
        }.runTaskTimer(Pixeltects.getPackageManager(), 2L, 2L);
    }

    public void despawnBalloon(Location despawned) {
        if(this.balStand != null) {
            this.balStand.remove();
        }

        if(this.bal != null) {
            this.bal.setLeashHolder(null);
            this.bal.remove();
        }

        if(getPlayer() != null) {
            getPlayer().setLeashHolder(null);
            getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_LAVA_POP, 1.0F, 2.5F);
            if(despawned != null) {
                getPlayer().getWorld().spawnParticle(Particle.CLOUD,despawned, 4, 0.05D, 0.05D, 0.05D, 0.05D, null, true);
            }
        }
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

}
