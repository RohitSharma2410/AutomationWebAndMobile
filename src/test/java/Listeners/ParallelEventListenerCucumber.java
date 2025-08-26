package Listeners;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.cucumber.java.Scenario;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.Plugin;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.event.TestStepStarted;
import io.qameta.allure.Allure; // Added for Allure API
import utilsClasses.PropertiesFIlesHelper;
import utilsClasses.WebDriverUtils;

public class ParallelEventListenerCucumber implements ConcurrentEventListener, Plugin {

	public static AppiumDriverLocalService service = null;
	public static ThreadLocal<Scenario> scenarios = null;
	public static ThreadLocal<AppiumDriver> mobileDrivers = null;
	public static ThreadLocal<WebDriverUtils> webdriverUtils = null;
	public static ThreadLocal<WebDriver> drivers = null;
	public static ThreadLocal<WebDriverWait> wait = null;
	public static ThreadLocal<JavascriptExecutor> js = null;
	public static ThreadLocal<ExtentTest> extentTest = null;
	public static ExtentReports report = null;
	public static PropertiesFIlesHelper pageObjects = null;
	public static PropertiesFIlesHelper config = null;
	public static ThreadLocal<String> scenarioName = new ThreadLocal<>();
	public static PropertiesFIlesHelper mobileObject = null;
	private static URL gridUrl = null;

	// Called once before all tests start
	private void handleTestRunStarted(TestRunStarted event) {
		scenarios = new ThreadLocal<>();
		mobileDrivers = new ThreadLocal<>();
		drivers = new ThreadLocal<>();
		wait = new ThreadLocal<>();
		js = new ThreadLocal<>();
		extentTest = new ThreadLocal<>();
		report = new ExtentReports();
		scenarioName = new ThreadLocal<>();
		webdriverUtils = new ThreadLocal<>();
		report = new ExtentReports();
		 ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("extent-report.html");
	       
	        report.attachReporter(htmlReporter);
		Allure.description("TEST RUN STARTED");
		try {
			config = new PropertiesFIlesHelper(
					System.getProperty("user.dir") + "/src/main/resources/config.properties");
			pageObjects = new PropertiesFIlesHelper(
					System.getProperty("user.dir") + "/src/main/resources/pageObjects2.properties");
			mobileObject = new PropertiesFIlesHelper(
					System.getProperty("user.dir") + "/src/main/resources/mobileObjects.properties");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Start Appium service once per entire test run if Android driver is configured
		if ("android".equalsIgnoreCase(config.getProperty("mobiledriver").toString())) {
			if (service == null || !service.isRunning()) {
				String androidHome = "/Users/rohitsharma/Library/Android/sdk";
				Map<String, String> env = new HashMap<>(System.getenv());
				env.put("ANDROID_HOME", androidHome);
				env.put("PATH", androidHome + "/platform-tools:" + env.get("PATH"));

				service = new AppiumServiceBuilder().withEnvironment(env)
						.withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
						.withLogFile(new File("appium.log")).withTimeout(Duration.ofSeconds(60)).usingPort(4725)
						.build();
				service.start();
				System.out.println("Appium Service started at port 4725");
			}
		}
	}

	// Called once after all tests finish
	private void handleTestRunFinished(TestRunFinished event) {
		if (service != null && service.isRunning()) {
			service.stop();
			System.out.println("Appium Service stopped after all tests");
		}
		report.flush();
	}

	@Override
	public void setEventPublisher(EventPublisher publisher) {
		publisher.registerHandlerFor(TestRunStarted.class, this::handleTestRunStarted);
		publisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);

		publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
		publisher.registerHandlerFor(TestStepStarted.class, this::handleTestStepStarted);

		publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
		publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
	}

