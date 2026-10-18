package com.learning.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * TestRunner is the primary test execution engine bridging Cucumber with TestNG.
 *
 * Responsibilities:
 * 1. Specifies the location of Gherkin feature files (features = "...").
 * 2. Specifies the glue packages containing Step Definitions and Hooks (glue = "...").
 * 3. Filters which tags to execute (tags = "@adminLoginLogout").
 * 4. Configures reporting plugins (pretty console logging, standard HTML/JSON reports).
 * 5. Controls sequential vs parallel scenario execution via TestNG DataProvider.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.learning.automation.stepdefinitions",
                "com.learning.automation.hooks"
        },
        tags = "@adminLoginLogout",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    /**
     * Overrides TestNG DataProvider to control scenario execution mode.
     * Set parallel = false for sequential execution (ideal for learning and stability).
     * Set parallel = true for multi-threaded parallel execution across scenarios.
     */
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
