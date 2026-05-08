package com.searchengine.storage;

import com.google.gson.Gson; //googles library for converting java objects into json
import com.searchengine.crawler2.CrawledPage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PageStorage {

    private final JedisPool jedisPool;
    private final Gson gson;

    private static final String PAGE_KEY_PREFIX = "page:";
    private static final String ALL_PAGES_KEY = "pages:all";

    public PageStorage(String host, int port, String password) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setTestOnBorrow(true);

        this.jedisPool = new JedisPool(
                poolConfig,
                host,
                port,
                2000,
                "default",
                password,
                false
        );

        this.gson = new Gson();
        System.out.println("PageStorage connected to Redis ✅");
    }

    // Save a crawled page to Redis
    public void savePage(CrawledPage page) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = gson.toJson(page);
            String key = PAGE_KEY_PREFIX + page.getUrl();
            jedis.set(key, json);
            jedis.sadd(ALL_PAGES_KEY, new String[]{page.getUrl()});
            System.out.println("  Saved: " + page.getTitle()
                    + " (" + page.getWordCount() + " words)");
        } catch (Exception e) {
            System.out.println("  Error saving: " + e.getMessage());
        }
    }

    // Get a page by URL
    public CrawledPage getPage(String url) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = PAGE_KEY_PREFIX + url;
            String json = jedis.get(key);
            if (json == null) return null;
            return gson.fromJson(json, CrawledPage.class);
        } catch (Exception e) {
            return null;
        }
    }

    // Get total pages stored
    public long getTotalPages() {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.scard(ALL_PAGES_KEY);
        } catch (Exception e) {
            return 0;
        }
    }

    // Get all stored URLs
    public List<String> getAllPageUrls() {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> urls = jedis.smembers(ALL_PAGES_KEY);
            return new ArrayList<>(urls);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Clear all stored pages
    public void clearAll() {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> urls = jedis.smembers(ALL_PAGES_KEY);
            for (String url : urls) {
                jedis.del(PAGE_KEY_PREFIX + url);
            }
            jedis.del(ALL_PAGES_KEY);
            System.out.println("PageStorage cleared ✅");
        } catch (Exception e) {
            System.out.println("Error clearing: " + e.getMessage());
        }
    }

    public void close() {
        jedisPool.close();
    }
}