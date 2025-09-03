package Listeners;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.http.entity.ContentType;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.cucumber.java.Scenario;
import io.cucumber.plugin.*;
import io.cucumber.plugin.event.*;
import io.cucumber.plugin.event.Status;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilsClasses.PropertiesFIlesHelper;
import utilsClasses.WebDriverUtils;

public class ParallelEventListenerCucumber implements ConcurrentEventListener, Plugin {

    public static AppiumDriverLocalService service = null;
    public static final String BASE_URL = System.getProperty("base.url", "https://default.url");
    public static final String BASEURI = System.getProperty("baseURI", "");
public static ThreadLocal<Map<String,Object>>datamaps=null;
    public static ThreadLocal<Scenario> scenarios = new ThreadLocal<>();
    public static ThreadLocal<AppiumDriver> mobileDrivers = new ThreadLocal<>();
    public static ThreadLocal<WebDriver> drivers = new ThreadLocal<>();
    public static ThreadLocal<WebDriverUtils> webdriverUtils = new ThreadLocal<>();
    public static ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();
    public static ThreadLocal<Response>response=null;
    public static ThreadLocal<JavascriptExecutor> js = new ThreadLocal<>();
    public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    public static ExtentReports report = null;
    public static PropertiesFIlesHelper pageObjects = null;
    public static PropertiesFIlesHelper config = null;
    public static PropertiesFIlesHelper mobileObject = null;
    public static ThreadLocal<String> scenarioName = new ThreadLocal<>();
    public static ThreadLocal<RequestSpecification>request=null;

