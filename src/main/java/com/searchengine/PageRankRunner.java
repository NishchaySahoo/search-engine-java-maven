package com.searchengine;

import com.searchengine.crawler2.CrawledPage;
import com.searchengine.pagerank.PageRankCalculator;
import com.searchengine.pagerank.PageRankStorage;
import com.searchengine.storage.PageStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageRankRunner {

    public static void main(String[] args) {
        String host = "redis-10955.crce276.ap-south-1-3.ec2.cloud.redislabs.com";
        int port = 10955;
        String password = "ndFMWmSWnCw4Ja01UnRUiIgF7EkGEfgu";

        System.out.println("Connecting to Redis...");
        PageStorage pageStorage = new PageStorage(host, port, password);
        PageRankStorage pageRankStorage = new PageRankStorage(host, port, password);

        System.out.println("Loading pages from Redis...");
        List<String> urls = pageStorage.getAllPageUrls();
        System.out.println("Found " + urls.size() + " pages");
        System.out.println();

        // Build link map: url -> list of urls it links to
        Map<String, List<String>> linkMap = new HashMap<>();
        for (String url : urls) {
            CrawledPage page = pageStorage.getPage(url);
            if (page != null && page.getLinks() != null) {
                // only keep links that point to pages we have crawled
                List<String> filteredLinks = new ArrayList<>();
                for (String link : page.getLinks()) {
                    if (urls.contains(link)) {
                        filteredLinks.add(link);
                    }
                }
                linkMap.put(url, filteredLinks);
            } else {
                linkMap.put(url, new ArrayList<>());
            }
        }

        System.out.println("Link map built:");
        for (Map.Entry<String, List<String>> entry : linkMap.entrySet()) {
            System.out.println(" " + entry.getKey()
                    + " -> " + entry.getValue().size() + " links");
        }
        System.out.println();

        // Calculate PageRank
        PageRankCalculator calculator = new PageRankCalculator(linkMap);
        Map<String, Double> ranks = calculator.calculate();

        System.out.println();
        System.out.println("FINAL PAGERANK SCORES:");
        ranks.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry -> System.out.println(
                        " " + entry.getValue() + " → " + entry.getKey()));

        // Save to Redis
        System.out.println();
        pageRankStorage.saveRanks(ranks);

        pageStorage.close();
        pageRankStorage.close();

        System.out.println();
        System.out.println("PAGERANK COMPLETE");
    }
}