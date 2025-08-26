package utilsClasses;

import static Listeners.ParallelEventListenerCucumber.drivers;
import static Listeners.ParallelEventListenerCucumber.pageObjects;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;

public class WebDriverUtils {

	public static WebElement returnElementAfterWaitCheck(String locator) {
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(drivers.get(), Duration.ofSeconds(30));
		By by = returnBy(locator);
		try {
			element = drivers.get().findElement(by);
			return element;
		} catch (Exception e) {
//			if (e.getClass() == NoSuchElementException.class) {
//				wait.until(ExpectedConditions.visibilityOfElementLocated(by));
//				return drivers.get().findElement(by);
//			}
		}
		return element;
	}

	public static List<WebElement> returnElementsAfterWaitCheck(String locator) {
		List<WebElement> elements = null;
		WebDriverWait wait = new WebDriverWait(drivers.get(), Duration.ofSeconds(30));
		By by = returnBy(locator);
		try {
			elements = drivers.get().findElements(by);
			return elements;
		} catch (Exception e) {
//			if (e.getClass() == NoSuchElementException.class) {
//				wait.until(ExpectedConditions.visibilityOfElementLocated(by));
//				return drivers.get().findElements(by);
//			}
		}
		return elements;
	}

	private static By returnBy(String locator) {
		if (locator.split("@@@")[0].equalsIgnoreCase("xpath")) {
			return By.xpath(locator.split("@@@")[1]);
		}
		return null;
	}

	public void clickOnElement(WebDriver driver, String locator) {
		try {
			returnElementAfterWaitCheck(locator).click();
		} catch (Exception e) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView()", drivers.get().findElement(returnBy(locator)));
		}

	}

	public static void focus(WebDriver driver, WebElement locator) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView()", locator);

	}

	public static WebElement findElementInFrames(WebDriver driver, By by) {
		List<WebElement> frames = drivers.get().findElements(By.xpath("//frames"));
		WebElement element = null;
		for (WebElement fr : frames) {
			try {
				element = fr.findElement(by);
				return element;
			} catch (Exception e) {

			}

		}
		return element;

	}

	public static void storeScreenshot(String path) {
		TakesScreenshot ts = (TakesScreenshot) drivers.get();

		File file = ts.getScreenshotAs(OutputType.FILE);
		try {
			Files.copy(file, new File(System.getProperty("user.dir").concat(path).concat(".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static boolean waitUntill(WebDriver driver, ExpectedCondition<?> conditions) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		return wait.until(ExpectedConditions.not(conditions));
	}

	public static WebElement getElement(By locator) {
		return drivers.get().findElement(locator);
	}

	public static WebElement getWebElement(String locator) {
		System.out.println("locator is " + locator);
		String locatortype = pageObjects.getProperty(locator).toString().split("@@@")[0];
		System.out.println(drivers.get().getCurrentUrl() + Thread.currentThread().getId());
		switch (locatortype) {
		case "xpath":
			System.out.println("locator value are " + pageObjects.getProperty(locator).toString().split("@@@")[1]);
			return drivers.get().findElement(By.xpath(pageObjects.getProperty(locator).toString().split("@@@")[1]));
		default:
			return drivers.get().findElement(By.xpath(pageObjects.getProperty(locator).toString().split("@@@")[1]));
		}
	}

	public static List<WebElement> getWebElements(String locator) {
		System.out.println("locator is " + locator);
		String locatortype = pageObjects.getProperty(locator).toString().split("@@@")[0];
		switch (locatortype) {
		case "xpath":
			System.out.println("locator value are " + pageObjects.getProperty(locator).toString().split("@@@")[1]);

			return drivers.get().findElements(By.xpath(pageObjects.getProperty(locator).toString().split("@@@")[1]));

		default:

			return drivers.get().findElements(By.xpath(pageObjects.getProperty(locator).toString().split("@@@")[1]));
		}

	}

	public static WebElement getWebElementOnElement(String locator, WebElement element) {
		String locatortype = pageObjects.getProperty(locator).toString().split("@@@")[0];
		switch (locatortype) {
		case "xpath":
			return element.findElement(By.xpath(pageObjects.getProperty(locator).toString().split("@@@")[1]));

		default:
			System.out.println("change from master");
			return element.findElement(By.xpath(pageObjects.getProperty(locator).toString().split("@@@")[1]));
		}

	}

	public static WebElement getWebElementWithUpdatedValue(String locator, String valueToReplace,
			String valueByReplace) {
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

}
