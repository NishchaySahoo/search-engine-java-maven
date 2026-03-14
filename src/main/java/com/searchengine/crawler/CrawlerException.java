package com.searchengine.crawler;

public class CrawlerException extends Exception{

    // stores the URL which caused the problem
    private final String url;

    //a constructor
    public CrawlerException(String message, String url){
        super(message);  //sending message UP to parent not bringing anything DOWN ,Child says to Parent:"Here is the message — YOU store it in your system using your constructor"
        this.url = url;
    }

    //this is a getter
    public String getUrl() {
        return url;
    }

    //I'm replacing the default version of this method with my own version." Every Java object has a default `toString()` that prints something ugly. We're overriding it to print something readable.
    @Override
    public String toString() {
        return "CrawlerException: [url=" + url + "] [message=" + getMessage() + "]";
    }

}
