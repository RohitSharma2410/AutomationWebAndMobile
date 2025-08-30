@Web
@Dashboard
@Smoke
Feature: Dashboard

  Scenario Outline: Verify Dashboard page
   Given I am on Login page
    When I enter <username> in "loginusername" web field
    And I enter <password> in "loginpassword" web field
    And I click on web "loginsigninbutton"
    Then I check transaction <transactionname> on <field> then status should be <status> and amount should be <Amount>
 
	Examples:
		||username||password||transactionname||status||field||Amount||
		||"rohit"||"rohit"||"Star"||"Complete"||"transactionrowbasedonTransactionName"||1250||
		||"deepak"||"deepak"||"Star"||"Complete"||"transactionrowbasedonTransactionName"||1250||



