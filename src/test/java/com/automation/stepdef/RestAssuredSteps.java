package com.automation.stepdef;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestAssuredSteps {

	private static final String BASE_URL = "https://reqres.in/api/users";

	private static Response response;
	private static int StatusCode;

	@Given("Get the List of Users")
	public void getListUser() {

		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();

		request.header("Content-Type", "application/json");

		response = request.get("?page=2");

	}

	@When("get the Status code")
	public void getstatuscode() {
		StatusCode = response.getStatusCode();
	}

	@Then("the Status code Should Be {int}")
	public void the_status_code_should_be(Integer int1) {
		Assert.assertTrue(StatusCode == int1);
//		 throw new io.cucumber.java.PendingException();
	}

	@When("get the response")
	public void getresponse() {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();

		request.header("Content-Type", "application/json");

		response = request.get("?page=2");
	}

	@Then("the employee with ID : {int} Should be {string}")
	public void the_employee_with_id_should_be(Integer int1, String string) {
		String jsonString = response.asString();

		JSONObject obj = new JSONObject(jsonString);
		JSONArray data = obj.getJSONArray("data");

		for (int i = 0; i < data.length(); i++) {
			if (data.getJSONObject(i).get("id").equals(int1)) {
				Assert.assertTrue(data.getJSONObject(i).get("first_name").equals(string));
			}
		}
	}

}
