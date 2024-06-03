package com.kw.objectrewriter.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.socket.PortFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class ApiObjectReaderTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();
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
    void shouldConsumeSingleObjectInBody() {
        mockServer.when(request().withMethod("GET").withPath("/"))
                .respond(response().withBody("\"sample body\""));
        ApiObjectReader<String> reader = new ApiObjectReader<>(httpClient, objectMapper, new TypeReference<>() {
        });

        Optional<String> response = reader.readResponseBody(createUri("/"));

        assertThat(response).hasValue("sample body");
    }

    @Test
    void shouldConsumeCollectionOfObjectsInBody() {
        mockServer.when(request().withMethod("GET").withPath("/objects"))
                .respond(response().withBody("[\"sample1\", \"sample2\"]"));
        ApiObjectReader<List<String>> reader = new ApiObjectReader<>(httpClient, objectMapper, new TypeReference<>() {
        });

        Optional<List<String>> response = reader.readResponseBody(createUri("/objects"));

        assertThat(response).hasValueSatisfying(list -> assertThat(list).contains("sample1", "sample2"));
    }

    private URI createUri(String path) {
        return URI.create("http://localhost:" + mockServer.getLocalPort() + path);
    }
}