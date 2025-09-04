package Selenium.Cucumber.ApISteps;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.testng.Assert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;

import static Listeners.ParallelEventListenerCucumber.*; 
public class ApiSteps {

	@Given("Preparing to call {string} api")
	public void preparing_to_call_api(String string) throws StreamReadException, DatabindException, IOException {
		// Write code here that turns the phrase above into concrete actions
		try {
			InputStream inputStream = getClass().getClassLoader()
				    .getResourceAsStream("apiRequestFiles/"+string+".json");

			Map<String, Object>dataObject=new ObjectMapper().readValue(inputStream.readAllBytes(), new TypeReference<Map<String, Object>>() {});
		
			Allure.description("request body is");
			Allure.description(dataObject.toString());
			
			datamaps.set(dataObject);
		}  catch (FileNotFoundException e) {
			
			
		} 
		catch(NullPointerException e) {
			
		}
	}

	@When("API body parameter {string} is {string}")
	public void api_body_parameter_is(String string1,String string2) throws StreamReadException, DatabindException, IOException {
		// Write code here that turns the phrase above into concrete actions
	System.out.println("Printing body object value");
		System.out.println(datamaps.get().toString());
		datamaps.get().put(string1,string2);
		System.out.println(datamaps.get().toString());

		
	}

	@When("I call {string} with {string}")
	public void i_call_api(String string,String string1) {
		// Write code here that turns the phrase above into concrete actions
		switch(string1.toLowerCase()) {
		case "post":
			response.set(request.get().log().all().body(datamaps.get()).when().post(string));
			break;
		case "put":
			response.set(request.get().log().all().body(datamaps.get()).when().put(string));
			break;
		case "get":
			response.set(request.get().log().all().when().get(string));
			break;
		case "delete":
			response.set(request.get().log().all().when().delete(string));
			break;
			default:
				response.set(request.get().when().get(string));
				break;
		}
		response.get().then().log().all();
		
	}

	@Then("Response body should have {string}")
	public void response_body_should_have(String string) {
		// Write code here that turns the phrase above into concrete actions
		Assert.assertTrue(response.get().body().asString().contains(string));
	}

	@Then("Response status should be {int}")
	public void response_status_should_be(Integer int1) {
		// Write code here that turns the phrase above into concrete actions
		Assert.assertTrue(response.get().statusCode()==int1);
		Allure.step(response.get().asString());
	}

	@Then("the response parameter {string} size should be {string} {int}")
	public void response_parameter_size_should_be(String string,String string2,Integer int1) throws JsonMappingException, JsonProcessingException {
		// Write code here that turns the phrase above into concrete actions
		 String responseString = response.get().getBody().asString();

		    ObjectMapper mapper = new ObjectMapper();
		    JsonNode rootNode = mapper.readTree(responseString);
		    JsonNode arrayNode = rootNode.path(string);

		    if (!arrayNode.isArray()) {
		        throw new AssertionError("The response parameter '" + string + "' is not a JSON array.");
		    }

		    int actualSize = arrayNode.size();

		    switch (string2.toLowerCase()) {
		        case "less than":
		            if (actualSize >= int1) {
		                throw new AssertionError("Expected size of '" + string + "' to be less than " + int1 +
		                        " but was " + actualSize);
		            }
		            break;
		        case "greater than":
		            if (actualSize <= int1) {
		                throw new AssertionError("Expected size of '" + string + "' to be greater than " + int1 +
		                        " but was " + actualSize);
		            }
		            break;
		        case "equal to":
		        case "equals":
		            if (actualSize != int1) {
		                throw new AssertionError("Expected size of '" + string + "' to be equal to " + int1 +
		                        " but was " + actualSize);
		            }
		            break;
		        default:
		            throw new IllegalArgumentException("Unsupported comparison operator: " + string2);
		    }
		
	}

	@Then("Response parameter {string} should exists")
	public void response_parameter_should_exists(String string) {
		// Write code here that turns the phrase above into concrete actions
		JsonNode root = null;
		try {
			root = new ObjectMapper().readTree(response.get().body().asString());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	Assert.assertTrue(root.get(string)!=null);
	}

	@Then("Response parameter {string} should be equal to {string}")
	public void response_parameter_should_be_equal_to(String string, String string2) {
		// Write code here that turns the phrase above into concrete actions
		JsonNode root =null;
		try {
			root = new ObjectMapper().readTree(response.get().body().asString());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
      Assert.assertTrue(root.get(string).textValue().equalsIgnoreCase(string2));
	}
}
