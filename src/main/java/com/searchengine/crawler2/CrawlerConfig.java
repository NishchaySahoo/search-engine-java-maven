package com.searchengine.crawler2;

public class CrawlerConfig {

    private int maxPages;
    private int maxDepth;
    private int delayBetweenRequestsMs;
    private String allowedDomain;
    private int connectionTimeoutMs;
    private String seedUrl;

    public CrawlerConfig(){
        this.maxPages = 100;
        this.maxDepth = 3;
        this.delayBetweenRequestsMs = 1000;
        this.allowedDomain = "en.wikipedia.org";
        this.connectionTimeoutMs = 3000;
        this.seedUrl = "https://en.wikipedia.org/wiki/Java_(programming_language)";
    }

    //Getters
    public int getMaxPages() { return maxPages;}
    public int getMaxDepth() { return maxDepth;}
    public int getDelayBetweenRequestsMs() { return delayBetweenRequestsMs; }
    public String getAllowedDomain() { return allowedDomain; }
    public int getConnectionTimeoutMs() { return connectionTimeoutMs; }
    public String getSeedUrl() { return seedUrl; }

    //Setters
    public void setMaxPages(int maxPages) { this.maxPages = maxPages; }
    public void setMaxDepth(int maxDepth) { this.maxDepth = maxDepth; }
    public void setDelayBetweenRequestsMs(int delay) { this.delayBetweenRequestsMs = delay;}
    public void setAllowedDomain(String domain) { this.allowedDomain = domain; }
    public void setConnectionTimeoutMs(int timeout ) { this.connectionTimeoutMs = timeout; }
    public void setSeedUrl(String seedUrl) { this.seedUrl = seedUrl; }

    @Override
    public String toString() {
        return "\nCrawlerConfig {" + seedUrl + "\n allowedDomain = " + allowedDomain + "\n maxPages = " + maxPages + "\n maxDepth = " + maxDepth + "\n delay = " + delayBetweenRequestsMs + "ms" + "\n timeout = " + connectionTimeoutMs + "ms" + "\n}";

    }
}
