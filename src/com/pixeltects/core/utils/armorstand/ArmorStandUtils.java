package com.pixeltects.core.utils.armorstand;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class ArmorStandUtils {

    public static EulerAngle directionToEuler(Location dir) {
        double xzLength = Math.sqrt(dir.getX()*dir.getX() + dir.getZ()*dir.getZ());
        double pitch = Math.atan2(xzLength, dir.getY()) - Math.PI / 2;
        double yaw = -Math.atan2(dir.getX(), dir.getZ()) + Math.PI / 4;
        return new EulerAngle(pitch, yaw, 0);
    }

    public static float getPitchFromTo(Location armorstandLoc, Location direction) {

        double dy = direction.getY() - armorstandLoc.getY();
        double dx = direction.getX() - armorstandLoc.getX();
        double hypo = Math.sqrt(dy * dy + dx * dx);
        float angle = (float) Math.toDegrees(Math.cos(dy / hypo));

        if (angle < 0.0F) {
            angle += 360.0F;
        }
        return angle;
    }

    public static Vector getVel(Location loc){
        double pitch = ((loc.clone().getPitch() + 90) * Math.PI) / 180;
        double yaw  = ((loc.clone().getYaw() + 90)  * Math.PI) / 180;
        double x = Math.sin(pitch) * Math.cos(yaw);
        double y = Math.sin(pitch) * Math.sin(yaw);
        double z = Math.cos(pitch);
        if(z>-0.2)z=-0.2;
        return new Vector(x, z, y);
    }

    public static Location lookAt(Location loc, Location lookat) {
        //Clone the loc to prevent applied changes to the input loc
        loc = loc.clone();

        // Values of change in distance (make it relative)
        double dx = lookat.getX() - loc.getX();
        double dy = lookat.getY() - loc.getY();
        double dz = lookat.getZ() - loc.getZ();

        // Set yaw
        if (dx != 0) {
            // Set yaw start value based on dx
            if (dx < 0) {
                loc.setYaw((float) (1.5 * Math.PI));
            } else {
                loc.setYaw((float) (0.5 * Math.PI));
            }
            loc.setYaw((float) loc.getYaw() - (float) Math.atan(dz / dx));
        } else if (dz < 0) {
            loc.setYaw((float) Math.PI);
        }

        // Get the distance from dx/dz
        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

        // Set pitch
        loc.setPitch((float) -Math.atan(dy / dxz));

        // Set values, convert to degrees (invert the yaw since Bukkit uses a different yaw dimension format)
        loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
        loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);

        return loc;
    }

    public static void setHeadPos(ArmorStand as, double yaw, double pitch){
        double xint = Math.cos(yaw/Math.PI);
        double zint = Math.sin(yaw/Math.PI);
//This will convert the yaw to a xint and zint between -1 and 1. Here are some examples of how the yaw changes:
/*
yaw = 0 : xint = 1. zint = 0;  East
yaw = 90 : xint = 0. zint = 1; South
yaw = 180: xint = -1. zint = 0; North
yaw = 270 : xint = 0. zint = -1; West
*/
        double yint = Math.sin(pitch/Math.PI);
//This converts the pitch to a yint
        EulerAngle ea = as.getHeadPose();
        ea.setX(xint);
        ea.setY(yint);
        ea.setZ(zint);
        as.setHeadPose(ea);
//This gets the EulerAngle of the armorStand, sets the values, and then updates the armorstand.
    }

}
