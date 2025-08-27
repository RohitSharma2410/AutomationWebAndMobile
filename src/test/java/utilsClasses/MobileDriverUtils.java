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
            String rawLocator = mobileObject.getProperty(locatorName).toString();
            System.out.println(rawLocator + " - value of whole locator");

            String locatorType = rawLocator.split("@@@")[0].trim().toLowerCase();
            String locatorValue = rawLocator.split("@@@")[1].trim();

            switch (locatorType) {
                case "accessibilityid":
                    element = mobileDrivers.get().findElement(AppiumBy.accessibilityId(locatorValue));
                    break;
                case "xpath":
                    element = mobileDrivers.get().findElement(By.xpath(locatorValue));
                    break;
                case "androiduiautomator":
                    element = mobileDrivers.get().findElement(AppiumBy.androidUIAutomator(locatorValue));
                    break;
                case "id":
                    element = mobileDrivers.get().findElement(By.id(locatorValue));
                    break;
                default:
                    element = mobileDrivers.get().findElement(By.xpath(locatorValue));
                    break;
            }
        } catch (NoSuchElementException e) {
            Allure.description("❌ No locator found: " + locatorName);
            System.err.println("NoSuchElementException: Could not locate " + locatorName);
        } catch (Exception e) {
            Allure.description("❌ Error locating element: " + locatorName + " - " + e.getMessage());
            e.printStackTrace();
        }

        return element;
    }

    public static List<WebElement> getMobileElements(String locatorName) {
        List<WebElement> elements = new ArrayList<>();
        try {
            String rawLocator = mobileObject.getProperty(locatorName).toString();
            System.out.println(rawLocator + " - value of whole locator");

            String locatorType = rawLocator.split("@@@")[0].trim().toLowerCase();
            String locatorValue = rawLocator.split("@@@")[1].trim();

            switch (locatorType) {
                case "accessibilityid":
                    elements = mobileDrivers.get().findElements(AppiumBy.accessibilityId(locatorValue));
                    break;
                case "xpath":
                    elements = mobileDrivers.get().findElements(By.xpath(locatorValue));
                    break;
                case "androiduiautomator":
                    elements = mobileDrivers.get().findElements(AppiumBy.androidUIAutomator(locatorValue));
                    break;
                case "id":
                    elements = mobileDrivers.get().findElements(By.id(locatorValue));
                    break;
                default:
                    elements = mobileDrivers.get().findElements(By.xpath(locatorValue));
                    break;
            }
        } catch (NoSuchElementException e) {
            Allure.description("❌ No locator found: " + locatorName);
            System.err.println("NoSuchElementException: Could not locate elements for " + locatorName);
        } catch (Exception e) {
            Allure.description("❌ Error locating elements: " + locatorName + " - " + e.getMessage());
            e.printStackTrace();
        }

        return elements;
    }

    public static void clickElement(String locatorName) {
        WebElement element = getMobileElement(locatorName);
        if (element != null) {
            element.click();
        }
    }

    public static void typeIntoElement(String locatorName, String input) {
        WebElement element = getMobileElement(locatorName);
        if (element != null) {
            element.sendKeys(input);
        }
    }
}
