package com.searchengine.pagerank;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

public class PageRankStorage {

    private final JedisPool jedisPool;
    private static final String PREFIX = "pagerank:";

    public PageRankStorage(String host, int port, String password) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        this.jedisPool = new JedisPool(poolConfig, host, port, 2000, password, false);
        System.out.println("PageRankStorage connected to Redis");
    }

    public void saveRanks(Map<String, Double> pageRanks) {
        try (Jedis jedis = jedisPool.getResource()) {
            for (Map.Entry<String, Double> entry : pageRanks.entrySet()) {
                String key = PREFIX + entry.getKey();
                String value = String.valueOf(entry.getValue());
                jedis.set(key, value);
            }
            System.out.println("Saved " + pageRanks.size() + " PageRank scores to Redis");
        }
    }

    public double getRank(String url) {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(PREFIX + url);
            if (value == null) return 0.0;
            return Double.parseDouble(value);
        }
    }

    public void close() {
        jedisPool.close();
    }
}