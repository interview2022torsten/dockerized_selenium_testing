package org.example.org.openqa.selenium.verifications;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LogEntriesVerifications {

    private static final Logger LOGGER = Logger.getLogger(LogEntriesVerifications.class.getName());

    private static final Collection<String> ERROR_LOG_KEYWORDS = Arrays.asList(
            "warn",      // Strict mode
            "error",
            "exception",
            "fail",
            "invalid"
    );

    public static void verifyNoErrorsInConsoleLog(SoftAssert softAssert, final LogEntries logEntries) {
        final Collection<LogEntry> logEntriesAsCollection = logEntries.getAll();
        verifyNoErrorsInConsoleLogWithCollection(softAssert, logEntriesAsCollection);
    }

    public static void verifyNoErrorsInConsoleLogWithCollection(SoftAssert softAssert, final Collection<LogEntry> logEntries) {
        for (LogEntry logEntry : logEntries) {
            final boolean isErrorLogEntry = isErrorLogEntry(logEntry);

            if (isErrorLogEntry) {
                final String logEntryAsString = logEntry.toString();
                LOGGER.info(logEntryAsString);
                final String errorMessage = String.format("Found error in console: <%s>", logEntryAsString);
                softAssert.fail(errorMessage);
            }
        }
    }

    private static boolean isErrorLogEntry(final LogEntry logEntry) {
        // Strict mode!
        if (logEntry.getLevel() == Level.WARNING) {
            return true;
        }

        if (logEntry.getLevel() == Level.SEVERE) {
            return true;
        }

        final String messageLowerCased = logEntry.getMessage().toLowerCase();
        return ERROR_LOG_KEYWORDS.stream()
                .anyMatch(messageLowerCased::contains);
    }
}
