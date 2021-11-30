package org.example.selenium;

import org.example.java.net.tools.HttpURLConnectionTools;
import org.example.java.net.verifications.ResponseCodeVerifications;
import org.example.org.openqa.selenium.verifications.LogEntriesVerifications;
import org.example.org.openqa.selenium.webDriver.tools.WebDriverTools;
import org.example.org.openqa.selenium.webelement.verifications.WebElementVerifications;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PageVerificationITCase {

    private ResponseCodeVerifications responseCodeVerifications;
    private WebElementVerifications webElementVerifications;

    private WebDriver webDriver;

    @DataProvider(name = "Page and Response Code Data Provider")
    public static Object[][] pageAndResponseCodeDataProvider() {

        return new Object[][]{
                {"https://www.w3.org/standards/badpage", HttpURLConnection.HTTP_NOT_FOUND},
                {"https://www.w3.org/standards/webofdevices/multimodal", HttpURLConnection.HTTP_OK},
                {"https://www.w3.org/standards/webdesign/htmlcss", HttpURLConnection.HTTP_OK}
        };
    }

    @BeforeTest
    @Parameters("browser")
    public void setup(final String browser) throws MalformedURLException {
        final String browserLowerCased = browser.toLowerCase();

        final URL seleniumGrid = new URL("http://localhost:4444/wd/hub");

        switch (browserLowerCased) {
            case "chrome" -> {
                final ChromeOptions chromeOptions = new ChromeOptions();
                webDriver = new RemoteWebDriver(seleniumGrid, chromeOptions);
            }
            case "firefox" -> {
                final FirefoxOptions firefoxOptions = new FirefoxOptions();
                webDriver = new RemoteWebDriver(seleniumGrid, firefoxOptions);
            }
            default -> throw new IllegalArgumentException("No such browser available!");
        }

        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().pageLoadTimeout(Duration.of(1, ChronoUnit.SECONDS));
    }

    @Test(dataProvider = "Page and Response Code Data Provider")
    public void pageValidation(final String url, final int expectedResponseCode) {
        final SoftAssert softAssert = new SoftAssert();
        final WebDriverTools webDriverTools = new WebDriverTools(webDriver);

        Reporter.log("Verify the response code of the page", true);
        responseCodeVerifications.verifyPageResponseCode(softAssert, url, expectedResponseCode);

        if (webDriver instanceof ChromeDriver) {
            Reporter.log("Verify the console log is \"error free\"", true);
            final LogEntries logEntries = webDriverTools.retrieveBrowserLogEntries();
            LogEntriesVerifications.verifyNoErrorsInConsoleLog(softAssert, logEntries);
        } else {
            Reporter.log("Not using Chrome; not verifying console log", true);
        }

        Reporter.log("Verify the response code of all page's hyperlinks", true);
        webDriver.navigate().to(url);
        final List<WebElement> hyperlinks = webDriverTools.findAllHyperlinks();
        webElementVerifications.verifyAllHyperlinksAreLive(softAssert, hyperlinks);

        // Assert all verifications!
        softAssert.assertAll();
    }

    @BeforeTest
    public void initializeDependencies(){
        final HttpURLConnectionTools httpURLConnectionTools = new HttpURLConnectionTools();

        responseCodeVerifications = new ResponseCodeVerifications(httpURLConnectionTools);
        webElementVerifications = new WebElementVerifications(httpURLConnectionTools);
    }

    @AfterTest
    public void cleanUp() {
        webDriver.quit();
    }
}