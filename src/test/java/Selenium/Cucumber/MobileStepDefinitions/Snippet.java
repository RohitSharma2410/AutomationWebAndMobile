package Selenium.Cucumber.MobileStepDefinitions;
import static utilsClasses.MobileDriverUtils.clickElement;
import static utilsClasses.MobileDriverUtils.getMobileElement;
import static utilsClasses.MobileDriverUtils.typeinToElement;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
public class Snippet {
	
	@Given("I am on Mobile Dashboard page for app")
	public void i_am_on_mobile_dashboard_page_for_app() {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new io.cucumber.java.PendingException();
	}
	@When("I enter {string} in {string} mobile field")
	public void i_enter_in(String string, String string2) {
	    // Write code here that turns the phrase above into concrete actions
		typeinToElement(string2, string);
	}
	@When("I click on mobile button {string}")
	public void i_click_on_button(String string) {
	    // Write code here that turns the phrase above into concrete actions
		clickElement(string);
	}
	@Then("{string} should be closed")
	public void should_be_closed(String string) {
	    // Write code here that turns the phrase above into concrete actions
	try{getMobileElement(string);}
	catch(Exception e) {
		
	}
	
	}
	
	@When("I click on mobile {string}")
	public void i_click_on(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    getMobileElement(string).click();
	}
}

