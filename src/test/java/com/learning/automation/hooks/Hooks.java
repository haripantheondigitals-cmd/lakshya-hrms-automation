package com.learning.automation.hooks;

import com.learning.automation.driver.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Hooks class manages the lifecycle of each Cucumber scenario.
 *
 * Responsibilities:
 * 1. @Before: Initializes the browser session via DriverFactory prior to any scenario step execution.
 * 2. @After: Captures a full-page screenshot and attaches it to the report if the scenario fails.
 * 3. @After: Teardown - Closes the browser session cleanly and purges ThreadLocal memory references.
 */
public class Hooks {

    /**
     * Executes BEFORE each scenario.
     * Order 0 ensures this hook runs before any higher-ordered setup hooks.
     *
     * @param scenario the active Cucumber scenario metadata
     */
    @Before(order = 0)
    public void setUp(Scenario scenario) {
        System.out.println("----------------------------------------------------------------------");
        System.out.println(">>> STARTING SCENARIO: " + scenario.getName());
        System.out.println("----------------------------------------------------------------------");

        // Instantiate the WebDriver instance for the current executing thread
        DriverFactory.initDriver();
    }

    /**
     * Executes AFTER each scenario.
     * In Cucumber, @After hooks execute in REVERSE numerical order (order 0 runs LAST).
     * This guarantees that failure screenshots are captured BEFORE the browser is closed.
     *
     * @param scenario the finished Cucumber scenario metadata
     */
    @After(order = 0)
    public void tearDown(Scenario scenario) {
        WebDriver driver = null;
        try {
            driver = DriverFactory.getDriver();
        } catch (Exception e) {
            System.err.println("WebDriver was not accessible during teardown: " + e.getMessage());
        }

        // Capture screenshot if the scenario encountered a failure
        if (scenario.isFailed() && driver != null) {
            try {
                byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshotBytes, "image/png", "FailureScreenshot_" + scenario.getName().replaceAll("[^a-zA-Z0-9_-]", "_"));
                System.out.println(">>> Screenshot captured and attached to Cucumber report for failed scenario: " + scenario.getName());
            } catch (Exception e) {
                System.err.println("Failed to capture screenshot on scenario failure: " + e.getMessage());
            }
        }

        System.out.println("----------------------------------------------------------------------");
        System.out.println(">>> FINISHED SCENARIO: " + scenario.getName() + " [STATUS: " + scenario.getStatus() + "]");
        System.out.println("----------------------------------------------------------------------");

        // Clean teardown: close browser window and remove ThreadLocal driver reference
        DriverFactory.quitDriver();
    }
}
