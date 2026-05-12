package com.searchengine;

import com.searchengine.crawler2.CrawlerConfig;
import com.searchengine.crawler2.RealCrawler;
import com.searchengine.storage.RedisUrlQueue;
import com.searchengine.storage.PageStorage;

public class CrawlerRunner {public static void main(String[] args) {
    String host = "redis-10955.crce276.ap-south-1-3.ec2.cloud.redislabs.com";
    int port = 10955;
    String password = "ndFMWmSWnCw4Ja01UnRUiIgF7EkGEfgu";

    System.out.println("Connecting to Redis...");

    RedisUrlQueue redisQueue = new RedisUrlQueue(host, port, password);

    redisQueue.clearAll();
    System.out.println("Redis cleared");

    CrawlerConfig config = new CrawlerConfig();
    config.setSeedUrl("https://en.wikipedia.org/wiki/Java");
    config.setMaxPages(100);
    config.setDelayBetweenRequestsMs(500);

    PageStorage pageStorage = new PageStorage(host, port, password);
    pageStorage.clearAll();
    RealCrawler crawler = new RealCrawler(config, redisQueue, pageStorage);
    crawler.startCrawling();

    redisQueue.close();
    pageStorage.close();

}
}
