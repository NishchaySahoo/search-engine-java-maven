package com.searchengine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JSoupTest {
    public static void main(String[] args) {
        try {
            // One line to fetch an entire webpage!
            Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Java")
                    .userAgent("MySearchEngine/1.0")
                    .timeout(3000)
                    .get();

            // Get page title
            System.out.println("Page Title: " + doc.title());
            System.out.println();

            // Get ALL links on the page
            Elements links = doc.select("a[href]");
            System.out.println("Total links found: " + links.size());
            System.out.println();

            // Print first 5 links
            System.out.println("First 5 links:");
            System.out.println("---");
            int count = 0;
            for (Element link : links) {
                if (count < 5) {
                    System.out.println(link.attr("href"));
                    count++;
                }
            }
            System.out.println("---");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}