package org.example.org.openqa.selenium.webDriver.tools;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;

import java.util.List;

public final class WebDriverTools {

    private static final By LOCATOR_ALL_HYPERLINKS = By.tagName("a");

    private  WebDriver webDriver;

    /**
     * Constructor.
     *
     * @param webDriver the {@link WebDriver}
     */
    public WebDriverTools(final WebDriver webDriver) {
       this.webDriver = webDriver;
    }

    public List<WebElement> findAllHyperlinks() {
        return webDriver.findElements(LOCATOR_ALL_HYPERLINKS);
    }

    public LogEntries retrieveBrowserLogEntries() {
        return webDriver.manage().logs().get(LogType.BROWSER);
    }
}
