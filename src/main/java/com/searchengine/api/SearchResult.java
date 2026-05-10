package com.searchengine.api;

public class SearchResult {

    private final String url;
    private final String title;
    private final float luceneScore;
    private final double pageRankScore;
    private final double combinedScore;

    public SearchResult(String url, String title,
                        float luceneScore, double pageRankScore,
                        double combinedScore) {
        this.url = url;
        this.title = title;
        this.luceneScore = luceneScore;
        this.pageRankScore = pageRankScore;
        this.combinedScore = combinedScore;
    }

    public String getUrl() { return url; }
    public String getTitle() { return title; }
    public float getLuceneScore() { return luceneScore; }
    public double getPageRankScore() { return pageRankScore; }
    public double getCombinedScore() { return combinedScore; }
}