	private void handleTestCaseStarted(TestCaseStarted event) {
		Allure.description("Test Case Started " + event.getTestCase().getName());
		scenarioName.set(event.getTestCase().getName());
		System.out.println("[START] " + scenarioName.get() + " on Thread " + Thread.currentThread().getId());
		extentTest.set(report.createTest(event.getTestCase().getName()));
		extentTest.get().assignAuthor("Rohit Sharma");

		// Start Allure test lifecycle - in Cucumber this is automatic but you can add
		// labels etc here if needed

		try {
			if (gridUrl == null) {
				gridUrl = URI.create("http://localhost:4444").toURL();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		if (event.getTestCase().getTags().contains("@Web")) {
			switch (config.getProperty("webdriver").toString().toLowerCase()) {
			case "chrome":
				ChromeOptions options = new ChromeOptions();
				options.setAcceptInsecureCerts(true);
				options.addArguments("--headless=new");
				options.addArguments("--disable-gpu");
				options.addArguments("--no-sandbox");
				options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
				drivers.set(new RemoteWebDriver(gridUrl, options));
				break;
			case "firefox":
				FirefoxOptions foptions = new FirefoxOptions();
				foptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
				drivers.set(new FirefoxDriver(foptions));
				break;
			}
			drivers.set(new EventFiringClass().getDriver(drivers.get()));
			drivers.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
			drivers.get().get(config.getProperty("appurl").toString());
			drivers.get().manage().window().maximize();
			js.set((JavascriptExecutor) drivers.get());
			wait.set(new WebDriverWait(drivers.get(), Duration.ofSeconds(30)));
		}

		if (event.getTestCase().getTags().contains("@Mobile")) {
			switch (config.getProperty("mobiledriver").toString().toLowerCase()) {
			case "android":
				UiAutomator2Options options = new UiAutomator2Options();
				options.setDeviceName("pixel7");
				options.setAutomationName("UIAutomator2");
				options.setApp("/Users/rohitsharma/eclipse-workspace/Appium/ApiDemos-debug.apk");

				try {
					// Connect to already running Appium service
					mobileDrivers.set(new AndroidDriver(new URL("http://localhost:4725"), options));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				mobileDrivers.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
				break;
			default:
				break;
			}
		}
	}

	private void handleTestStepStarted(TestStepStarted event) {
		if (event.getTestStep() instanceof PickleStepTestStep) {
			PickleStepTestStep currentStep = (PickleStepTestStep) event.getTestStep();
			String stepText = currentStep.getStep().getKeyword() + currentStep.getStep().getText();
			System.out.println("Test Step started: " + stepText);

			// Log step start in Allure (creates a step)
			Allure.step(stepText);
		}
	}

	private void handleTestStepFinished(TestStepFinished event) {
		if (event.getResult().getStatus() == Status.FAILED) {
			System.out.println("[FAIL] " + scenarioName.get() + ": " + event.getResult().getError());

			// Capture screenshot for WebDriver
			try {
				if (drivers.get() != null) {
					TakesScreenshot ts = (TakesScreenshot) drivers.get();
					byte[] screenshotBytes = ts.getScreenshotAs(OutputType.BYTES);

					// Attach screenshot to Allure report
					Allure.addAttachment("Screenshot - Failed Step", new ByteArrayInputStream(screenshotBytes));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void handleTestCaseFinished(TestCaseFinished event) {
		System.out.println("[END] " + scenarioName.get() + " on Thread " + Thread.currentThread().getId());
		scenarioName.remove(); // Clean up

		try {
			if (event.getResult().getStatus() == Status.FAILED) {
				if (drivers.get() != null) {
					TakesScreenshot ts = (TakesScreenshot) drivers.get();
					File file = new File(
							System.getProperty("user.dir") + "/screenshots/" + event.getTestCase().getName() + ".png");
					FileUtils.copyFile(ts.getScreenshotAs(OutputType.FILE), file);
					extentTest.get().addScreenCaptureFromPath(file.getAbsolutePath());
					extentTest.get().fail(event.getTestCase().getName() + " failed.");

					// Attach screenshot also to Allure report
					byte[] screenshotBytes = ts.getScreenshotAs(OutputType.BYTES);
					Allure.addAttachment("Screenshot - Test Failed", new ByteArrayInputStream(screenshotBytes));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (mobileDrivers != null && mobileDrivers.get() != null) {
					mobileDrivers.get().quit();
					mobileDrivers.remove();
				}
			} catch (Exception e) {
				System.err.println("Failed to quit mobile driver: " + e.getMessage());
			}

			try {
				if (drivers != null && drivers.get() != null) {
					drivers.get().quit();
					drivers.remove();
				}
			} catch (Exception e) {
				System.err.println("Failed to quit web driver: " + e.getMessage());
			}

			System.out.println("Test case cleanup finished.");
		}
	}
}
