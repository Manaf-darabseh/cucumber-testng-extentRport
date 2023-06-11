@UI

Feature: Login

  Scenario: Verify able to login with valid credentials
    Given Verify that Login page is displayed
    When   enter user name "standard_user" and password "secret_sauce"
    Then   Click Login button

