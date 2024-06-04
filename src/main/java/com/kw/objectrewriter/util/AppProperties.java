package com.kw.objectrewriter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Properties;

public class AppProperties {
    private static final Logger logger = LoggerFactory.getLogger(AppProperties.class);
    private final Properties properties;
    private final String propertyFileName;

    public AppProperties(String propertyFileName) {
        this.propertyFileName = propertyFileName;
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertyFileName)) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Could not load file {} due to {}", propertyFileName, e.getMessage(), e);
        }
    }

    public String getProperty(String propertyName) {
        String property = properties.getProperty(propertyName);
        if (Objects.nonNull(property)) {
            return property;
        }
        logger.error("Could not find property {} in file {}", propertyName, propertyFileName);
        throw new NoSuchElementException("Could not find property " + propertyName + " in file " + propertyFileName);
    }
}
