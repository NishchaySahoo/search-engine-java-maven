package com.searchengine;

import com.searchengine.storage.RedisUrlQueue;

public class RedisTest {
    public static void main(String[] args) {

        // Your Redis Cloud connection details
        String host = "MY REDIS HOST";  // replace this
        int port = 13213;                  // replace with your port
        String password = "MY REDIS PASSWORD"; // replace this

        System.out.println("Redis Connection Test");
        System.out.println();

        // Create RedisUrlQueue — connects to Redis Cloud
        RedisUrlQueue queue = new RedisUrlQueue(host, port, password);

        // Clear any old data first
        queue.clearAll();
        System.out.println("Cleared old data");

        // Test 1 — Add URLs to queue
        System.out.println();
        System.out.println("--- Test 1: Adding URLs to queue ---");
        queue.addToQueue("https://wikipedia.org");
        queue.addToQueue("https://google.com");
        queue.addToQueue("https://github.com");
        System.out.println("Added 3 URLs to queue");
        System.out.println("Queue size: " + queue.getQueueSize());

        // Test 2 — Mark as visited
        System.out.println();
        System.out.println("--- Test 2: Marking URLs as visited ---");
        queue.markVisited("https://wikipedia.org");
        queue.markVisited("https://google.com");
        System.out.println("Marked 2 URLs as visited");
        System.out.println("Visited count: " + queue.getVisitedCount());

        // Test 3 — Check isVisited
        System.out.println();
        System.out.println("--- Test 3: Checking visited status ---");
        System.out.println("wikipedia.org visited? " +
                queue.isVisited("https://wikipedia.org"));
        System.out.println("github.com visited? " +
                queue.isVisited("https://github.com"));

        // Test 4 — Process queue
        System.out.println();
        System.out.println("--- Test 4: Processing queue ---");
        String nextUrl;
        while ((nextUrl = queue.getNextUrl()) != null) {
            if (queue.isVisited(nextUrl)) {
                System.out.println("Skipping: " + nextUrl);
            } else {
                System.out.println("Processing: " + nextUrl);
                queue.markVisited(nextUrl);
            }
        }

        // Final result
        System.out.println();
        System.out.println("Queue empty: " + (queue.getQueueSize() == 0));
        System.out.println("Total visited: " + queue.getVisitedCount());

        // Close connection
        queue.close();
        System.out.println();
        System.out.println("=== Test Complete ===");
    }
}