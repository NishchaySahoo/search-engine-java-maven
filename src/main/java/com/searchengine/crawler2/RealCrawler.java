package com.searchengine.crawler2;

import com.searchengine.storage.RedisUrlQueue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

public class RealCrawler {

    private final CrawlerConfig config;
    private final LinkExtractor linkExtractor;
    private final RobotsChecker robotsChecker;
    private final RedisUrlQueue redisQueue;

    private int pagesCrawled = 0;

    public RealCrawler( CrawlerConfig config, RedisUrlQueue redisQueue) {

        this.config = config;
        this.redisQueue = redisQueue;
        this.linkExtractor = new LinkExtractor(config);
        this.robotsChecker = new RobotsChecker(config.getConnectionTimeoutMs());

    }

    public void startCrawling() {
        System.out.println("REAL CRAWLER STARTING");
        System.out.println(config.toString());
        System.out.println();

        redisQueue.addToQueue(config.getSeedUrl());
        System.out.println("Seed URL added:" + config.getSeedUrl());
        System.out.println();

        while (pagesCrawled < config.getMaxPages()) {

            String url = redisQueue.getNextUrl();

            if(url == null) {
                System.out.println("Queue empty - crawling complete!");
                break;
            }

            if(redisQueue.isVisited(url)) {
                System.out.println("Skipping (visited): " + url);
                continue;
            }

            crawlPage(url);

        }

        System.out.println();
        System.out.println("CRAWLING COMPLETE");
        System.out.println("Pages Crawled: " + pagesCrawled);
        System.out.println("URLs in queue: " + redisQueue.getQueueSize());
        System.out.println("Total Visited: " + redisQueue.getVisitedCount());


    }

    private void crawlPage(String url) {
        System.out.println("[" + (pagesCrawled + 1) + "/" + config.getMaxPages() + "] Crawling: " + url);

        if(!robotsChecker.isAllowed(url)) {
            System.out.println(" Blocked by robots.txt - skipping");
            return;
        }

        try {
            Document document = Jsoup.connect(url).userAgent("MySearchEngine/1.0").timeout(config.getConnectionTimeoutMs()).get();

            redisQueue.markVisited(url);
            pagesCrawled++;

            String pageTitle = document.title();
            String pageText = document.body().text();
            System.out.println(" Title: " + pageTitle);
            System.out.println("  Content: " + pageText.length() + " chars");

            //Extract links
            List<String> links = linkExtractor.extractLinks(document, url);

            //add new links to redis
            int newLinksAdded = 0;
            for(String link : links) {
                if (!redisQueue.isVisited(link)) {
                    redisQueue.addToQueue(link);
                    newLinksAdded++;
                }
            }
            System.out.println(" new links queued: " + newLinksAdded);

            System.out.println("  waiting " + config.getDelayBetweenRequestsMs() + "ms...");
            Thread.sleep(config.getDelayBetweenRequestsMs());

        } catch (InterruptedException e) {
            /*InterruptedException = special Java exception
                                   thrown when Thread.sleep() is interrupted
                                   needs special handling
                                   must call Thread.currentThread().interrupt()
                                   to restore the interrupted status

             */
            Thread.currentThread().interrupt();
            System.out.println(" Crawler interrupted!");

        } catch (Exception e) {
            /*Exception = everything else
            /            network error, page not found,
            /            connection timeout, parsing error
            /           just print and continue
            */
            System.out.println(" Error: " + e.getMessage());
        }
    }

    public int getPagesCrawled() { return pagesCrawled; }

}
