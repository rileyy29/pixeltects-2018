package com.pixeltects.core.rides.flatrides.util;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class FlatrideUtils {

    public static boolean hasDonatorPassenger(ArmorStand armorStand) {
        if(armorStand.getPassenger() != null) {
            if(armorStand.getPassenger().isOp()) { //Check for donator perms here
                return true;
            }
        }
        return false;
    }

    public static Location rotate(Location middle, Location point, double degrees, double radius){
        float oldyaw = point.getYaw()%360.0F;
        double y = 0.0;
        double angle = Math.acos((point.getX()-middle.getX())/radius);

        if(point.getZ() < middle.getZ()){
            double newangle = 2*Math.PI-angle + (degrees * Math.PI / 180);
            double newx = Math.cos(newangle) * radius;
            double newz = Math.sin(newangle) * radius;

            point = middle.clone().add(newx, y, newz);
            point.setYaw((float) ((oldyaw+degrees)%360.0F));
            return point;
        }else{
            double newangle = angle + (degrees * Math.PI / 180);
            double newx = Math.cos(newangle) * radius;
            double newz = Math.sin(newangle) * radius;

            point = middle.clone().add(newx, y, newz);
            point.setYaw((float) ((oldyaw+degrees)%360.0F));
            return point;
        }
    }

    public static Vector rotateY(Vector v, double radians){
        double x = v.getX();
        double z = v.getZ();
        Vector p = v.clone();

        p.setX(x*Math.cos(radians) - z*Math.sin(radians));
        p.setZ(x*Math.sin(radians) + z*Math.cos(radians));

        return p;
    }

    public static ArmorStandGroup pickRandom(ArmorStandGroup[] modelStands) {
        Random random = ThreadLocalRandom.current();
        ArmorStandGroup armorStandGroup = modelStands[random.nextInt(modelStands.length)];
        return armorStandGroup;
    }

    public static void sendMessage(ArmorStandGroup[] modelStands, String message, String type) {
        if(type.equalsIgnoreCase("donator")) {
            for (ArmorStandGroup armorStandGroup : modelStands) {
                if (armorStandGroup.getMainArmorStand().getArmorStand().getPassenger() != null) {
                    if(hasDonatorPassenger(armorStandGroup.getMainArmorStand().getArmorStand())) {
                        armorStandGroup.getMainArmorStand().getArmorStand().getPassenger().sendMessage(message);
                    }
                }
            }
        }else if (type.equalsIgnoreCase("non-donator")){
            for (ArmorStandGroup armorStandGroup : modelStands) {
                if (armorStandGroup.getMainArmorStand().getArmorStand().getPassenger() != null) {
                    if(!hasDonatorPassenger(armorStandGroup.getMainArmorStand().getArmorStand())) {
                        armorStandGroup.getMainArmorStand().getArmorStand().getPassenger().sendMessage(message);
                    }
                }
            }
        }else{
            for(ArmorStandGroup armorStandGroup : modelStands) {
                if(armorStandGroup.getMainArmorStand().getArmorStand().getPassenger() != null) {
                    armorStandGroup.getMainArmorStand().getArmorStand().getPassenger().sendMessage(message);
                }
            }
        }
    }

    public static void sendSoundEffect(ArmorStandGroup[] modelStands, Sound sound, float volume, float pitch) {
        for(ArmorStandGroup armorStandGroup : modelStands) {
            if(armorStandGroup.getMainArmorStand().getArmorStand().getPassenger() != null) {
                if(armorStandGroup.getMainArmorStand().getArmorStand().getPassenger() instanceof Player) {
                    Player passenger = (Player) armorStandGroup.getMainArmorStand().getArmorStand().getPassenger();
                    passenger.playSound(armorStandGroup.getMainArmorStand().getArmorStand().getLocation(), sound, volume, pitch);
                }
            }
        }
    }

    public static void sendParticle(ArmorStandGroup[] modelStands, Particle particle, int amount, double radius) {
        for(ArmorStandGroup armorStandGroup : modelStands) {
            if(armorStandGroup.getMainArmorStand().getArmorStand().getPassenger() != null) {
                if(armorStandGroup.getMainArmorStand().getArmorStand().getPassenger() instanceof Player) {
                    Player passenger = (Player) armorStandGroup.getMainArmorStand().getArmorStand().getPassenger();
                    passenger.getWorld().spawnParticle(particle, armorStandGroup.getMainArmorStand().getArmorStand().getLocation()
                            , amount,radius,radius,radius,radius, null, true);
                }
            }
        }
    }




}
