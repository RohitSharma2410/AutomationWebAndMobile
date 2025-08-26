package Listeners;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

public class EventFiringClass {

	private EventFiringDecorator<WebDriver> eventfiring = null;

	public EventFiringClass() {
		eventfiring = new EventFiringDecorator<>(new WebEventsListener());

	}

	public synchronized WebDriver getDriver(WebDriver driver) {
		return eventfiring.decorate(driver);
	}

}
