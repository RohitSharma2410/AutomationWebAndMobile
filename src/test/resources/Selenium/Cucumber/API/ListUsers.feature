@Api
Feature: List User

  Feature: List user success

  Scenario: List user positive
    Given Preparing to call "listuser" api
    When I call "<apiendpoint>" with "post"
    Then Response status should be <statuscode>
    And Response parameter "name" should be equal to "<name>"
    And Response parameter "job" should be equal to "<job>"
   # And Response parameter "createdAt" should exist

    Examples:
     |statuscode|apiendpoint|
     |200|api/users?page=2|

