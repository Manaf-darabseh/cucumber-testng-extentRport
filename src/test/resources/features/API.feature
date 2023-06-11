@API
Feature: APIs

  Scenario: Verify Get APIs
    Given Get the List of Users
    When get the Status code 
    Then the Status code Should Be 200
    
 Scenario: Verify the Employee Name
    Given Get the List of Users
    When get the response
    Then the employee with ID : 10 Should be "Byron"
