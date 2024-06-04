package com.kw.objectrewriter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kw.objectrewriter.model.Post;
import com.kw.objectrewriter.reader.ApiObjectReader;
import com.kw.objectrewriter.util.FileHandler;
import com.kw.objectrewriter.writer.ObjectSerializer;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.Collections;
import java.util.List;

public class ObjectRewriter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void rewritePosts(String url, String targetDirectory) {
        List<Post> posts = readPostsFromApi(URI.create(url));
        writePostsToFile(posts, targetDirectory);
    }

    private List<Post> readPostsFromApi(URI uri) {
        ApiObjectReader<List<Post>> reader = new ApiObjectReader<>(HttpClient.newHttpClient(), objectMapper, new TypeReference<>() {
        });
        return reader.readResponseBody(uri).orElse(Collections.emptyList());
    }

    private void writePostsToFile(List<Post> posts, String targetDirectory) {
        FileHandler.validateDirectory(targetDirectory);
        ObjectSerializer<Post> objectSerializer = new ObjectSerializer<>(objectMapper);
        objectSerializer.writeToSeparateFiles(posts, post -> new File(String.format("%s%s%s.json", targetDirectory, File.separator, post.id())));
    }
}
