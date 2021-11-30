package org.example.org.openqa.selenium.verifications;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.testng.asserts.SoftAssert;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class LogEntriesVerificationsTest {

    private static final List<String> ERROR_LOG_KEYWORDS = Arrays.asList(
            "warn",      // Strict mode
            "error",
            "exception",
            "fail",
            "invalid"
    );

    @Mock
    private SoftAssert mockSoftAssert;

    @Mock
    private LogEntries mockLogEntries;

    @Mock
    private LogEntry mockLogEntry;

    @Test
    void verifyNoErrorsInConsoleLogWithCollection_happyPath() {
        Mockito.doReturn(Level.ALL).when(mockLogEntry).getLevel();
        Mockito.doReturn("").when(mockLogEntry).getMessage();
        Collection<LogEntry> logEntriesAsCollection = Collections.singletonList(mockLogEntry);

        LogEntriesVerifications.verifyNoErrorsInConsoleLogWithCollection(mockSoftAssert, logEntriesAsCollection);

        Mockito.verifyNoInteractions(mockSoftAssert);
    }

    @Test
    void verifyNoErrorsInConsoleLogWithCollection_badPath_warningLogEntry() {
        Mockito.doReturn(Level.WARNING).when(mockLogEntry).getLevel();
        Collection<LogEntry> logEntriesAsCollection = Collections.singletonList(mockLogEntry);
        final SoftAssert softAssert = new SoftAssert();

        LogEntriesVerifications.verifyNoErrorsInConsoleLogWithCollection(softAssert, logEntriesAsCollection);

        Assertions.assertThrows(AssertionError.class, softAssert::assertAll);
    }

    @Test
    void verifyNoErrorsInConsoleLogWithCollection_badPath_severeLogEntry() {
        Mockito.doReturn(Level.SEVERE).when(mockLogEntry).getLevel();
        Collection<LogEntry> logEntriesAsCollection = Collections.singletonList(mockLogEntry);
        final SoftAssert softAssert = new SoftAssert();

        LogEntriesVerifications.verifyNoErrorsInConsoleLogWithCollection(softAssert, logEntriesAsCollection);

        Assertions.assertThrows(AssertionError.class, softAssert::assertAll);
    }

    @Test
    void verifyNoErrorsInConsoleLogWithCollection_badPath_errorKeyword() {
        Mockito.doReturn(Level.ALL).when(mockLogEntry).getLevel();
        Mockito.doAnswer(new SequenceAnswer<>(ERROR_LOG_KEYWORDS)).when(mockLogEntry).getMessage();
        Collection<LogEntry> logEntriesAsCollection = ERROR_LOG_KEYWORDS.stream()
                .map(item -> mockLogEntry)
                .collect(Collectors.toList());

        LogEntriesVerifications.verifyNoErrorsInConsoleLogWithCollection(mockSoftAssert, logEntriesAsCollection);


        Mockito.verify(mockSoftAssert, Mockito.times(ERROR_LOG_KEYWORDS.size())).fail(Mockito.anyString());
    }

    @Test
    void verifyNoErrorsInConsoleLog_happyPath() {
        Mockito.doReturn(Level.ALL).when(mockLogEntry).getLevel();
        Mockito.doReturn("").when(mockLogEntry).getMessage();
        Mockito.doReturn(Collections.singletonList(mockLogEntry)).when(mockLogEntries).getAll();

        LogEntriesVerifications.verifyNoErrorsInConsoleLog(mockSoftAssert, mockLogEntries);

        Mockito.verifyNoInteractions(mockSoftAssert);
    }

    @Test
    void verifyNoErrorsInConsoleLog_badPath_warningLogEntry() {
        Mockito.doReturn(Level.WARNING).when(mockLogEntry).getLevel();
        Mockito.doReturn(Collections.singletonList(mockLogEntry)).when(mockLogEntries).getAll();
        final SoftAssert softAssert = new SoftAssert();

        LogEntriesVerifications.verifyNoErrorsInConsoleLog(softAssert, mockLogEntries);

        Assertions.assertThrows(AssertionError.class, softAssert::assertAll);
    }

    @Test
    void verifyNoErrorsInConsoleLog_badPath_severeLogEntry() {
        Mockito.doReturn(Level.SEVERE).when(mockLogEntry).getLevel();
        Mockito.doReturn(Collections.singletonList(mockLogEntry)).when(mockLogEntries).getAll();
        final SoftAssert softAssert = new SoftAssert();

        LogEntriesVerifications.verifyNoErrorsInConsoleLog(softAssert, mockLogEntries);

        Assertions.assertThrows(AssertionError.class, softAssert::assertAll);
    }

    @Test
    void verifyNoErrorsInConsoleLog_badPath_errorKeyword() {
        Mockito.doReturn(Level.ALL).when(mockLogEntry).getLevel();
        Mockito.doAnswer(new SequenceAnswer<>(ERROR_LOG_KEYWORDS)).when(mockLogEntry).getMessage();
        Collection<LogEntry> logEntriesAsCollection = ERROR_LOG_KEYWORDS.stream()
                .map(item -> mockLogEntry)
                .collect(Collectors.toList());
        Mockito.doReturn(logEntriesAsCollection).when(mockLogEntries).getAll();

        LogEntriesVerifications.verifyNoErrorsInConsoleLog(mockSoftAssert, mockLogEntries);

        Mockito.verify(mockSoftAssert, Mockito.times(ERROR_LOG_KEYWORDS.size())).fail(Mockito.anyString());
    }

    private static class SequenceAnswer<T> implements Answer<T> {

        private final Iterator<T> resultIterator;

        private final T last;

        public SequenceAnswer(List<T> results) {
            this.resultIterator = results.iterator();
            this.last = results.get(results.size() - 1);
        }

        @Override
        public T answer(InvocationOnMock invocation) {
            if (resultIterator.hasNext()) {
                return resultIterator.next();
            }
            return last;
        }
    }
}