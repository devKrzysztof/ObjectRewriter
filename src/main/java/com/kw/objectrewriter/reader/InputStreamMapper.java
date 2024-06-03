package com.kw.objectrewriter.reader;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

class InputStreamMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(InputStreamMapper.class);
    private final ObjectMapper objectMapper;
    private final TypeReference<T> typeReference;

    InputStreamMapper(ObjectMapper objectMapper, TypeReference<T> typeReference) {
        this.objectMapper = objectMapper;
        this.typeReference = typeReference;
    }

    Optional<T> mapToObject(InputStream inputStream) {
        try {
            return Optional.of(objectMapper.readValue(inputStream, typeReference));
        } catch (StreamReadException e) {
            logger.error("There is a problem with reading object from InputStream of type {} due to following error: {}",
                    typeReference.getType().getTypeName(), e.getMessage(), e);
        } catch (DatabindException e) {
            logger.error("There was a problem with deserialization due to following error: {}", e.getMessage(), e);
        } catch (IOException e) {
            logger.error("There was a problem reading of object due to following error: {}", e.getMessage(), e);
        }
        return Optional.empty();
    }
}
