package Selenium.Cucumber;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = { "src/test/resources/Selenium/Cucumber" }, glue = { "Selenium.Cucumber" }, plugin = {
		"json:target/cucumber-reports/Cucumber.json", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
		"Listeners.ParallelEventListenerCucumber", "pretty", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" })
public class RunCucumberTest extends AbstractTestNGCucumberTests {

	@Override
	@DataProvider(parallel = true)
	public Object[][] scenarios() {
//		Object[][] scArray=new Object[super.scenarios().length][];

		System.out.println(super.scenarios().length + "total ");
//		System.out.println(super.scenarios()[0].length);
//		Arrays.sort(super.scenarios());
		return super.scenarios();
	}

}
