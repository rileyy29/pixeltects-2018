package com.pixeltects.core.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.pixeltects.core.Pixeltects;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BungeeCordRequests {

    public static void sendPlayerToServer(Player player, String server) {
        if(Pixeltects.getPackageManager() == null) {
            player.sendMessage(ChatColor.RED + "We are still working on our magic. Please wait a few seconds..!");
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(Pixeltects.getPackageManager(), "BungeeCord", out.toByteArray());
    }


}
