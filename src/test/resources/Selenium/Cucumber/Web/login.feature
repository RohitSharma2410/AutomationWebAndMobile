@Web
@Login
Feature: Login

  Scenario Outline: Login with <username> and <password>
    Given I am on Login page
    When I enter <username> in "loginusername" web field
    When data is like
    |name|password|extra|
    |5.8|5|"ExtraData"|
    |"Rohit"|"rohit"|"ExtraOnly"|
    And I enter <password> in "loginpassword" web field
    And I click on web "loginsigninbutton"
    Then User login should see "dashboardsearchfield"
    
	Examples:
		||username||password||
		||"rohit"||"rohit"||
	
