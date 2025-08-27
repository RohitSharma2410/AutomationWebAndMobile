package Selenium.Cucumber.MobileStepDefinitions;

import static utilsClasses.MobileDriverUtils.*;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Snippet {

    @Given("I am on Mobile Dashboard page for app")
    public void i_am_on_mobile_dashboard_page_for_app() {
        // Optionally check if you are on the expected screen
        System.out.println("[INFO] Mobile app dashboard assumed ready.");
    }

    @When("I enter {string} in {string} mobile field")
    public void i_enter_in(String text, String locatorName) {
        try {
            typeIntoElement(locatorName, text);
            System.out.println("[INFO] Typed '" + text + "' into field: " + locatorName);
        } catch (Exception e) {
            System.err.println("[ERROR] Could not type into field: " + locatorName);
            e.printStackTrace();
            Assert.fail("Failed to type into field: " + locatorName);
        }
    }

    @When("I click on mobile button {string}")
    public void i_click_on_button(String locatorName) {
        try {
            clickElement(locatorName);
            System.out.println("[INFO] Clicked on button: " + locatorName);
        } catch (Exception e) {
            System.err.println("[ERROR] Could not click on button: " + locatorName);
            e.printStackTrace();
            Assert.fail("Failed to click on button: " + locatorName);
        }
    }

    @Then("{string} should be closed")
    public void should_be_closed(String locatorName) {
        try {
            List<WebElement> elements = getMobileElements(locatorName);
            Assert.assertTrue(elements.size()>1, "Element '" + locatorName + "' is still visible.");
            System.out.println("[INFO] Element " + locatorName + " is closed.");
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to verify if element is closed: " + locatorName);
            e.printStackTrace();
            Assert.fail("Verification failed for closed element: " + locatorName);
        }
    }

    @When("I click on mobile {string}")
    public void i_click_on(String locatorName) {
        try {
            WebElement element = getMobileElement(locatorName);
            Assert.assertNotNull(element, "Element not found: " + locatorName);
            element.click();
            System.out.println("[INFO] Clicked on: " + locatorName);
        } catch (Exception e) {
            System.err.println("[ERROR] Could not click on element: " + locatorName);
            e.printStackTrace();
            Assert.fail("Failed to click on element: " + locatorName);
        }
    }
}
