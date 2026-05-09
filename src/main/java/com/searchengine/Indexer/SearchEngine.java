package com.searchengine.Indexer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class SearchEngine {

    private final IndexSearcher searcher;
    private final QueryParser parser;

    public SearchEngine(String indexPath) throws IOException {
        FSDirectory directory = FSDirectory.open(Paths.get(indexPath));
        DirectoryReader reader = DirectoryReader.open(directory);
        this.searcher = new IndexSearcher(reader);
        this.parser = new QueryParser("content", new StandardAnalyzer());
    }

    public void search(String queryText, int maxResults) throws Exception {
        Query query = parser.parse(queryText);
        TopDocs results = searcher.search(query, maxResults);

        System.out.println("Search: \"" + queryText + "\"");
        System.out.println("Found: " + results.totalHits.value + " matching pages");
        System.out.println();

        if (results.totalHits.value == 0) {
            System.out.println("No results found.");
            return;
        }

        int rank = 1;
        for (ScoreDoc scoreDoc : results.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println("[" + rank + "] " + doc.get("title"));
            System.out.println("     URL: " + doc.get("url"));
            System.out.println("     Score: " + scoreDoc.score);
            System.out.println();
            rank++;
        }
    }
}
