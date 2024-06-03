package com.kw.objectrewriter.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Optional;

public class ApiObjectReader<T> {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final TypeReference<T> typeReference;

    public ApiObjectReader(HttpClient httpClient, ObjectMapper objectMapper, TypeReference<T> typeReference) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.typeReference = typeReference;
    }

    public Optional<T> readResponseBody(URI uri) {
        InputStreamMapper<T> inputStreamMapper = new InputStreamMapper<>(objectMapper, typeReference);
        RestApiConsumer<T> restApiConsumer = new RestApiConsumer<>(httpClient, new InputStreamBodySubscriberProvider<>(inputStreamMapper));
        HttpRequest httpRequest = HttpRequest.newBuilder(uri).build();
        return restApiConsumer.retrieveResponseBody(httpRequest);
    }

}
