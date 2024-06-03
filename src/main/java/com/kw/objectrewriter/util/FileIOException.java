package com.kw.objectrewriter.util;

public class FileIOException extends RuntimeException {

    FileIOException(String message) {
        super(message);
    }

    FileIOException(String message, Throwable cause) {
        super(message, cause);
    }

}
