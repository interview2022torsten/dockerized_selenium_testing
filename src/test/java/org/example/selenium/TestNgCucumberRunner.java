package org.example.selenium;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        plugin = {"pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json"
        },
        features = {"src/test/resources/features"},
        glue = {"com.example.stepdefinitions"}
)
public final class TestNgCucumberRunner extends AbstractTestNGCucumberTests {
        // NOOP (NO OPERATION)
}