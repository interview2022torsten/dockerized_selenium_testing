package org.example.org.openqa.selenium.webDriver.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.Logs;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class WebDriverToolsTest {

    private WebDriverTools classUnderTest;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebDriver mockWebDriver;
    @Mock
    private WebElement mockWebElement;
    @Mock
    private LogEntries mockLogEntries;

    @Test
    public void findAllHyperlinks_happyPath() {
        final Collection<WebElement> expectedHyperlinks = Collections.singletonList(mockWebElement);
        Mockito.doReturn(expectedHyperlinks).when(mockWebDriver).findElements(Mockito.any(By.class));

        List<WebElement> hyperlinks = classUnderTest.findAllHyperlinks();

        Assertions.assertEquals(expectedHyperlinks, hyperlinks);
    }

    @Test
    public void retrieveBrowserLogEntries_happyPath() {
        final Logs logs = mockWebDriver.manage().logs();
        Mockito.doReturn(mockLogEntries).when(logs).get(Mockito.anyString());

        LogEntries logEntries = classUnderTest.retrieveBrowserLogEntries();

        Assertions.assertEquals(mockLogEntries, logEntries);
    }

    @BeforeEach
    public void initializeClassUnderTest() {
        classUnderTest = new WebDriverTools(mockWebDriver);
    }
}