package Selenium.Cucumber;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = { "src/test/resources/Selenium/Cucumber/Mobile" }, glue = { "Selenium.Cucumber.MobileStepDefinitions" }, plugin = {
		"json:target/cucumber-reports/Cucumber.json", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
		"Listeners.ParallelEventListenerCucumber", "pretty", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" },tags="@Mobile")
public class RunCucumberTest extends AbstractTestNGCucumberTests {

	@Override
	@DataProvider(parallel = false)
	public Object[][] scenarios() {

		System.out.println(super.scenarios().length + "total ");
//
//		Arrays.sort(super.scenarios());
		return super.scenarios();
	}

}
