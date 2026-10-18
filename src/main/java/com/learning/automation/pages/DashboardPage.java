package com.learning.automation.pages;

import com.learning.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * DashboardPage represents the post-login application dashboard of Lakshya Portal.
 *
 * Page Responsibilities:
 * 1. Encapsulates locators for dashboard identification, profile/account dropdown, and Logout button.
 * 2. Provides business actions (openProfileMenu, clickLogout).
 * 3. Exposes verification methods (isDashboardDisplayed) using reliable DOM elements.
 */
public class DashboardPage extends BasePage {

    // =========================================================================
    // Verified Real DOM Locators
    // =========================================================================
    // Profile avatar button in the top navigation bar
    private final By profileAccountButton = By.cssSelector("button[aria-label='Account menu']");

    // Dashboard header branding
    private final By brandHeader = By.xpath("//*[contains(text(),'Lakshya 360')]");

    // Logout button located inside the opened profile dropdown menu
    private final By logoutButton = By.xpath("//button[normalize-space()='Log out']");

    /**
     * Constructor initializing BasePage with the active WebDriver instance.
     *
     * @param driver the active WebDriver
     */
    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Verifies dashboard visibility using URL and reliable topbar elements.
     *
     * @return true if dashboard is visible and user is authenticated
     */
    public boolean isDashboardDisplayed() {
        // Wait for dashboard URL redirection and topbar account button visibility
        boolean urlMatch = waitForUrlContains("/hr");
        boolean profileVisible = isElementDisplayed(profileAccountButton);
        return urlMatch || profileVisible;
    }

    /**
     * Clicks the Admin profile icon/button to open the dropdown menu.
     */
    public void openProfileMenu() {
        click(profileAccountButton);
    }

    /**
     * Clicks the 'Log out' button from the opened dropdown menu.
     */
    public void clickLogout() {
        click(logoutButton);
    }
}
