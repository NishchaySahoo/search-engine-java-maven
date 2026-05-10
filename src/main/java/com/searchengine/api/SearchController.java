package com.searchengine.api;

import com.searchengine.Indexer.SearchEngine;
import com.searchengine.pagerank.PageRankStorage;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SearchController {

    private final SearchEngine searchEngine;
    private final PageRankStorage pageRankStorage;

    public SearchController() throws Exception {
        this.searchEngine = new SearchEngine("lucene-index");
        this.pageRankStorage = new PageRankStorage(
                "redis-10955.crce276.ap-south-1-3.ec2.cloud.redislabs.com",
                10955,
                "ndFMWmSWnCw4Ja01UnRUiIgF7EkGEfgu"
        );
    }

    @GetMapping("/search")
    public List<SearchResult> search(@RequestParam String query) throws Exception {
        return searchEngine.searchWithPageRank(query, 10, pageRankStorage);
    }

    @GetMapping("/health")
    public String health() {
        return "Search Engine is running!";
    }
}
