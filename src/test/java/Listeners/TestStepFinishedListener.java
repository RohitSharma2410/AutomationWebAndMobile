package Listeners;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import Selenium.Cucumber.TestBase;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Result;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestStepFinished;

public class TestStepFinishedListener implements ConcurrentEventListener{


	@Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, 
        		event -> {
					try {
						handleTestFinished(event);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}

    
	private void handleTestFinished(TestStepFinished finish) throws Exception {
		PickleStepTestStep step=null;
		if(finish.getTestStep() instanceof PickleStepTestStep ) {
			 step=(PickleStepTestStep)finish.getTestStep();
		
        if (finish.getResult().getError()!=null || 
        		finish.getResult().getStatus().is(Status.FAILED)){			
        				byte[] js = ((TakesScreenshot) TestBase.drivers.get()).
        						getScreenshotAs(OutputType.BYTES);
        				//TestBase.scenarios.get().attach(js, "image/png", TestBase.scenarios.get().getName());
        				TakesScreenshot ts = (TakesScreenshot) TestBase.drivers.get();
        				
        				
        					File file = new File(System.getProperty("user.dir").
            						concat("/target/surefire-reports/screenshots/"+step.getStep().getText()+".png"));
            				FileUtils.copyFile(ts.getScreenshotAs(OutputType.FILE), file);
            				TestBase.extentTest.get().addScreenCaptureFromPath(file.getAbsolutePath(),step.getStep().getText());
        					TestBase.extentTest.get().log(com.aventstack.extentreports.Status.FAIL,"\n"+ step.getStep().getText());
        					//TestBase.scenarios.get().log("test failed from event");
        		        	  System.out.println("Test Step finished with fail"+step.getStep().getText());
		
        }
        			
        	
        if(finish.getResult().getStatus()==Status.PASSED) {
        	  System.out.println("Test Step finished with pass"+step.getStep().getText());
			TestBase.extentTest.get().log(com.aventstack.extentreports.Status.PASS, step.getStep().getText());
        }
        	
	}	
	}
        		
        
    
}
