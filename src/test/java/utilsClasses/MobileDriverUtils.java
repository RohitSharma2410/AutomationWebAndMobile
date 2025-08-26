package utilsClasses;

import static Listeners.ParallelEventListenerCucumber.mobileDrivers;
import static Listeners.ParallelEventListenerCucumber.mobileObject;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Allure;

public class MobileDriverUtils {

	public static WebElement getMobileElement(String locatorName) {
		WebElement element = null;
try {
		System.out.println(mobileObject.getProperty(locatorName).toString() + "value of whole locator");
		String locatorType = mobileObject.getProperty(locatorName).toString().split("@@@")[0];
		String locatorValue = mobileObject.getProperty(locatorName).toString().split("@@@")[1];
		switch (locatorType.toLowerCase()) {
		case "accessibilityid":
			element = (mobileDrivers).findElement(AppiumBy.accessibilityId(locatorValue));
			break;
		case "xpath":
			element = (mobileDrivers).findElement(By.xpath(locatorValue));
			break;
		case "androiduiautomator":
			element = (mobileDrivers).findElement(AppiumBy.androidUIAutomator(locatorValue));
			break;
		case "id":
			element = (mobileDrivers).findElement(By.id(locatorValue));
			break;
			default:
				element = (mobileDrivers).findElement(By.xpath(locatorValue));
break;
		}
}
catch(NoSuchElementException _) {
Allure.description("No Locator found");
}
		return element;

	}

	public static List<WebElement> getMobileElements(String locatorName) {
		List<WebElement> elements = new ArrayList<>();
try {
		System.out.println(mobileObject.getProperty(locatorName).toString() + "value of whole locator");
		String locatorType = mobileObject.getProperty(locatorName).toString().split("@@@")[0];
		String locatorValue = mobileObject.getProperty(locatorName).toString().split("@@@")[1];
		switch (locatorType.toLowerCase()) {
		case "accessibilityid":
			elements = (mobileDrivers).findElements(AppiumBy.accessibilityId(locatorValue));
			break;
		case "xpath":
			elements = (mobileDrivers).findElements(By.xpath(locatorValue));
			break;
		case "androiduiautomator":
			elements = (mobileDrivers).findElements(AppiumBy.androidUIAutomator(locatorValue));
			break;
		case "id":
			elements = (mobileDrivers).findElements(By.id(locatorValue));
			break;
			default:
				elements = (mobileDrivers).findElements(By.xpath(locatorValue));
break;
		}
}
catch(NoSuchElementException _) {
Allure.description("No Locator found");
}
		return elements;

	}
	public static void clickElement(String locator) {
		getMobileElement(locator).click();
	}

	public static void typeinToElement(String locator, String type) {
		getMobileElement(locator).sendKeys(type);


	}

}
