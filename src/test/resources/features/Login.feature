@Web
Feature: Login Functionality
  As a user
  I want to be able to login to the application
  So that I can access my account

  Background: 
    Given I am on the login page
    And the login form is displayed

  @Smoke @Positive
  Scenario: Successful login with valid credentials
    When I enter username "standard_user"
    And I enter password "secret_sauce"
    And I click the login button
    Then I should be logged in successfully

  @Negative
  Scenario Outline: Failed login with invalid credentials
    When I enter username "<username>"
    And I enter password "<password>"
    And I click the login button
    Then I should see the error message "<error_message>"

    Examples:
      | username      | password      | error_message                                                                    |
      | standard_user | wrong_pass    | Epic sadface: Username and password do not match any user in this service       |
      | wrong_user    | secret_sauce  | Epic sadface: Username and password do not match any user in this service       |
      | locked_user   | secret_sauce  | Epic sadface: Username and password do not match any user in this service       |
      |               | secret_sauce  | Epic sadface: Username is required                                              |
      | standard_user |               | Epic sadface: Password is required                                              |

  @Smoke @Security
  Scenario: Verify password field masks the input
    When I enter password "secret_sauce"
    Then the password should be masked

  @Performance
  Scenario: Verify login page loads within acceptable time
    Then the login page should load within 5 seconds
    And all login elements should be interactive

