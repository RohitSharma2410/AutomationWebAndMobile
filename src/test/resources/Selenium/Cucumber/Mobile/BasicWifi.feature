@Mobile
Feature: Login

  Scenario: Basic Mobile automation feature
    Given I am on Mobile Dashboard page for app
    When I click on mobile "preference"
    And I click on mobile "preference_dependencies"
    And I click on mobile "wificheckbox"
    And  I click on mobile "wifibutton"
    And I enter "rohit" in "wifiinputtext" mobile field
    And I click on mobile button "Ok"
    Then "wifiinputtext" should be closed
    

	