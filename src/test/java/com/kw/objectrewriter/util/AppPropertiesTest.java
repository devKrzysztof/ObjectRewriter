package com.kw.objectrewriter.util;

import com.kw.objectrewriter.LogMessageVerifier;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AppPropertiesTest {
    @Test
    void shouldGetProvidedProperty() {
        AppProperties appProperties = new AppProperties("test.properties");

        String result = appProperties.getProperty("testProperty");

        assertThat(result).isEqualTo("sampleProperty");
    }

    @Test
    void shouldThrowExceptionWhenPropertyNotFound() {
        String propertyFileName = "test.properties";
        AppProperties appProperties = new AppProperties(propertyFileName);
        String property = "nonexistentProperty";

        try (LogMessageVerifier verifier = new LogMessageVerifier(AppProperties.class)) {
            assertThatThrownBy(() -> appProperties.getProperty(property))
                    .isExactlyInstanceOf(NoSuchElementException.class)
                    .hasMessage("Could not find property nonexistentProperty in file test.properties");
            verifier.assertErrorMessageInLogs("Could not find property " + property + " in file " + propertyFileName);
        }
    }
}