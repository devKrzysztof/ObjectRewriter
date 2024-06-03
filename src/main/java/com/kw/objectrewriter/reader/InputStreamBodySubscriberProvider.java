package com.kw.objectrewriter.reader;

import java.net.http.HttpResponse;
import java.util.Optional;

class InputStreamBodySubscriberProvider<T> {

    private final InputStreamMapper<T> inputStreamMapper;

    InputStreamBodySubscriberProvider(InputStreamMapper<T> inputStreamMapper) {
        this.inputStreamMapper = inputStreamMapper;
    }

    HttpResponse.BodySubscriber<Optional<T>> getBodySubscriber(HttpResponse.ResponseInfo responseInfo) {
        return HttpResponse.BodySubscribers.mapping(HttpResponse.BodySubscribers.ofInputStream(), inputStreamMapper::mapToObject);
    }
}
