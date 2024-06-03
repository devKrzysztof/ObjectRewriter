package com.kw.objectrewriter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class FileHandler {
    private static final Logger logger = LoggerFactory.getLogger(FileHandler.class);

    private FileHandler() {
    }

    private static class ParentFileTypeHandler extends Handler {
        @Override
        void handle(Path dir) {
            if (!Files.isDirectory(dir)) {
                logger.error("File '{}' is not a directory", dir.toAbsolutePath());
                throw new FileIOException("File is not a directory '" + dir.toAbsolutePath() + "'");
            }
            handleNext(dir);
        }
    }

    private static class PathPermissionHandler extends Handler {
        @Override
        void handle(Path dir) {
            if (!Files.isWritable(dir)) {
                logger.error("Path '{}' is not writable", dir.toAbsolutePath());
                throw new FileIOException("Path '" + dir.toAbsolutePath() + "' is not writable");
            }
            handleNext(dir);
        }
    }

    private abstract static class Handler {
        private Handler next;

        abstract void handle(Path dir);

        private static Handler setHandlers(Handler first, Handler... handlers) {
            Handler head = first;
            for (Handler handler : handlers) {
                head.next = handler;
                head = handler;
            }
            return first;
        }

        void handleNext(Path dir) {
            if (Objects.nonNull(next)) {
                next.handle(dir);
            }
        }
    }
}
