package Selenium.Cucumber;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import Listeners.EventFiringClass;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import utilsClasses.PropertiesFIlesHelper;

public class TestBase {
	public static PropertiesFIlesHelper pageObjects = null;
	public static PropertiesFIlesHelper config = null;
public static ThreadLocal<Scenario>scenarios=null;
	
	public static ThreadLocal<WebDriver> drivers = null;
	public static ThreadLocal<WebDriverWait> wait = null;
	public static ThreadLocal<JavascriptExecutor> js = null;
	public static ThreadLocal<ExtentTest> extentTest = null;
	public static ThreadLocal<List<Map<String,Object>>> testObject=null;
	public static ExtentReports report = null;

	@BeforeAll
	public static void before_or_after_all() throws IOException {
		
		
		if (pageObjects == null) {
			pageObjects = new PropertiesFIlesHelper(
					System.getProperty("user.dir").concat("/src/main/resources/" + "pageObjects2.properties"));

			config = new PropertiesFIlesHelper(
					System.getProperty("user.dir").concat("/src/main/resources/" + "config.properties"));

		}
		drivers = new ThreadLocal<>();
		js = new ThreadLocal<>();
		wait = new ThreadLocal<>();
		extentTest = new ThreadLocal<>();
		report = new ExtentReports();
		scenarios=new ThreadLocal<>();
		testObject=new ThreadLocal<>();
		
		System.setProperty("hudson.model.DirectoryBrowserSupport.CSP",
				"sandbox allow-same-origin; default-src 'self';");
		
		ExtentSparkReporter rs = new ExtentSparkReporter(
				System.getProperty("user.dir").concat("/target/surefire-reports/SparkReport.html"));

		rs.loadXMLConfig(
				new File(System.getProperty("user.dir").concat("/src/test/resources/extent.xml")));
		report.attachReporter(rs);
	}

//	@Before
	public void before_or_after(Scenario test) {
	
		scenarios.set(test);
		extentTest.set(report.createTest(test.getName()));
		extentTest.get().assignAuthor("Rohit Sharma");
		
		switch (config.getProperty("browser").toString().toLowerCase()) {
		case "chrome":
			ChromeOptions options = new ChromeOptions();
			options.setAcceptInsecureCerts(true);
//			options.addArguments("--headless=new");
			options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
			drivers.set(new ChromeDriver(options));
			break;
		case "firefox":
			FirefoxOptions foptions=new FirefoxOptions();
			foptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
			drivers.set(new FirefoxDriver(foptions));
			break;
		}
		drivers.set(new EventFiringClass().getDriver(drivers.get()));
		drivers.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		drivers.get().get(config.getProperty("appurl").toString());
		drivers.get().manage().window().maximize();
		js.set((JavascriptExecutor) drivers.get());
		wait.set((new WebDriverWait(drivers.get(), Duration.ofSeconds(30))));
	}

	@After
	public void after(Scenario test) throws Exception {

		
		
	}

	@AfterAll
	public static void afterall() {
//MailerClass.prepareEmail();
//		drivers.get().quit();
		report.flush();
	}

	public static WebElement getElement(By locator) {
		return drivers.get().findElement(locator);
	}

	public static WebElement getElement(String locator) {
		System.out.println("locator is "+locator);
		String locatortype = pageObjects.getProperty(locator).toString().split("@@@")[0];
		switch (locatortype) {
		case "xpath":
			System.out.println("locator value are "+pageObjects.getProperty(locator).toString().split("@@@")[1]);
			return drivers.get().findElement(By.xpath(pageObjects.getProperty(locator).toString().split("@@@")[1]));
		default:
			return drivers.get().findElement(By.xpath(pageObjects.getProperty(locator).toString().split("@@@")[1]));
		}
	}
	public static List<WebElement> getElements(String locator) {
		System.out.println("locator is "+locator);
		String locatortype = pageObjects.getProperty(locator).toString().split("@@@")[0];
		switch (locatortype) {
		case "xpath":
			System.out.println("locator value are "+pageObjects.getProperty(locator).toString().split("@@@")[1]);
	

			return drivers.get().findElements(By.xpath(pageObjects.getProperty(locator).toString().split("@@@")[1]));

		default:

			return drivers.get().findElements(By.xpath(pageObjects.getProperty(locator).toString().split("@@@")[1]));
		}

	}
	public static WebElement getElementOnElement(String locator, WebElement element) {
		String locatortype = pageObjects.getProperty(locator).toString().split("@@@")[0];
		switch (locatortype) {
		case "xpath":
			return element.findElement(By.xpath(pageObjects.getProperty(locator).toString().split("@@@")[1]));

		default:
			System.out.println("change from master");
			return element.findElement(By.xpath(pageObjects.getProperty(locator).toString().split("@@@")[1]));
		}

	}

	public static WebElement getElementWithUpdatedValue(String locator, String valueToReplace, String valueByReplace) {
		String locatortype = pageObjects.getProperty(locator).toString().split("@@@")[0];
		switch (locatortype) {
		case "xpath":
			if (valueToReplace != null) {
				return drivers.get().findElement(By.xpath(pageObjects.getProperty(locator).toString().split("@@@")[1]
						.replace(valueToReplace, valueByReplace)));
			}
			return drivers.get().findElement(By.xpath(pageObjects.getProperty(locator).toString().split("@@@")[1]));

		default:
			if (valueToReplace != null) {
				return drivers.get().findElement(By.xpath(pageObjects.getProperty(locator).toString().split("@@@")[1]
						.replace(valueToReplace, valueByReplace)));
			}
			return drivers.get().findElement(By.xpath(pageObjects.getProperty(locator).toString().split("@@@")[1]));
		}

	}

	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.sdfdsfxcsfdsfdfsfxcsdfs.com");
		System.out.println(driver.getPageSource());
		driver.get("https://practice.expandtesting.com");
		System.out.println(driver.getPageSource());
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(By.xpath("(//*[@id=\"examples\"]//a)[2]"))).build().perform();
		driver.findElement(By.xpath("(//*[@id=\"examples\"]//a)[2]")).click();
		actions.moveToElement(driver.findElement(By.xpath("//input[@id=\"username\"]"))).build().perform();

		driver.findElement(By.xpath("//input[@id=\"username\"]")).sendKeys("practice");
		actions.moveToElement(driver.findElement(By.xpath("//input[@id=\"password\"]"))).build().perform();

		driver.findElement(By.xpath("//input[@id=\"password\"]")).sendKeys("SuperSecretPassword!");
		actions.moveToElement(driver.findElement(By.xpath("//button[@type=\"submit\"]"))).build().perform();

		driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();
		// Thread.sleep(5000);
		Set<Cookie> cookies = driver.manage().getCookies();
		for (Cookie co : cookies) {
			System.out.println("Cookie is " + co.getName());
		}
		driver.close();
		driver = new ChromeDriver();
		actions = new Actions(driver);
		driver.get("https://practice.expandtesting.com");
		for (Cookie c : cookies) {
			driver.manage().addCookie(c);
		}
		actions.moveToElement(driver.findElement(By.xpath("(//*[@id=\"examples\"]//a)[2]"))).build().perform();
		driver.findElement(By.xpath("(//*[@id=\"examples\"]//a)[2]")).click();
		actions.moveToElement(driver.findElement(By.xpath("//button[@type=\"submit\"]"))).build().perform();
		driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();

	}

}
