@Api
Feature: List User

  Feature: List user success

  Scenario: List user positive
    Given Preparing to call "listuser" api
    When I call "<apiendpoint>" with "get"
    Then Response status should be <statuscode>
	And response parameter "data" size should be "equal to" 10

    Examples:
     |statuscode|apiendpoint|
     |200|api/users?page=2|

