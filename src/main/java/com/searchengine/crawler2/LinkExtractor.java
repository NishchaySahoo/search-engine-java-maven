package com.searchengine.crawler2;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class LinkExtractor {

    private final CrawlerConfig config;

    public LinkExtractor(CrawlerConfig config) {
        this.config = config;

    }

    public List<String> extractLinks(Document document, String baseUrl) {

        List<String> validLinks = new ArrayList<>();
        Elements allLinks = document.select("a[href]");

        System.out.println("  Raw links found: " + allLinks.size());

        for(Element link : allLinks) {

            String href = link.attr("abs:href");

            if(isValidLink(href)) {
                validLinks.add(href);
            }
        }

        System.out.println("  Valid links after filtering: " + validLinks.size());

        return validLinks;
    }

    private boolean isValidLink(String url) {

        if(url == null || url.isEmpty()) {
            return false;
        }

        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            return false;
        }

        if (url.contains("#")) {
            return false;
        }

        if (url.contains("Special:") ||
                url.contains("File:") ||
                url.contains("Talk:") ||
                url.contains("User:") ||
                url.contains("Help:") ||
                url.contains("Template:") ||
                url.contains("Category:")) {
            return false;
        }

        if(url.contains("action=edit") || url.contains("action=history") || url.contains("redlink=1")){
            return false;
        }

        return true;

    }
}
