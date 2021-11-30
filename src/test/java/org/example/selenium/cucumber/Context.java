package org.example.selenium.cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URL;

public final class Context {

    private static boolean startBrowser = false;

    private WebDriver webDriver;
    private SoftAssert softAssert = new SoftAssert();

    @Before
    public void init() throws MalformedURLException {
        if (!startBrowser) {
            final URL seleniumGrid = new URL("http://localhost:4444/wd/hub");
            final ChromeOptions chromeOptions = new ChromeOptions();
            webDriver = new RemoteWebDriver(seleniumGrid, chromeOptions);

            startBrowser = false;
        }
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public SoftAssert getSoftAssert() {
        return softAssert;
    }

    @After
    public void cleanUp() {
        webDriver.close();
        webDriver.quit();
    }
}