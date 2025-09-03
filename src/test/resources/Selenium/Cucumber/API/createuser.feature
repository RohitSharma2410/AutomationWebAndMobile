@Api
Feature: Create User

  Feature: Create user API

  Scenario Outline: Create user success
    Given Preparing to call "Createuser" api
    When API body parameter "name" is "<name>"
    And API body parameter "job" is "<job>"
    And I call "<apiendpoint>" with "post"
    Then Response status should be <statuscode>
    And Response parameter "name" should be equal to "<name>"
    And Response parameter "job" should be equal to "<job>"
   # And Response parameter "createdAt" should exist

    Examples:
      | name      | job    |statuscode|apiendpoint|
      | morpheus  | leader |201|/api/users|

