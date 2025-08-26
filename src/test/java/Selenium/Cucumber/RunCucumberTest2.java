package Selenium.Cucumber;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = { "src/test/resources/Selenium/Cucumber/Web" }, glue = { "Selenium.Cucumber.WebSteps" }, plugin = {
		"json:target/cucumber-reports/Cucumber.json", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
		"Listeners.ParallelEventListenerCucumber", "pretty", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" },tags="@Web")
public class RunCucumberTest2 extends AbstractTestNGCucumberTests {

	@Override
	@DataProvider(parallel = true)
	public Object[][] scenarios() {

		System.out.println(super.scenarios().length + "total ");
		return super.scenarios();
	}

}
