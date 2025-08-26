package utilsClasses;

import static Listeners.ParallelEventListenerCucumber.mobileDrivers;
import static Listeners.ParallelEventListenerCucumber.mobileObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumBy;

public class MobileDriverUtils {

	public static WebElement getMobileElement(String locatorName) {
		WebElement element = null;

		System.out.println(mobileObject.getProperty(locatorName).toString() + "value of whole locator");
		String locatorType = mobileObject.getProperty(locatorName).toString().split("@@@")[0];
		String locatorValue = mobileObject.getProperty(locatorName).toString().split("@@@")[1];
		switch (locatorType.toLowerCase()) {
		case "accessibilityid":
			element = (mobileDrivers.get()).findElement(AppiumBy.accessibilityId(locatorValue));
			break;
		case "xpath":
			element = (mobileDrivers.get()).findElement(By.xpath(locatorValue));
			break;
		case "androiduiautomator":
			element = (mobileDrivers.get()).findElement(AppiumBy.androidUIAutomator(locatorValue));
			break;
		case "id":
			element = (mobileDrivers.get()).findElement(By.id(locatorValue));
			break;
		}

		return element;

	}

	public static void clickElement(String locator) {
		getMobileElement(locator).click();
	}

	public static void typeinToElement(String locator, String type) {
		getMobileElement(locator).sendKeys(type);


	}

}
