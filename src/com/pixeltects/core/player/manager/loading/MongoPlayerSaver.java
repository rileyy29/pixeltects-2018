package com.pixeltects.core.player.manager.loading;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.database.mongo.DatabaseKey;
import com.pixeltects.core.player.PlayerProfile;
import com.pixeltects.core.utils.executor.BukkitAsyncExecutor;
import com.pixeltects.core.utils.executor.Scheduler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class MongoPlayerSaver {

    private final BukkitAsyncExecutor asyncExecutor = Pixeltects.getAsyncExecutor();

    public void createPlayer(UUID uuid) {
        new BukkitRunnable() {
            public void run() {
                Scheduler.runAsync(() -> {
                    PlayerProfile profile = Pixeltects.getPackageManager().getProfileManager().getPlayer(uuid);
                    DBCollection dbCollection = Pixeltects.getPackageManager().getDatabaseManager().getCollection(DatabaseKey.profilesKey);
                    BasicDBObject dbObject = new BasicDBObject("uuid", uuid);
                    DBObject objectFound = dbCollection.findOne(dbObject);
                    if(objectFound == null) {
                        dbObject.put(PlayerKey.RANK, profile.getRank().getGroupID());
                        dbObject.put(PlayerKey.FIRST_JOIN, profile.getFirstJoin());
                        dbObject.put(PlayerKey.LAST_JOIN, profile.getLastJoin());
                        dbObject.put(PlayerKey.KNOWN_IPS, new BasicDBList());
                        dbObject.put(PlayerKey.KNOWN_NAMES, new BasicDBList());
                        dbObject.put(PlayerKey.BALANCE, 0.0);

                        dbCollection.insert(dbObject);
                    }
                });
            }
        }.runTaskLater(Pixeltects.getPackageManager(), 20L);
    }

    public boolean savePlayer(UUID uuid) {
        PlayerProfile profile = Pixeltects.getPackageManager().getProfileManager().getPlayer(uuid, false);
        if(profile != null) {
            Map<String, Object> queue = profile.getSaveQueue();

            Iterator queueStrings = queue.entrySet().iterator();

            while (queueStrings.hasNext()) {
                Map.Entry pair = (Map.Entry)queueStrings.next();
                String key = (String) pair.getKey();
                Object value = pair.getValue();
                if(key != null && value != null) {
                    Scheduler.runAsync(() -> {
                        DBCollection collection = Pixeltects.getPackageManager().getDatabaseManager().getCollection(DatabaseKey.profilesKey);
                        BasicDBObject basicDBObject1 = new BasicDBObject("uuid", uuid);
                        DBObject found = collection.findOne((DBObject)basicDBObject1);
                        BasicDBObject object = new BasicDBObject("$set", basicDBObject1);
                        object.append("$set", new BasicDBObject(key,value));
                        collection.update(found, object);
                    });
                }
                queueStrings.remove();
            }

            return true;
        }
        return false;
    }

}
