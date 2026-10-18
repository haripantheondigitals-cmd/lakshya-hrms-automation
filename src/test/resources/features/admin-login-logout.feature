@adminLoginLogout
Feature: Admin Login and Logout
  As an administrator of the Lakshya HRMS Portal
  I want to securely log in using my email and OTP verification
  And access the administration dashboard
  So that I can manage compliance and safely log out after my session

  @smoke @regression
  Scenario: Admin logs in, verifies the dashboard, and logs out
    Given the application is opened
    When the admin enters the email "admin@britannia.com"
    And the admin clicks Continue
    And the admin enters the test OTP "999999"
    And the admin clicks Verify OTP
    Then the admin should see the dashboard
    When the admin opens the profile menu
    And the admin clicks Log out
    Then the admin should be redirected to the login page
