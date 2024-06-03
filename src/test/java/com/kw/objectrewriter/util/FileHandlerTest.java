package com.kw.objectrewriter.util;

import com.kw.objectrewriter.LogMessageVerifier;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FileHandlerTest {
    @Test
    void shouldLogIncorrectPath() {
        try (LogMessageVerifier verifier = new LogMessageVerifier(FileHandler.class)) {
            assertThatThrownBy(() -> FileHandler.validateDirectory("*"))
                    .isExactlyInstanceOf(FileIOException.class)
                    .hasMessage("There was problem with given path '*': Illegal char <*> at index 0: *");
            verifier.assertErrorMessageInLogs("There was problem with given path '*': Illegal char <*> at index 0: *");
        }
    }
}