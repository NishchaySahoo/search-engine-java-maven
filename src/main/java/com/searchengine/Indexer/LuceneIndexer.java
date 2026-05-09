package com.searchengine.Indexer;

import com.searchengine.crawler2.CrawledPage;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class LuceneIndexer {

    private final IndexWriter indexWriter;

    public LuceneIndexer(String indexPath) throws IOException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        FSDirectory directory = FSDirectory.open(Paths.get(indexPath));
        this.indexWriter = new IndexWriter(directory, config);
    }

    public void indexPage(CrawledPage page) throws IOException {
        Document doc = new Document();
        doc.add(new StringField("url", page.getUrl(), Field.Store.YES));
        doc.add(new TextField("title", page.getTitle(), Field.Store.YES));
        doc.add(new TextField("content", page.getContent(), Field.Store.YES));
        indexWriter.addDocument(doc);
        System.out.println("Indexed: " + page.getTitle());
    }

    public void close() throws IOException {
        indexWriter.close();
    }

}
