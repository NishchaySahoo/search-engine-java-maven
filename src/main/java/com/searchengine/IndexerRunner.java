package com.searchengine;

import com.searchengine.Indexer.LuceneIndexer;
import com.searchengine.storage.PageStorage;

public class IndexerRunner {

    public static void main(String[] args) {
        String host = "redis-10955.crce276.ap-south-1-3.ec2.cloud.redislabs.com";
        int port = 10955;
        String password = "ndFMWmSWnCw4Ja01UnRUiIgF7EkGEfgu";
        String indexPath = "lucene-index";

        System.out.println("Connecting to Redis...");
        PageStorage pageStorage = new PageStorage(host, port, password);

        System.out.println("Setting up Lucene indexer...");

        try {
            LuceneIndexer indexer = new LuceneIndexer(indexPath);

            System.out.println("Fetching pages from Redis...");
            var urls = pageStorage.getAllPageUrls();
            System.out.println("Found " + urls.size() + " pages to index");
            System.out.println();

            for (String url : urls) {
                var page = pageStorage.getPage(url);
                if (page != null) {
                    indexer.indexPage(page);
                }
            }

            indexer.close();
            pageStorage.close();

            System.out.println();
            System.out.println("INDEXING COMPLETE");
            System.out.println("Index saved to: " + indexPath);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
