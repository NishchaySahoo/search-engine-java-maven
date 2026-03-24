package com.searchengine;

import com.searchengine.crawler2.CrawlerConfig;
import com.searchengine.crawler2.RealCrawler;
import com.searchengine.storage.RedisUrlQueue;

public class CrawlerRunner {public static void main(String[] args) {
    String host = "redis-13213.crce283.ap-south-1-2.ec2.cloud.redislabs.com";
    int port = 13213;
    String password = "XbAYC8KLXS7tcmIyhlAWXbNpblW4kPoG";

    System.out.println("Connecting to Redis...");

    RedisUrlQueue redisQueue = new RedisUrlQueue(host, port, password);

    redisQueue.clearAll();
    System.out.println("Redis cleared");

    CrawlerConfig config = new CrawlerConfig();
    config.setSeedUrl("https://en.wikipedia.org/wiki/Java");
    config.setMaxPages(5);
    config.setDelayBetweenRequestsMs(500);

    RealCrawler crawler = new RealCrawler(config, redisQueue);
    crawler.startCrawling();

    redisQueue.close();

}
}
