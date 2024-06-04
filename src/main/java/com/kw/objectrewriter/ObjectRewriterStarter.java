package com.kw.objectrewriter;

import com.kw.objectrewriter.util.AppProperties;

public class ObjectRewriterStarter {
    private static final AppProperties appProperties = new AppProperties("application.properties");

    public static void main(String[] args) {
        String postsUrl = appProperties.getProperty("api.url.posts");
        String targetDirectory = System.getProperty("outputDir.posts", "posts");
        new ObjectRewriter().rewritePosts(postsUrl, targetDirectory);
    }
}