    private static URL gridUrl = null;

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, this::handleTestRunStarted);
        publisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestStepStarted.class, this::handleTestStepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
    }

    private void handleTestRunStarted(TestRunStarted event) {
        extentTest = new ThreadLocal<>();
        drivers = new ThreadLocal<>();
        mobileDrivers = new ThreadLocal<>();
        wait = new ThreadLocal<>();
        js = new ThreadLocal<>();
        scenarioName = new ThreadLocal<>();
        webdriverUtils = new ThreadLocal<>();
request=new ThreadLocal<>();
datamaps=new ThreadLocal<>();
response=new ThreadLocal<>();
        report = new ExtentReports();
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("extent-report.html");
        report.attachReporter(htmlReporter);

        try {
            config = new PropertiesFIlesHelper(System.getProperty("user.dir") + "/src/main/resources/config.properties");
            pageObjects = new PropertiesFIlesHelper(System.getProperty("user.dir") + "/src/main/resources/pageObjects2.properties");
            mobileObject = new PropertiesFIlesHelper(System.getProperty("user.dir") + "/src/main/resources/mobileObjects.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Allure.description("TEST RUN STARTED");
    }

    private void handleTestRunFinished(TestRunFinished event) {
        report.flush();
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        String name = event.getTestCase().getName();
        scenarioName.set(name);
        extentTest.set(report.createTest(name));
        extentTest.get().assignAuthor("Rohit Sharma");
        System.out.println("[START] " + name + " on Thread " + Thread.currentThread().getName());

        try {
            String gridUrlEnv = System.getenv("SELENIUM_GRID_URL");
            if (gridUrlEnv == null || gridUrlEnv.isEmpty()) {
                gridUrlEnv = "http://localhost:4444";
            }
            gridUrl = URI.create(gridUrlEnv).toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid SELENIUM_GRID_URL: " + e.getMessage());
        }

        if (event.getTestCase().getTags().contains("@Web")) {
            if (!isSeleniumGridAvailable(gridUrl)) {
                throw new RuntimeException("Selenium Grid not reachable at " + gridUrl);
            }

            ChromeOptions options = new ChromeOptions();
            options.setAcceptInsecureCerts(true);
            options.addArguments("--headless=new");
            options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);

            WebDriver driver = new RemoteWebDriver(gridUrl, options);
            drivers.set(new EventFiringClass().getDriver(driver));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
            driver.get(BASE_URL);
            driver.manage().window().maximize();

            js.set((JavascriptExecutor) driver);
            wait.set(new WebDriverWait(driver, Duration.ofSeconds(30)));
        }

        if (event.getTestCase().getTags().contains("@Mobile")) {
            if (service == null || !service.isRunning()) {
                String androidHome = System.getenv("ANDROID_HOME");
                Map<String, String> env = new HashMap<>(System.getenv());
                env.put("ANDROID_HOME", androidHome);
                env.put("PATH", androidHome + "/platform-tools:" + env.get("PATH"));

                service = new AppiumServiceBuilder()
                        .withEnvironment(env)
                        .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
                        .withLogFile(new File("appium.log"))
                        .withTimeout(Duration.ofSeconds(60))
                        .usingPort(4725)
                        .build();
                service.start();
            }

            UiAutomator2Options options = new UiAutomator2Options();
            options.setDeviceName("pixel7");
            options.setAutomationName("UIAutomator2");
            options.setApp("/Users/rohitsharma/eclipse-workspace/Appium/ApiDemos-debug.apk");

            try {
                AppiumDriver driver = new AndroidDriver(URI.create("http://localhost:4725").toURL(), options);
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                mobileDrivers.set(driver);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to create Appium driver.");
            }
        }
        if (event.getTestCase().getTags().contains("@Api")) {
        	request.set(RestAssured.given());
        	request.get().baseUri(BASEURI);
        	request.get().contentType(ContentType.APPLICATION_JSON.toString());
        	request.get().log().all();
        }
    }

    private void handleTestStepStarted(TestStepStarted event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep currentStep = (PickleStepTestStep) event.getTestStep();
            String stepText = currentStep.getStep().getKeyword() + currentStep.getStep().getText();
            System.out.println("Test Step started: " + stepText);
            Allure.step(stepText);
        }
    }

    private void handleTestStepFinished(TestStepFinished event) {
        if (event.getResult().getStatus() == Status.FAILED) {
            File file = new File("screenshots/" + scenarioName.get() + ".png");

            try {
                if (drivers.get() != null) {
                    TakesScreenshot ts = (TakesScreenshot) drivers.get();
                    byte[] screenshotBytes = ts.getScreenshotAs(OutputType.BYTES);
                    Allure.addAttachment("Screenshot - Failed Step", new ByteArrayInputStream(screenshotBytes));
                    FileUtils.copyFile(ts.getScreenshotAs(OutputType.FILE), file);
                    extentTest.get().addScreenCaptureFromPath(file.getAbsolutePath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleTestCaseFinished(TestCaseFinished event) {
        System.out.println("[END] " + scenarioName.get() + " on Thread " + Thread.currentThread().getName());
if(event.getResult().getStatus()==Status.FAILED||event.getResult().getError()!=null)
{
	extentTest.get().fail(event.getTestCase().getName());
}
        if (event.getTestCase().getTags().contains("@Web")) {
            try {
                if (drivers.get() != null) {
                    drivers.get().quit();
                    drivers.remove();
                }
            } catch (Exception e) {
                System.err.println("Failed to quit web driver: " + e.getMessage());
            }
        }

        if (event.getTestCase().getTags().contains("@Mobile")) {
            try {
                if (mobileDrivers.get() != null) {
                    mobileDrivers.get().quit();
                    mobileDrivers.remove();
                }
                if (service != null && service.isRunning()) {
                    service.stop();
                    System.out.println("Appium Service stopped after all tests");
                }
            } catch (Exception e) {
                System.err.println("Failed to quit mobile driver: " + e.getMessage());
            }
        }
        if (event.getTestCase().getTags().contains("@Api")) {
        	request.remove();
        }
        scenarioName.remove();
    }

    private boolean isSeleniumGridAvailable(URL gridUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) gridUrl.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            System.out.println("Selenium Grid response code: " + responseCode);
            return (responseCode == 200 || responseCode == 301 || responseCode == 302);
        } catch (Exception e) {
            System.err.println("Could not connect to Selenium Grid: " + e.getMessage());
            return false;
        }
    }
}
