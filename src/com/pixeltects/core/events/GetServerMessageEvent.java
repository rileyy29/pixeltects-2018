package com.pixeltects.core.events;

import com.pixeltects.core.Pixeltects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class GetServerMessageEvent implements PluginMessageListener {

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) return;

        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
            String command = in.readUTF();

            if(command.equals("GetServers")) {
                Pixeltects.getPackageManager().getNetworkedServers().clear();
                String[] serverList = in.readUTF().split(", ");
                StringBuilder sb = new StringBuilder();
                for (String servers : serverList) {
                    Pixeltects.getPackageManager().getNetworkedServers().add(servers);
                    sb.append(ChatColor.WHITE + servers + ChatColor.GRAY + ", ");
                }
                OfflinePlayer offlinePlayer = Bukkit.getPlayer(Pixeltects.getPackageManager().RILEY_USERNAME);
                if((offlinePlayer != null) && offlinePlayer.isOnline()) {
                    ((Player)offlinePlayer).sendMessage(ChatColor.AQUA + "[PIXELTECTS] Servers loaded: " + sb.toString());
                }
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
