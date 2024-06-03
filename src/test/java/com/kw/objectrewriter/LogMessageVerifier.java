package com.kw.objectrewriter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class LogMessageVerifier implements AutoCloseable {

    private final Logger logger;
    private final ListAppender<ILoggingEvent> appender;


    public LogMessageVerifier(Class<?> clazz) {
        logger = (Logger) LoggerFactory.getLogger(clazz);
        appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
    }

    public void assertTraceMessageInLogs(String message) {
        hasMessageWithLevel(message, Level.TRACE);
    }

    public void assertErrorMessageInLogs(String message) {
        hasMessageWithLevel(message, Level.ERROR);
    }

    private void hasMessageWithLevel(String message, Level level) {
        assertThat(appender.list)
                .filteredOn(c -> c.getLevel().equals(level))
                .map(ILoggingEvent::getFormattedMessage)
                .contains(message);
    }

    @Override
    public void close() {
        logger.detachAppender(appender);
    }
}
