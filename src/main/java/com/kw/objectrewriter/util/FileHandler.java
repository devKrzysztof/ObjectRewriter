package com.kw.objectrewriter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Objects;

public class FileHandler {
    private static final Logger logger = LoggerFactory.getLogger(FileHandler.class);

    private FileHandler() {
    }

    public static void validateDirectory(String path) {
        Handler.setHandlers(new ParentExistenceHandler(), new ParentFileTypeHandler(), new PathPermissionHandler())
                .handle(createPath(path));
    }

    private static Path createPath(String path) {
        try {
            return Path.of(path);
        } catch (InvalidPathException e) {
            logger.error("There was problem with given path '{}': {}", path, e.getMessage(), e);
            throw new FileIOException("There was problem with given path '" + path + "': " + e.getMessage(), e);
        }
    }

    private static class ParentExistenceHandler extends Handler {
        @Override
        void handle(Path dir) {
            if (!Files.exists(dir)) {
                try {
                    Files.createDirectories(dir);
                } catch (IOException e) {
                    logger.error("Could not create suitable folder tree for path '{}' due to following exception: {}", dir.getParent(), e.getMessage(), e);
                    throw new FileIOException("Could not create suitable folder tree for path " + dir.toAbsolutePath(), e);
                }
            }
            handleNext(dir);
        }
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
