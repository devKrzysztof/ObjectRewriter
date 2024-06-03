package com.kw.objectrewriter.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Optional;

class InputStreamMapper<T> {
    private final ObjectMapper objectMapper;
    private final TypeReference<T> typeReference;

    InputStreamMapper(ObjectMapper objectMapper, TypeReference<T> typeReference) {
        this.objectMapper = objectMapper;
        this.typeReference = typeReference;
    }

    Optional<T> mapToObject(InputStream inputStream) {
        try {
            return Optional.of(objectMapper.readValue(inputStream, typeReference));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
