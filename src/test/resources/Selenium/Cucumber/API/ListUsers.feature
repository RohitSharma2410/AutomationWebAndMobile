@Api
Feature: List User

  Feature: List user success

  Scenario: List user positive
    Given Preparing to call "listuser" api
    When I call "<apiendpoint>" with "post"
    Then Response status should be <statuscode>


    Examples:
     |statuscode|apiendpoint|
     |200|api/users?page=2|

