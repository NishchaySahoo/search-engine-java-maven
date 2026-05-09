package com.searchengine.pagerank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageRankCalculator {

    private static final double DAMPING_FACTOR = 0.85;
    private static final int MAX_ITERATIONS = 50;
    private static final double CONVERGENCE_THRESHOLD = 0.0001;

    // links: url -> list of urls it links to
    private final Map<String, List<String>> links;
    private final Map<String, Double> pageRanks;

    public PageRankCalculator(Map<String, List<String>> links) {
        this.links = links;
        this.pageRanks = new HashMap<>();
    }

    public Map<String, Double> calculate() {
        int totalPages = links.size();

        // initializing every page with equal rank
        double initialRank = 1.0 / totalPages;
        for (String url : links.keySet()) {
            pageRanks.put(url, initialRank);
        }

        System.out.println("Starting PageRank with " + totalPages + " pages");
        System.out.println("Initial rank per page: " + initialRank);
        System.out.println();

        // iterate
        for (int iteration = 1; iteration <= MAX_ITERATIONS; iteration++) {
            Map<String, Double> newRanks = new HashMap<>();
            double maxChange = 0.0;

            for (String url : links.keySet()) {
                double rankSum = 0.0;

                // find all pages that link TO this url
                for (Map.Entry<String, List<String>> entry : links.entrySet()) {
                    String otherUrl = entry.getKey();
                    List<String> outLinks = entry.getValue();

                    if (outLinks.contains(url)) {
                        double otherRank = pageRanks.get(otherUrl);
                        rankSum += otherRank / outLinks.size();
                    }
                }

                double newRank = (1 - DAMPING_FACTOR) / totalPages
                        + DAMPING_FACTOR * rankSum;
                newRanks.put(url, newRank);

                double change = Math.abs(newRank - pageRanks.get(url));
                maxChange = Math.max(maxChange, change);
            }

            pageRanks.putAll(newRanks);

            System.out.println("Iteration " + iteration
                    + " - max change: " + maxChange);

            // then stop early if scores have stabilized
            if (maxChange < CONVERGENCE_THRESHOLD) {
                System.out.println("Converged after " + iteration
                        + " iterations!");
                break;
            }
        }

        return pageRanks;
    }
}
