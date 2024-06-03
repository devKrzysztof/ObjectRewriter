package com.kw.objectrewriter.reader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kw.objectrewriter.LogMessageVerifier;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InputStreamMapperTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldMapObject() {
        InputStreamMapper<String> inputStreamMapper = new InputStreamMapper<>(objectMapper, new TypeReference<>() {
        });
        String input = "\"test\"";

        Optional<String> result = inputStreamMapper.mapToObject(new ByteArrayInputStream(input.getBytes()));

        assertThat(result).hasValue("test");
    }

    @Test
    void shouldMapCollectionOfObjects() {
        InputStreamMapper<List<String>> inputStreamMapper = new InputStreamMapper<>(objectMapper, new TypeReference<>() {
        });
        String input = "[\"first\", \"second\"]";

        Optional<List<String>> result = inputStreamMapper.mapToObject(new ByteArrayInputStream(input.getBytes()));

        assertThat(result).hasValueSatisfying(list -> assertThat(list).contains("first", "second"));
    }

    @DataProvider
    public Object[] exceptionProvider() {
        return new Object[][]{
                {IOException.class, "There was a problem reading of object due to following error: null"},
                {JsonMappingException.class, "There was a problem with deserialization due to following error: N/A"},
                {JsonParseException.class, "There is a problem with reading object from InputStream of type java.lang.String due to following error: N/A"},
        };
    }

    @Test(dataProvider = "exceptionProvider")
    void shouldReturnEmptyOptionalOnException(Class<? extends Throwable> exception, String errorMessage) throws IOException {
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        when(mockedMapper.readValue(any(InputStream.class), any(TypeReference.class))).thenThrow(exception);
        InputStreamMapper<String> inputStreamMapper = new InputStreamMapper<>(mockedMapper, new TypeReference<>() {
        });

        try (LogMessageVerifier verifier = new LogMessageVerifier(InputStreamMapper.class)) {
            Optional<String> result = inputStreamMapper.mapToObject(new ByteArrayInputStream("".getBytes()));

            verifier.assertErrorMessageInLogs(errorMessage);
            assertThat(result).isEmpty();
        }
    }
}