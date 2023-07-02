package com.pixeltects.core.player.permissions;

import com.mongodb.*;
import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.database.mongo.DatabaseKey;
import com.pixeltects.core.database.mongo.DatabaseManager;
import com.pixeltects.core.player.PlayerProfile;
import com.pixeltects.core.player.rank.Rank;
import com.pixeltects.core.utils.executor.Scheduler;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PermissionManager {

    private Pixeltects packageManager;

    @Getter
    private boolean moduleEnabled;

    public PermissionManager(Pixeltects mainClass) {
        this.packageManager = mainClass;
        DatabaseManager databaseManager = Pixeltects.getPackageManager().getDatabaseManager();

        if(databaseManager.getMongoClient() == null || databaseManager.getDatabase() == null) {
            moduleEnabled = false;
        }
        moduleEnabled = true;
    }

    public List<String> getAllUserPermissions(UUID uuid) {
        List<String> perms = new ArrayList<String>();

        Scheduler.runAsync(() -> {
            DatabaseManager databaseManager = Pixeltects.getPackageManager().getDatabaseManager();
            DBCollection dbCollection = databaseManager.getCollection(DatabaseKey.userPermissionsKey);

            BasicDBObject basicDBObject = new BasicDBObject("player_uuid", uuid);
            DBCursor objectFound = dbCollection.find(basicDBObject);

            objectFound.forEach((object) -> {
                String perm = (String) object.get("permission_string");
                perms.add(perm);
            });

        });

        return perms;
    }

    public List<String> getAllGroupPermissions(Rank rank) {
        List<String> perms = new ArrayList<String>();

        Scheduler.runAsync(() -> {
            DatabaseManager databaseManager = Pixeltects.getPackageManager().getDatabaseManager();
            DBCollection dbCollection = databaseManager.getCollection(DatabaseKey.groupPermissionsKey);

            BasicDBObject basicDBObject = new BasicDBObject("group_id", rank.getGroupID());
            DBCursor objectFound = dbCollection.find(basicDBObject);

            objectFound.forEach((object) -> {
                String perm = (String) object.get("permission_string");
                perms.add(perm);
            });
        });

        return perms;
    }

    public void deleteUserPermissions(UUID uuid) {
        Scheduler.runAsync(() -> {
            DatabaseManager databaseManager = Pixeltects.getPackageManager().getDatabaseManager();
            DBCollection dbCollection = databaseManager.getCollection(DatabaseKey.userPermissionsKey);
            BasicDBObject basicDBObject = new BasicDBObject("player_uuid", uuid);

            dbCollection.remove(basicDBObject);
        });
    }

    public void resetDatabase() {
        Scheduler.runAsync(() -> {
            DatabaseManager databaseManager = Pixeltects.getPackageManager().getDatabaseManager();
            DBCollection dbCollection = databaseManager.getCollection(DatabaseKey.userPermissionsKey);
            dbCollection.drop();

            DBCollection dbCollection2 = databaseManager.getCollection(DatabaseKey.groupPermissionsKey);
            dbCollection2.drop();

        });

    }

    public void deleteGroupPermissions(Rank rank) {
        Scheduler.runAsync(() -> {
            DatabaseManager databaseManager = Pixeltects.getPackageManager().getDatabaseManager();
            DBCollection dbCollection = databaseManager.getCollection(DatabaseKey.groupPermissionsKey);
            BasicDBObject basicDBObject = new BasicDBObject("group_id", rank.getGroupID());

            dbCollection.remove(basicDBObject);
        });
    }

    public void clearPermissionMaps() {
        for(PlayerProfile profile : this.packageManager.getProfileManager().getProfiles()) {
            ArrayList<String> permissions = profile.getPermissions();
            permissions.clear();
        }
    }

    public void applyUserPermissions(PlayerProfile profile) {
        ArrayList<String> permissions = profile.getPermissions();
        List<String> userPerms = getAllUserPermissions(profile.getUuid());
        userPerms.forEach((perm) -> {
                permissions.add(perm);
            });
    }

    public void applyGroupPermissions(PlayerProfile profile) {
        ArrayList<String> permissions = profile.getPermissions();
        List<String> groupPerms = getAllGroupPermissions(profile.getRank());
        groupPerms.forEach((perm) -> {
                permissions.add(perm);
        });
    }

    public void updatePermissions(PlayerProfile profile) {
        profile.getPermissions().clear();

        applyUserPermissions(profile);
        applyGroupPermissions(profile);
    }

    public void addGroupPermission(CommandSender sender, String id, String perm) {
        new BukkitRunnable() {
            public void run() {
                Scheduler.runAsync(() -> {
                    DBCollection dbCollection = Pixeltects.getPackageManager().getDatabaseManager().getCollection(DatabaseKey.groupPermissionsKey);
                    BasicDBObject dbObject = new BasicDBObject("group_id", id);
                    dbObject.put("permission_string", perm);
                    DBObject objectFound = dbCollection.findOne(dbObject);
                    if(objectFound == null) {
                        dbCollection.insert(dbObject);
                        for(PlayerProfile profile : Pixeltects.getPackageManager().getProfileManager().getProfiles()) {
                            if(profile.getRank().getGroupID().equalsIgnoreCase(id)) {
                                updatePermissions(profile);
                            }
                        }
                        sender.sendMessage(ChatColor.GREEN + "Set.");
                        return;
                    }else{
                        sender.sendMessage(ChatColor.RED + "Already exists.");
                        return;
                    }
                });

            }
        }.runTaskLater(Pixeltects.getPackageManager(), 20L);


    }

    public boolean hasPermission(CommandSender sender, String... permissionNode) {
        if(sender.isOp()) {
            return true;
        }

        if(sender instanceof Player) {
            Player player = (Player) sender;
            PlayerProfile profile = Pixeltects.getPackageManager().getProfileManager().of(player);
            if(profile == null || !profile.isLoaded()) {
                return false;
            }

            if(profile.getPermissions().contains(permissionNode)) {
                return true;
            }
        }
        return false;
    }
}
