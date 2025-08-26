package Selenium.Cucumber;

import static Listeners.ParallelEventListenerCucumber.config;
import static Listeners.ParallelEventListenerCucumber.drivers;
import static Listeners.ParallelEventListenerCucumber.webdriverUtils;
import static utilsClasses.WebDriverUtils.getWebElement;
import static utilsClasses.WebDriverUtils.getWebElementOnElement;
import static utilsClasses.WebDriverUtils.getWebElementWithUpdatedValue;
import static utilsClasses.WebDriverUtils.getWebElements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.HasAuthentication;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import Listeners.ParallelEventListenerCucumber;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utilsClasses.StringUtilsFunctions;
import utilsClasses.WebDriverUtils;

public class StepDefinitions{
	

	

	
	@When("data is like")
	public void data_is_like(DataTable data) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		
		
//		List<pageObjects.Data> dataList=data.asList(pageObjects.Data.class);
//		dataList.forEach(s-> System.out.println(s.getName()));
	
}


	
	 @DataTableType
	    public pageObjects.Data authorEntry(Map<String, String> entry) {
		 pageObjects.Data data=new pageObjects.Data();
		 data.setName(entry.get("name"));
		 data.setPassword(entry.get("password"));
	        return data;
	    }

	

@When("I autheticate user with credentials")
public void i_autheticate_user_with_credentials() {
    // Write code here that turns the phrase above into concrete actions
	
	 HasAuthentication authentication=(HasAuthentication)drivers.get();
	 authentication.register(()->new UsernameAndPassword("admin","admin"));
	 
    
}
@Then("following {string} should be available on page")
public void following_should_be_available_on_page(String string) {
   
	// Write code here that turns the phrase above into concrete actions
//	 scenarios.get().log("Assert "+string+ "is visible on page");
	Assert.assertTrue(getWebElement(string).isDisplayed());
   
}

	@Given("an example scenario")
	public void anExampleScenario() {
	}

	@When("all step definitions are implemented")
	public void allStepDefinitionsAreImplemented() {
	}

	@Then("the scenario passes")
	public void theScenarioPasses() {
	}

	@Given("I am on Login page")
	public void i_am_on_login_page() {

	}

	@Then("Validate all of response for {string} should be \\{{int}}")
public void validate_all_of(String string,Integer int1) throws MalformedURLException, IOException {
	webdriverUtils.get();
	List<WebElement>allElements=getWebElements(string);
	for(int i=0;i<allElements.size();i++) {
		String src=allElements.get(i)
				.getDomAttribute("src");
		System.out.println("src is "+src);
		HttpURLConnection conn =(HttpURLConnection) URI.create(config.
				getProperty("appurl").toString().concat(src)).toURL().openConnection() ;
        conn.setRequestMethod("HEAD");
        conn.connect();
       Assert.assertTrue(conn.getResponseCode()==int1.intValue());
}
}
@Then("all links should populate all of link status")
public void all_links_should_populate_all_of_link_status() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
}

@When("I dragAndDrop element {string} to element {string}")
public void dragAndDrop(String string,String string2) {
    // Write code here that turns the phrase above into concrete actions

   Actions actions=new Actions(drivers.get());
   ParallelEventListenerCucumber.webdriverUtils.get();
ParallelEventListenerCucumber.webdriverUtils.get();
actions
   .dragAndDrop(WebDriverUtils.getWebElement(string),  getWebElement(string2)).build().perform();
}

@Then("element {string} should be draged to target successfully")
public void dragSuccess(String string) {
ParallelEventListenerCucumber.webdriverUtils.get();
	ParallelEventListenerCucumber.webdriverUtils.get();
	//	 scenarios.get().log("Asserting step");
	Assert.assertTrue( getWebElements("DragElements").get(1).getLocation().getY()== getWebElement(string).getLocation().getY());
	
}
@When("I right click on {string}")
public void i_right_click_on(String string) {
    // Write code here that turns the phrase above into concrete actions

   Actions actions=new Actions( drivers.get());
   ParallelEventListenerCucumber.webdriverUtils.get();
actions
   .contextClick( getWebElement(string)).build().perform();
}
@Then("alert should be available on the page")
public void alert_should_be_available_on_the_page() {
    // Write code here that turns the phrase above into concrete actions
    try{ drivers.get().switchTo().alert().dismiss();
    }
    catch(NoAlertPresentException e) {
//		 scenarios.get().log("Assertion failed");

    	new SoftAssert().fail("Alert is not pop up");
    }
}


	@When("I click on web {string}")
	public void i_click_on(String string) {
		// Write code here that turns the phrase above into concrete actions
		getWebElement(string).click();

	}

	@When("I enter {string} in {string} web field")
	public void i_enter_in_field(String string, String string2) {
		 ParallelEventListenerCucumber.webdriverUtils.get();
		getWebElement(string2).sendKeys(string);

	}

	@Then("User login should see {string}")
	public void user_login_should(String string) {
		Assert.assertTrue( getWebElement("dashboardsearchfield").isDisplayed());

	}

	@Given("I am on Dashboard page")
	public void i_am_on_dashboard_page() {
		// Write code here that turns the phrase above into concrete actions

	}

	@When("I check transaction {string} on {string}")
	public void i_check_transaction_on(String string, String string2) {
		// Write code here that turns the phrase above into concrete actions
		 getWebElementOnElement("allTransactionsStatus",
				 getWebElementWithUpdatedValue(string, "transaction", "string2"));
	}

	@Then("I check transaction {string} on {string} then status should be {string} and amount should be {int}")
	public void i_check_transaction_on_then_status_should_be_and_amount_should_be(String string, String string2,
			String string3, Integer int1) throws Exception {
		{
			// Write code here that turns the phrase above into concrete actions
			String valueOfStatus =  getWebElementOnElement("allTransactionsStatus",
					 getWebElementWithUpdatedValue(string2, "transaction", string)).getText();
			System.out.println(valueOfStatus);
//			 scenarios.get().log(assertionCheckValue(valueOfStatus,string3));

			Assert.assertTrue (valueOfStatus.equalsIgnoreCase(string3));
		
			int amountvalue = StringUtilsFunctions
					.returnOnlyNumeric( getWebElementOnElement("alltransactionsamount",
							 getWebElementWithUpdatedValue(string2, "transaction", string)).getText());
//			 scenarios.get().log(assertionCheckValue(amountvalue,int1));
			Assert.assertTrue (amountvalue == int1);
			System.out.println(ParallelEventListenerCucumber.drivers.get().getCurrentUrl());
		}

	}
	
	
	private String assertionCheckValue(Object actual,Object expected) {
		return "Asserting actual"+actual+ " against expected"+expected;
	}

}
