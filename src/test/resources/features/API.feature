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
    
    
Scenario: Verify create new user
    Given creat new user
    When get the response
    Then the Status code Should Be 201
    
    
Scenario:  Verify if the id is generated
    Given creat new user
    When get the response
    Then Verify if the ID is generate
    
    
Scenario:  Verify the response json scheme
    Given creat new user
    When get the response
    Then Verify the response json scheme
