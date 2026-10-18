package com.learning.automation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader is responsible for loading and providing access to framework
 * configuration properties defined in config.properties.
 *
 * It gives highest priority to Command Line (CLI) system properties passed via
 * Maven (-Dbrowser=..., -Dheadless=...) so CI/CD pipelines can override settings dynamically.
 */
public class ConfigReader {

    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE_PATH = "config.properties";

    static {
        loadProperties();
    }

    /**
     * Loads the properties file from the test classpath.
     */
    private static void loadProperties() {
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH)) {
            if (inputStream == null) {
                throw new RuntimeException("CRITICAL: Configuration file '" + CONFIG_FILE_PATH + "' not found in classpath (src/test/resources/).");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("CRITICAL: Failed to read '" + CONFIG_FILE_PATH + "': " + e.getMessage(), e);
        }
    }

    /**
     * Reads a property value by key.
     * System properties (CLI arguments) take precedence over properties file values.
     *
     * @param key the property key
     * @return the resolved property value, or null if not found
     */
    public static String getProperty(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            return systemValue.trim();
        }
        String fileValue = properties.getProperty(key);
        return (fileValue != null) ? fileValue.trim() : null;
    }

    /**
     * Returns the base application URL under test.
     */
    public static String getAppUrl() {
        String url = getProperty("app.url");
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalStateException("Property 'app.url' is missing in config.properties!");
        }
        return url;
    }

    /**
     * Returns the target browser name (e.g. chrome, edge, firefox).
     * Defaults to 'chrome' if unspecified.
     */
    public static String getBrowser() {
        String browser = getProperty("browser");
        return (browser != null && !browser.isEmpty()) ? browser.toLowerCase() : "chrome";
    }

    /**
     * Returns true if tests should run in headless mode (without opening a visible browser window).
     */
    public static boolean isHeadless() {
        String headless = getProperty("headless");
        return Boolean.parseBoolean(headless);
    }

    /**
     * Returns the explicit wait timeout in seconds.
     * Defaults to 15 seconds if unconfigured.
     */
    public static int getExplicitWaitSeconds() {
        String timeout = getProperty("explicit.wait.seconds");
        try {
            return (timeout != null) ? Integer.parseInt(timeout) : 15;
        } catch (NumberFormatException e) {
            return 15;
        }
    }

    /**
     * Returns the page load timeout in seconds.
     * Defaults to 30 seconds if unconfigured.
     */
    public static int getPageLoadTimeoutSeconds() {
        String timeout = getProperty("page.load.timeout.seconds");
        try {
            return (timeout != null) ? Integer.parseInt(timeout) : 30;
        } catch (NumberFormatException e) {
            return 30;
        }
    }
}
