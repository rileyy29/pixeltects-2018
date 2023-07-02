package com.pixeltects.core.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.net.InetAddresses;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.player.data.RideStreak;
import com.pixeltects.core.player.manager.loading.PlayerKey;
import com.pixeltects.core.player.permissions.PermissionManager;
import com.pixeltects.core.player.rank.Rank;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class PlayerProfile {

    @Getter
    private HashMap<String, Object> saveQueue = Maps.newHashMap();
    @Getter
    private UUID uuid;
    @Getter
    private ArrayList<String> permissions = Lists.newArrayList();
    @Getter
    private Rank rank = Rank.DEFAULT;
    @Getter
    private Set<String> knownIPs = Sets.newHashSet();
    @Getter
    private Set<String> knownNames = Sets.newHashSet();
    @Getter
    private Long firstJoin;
    @Getter
    private Long lastJoin;
    @Getter
    private double balance;
    @Getter
    private RideStreak rideStreak;

    @Getter
    private boolean uniqueJoin = false;

    private WeakReference<Player> player;

    @Getter
    private volatile boolean loaded = false;

    public PlayerProfile(UUID uuid) {
        Preconditions.checkNotNull(uuid);
        this.uuid = uuid;
        this.firstJoin = System.currentTimeMillis();
        this.lastJoin = System.currentTimeMillis();
        this.balance = 0.0;
        this.uniqueJoin = true;
        this.rideStreak = new RideStreak(this);

        createUser();
        this.loaded = true;
    }

    public PlayerProfile(UUID uuid, DBObject dbObject) {
        Preconditions.checkNotNull(uuid);
        this.uuid = uuid;
        this.uniqueJoin = false;

        Player player = Bukkit.getPlayer(uuid);
        String rankID = (String) dbObject.get(PlayerKey.RANK);
        if(rankID != null) {
            this.rank = Rank.byGroupID(rankID);
        }

        Long firstJoin = (Long)dbObject.get(PlayerKey.FIRST_JOIN);
        this.firstJoin = (firstJoin == null) ? System.currentTimeMillis() : firstJoin.longValue();
        Long lastJoin = (Long)dbObject.get(PlayerKey.LAST_JOIN);
        this.lastJoin = (lastJoin == null) ? System.currentTimeMillis() : lastJoin.longValue();

        for (int i = 0; i < 2; i++) {
               if (i == 0) {
                    BasicDBList list = (BasicDBList)dbObject.get(PlayerKey.KNOWN_IPS);
                    if (!list.isEmpty() && list != null)
                        for (Object object : list)
                            this.knownIPs.add((String)object);
                }
                if (i == 1) {
                    BasicDBList list = (BasicDBList)dbObject.get(PlayerKey.KNOWN_NAMES);
                    if (!list.isEmpty() && list != null)
                        for (Object object : list)
                            this.knownNames.add((String)object);
                }
            }

        this.balance = (double)dbObject.get(PlayerKey.BALANCE);
        this.rideStreak = new RideStreak(this);
        this.loaded = true;
    }

    public void createUser() {
        Pixeltects.getPackageManager().getProfileManager().saver.createPlayer(this.uuid);
    }

    public void setPlayer(Player player) {
        Player oldPlayer = getPlayer();
        if (oldPlayer != null && oldPlayer.isOp())
            oldPlayer.setOp(false);
        this.player = new WeakReference<>(player);
        if (player != null) {

            Rank rank = getRank();
            if (rank != null && rank.has(Rank.DIRECTOR))
                player.setOp(true);

            loadPermissions();
            //UpdateTag
        }
    }

    public void updateNetworkStats() {
        if(getPlayer() != null) {
            Player player = getPlayer();
            addUpdate(PlayerKey.LAST_JOIN, System.currentTimeMillis());
            String name = player.getName();
            if (name != null) {
                addKnownName(name);
                addUpdate(PlayerKey.LAST_NAME, name);
            }
            String address = InetAddresses.toAddrString(player.getAddress().getAddress());
            addKnownIP(address);
        }else{
            System.out.print("[ProfileManager] Failed to update network statistics due to player object being null");
        }
    }

    public void loadPermissions() {
        if(this.player != null) {
            PermissionManager permissionManager = Pixeltects.getPackageManager().getPermissionManager();
            permissionManager.applyUserPermissions(this);
            permissionManager.applyGroupPermissions(this);
        }
    }

    public Player getPlayer() {
        if (!this.loaded || this.player == null)
            return null;
        return this.player.get();
    }

    public void setRank(Rank rank) {
        if (this.rank == rank)
            return;
        this.rank = rank;
        addUpdate("rank", rank.name());
        //updateTag();
    }

    public boolean addKnownIP(String address) {
        if (!this.knownIPs.contains(address)) {
            this.knownIPs.add(address);
            addUpdate("knownIps", this.knownIPs);
            return true;
        }
        return false;
    }

    public boolean addKnownName(String name) {
        if (!this.knownNames.contains(name)) {
            this.knownNames.add(name);
            addUpdate("knownNames", this.knownNames);
            return true;
        }
        return false;
    }

    public void setLastJoin() {
        this.lastJoin = Long.valueOf(System.currentTimeMillis());
        addUpdate("lastJoin", this.lastJoin);
    }

    public void addUpdate(String key, Object object) {
        this.saveQueue.put(key, object);
    }

}
