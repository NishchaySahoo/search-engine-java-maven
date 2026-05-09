package com.searchengine;

import com.searchengine.Indexer.SearchEngine;

public class SearchRunner {

    public static void main(String[] args) throws Exception {
        String indexPath = "lucene-index";

        System.out.println("Starting Search Engine...");
        System.out.println();

        SearchEngine engine = new SearchEngine(indexPath);

        engine.search("Java", 5);
        engine.search("programming language", 5);
        engine.search("wikipedia", 5);
    }
}