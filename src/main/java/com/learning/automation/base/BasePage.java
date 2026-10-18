package com.learning.automation.base;

import com.learning.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * BasePage is the foundation for all Page Object classes.
 *
 * Responsibilities:
 * 1. Encapsulates the WebDriver and WebDriverWait instances.
 * 2. Provides robust, explicit-wait interaction wrappers (click, type, visibility, text extraction).
 * 3. Prevents flaky tests by dynamically polling the DOM instead of using hardcoded Thread.sleep().
 * 4. Strictly contains generic browser interaction logic; application-specific locators MUST NOT exist here.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Initializes the BasePage with the active WebDriver and configures
     * explicit wait using timeout from config.properties.
     *
     * @param driver the active WebDriver instance
     */
    public BasePage(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver instance cannot be null when initializing BasePage!");
        }
        this.driver = driver;
        int waitTimeout = ConfigReader.getExplicitWaitSeconds();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitTimeout));
    }

    /**
     * Waits explicitly until the element located by locator is visible on the DOM.
     *
     * @param locator the element locator
     * @return the visible WebElement
     */
    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits explicitly until the element located by locator is visible and enabled for clicking.
     *
     * @param locator the element locator
     * @return the clickable WebElement
     */
    public WebElement waitForClickability(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Clicks an element after ensuring it is clickable.
     *
     * @param locator the element locator
     */
    public void click(By locator) {
        WebElement element = waitForClickability(locator);
        element.click();
    }

    /**
     * Sends keys to an input element after ensuring it is visible.
     *
     * @param locator the element locator
     * @param text the text to send
     */
    public void type(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.sendKeys(text);
    }

    /**
     * Clears existing content in an input and sends the specified text.
     *
     * @param locator the element locator
     * @param text the text to send
     */
    public void clearAndType(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Retrieves visible text from an element after ensuring visibility.
     *
     * @param locator the element locator
     * @return the trimmed text content
     */
    public String getText(By locator) {
        WebElement element = waitForVisibility(locator);
        return element.getText().trim();
    }

    /**
     * Checks if an element is currently displayed within the default explicit wait timeout.
     *
     * @param locator the element locator
     * @return true if visible, false if not found or timed out
     */
    public boolean isElementDisplayed(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Checks if an element is displayed using a custom timeout (useful for fast negative checks).
     *
     * @param locator the element locator
     * @param timeoutInSeconds custom timeout in seconds
     * @return true if visible within the timeout, false otherwise
     */
    public boolean isElementDisplayed(By locator, int timeoutInSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            return customWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Finds and returns a list of elements matching the locator after waiting for visibility of at least one.
     *
     * @param locator the element locator
     * @return the list of WebElements
     */
    public List<WebElement> findElements(By locator) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        return driver.findElements(locator);
    }

    /**
     * Waits until the current URL contains the expected fraction string.
     *
     * @param urlFraction the partial URL string to expect
     * @return true if URL contains the fraction within the wait timeout
     */
    public boolean waitForUrlContains(String urlFraction) {
        return wait.until(ExpectedConditions.urlContains(urlFraction));
    }

    /**
     * Returns the current page URL.
     *
     * @return the active URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Returns the active browser title.
     *
     * @return the current page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
}
