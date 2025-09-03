@Api
Feature: Create User

  Scenario Outline: Create user success 
    Given Preparing to call "Createuser" api
    When api body parameter "name" is <name>
    And api body parameter "job" is <job>
    When I call "apiendpoint" with "method"
    Then Response body should have ""
    And Response status should be <201>
   # And Response parameter "createdAt" should exists
    And Response parameter "name" should be equal to <name>
    And Response parameter "job" should be equal to <job>
    
   Examples:
||name|| job||
||"morpheus"||"leader"||
    
	
