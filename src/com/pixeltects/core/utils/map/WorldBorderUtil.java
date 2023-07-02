package com.pixeltects.core.utils.map;

import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_14_R1.WorldBorder;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class WorldBorderUtil {

    public static void sendWorldBorderPacket(Player player, int warningBlocks) {
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        WorldBorder playerWorldBorder = nmsPlayer.world.getWorldBorder();
        PacketPlayOutWorldBorder worldBorder = new PacketPlayOutWorldBorder(playerWorldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_BLOCKS);
        try {
            Field field = worldBorder.getClass().getDeclaredField("i");
            field.setAccessible(true);
            field.setInt(worldBorder, warningBlocks);
            field.setAccessible(!field.isAccessible());
        } catch (Exception e) {
            e.printStackTrace();
        }
        nmsPlayer.playerConnection.sendPacket(worldBorder);
    }

}
