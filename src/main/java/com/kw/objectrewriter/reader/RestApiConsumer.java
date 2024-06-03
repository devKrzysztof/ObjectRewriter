package com.kw.objectrewriter.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Optional;

class RestApiConsumer<T> {
    private static final Logger logger = LoggerFactory.getLogger(RestApiConsumer.class);
    private final HttpClient httpClient;
    private final InputStreamBodySubscriberProvider<T> inputStreamBodySubscriberProvider;

    RestApiConsumer(HttpClient httpClient, InputStreamBodySubscriberProvider<T> inputStreamBodySubscriberProvider) {
        this.httpClient = httpClient;
        this.inputStreamBodySubscriberProvider = inputStreamBodySubscriberProvider;
    }

    Optional<T> retrieveResponseBody(HttpRequest httpRequest) {
        try {
            return httpClient.send(httpRequest, inputStreamBodySubscriberProvider::getBodySubscriber).body();
        } catch (IOException e) {
            logger.error("Could not read response due to following error: {}", e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error("Request failed due to thread interruption: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return Optional.empty();
    }

}
