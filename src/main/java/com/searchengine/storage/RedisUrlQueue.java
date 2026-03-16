package com.searchengine.storage;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

//Jedis is the actual REDIS connection object
//JedisPool is used to make multiple connection at a time
//JedisPoolConfig configuration settings for the pool

public class RedisUrlQueue {

    private final JedisPool jedisPool;

    private static final String URL_QUEUE_KEY = "crawler:queue";
    private static final String VISITED_SET_KEY = "crawler:visited";

    public RedisUrlQueue(String host, int port, String password) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setTestOnBorrow(true);

        // Connect without SSL for now
        this.jedisPool = new JedisPool(
                poolConfig,
                host,
                port,
                2000,    // timeout
                "default", //username
                password,
                false    // SSL disabled
        );

        System.out.println("Connected to Redis at " + host + ":" + port);
    }

    public void addToQueue(String url) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(URL_QUEUE_KEY, url);  //push to the right
        }
    }

    public String getNextUrl(){
        try(Jedis jedis = jedisPool.getResource()) {
            return jedis.lpop(URL_QUEUE_KEY);  //pop from the left
        }
    }

    public void markVisited(String url) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.sadd(VISITED_SET_KEY, new String[]{url});  //add to a set
        }
    }

    public boolean isVisited(String url){
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.sismember(VISITED_SET_KEY, url); //check if item exists in SET
        }
    }

    public long getQueueSize(){
        try (Jedis jedis = jedisPool.getResource()){
            return jedis.llen(URL_QUEUE_KEY); //list length
        }
    }

    public long getVisitedCount(){
        try(Jedis jedis = jedisPool.getResource()){
            return jedis.scard(VISITED_SET_KEY); //set size
        }
    }

    public void clearAll(){
        try(Jedis jedis = jedisPool.getResource()){
            jedis.del(URL_QUEUE_KEY);
            jedis.del(VISITED_SET_KEY);  //delete
        }
    }

    public void close(){
        jedisPool.close();
    }
}
