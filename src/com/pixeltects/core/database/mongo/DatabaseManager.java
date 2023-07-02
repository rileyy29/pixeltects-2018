package com.pixeltects.core.database.mongo;

import com.mongodb.*;
import com.pixeltects.core.Pixeltects;
import lombok.Getter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Arrays;

public class DatabaseManager {

    private Pixeltects packageManager;

    @Getter
    private MongoClient mongoClient;

    @Getter
    private DB database;

    private String databaseName;
    private String host;

    public DatabaseManager(Pixeltects mainClass) {
        this.packageManager = mainClass;
        System.out.print("[PixeltectsCore] 'DatabaseManager' class initialized");

        this.host = "[REDACTED]";
        this.databaseName = "[REDACTED]";

        if(connectDatabase(this.host, this.databaseName)) { //Attempt the connection
            System.out.print("[DatabaseManager] Connection successful.");
        }else{
            System.out.print("[DatabaseManager] Connection failed.");
        }

    }

    public boolean connectDatabase(String ip, String databaseName) {
        mongoClient = new MongoClient(ip, 0000);

        if(mongoClient == null) {
            return false;
        }else{
            this.database = mongoClient.getDB(databaseName);
        }

        return true;
    }

    public boolean connectDatabase() {
        mongoClient = new MongoClient("[REDACTED]", 0000);

        if(mongoClient == null) {
            return false;
        }else{
            this.database = mongoClient.getDB(this.databaseName);
        }

        return true;
    }

    public void closeConnection() {
        if(this.mongoClient != null) {
            this.mongoClient.close();
            this.mongoClient = null;
        }
    }

    public DBCollection getCollection(String collName) {
        return this.database.getCollection(collName);
    }

}
