package com.pixeltects.core.database.redis;

import com.pixeltects.core.Pixeltects;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedisManager {

    private Pixeltects packageManager;

    @Getter
    private JedisPool jedisPool;

    private Map<String, RedisListener> listenerMap = new ConcurrentHashMap<>();
    private String host;
    private int port;

    public RedisManager(Pixeltects mainClass) {
        this.packageManager = mainClass;
        System.out.print("[PixeltectsCore] 'RedisManager' class initialized");

        if(connect("[REDACTED]", 6379)) {
            System.out.print("[RedisManager] Redis connected successfully");

            new Thread(this::runSubscriber, "Redis Subscriber Thread").start();
        }else{
            System.out.print("[RedisManager] Redis failed to connect");
        }

    }

    public boolean connect(String host, int port) {
        this.host = host;
        this.port = port;

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(9000);

        this.jedisPool = new JedisPool(jedisPoolConfig,host,port);

        if(jedisPool != null) {
            return true;
        }
        return false;
    }

    private void runSubscriber() {
        int sec = 1;
        while(true) {
            Jedis subscriber = null;
            try {
                subscriber = new Jedis(host, port);
                sec = 1;
                subscriber.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONParser().parse(message);
                            String subChannel = (String) jsonObject.remove("channel");

                            RedisListener redisListener = listenerMap.get(subChannel);
                            if(redisListener != null) {
                                redisListener.receive(subChannel, jsonObject);
                            }
                        }catch(ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, "[REDACTED]");

            }catch(JedisConnectionException e) {
                if(subscriber != null) {
                    subscriber.close();
                }
                System.out.print("[Redis Status] Lost redis connection.. going to retry in " + sec + " seconds...");
                e.printStackTrace();
                try{
                    Thread.sleep(sec * 1000);
                }catch(InterruptedException ex) {
                    return;
                }
                sec += sec;
            }
        }
    }

    public void registerListener(RedisListener listener, String... channels) {
        Arrays.stream(channels).forEach(channel -> {
            listenerMap.putIfAbsent(channel, listener);
            System.out.print("[Redis] Registered channel listener for " + channel + ".");
        });
    }

    public void registerListener(String channel, RedisListener listener) {
        listenerMap.putIfAbsent(channel, listener);
        System.out.print("[Redis] Registered channel listener for " + channel + ".");
    }

    public void post(JSONObject object) {
        try (Jedis publisher = jedisPool.getResource()) {
            publisher.publish("[REDACTED]", object.toJSONString());
        }
    }


}
