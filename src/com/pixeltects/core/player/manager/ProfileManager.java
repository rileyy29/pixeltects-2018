package com.pixeltects.core.player.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.database.mongo.DatabaseManager;
import com.pixeltects.core.player.PlayerProfile;
import com.pixeltects.core.player.data.RideStreak;
import com.pixeltects.core.player.manager.loading.MongoPlayerLoader;
import com.pixeltects.core.player.manager.loading.MongoPlayerSaver;
import lombok.Getter;
import org.bson.BSON;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ProfileManager {

    public final long SAVE_TIME = 20L;
    public final long LOAD_DURATION = TimeUnit.SECONDS.toMillis(10L);

    static {
        BSON.addEncodingHook(UUID.class, String::valueOf);
    }

    public Map<UUID, PlayerProfile> players = Maps.newHashMap();

    public MongoPlayerLoader loader = new MongoPlayerLoader();
    public MongoPlayerSaver saver = new MongoPlayerSaver();

    @Getter
    public BukkitTask bukkitTask;

    @Getter
    public BukkitTask rideStreakTask;

    @Getter
    private boolean moduleEnabled;

    public void init() {
        this.loader = new MongoPlayerLoader();
        this.saver = new MongoPlayerSaver();

        DatabaseManager databaseManager = Pixeltects.getPackageManager().getDatabaseManager();

        if(databaseManager.getMongoClient() == null || databaseManager.getDatabase() == null) {
            moduleEnabled = false;
        }
        moduleEnabled = true;
    }

    public void startTask() {
        if(this.bukkitTask != null) {
            this.bukkitTask.cancel();
        }

        if(this.rideStreakTask != null) {
            this.rideStreakTask.cancel();
        }
        //Space for redis rank listener

        this.bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Pixeltects.getPackageManager(), () -> this.players.values().forEach(this::attemptSave), 20L, 20L);
        this.rideStreakTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Pixeltects.getPackageManager(), () -> this.players.values().forEach(this::attemptReset), 20L, 20L);

    }
    
    public PlayerProfile of(Player player) {
        return this.players.get(player.getUniqueId());
    }

    public PlayerProfile getPlayer(Player player) {
        return getPlayer(player.getUniqueId(), false);
    }

    public PlayerProfile getPlayer(Player player, boolean load) {
        return awaitLoad(player.getUniqueId(), load);
    }

    public PlayerProfile loadOfflinePlayer(UUID uuid) {
        return awaitLoad(uuid, true);
    }

    public PlayerProfile awaitLoad(UUID uuid, boolean load) {
        PlayerProfile current = this.players.get(uuid);
        if (current != null || !load)
            return current;
        return loadPlayer(uuid);
    }

    public PlayerProfile loadPlayer(UUID uuid) {
        PlayerProfile future = this.loader.loadPlayer(uuid);
        this.players.put(uuid, future);
        return future;
    }

    public PlayerProfile getPlayer(UUID uuid) {
        return getPlayer(Bukkit.getPlayer(uuid));
    }

    public PlayerProfile getPlayer(UUID uuid, boolean load) {
        return awaitLoad(uuid, load);
    }

    public void removePlayer(Player player) {
        removePlayer(player.getUniqueId());
    }

    public void removePlayer(UUID uuid) {
        PlayerProfile player = this.players.get(uuid);
        if (player != null) {
            attemptSave(player);
            this.players.remove(uuid, player);
        }
    }

    public void attemptSave(PlayerProfile playerProfile) {
        this.saver.savePlayer(playerProfile.getUuid());
    }

    public Collection<PlayerProfile> getProfiles() {
        return this.players.values();
    }

    public void attemptReset(PlayerProfile playerProfile) {
        RideStreak rideStreak = playerProfile.getRideStreak();
        if(rideStreak.hasTimedOut()) {
            playerProfile.getPlayer().sendMessage("Reset");
        }
    }

}
