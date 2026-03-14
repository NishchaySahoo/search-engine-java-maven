package com.searchengine.crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class BasicCrawler implements Crawler {

    //final : this variable can only be assigned ONCE
    //          once set in constructor, can never be
    //          replaced with a different HashSet
    private final HashSet<String> visitedUrls;
    private final Queue<String> urlQueue;

    public BasicCrawler(){
        this.visitedUrls = new HashSet<>();
        this.urlQueue = new LinkedList<>();
    }

    @Override
    public void crawl(String url) throws CrawlerException {

        if (url == null || url.isEmpty()){
            throw new CrawlerException("URL cannot be null or empty", url);
        }

        if( isVisited(url)){
            System.out.println("Skipping (already visited): " + url);
            return;
        }

        try{
            System.out.println("Crawling: " + url);

            visitedUrls.add(url);

            String newLink1 = url + "/page1";
            String newLink2 = url + "/page2";

            urlQueue.add(newLink1);
            urlQueue.add(newLink2);

            System.out.println(" Found Links: " + newLink1 + ", " + newLink2);
        } catch (Exception e){
            throw new CrawlerException("Failed to crawl: " + e.getMessage(), url);
        }

    }

    @Override
    public boolean isVisited(String url){
        return visitedUrls.contains(url);
    }

    @Override
    public int getTotalPagesCrawled(){
        return visitedUrls.size();
    }

    public void crawlQueue(){
        System.out.println("\nStarting queue crawl...");
        System.out.println("URLs in queue: " + urlQueue.size());
        System.out.println("---");

        int maxCrawls = 10;
        int count = 0;

        while (!urlQueue.isEmpty() && count < maxCrawls) {
            String url = urlQueue.poll();
            try{
                crawl(url);
                count++;
            } catch (CrawlerException e){
                System.out.println("Error skipping URL: " + e);
            }
        }
        System.out.println("---");
        System.out.println("Queue empty. Done.");
    }

}
