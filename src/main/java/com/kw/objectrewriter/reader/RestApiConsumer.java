package com.kw.objectrewriter.reader;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Optional;

class RestApiConsumer<T> {
    private final HttpClient httpClient;
    private final InputStreamBodySubscriberProvider<T> inputStreamBodySubscriberProvider;

    RestApiConsumer(HttpClient httpClient, InputStreamBodySubscriberProvider<T> inputStreamBodySubscriberProvider) {
        this.httpClient = httpClient;
        this.inputStreamBodySubscriberProvider = inputStreamBodySubscriberProvider;
    }

    Optional<T> retrieveResponseBody(HttpRequest httpRequest) {
        try {
            return httpClient.send(httpRequest, inputStreamBodySubscriberProvider::getBodySubscriber).body();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
