package com.kw.objectrewriter.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Function;

public class ObjectSerializer<T> {
    private static final Logger logger = LoggerFactory.getLogger(ObjectSerializer.class);
    private final ObjectMapper objectMapper;

    public ObjectSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private void writeToFile(File file, T object) {
        try {
            logger.trace("Writing object {} to file {}", object, file.getAbsolutePath());
            objectMapper.writeValue(file, object);
        } catch (IOException e) {
            logger.error("Could not write object {} to file '{}' due to following exception: {}", object, file.getAbsolutePath(), e.getMessage(), e);
        }
    }

    public void writeToSeparateFiles(Collection<T> objects, Function<T, File> filePath) {
        objects.forEach(object -> writeToFile(filePath.apply(object), object));
    }
}
