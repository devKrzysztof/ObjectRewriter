package com.kw.objectrewriter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kw.objectrewriter.model.Post;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.socket.PortFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class ObjectRewriterTest {
    private static final Logger logger = LoggerFactory.getLogger(ObjectRewriterTest.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private ClientAndServer mockServer;

    @BeforeClass
    private void startServer() {
        mockServer = startClientAndServer(PortFactory.findFreePort());
    }

    @AfterClass
    private void stopServer() {
        mockServer.stop();
    }

    @Test
    void shouldRewritePosts() {
        String tmpdir = System.getProperty("java.io.tmpdir");
        String body = "[{\"userId\":1,\"id\":11,\"title\":\"title1\",\"body\":\"body1\"},{\"userId\":2,\"id\":22,\"title\":\"title2\",\"body\":\"body2\"}]";
        mockServer.when(request().withMethod("GET").withPath("/"))
                .respond(response().withBody(body));
        ObjectRewriter objectRewriter = new ObjectRewriter();

        objectRewriter.rewritePosts("http://localhost:" + mockServer.getLocalPort() + "/", tmpdir);

        Arrays.asList(1, 2).forEach(id ->
                assertThat(createTmpFilePath((id * 11) + ".json"))
                        .exists()
                        .content()
                        .matches(content -> postDeserializer(content, id), "is equal to expected post")
        );
    }

    @AfterTest
    private void removeTmpFiles() {
        Arrays.asList(createTmpFilePath("11.json"), createTmpFilePath("22.json"))
                .forEach(this::deleteFile);
    }

    private static Path createTmpFilePath(String filename) {
        return Path.of(System.getProperty("java.io.tmpdir") + File.separator + filename);
    }

    private boolean postDeserializer(String content, int id) {
        try {
            return mapper.readValue(content, Post.class).equals(new Post(id, id * 11L, "title" + id, "body" + id));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteFile(Path file) {
        try {
            Files.delete(file);
            logger.info("Deleted file {}", file.toAbsolutePath());
        } catch (FileSystemException e) {
            logger.info("Could not remove nonexistent file {}", file.toAbsolutePath(), e);
        } catch (IOException e) {
            logger.error("Could not remove file due to IOException: ", e);
            throw new RuntimeException(e);
        }
    }
}