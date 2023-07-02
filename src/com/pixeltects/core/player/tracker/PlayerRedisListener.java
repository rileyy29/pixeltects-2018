package com.pixeltects.core.player.tracker;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.database.redis.RedisListener;
import com.pixeltects.core.events.NetworkEvent;
import com.pixeltects.core.events.PlayerJoinNetworkEvent;
import com.pixeltects.core.events.PlayerLeaveNetworkEvent;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;

import java.util.UUID;

public class PlayerRedisListener implements RedisListener {
    @Override
    public void receive(String channel, JSONObject object) {
        UUID player = UUID.fromString((String) object.get("player"));
        NetworkEvent event;

        if ("player-join".equals(channel)) {
            event = new PlayerJoinNetworkEvent(player);
        } else if ("player-quit".equals(channel)) {
            event = new PlayerLeaveNetworkEvent(player);
        } else {
            throw new IllegalStateException("invalid channel listening");
        }

        Bukkit.getScheduler().runTask(Pixeltects.getPackageManager(), () -> Bukkit.getPluginManager().callEvent(event));
    }
}