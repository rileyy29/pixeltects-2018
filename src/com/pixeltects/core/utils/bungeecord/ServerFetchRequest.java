package com.pixeltects.core.utils.bungeecord;

import com.pixeltects.core.Pixeltects;
import org.bukkit.Bukkit;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class ServerFetchRequest {

    public static void getAllServers() {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("GetServers");
        } catch(Exception e) {
            e.printStackTrace();
        }
        Bukkit.getServer().sendPluginMessage(Pixeltects.getPackageManager(), "BungeeCord", b.toByteArray());
    }

}
