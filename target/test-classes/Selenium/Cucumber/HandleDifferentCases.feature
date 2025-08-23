#@Hookup
#Feature: All Possible Situations
#
  #Scenario: Validate ABTesting page
    #Given I am on Dashboard page
    #When I click on "ABTesting"
    #Then following "ABTestingpage" should be available on page
   #
    #
     #Scenario: Validate AddRemove elements page
    #Given I am on Dashboard page
    #When I click on "AddRemoveElements"
    #When I click on "AddElementButton" 
    #Then following "DeleteElement" should be available on page
    #
        #
     #Scenario: Validate basicAuth page
    #Given I am on Dashboard page
    #When I autheticate user with credentials
    #And I click on "BasicAuth"
    #Then following "BasicAuthSuceessmessage" should be available on page
    #
     #Scenario: Validate BrokenImages page
    #Given I am on Dashboard page
    #When I click on "BrokenImages"
    #Then Validate all of response for "AllImageesLink" should be {200}
   #
    #
     #Scenario: Validate ChallengingDOM elements page
    #Given I am on Dashboard page
    #When I click on "ChallengingDOM"
    #Then following "firstbuttonCDOM" should be available on page
    #Then following "secondbuttonCDOM" should be available on page
    #Then following "thridbuttonCDOM" should be available on page
        #
     #Scenario: Validate ContextMenu elements page
    #Given I am on Dashboard page
    #When I click on "ContextMenu"
    #When I right click on "contextmenuelement" 
    #Then alert should be available on the page
    #
         #
     #Scenario: Validate DragDrop page
    #Given I am on Dashboard page
    #When I click on "DragAndDrop"
    #When I dragAndDrop element "DragSource" to element "DragTarget"
    #Then element "DragSource" should be draged to target successfully
    #
    #
   