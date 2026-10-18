package com.learning.automation.driver;

import com.learning.automation.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * DriverFactory is the centralized browser management engine.
 *
 * Responsibilities:
 * 1. Creates and configures WebDriver instances (Chrome, Edge, Firefox).
 * 2. Applies browser-level capabilities (headless, window size, sandbox flags).
 * 3. Utilizes ThreadLocal<WebDriver> to ensure thread safety (essential for clean parallel execution).
 * 4. Relies on Selenium Manager (built into Selenium 4.6+) to automatically resolve matching driver binaries.
 * 5. Provides safe getters and clean teardown methods.
 */
public class DriverFactory {

    // ThreadLocal ensures that each execution thread holds its own independent WebDriver instance
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    // Prevent direct instantiation (Utility / Factory pattern)
    private DriverFactory() {
    }

    /**
     * Initializes the WebDriver based on browser type and configuration.
     *
     * @return the newly created WebDriver instance
     */
    public static WebDriver initDriver() {
        String browser = ConfigReader.getBrowser();
        boolean headless = ConfigReader.isHeadless();

        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }
                edgeOptions.addArguments("--start-maximized");
                edgeOptions.addArguments("--disable-notifications");
                driver = new EdgeDriver(edgeOptions);
                break;

            case "chrome":
            default:
                // Auto-detect and bind matching ChromeDriver 155 if cached to prevent service-profile version mismatch
                if (System.getProperty("webdriver.chrome.driver") == null) {
                    java.io.File cachedDriver = new java.io.File("C:\\Users\\SIM\\.cache\\selenium\\chromedriver\\win64\\155.0.8059.84\\chromedriver.exe");
                    if (cachedDriver.exists()) {
                        System.setProperty("webdriver.chrome.driver", cachedDriver.getAbsolutePath());
                    }
                }

                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    // Modern headless flag in Chromium (matches real headed browser rendering)
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        // Configure standard timeouts
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeoutSeconds()));
        driver.manage().window().maximize();

        // Bind the driver instance to the current executing thread
        driverThreadLocal.set(driver);

        return driver;
    }

    /**
     * Retrieves the WebDriver instance associated with the current thread.
     *
     * @return the active WebDriver instance
     * @throws IllegalStateException if the driver has not been initialized yet
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized for the current thread! Call DriverFactory.initDriver() first.");
        }
        return driver;
    }

    /**
     * Safely quits the active WebDriver session and removes the ThreadLocal reference
     * to prevent memory leaks in JVM.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                // Mandatory clean-up to prevent ThreadLocal memory leaks
                driverThreadLocal.remove();
            }
        }
    }
}
