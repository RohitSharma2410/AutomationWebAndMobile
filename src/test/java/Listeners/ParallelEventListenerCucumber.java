package Listeners;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import Selenium.Cucumber.TestBase;
import io.cucumber.java.Scenario;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.Result;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.Plugin;
public class ParallelEventListenerCucumber implements ConcurrentEventListener,Plugin {
	

	private static ThreadLocal<String>scenarioName=new ThreadLocal<>();

	@Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
        publisher.registerHandlerFor(TestCaseFinished.class, event -> {
			try {
				handleTestCaseFinished(event);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        scenarioName.set(event.getTestCase().getName());
        System.out.println("[START] " + scenarioName.get() + " on Thread " + Thread.currentThread().getId());

		TestBase.extentTest.set(TestBase.report.createTest(event.getTestCase().getName()));
		TestBase.extentTest.get().assignAuthor("Rohit Sharma");
		
		switch (TestBase.config.getProperty("browser").toString().toLowerCase()) {
		case "chrome":
			ChromeOptions options = new ChromeOptions();
			options.setAcceptInsecureCerts(true);
//			options.addArguments("--headless=new");
			options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
			TestBase.drivers.set(new ChromeDriver(options));
			break;
		case "firefox":
			FirefoxOptions foptions=new FirefoxOptions();
			foptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
			TestBase.drivers.set(new FirefoxDriver(foptions));
			break;
		}
		TestBase.drivers.set(new EventFiringClass().getDriver(TestBase.drivers.get()));
		TestBase.drivers.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		TestBase.drivers.get().get(TestBase.config.getProperty("appurl").toString());
		TestBase.drivers.get().manage().window().maximize();
		TestBase.js.set((JavascriptExecutor)TestBase. drivers.get());
		TestBase.wait.set((new WebDriverWait(TestBase.drivers.get(), Duration.ofSeconds(30))));
	
    }

    private void handleTestStepFinished(TestStepFinished event) {
        if (event.getResult().getStatus() == Status.FAILED) {
            System.out.println("[FAIL] " + scenarioName.get() + ": " + event.getResult().getError());
        }
    }

   
    private void handleTestCaseFinished(TestCaseFinished event)  {
        System.out.println("[END] " + scenarioName.get() + " on Thread " + Thread.currentThread().getId());
        scenarioName.remove();  // Clean up
        try {
        if(event.getResult().getStatus()==Status.FAILED) {
        	
    			byte[] js = ((TakesScreenshot) TestBase.drivers.get()).
    					getScreenshotAs(OutputType.BYTES);
    			
    			TakesScreenshot ts = (TakesScreenshot) TestBase.drivers.get();
    			File file = new File(System.getProperty("user.dir").
    					concat("/screenshots/"+event.getTestCase().getName()+".png"));
    			FileUtils.copyFile(ts.getScreenshotAs(OutputType.FILE), file);
    			TestBase.extentTest.get().addScreenCaptureFromPath(file.getAbsolutePath());
    			TestBase.extentTest.get().fail(event.getTestCase().getName()+" from test base");
    			}	
       
        }
        catch(Exception e) {
        	System.out.println("error while puting screenshot to report");
        }
        finally {
        TestBase.drivers.get().close();
        System.out.println("Test case finished");
        }
    }
}
	  

