package com.pixeltects.core.player.manager.loading;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.database.mongo.DatabaseKey;
import com.pixeltects.core.database.mongo.DatabaseManager;
import com.pixeltects.core.player.PlayerProfile;
import com.pixeltects.core.utils.executor.BukkitAsyncExecutor;

import java.util.UUID;

//Global-profileStorage

public class MongoPlayerLoader {

    private final BukkitAsyncExecutor asyncExecutor = Pixeltects.getAsyncExecutor();

    public PlayerProfile loadPlayer(UUID uuid) {
        PlayerProfile profile;
        DatabaseManager databaseManager = Pixeltects.getPackageManager().getDatabaseManager();
        DBCollection dbCollection = databaseManager.getCollection(DatabaseKey.profilesKey);
        BasicDBObject basicDBObject = new BasicDBObject("uuid", uuid);
        DBObject objectFound = dbCollection.findOne(basicDBObject);

        if(objectFound == null) {
            profile = new PlayerProfile(uuid);
        }else{
            profile = new PlayerProfile(uuid, objectFound);
        }
        return profile;
    }

}
