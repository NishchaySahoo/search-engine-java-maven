package com.searchengine.crawler2;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RobotsChecker {

    private final HashMap<String, List<String>> robotsCache;

    private final int timeoutMs;

    public RobotsChecker (int timeoutMs) {
        this.robotsCache = new HashMap<>();
        this.timeoutMs = timeoutMs;
    }

    public boolean isAllowed(String url) {
        String domain = extractDomain(url);
        if(domain == null) return false;

        List<String> disallowedPaths = getDisallowedPaths(domain);

        for(String disallowedPath : disallowedPaths) {
            if(url.contains(disallowedPath)) {
                return false;
            }
        }

        return true;

    }

    private List<String> getDisallowedPaths(String domain) {

        if(robotsCache.containsKey(domain)) {
            System.out.println(" robots.txt cache hit for: " + domain);
            return robotsCache.get(domain);

        }

        System.out.println(" Downloading robots.txt for: " + domain);
        List<String> disallowedPaths = downloadRobotsTxt(domain);

        robotsCache.put(domain, disallowedPaths);

        return disallowedPaths;
    }

    private List<String> downloadRobotsTxt(String domain) {
        List<String> disallowedPaths = new ArrayList<>();

        try{
            String robotsUrl = "https://" + domain + "/robots.txt";

            String content = Jsoup.connect(robotsUrl).userAgent("MySearchEngine/1.0").timeout(timeoutMs).execute().body();

            String[] lines = content.split("\n");
            boolean applicableSection = false;

            for(String line : lines) {
                line = line.trim();

                if(line.startsWith("Use-agent:")) {
                    String agent = line.substring(11).trim();
                    applicableSection = agent.equals("*") || agent.equals("MySearchEngine");
                }

                if(applicableSection && line.startsWith("Disallow")) {
                    String path = line.substring(9).trim();
                    if(!path.isEmpty()) {
                        disallowedPaths.add(path);
                    }
                }

            }
            System.out.println("  Found " + disallowedPaths.size() + " disallowed paths");

        } catch (Exception e) {
            System.out.println("  Could not fetch robots.txt: " + e.getMessage());

        }
        return disallowedPaths;
    }


    //extract domain method
    private String extractDomain(String url) {
        try {
            String withoutProtocol = url.replace("https://", "").replace("htt://","");

            int slashIndex = withoutProtocol.indexOf("/");
            if(slashIndex == -1) {
                return withoutProtocol;
            }
            return withoutProtocol.substring(0, slashIndex);

        } catch(Exception e) {
            return null;
        }
    }

    // size of cache method
    public int getCacheSize() {
        return robotsCache.size();
    }



}
