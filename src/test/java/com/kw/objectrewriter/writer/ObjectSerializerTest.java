package com.kw.objectrewriter.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kw.objectrewriter.LogMessageVerifier;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class ObjectSerializerTest {
    @Test
    void shouldCallWriteValueOnObjectMapper() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ObjectSerializer<String> objectSerializer = new ObjectSerializer<>(objectMapper);
        List<String> objects = Collections.singletonList("test");
        File file = mock(File.class);
        when(file.getAbsolutePath()).thenReturn("somePath");
        doNothing().when(objectMapper).writeValue(file, objects.getFirst());

        try (LogMessageVerifier verifier = new LogMessageVerifier(ObjectSerializer.class)) {
            objectSerializer.writeToSeparateFiles(objects, string -> file);

            verify(objectMapper, times(1)).writeValue(file, objects.getFirst());
            verifier.assertTraceMessageInLogs("Writing object test to file somePath");
        }
    }

    @Test
    void shouldLogErrorWhenIoException() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ObjectSerializer<String> objectSerializer = new ObjectSerializer<>(objectMapper);
        List<String> objects = Collections.singletonList("test");
        File file = mock(File.class);
        when(file.getAbsolutePath()).thenReturn("somePath");
        doThrow(IOException.class).when(objectMapper).writeValue(file, objects.getFirst());

        try (LogMessageVerifier verifier = new LogMessageVerifier(ObjectSerializer.class)) {
            objectSerializer.writeToSeparateFiles(objects, string -> file);

            verifier.assertTraceMessageInLogs("Writing object test to file somePath");
            verifier.assertErrorMessageInLogs("Could not write object test to file 'somePath' due to following exception: null");
        }
    }
}