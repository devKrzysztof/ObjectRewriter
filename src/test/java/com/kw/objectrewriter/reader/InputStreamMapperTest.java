package com.kw.objectrewriter.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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

}