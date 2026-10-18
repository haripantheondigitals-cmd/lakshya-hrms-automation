package com.learning.automation.pages;

import com.learning.automation.base.BasePage;
import com.learning.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * LoginPage represents the authentication screen of Lakshya Portal (https://lakshya.zfourhr.in).
 *
 * Page Responsibilities:
 * 1. Encapsulates locators for email input, Continue button, OTP digit boxes, and Verify OTP button.
 * 2. Provides business actions (enterEmail, clickContinue, enterOtp, clickVerifyOtp).
 * 3. Handles multi-box OTP entry (distributing each digit across 6 input fields).
 * 4. Extends BasePage to leverage robust explicit waits without Thread.sleep().
 */
public class LoginPage extends BasePage {

    // =========================================================================
    // Verified Real DOM Locators
    // =========================================================================
    private final By emailInput = By.id("login-email");
    private final By continueButton = By.xpath("//button[normalize-space()='Continue']");
    private final By otpScreenHeading = By.xpath("//*[contains(text(),'Confirm OTP')]");
    private final By otpInputBoxes = By.cssSelector("input[class*='text-center']");
    private final By verifyOtpButton = By.xpath("//button[normalize-space()='Verify OTP']");

    /**
     * Constructor initializing BasePage with the active WebDriver instance.
     *
     * @param driver the active WebDriver
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates to the base application login URL.
     */
    public void openLoginPage() {
        String url = ConfigReader.getAppUrl();
        driver.get(url);
        waitForVisibility(emailInput);
    }

    /**
     * Enters the email address into the email input field.
     *
     * @param email user email address
     */
    public void enterEmail(String email) {
        clearAndType(emailInput, email);
    }

    /**
     * Clicks the Continue button to proceed to the OTP screen.
     */
    public void clickContinue() {
        click(continueButton);
    }

    /**
     * Checks if the OTP verification screen ('Confirm OTP') is displayed.
     *
     * @return true if OTP screen heading appears within explicit wait
     */
    public boolean isOtpScreenDisplayed() {
        return isElementDisplayed(otpScreenHeading);
    }

    /**
     * Enters a 6-digit OTP across the 6 separate input boxes rendered by the UI.
     *
     * @param otp 6-digit OTP code (e.g. "999999")
     */
    public void enterOtp(String otp) {
        if (otp == null || otp.length() != 6) {
            throw new IllegalArgumentException("Expected a 6-digit OTP, but received: " + otp);
        }

        // Wait until all 6 OTP inputs are present on the DOM
        List<WebElement> inputs = findElements(otpInputBoxes);
        if (inputs.size() < 6) {
            throw new IllegalStateException("Expected at least 6 OTP input boxes, but found only " + inputs.size());
        }

        // Distribute each digit into its corresponding input box
        for (int i = 0; i < 6; i++) {
            WebElement box = inputs.get(i);
            box.clear();
            box.sendKeys(String.valueOf(otp.charAt(i)));
        }
    }

    /**
     * Clicks the 'Verify OTP' button to submit authentication.
     */
    public void clickVerifyOtp() {
        click(verifyOtpButton);
    }

    /**
     * Verifies that the browser is currently on the login page (used after logout).
     *
     * @return true if the email input field is visible
     */
    public boolean isLoginPageDisplayed() {
        return isElementDisplayed(emailInput);
    }
}
