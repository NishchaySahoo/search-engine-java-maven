package com.searchengine.crawler;

public interface Crawler {

    //visits the URL and throws exception if site not reached
    void crawl(String url) throws CrawlerException;

    //returns true if the site is visited and false if its not
    boolean isVisited(String url);

    //gets the total number of pages crawled so far
    int getTotalPagesCrawled();
}
