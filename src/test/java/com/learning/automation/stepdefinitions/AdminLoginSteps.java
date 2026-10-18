package com.learning.automation.stepdefinitions;

import com.learning.automation.driver.DriverFactory;
import com.learning.automation.pages.DashboardPage;
import com.learning.automation.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * AdminLoginSteps maps the Gherkin steps defined in admin-login-logout.feature
 * to business methods in LoginPage and DashboardPage.
 *
 * Responsibilities:
 * 1. Delegates all browser interactions to Page Object methods.
 * 2. Does NOT contain locators or raw Selenium commands (By, findElement).
 * 3. Asserts expected outcomes using TestNG Assert with meaningful failure messages.
 * 4. Never fakes success or bypasses dashboard assertions.
 */
public class AdminLoginSteps {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    public AdminLoginSteps() {
        // Initialize page objects using the active thread's WebDriver
        this.loginPage = new LoginPage(DriverFactory.getDriver());
        this.dashboardPage = new DashboardPage(DriverFactory.getDriver());
    }

    @Given("the application is opened")
    public void the_application_is_opened() {
        loginPage.openLoginPage();
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page failed to load or email input is not visible!");
    }

    @When("the admin enters the email {string}")
    public void the_admin_enters_the_email(String email) {
        loginPage.enterEmail(email);
    }

    @When("the admin clicks Continue")
    public void the_admin_clicks_continue() {
        loginPage.clickContinue();
    }

    @When("the admin enters the test OTP {string}")
    public void the_admin_enters_the_test_otp(String otp) {
        // Verify that OTP verification screen appears before entering digits
        Assert.assertTrue(loginPage.isOtpScreenDisplayed(), "OTP verification screen ('Confirm OTP') was not displayed after clicking Continue!");
        loginPage.enterOtp(otp);
    }

    @When("the admin clicks Verify OTP")
    public void the_admin_clicks_verify_otp() {
        loginPage.clickVerifyOtp();
    }

    @Then("the admin should see the dashboard")
    public void the_admin_should_see_the_dashboard() {
        boolean isVisible = dashboardPage.isDashboardDisplayed();
        Assert.assertTrue(isVisible, "Dashboard verification failed! Authentication was unsuccessful or dashboard element did not load in time.");
    }

    @When("the admin opens the profile menu")
    public void the_admin_opens_the_profile_menu() {
        dashboardPage.openProfileMenu();
    }

    @When("the admin clicks Log out")
    public void the_admin_clicks_log_out() {
        dashboardPage.clickLogout();
    }

    @Then("the admin should be redirected to the login page")
    public void the_admin_should_be_redirected_to_the_login_page() {
        boolean isAtLogin = loginPage.isLoginPageDisplayed();
        Assert.assertTrue(isAtLogin, "Redirection to login page failed after logging out!");
    }
}
