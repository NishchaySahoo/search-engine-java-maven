package com.searchengine.crawler2;

import java.util.List;

public class CrawledPage {

    private final String url;
    private final String title;
    private final String content;
    private final long timestamp;
    private final int wordCount;
    private final List<String> links;

    public CrawledPage(String url, String title, String content, List<String> links) {
        this.url = url;
        this.title = title;
        this.content = content;
        this.links = links;
        this.timestamp = System.currentTimeMillis();
        this.wordCount = countWords(content);
    }

    private int countWords(String text) {
        if (text == null || text.isEmpty()) return 0;
        return text.split("\\s+").length;
    }

    public String getUrl() { return url;}
    public String getTitle() {return title; }
    public String getContent(){ return content; }
    public long getTimestamp() { return timestamp; }
    public int getWordCount() { return wordCount; }
    public List<String> getLinks() { return links; }

    @Override
    public String toString(){
        return "CrawledPage {" + "\n url  =" + url + "\n title  =" + title + "\n words  =" + wordCount + "\n timestamp = " + timestamp + "\n}";

    }
}
