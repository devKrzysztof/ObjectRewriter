package com.kw.objectrewriter.util;

import java.nio.file.Path;
import java.util.Objects;

public class FileHandler {
    private FileHandler() {
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
