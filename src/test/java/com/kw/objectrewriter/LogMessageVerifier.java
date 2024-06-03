package com.kw.objectrewriter;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

public class LogMessageVerifier implements AutoCloseable {

    private final Logger logger;
    private final ListAppender<ILoggingEvent> appender;


    public LogMessageVerifier(Class<?> clazz) {
        logger = (Logger) LoggerFactory.getLogger(clazz);
        appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
    }

    @Override
    public void close() {
        logger.detachAppender(appender);
    }
}
