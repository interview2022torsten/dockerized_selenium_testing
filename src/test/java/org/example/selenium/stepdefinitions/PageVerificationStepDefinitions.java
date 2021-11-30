package org.example.selenium.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.java.net.tools.HttpURLConnectionTools;
import org.example.java.net.verifications.ResponseCodeVerifications;
import org.example.org.openqa.selenium.verifications.LogEntriesVerifications;
import org.example.org.openqa.selenium.webDriver.tools.WebDriverTools;
import org.example.org.openqa.selenium.webelement.verifications.WebElementVerifications;
import org.example.selenium.cucumber.Context;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class PageVerificationStepDefinitions {

    Context context;
    private ResponseCodeVerifications responseCodeVerifications;
    private WebElementVerifications webElementVerifications;

    public PageVerificationStepDefinitions() {
        this.context = new Context();
        initializeDependencies();
    }

    public PageVerificationStepDefinitions(final Context context) {
        this.context = context;
        initializeDependencies();
    }

    @Given("^The browser is open$")
    public void theBrowserIsOpen() {
        // NOOP (NO OPERATION)
    }

    @When("I open to the <(.*?)>?")
    public void iOpenToThePage(final String page) {
        context.getWebDriver().navigate().to(page);
    }

    @Then("the <(.*?)> is loaded with <(.*?)>")
    public void thePageIsLoadedWithResponseCode(final String page, final int responseCode) {
        Reporter.log("Verify the response code of the page", true);
        final SoftAssert softAssert = new SoftAssert();
        responseCodeVerifications.verifyPageResponseCode(context.getSoftAssert(), page, responseCode);
    }

    @And("the <(.*?)> contains no errors in the console log")
    public void thePageContainsNoErrorsInTheConsoleLog(final String page) {
        final WebDriver webDriver = context.getWebDriver();
        final WebDriverTools webDriverTools = new WebDriverTools(webDriver);

        if (webDriver instanceof ChromeDriver) {
            Reporter.log("Verify the console log is \"error free\"", true);
            final LogEntries logEntries = webDriverTools.retrieveBrowserLogEntries();
            LogEntriesVerifications.verifyNoErrorsInConsoleLog(context.getSoftAssert(), logEntries);
        } else {
            Reporter.log("Not using Chrome; not verifying console log", true);
        }
    }

    @And("the <(.*?)>'s hyperlinks are \"live\"")
    public void thePageSHyperlinksAre(final String page) {
        final WebDriver webDriver = context.getWebDriver();
        final WebDriverTools webDriverTools = new WebDriverTools(webDriver);

        Reporter.log("Verify the response code of all page's hyperlinks", true);
        iOpenToThePage(page);
        final List<WebElement> hyperlinks = webDriverTools.findAllHyperlinks();
        webElementVerifications.verifyAllHyperlinksAreLive(context.getSoftAssert(), hyperlinks);
    }

    public void initializeDependencies() {
        final HttpURLConnectionTools httpURLConnectionTools = new HttpURLConnectionTools();

        responseCodeVerifications = new ResponseCodeVerifications(httpURLConnectionTools);
        webElementVerifications = new WebElementVerifications(httpURLConnectionTools);
    }

    @After("@CompleteSoftAssert")
    public void assertAll() {
        context.getSoftAssert().assertAll();
    }
}
