package com.automation.stepdef;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.processors.syntax.SyntaxValidator;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestAssuredSteps {

	private static final String BASE_URL = "https://reqres.in/api/users";

	private static String token;
	private static Response response;
	private static int StatusCode;

	@Given("Get the List of Users")
	public void getListUser() {

		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();

		request.header("Content-Type", "application/json");

		response = request.get("?page=2");

//		String jsonString = response.asString();
//		token = JsonPath.from(jsonString).get("data").toString();
//
//		JSONObject obj = new JSONObject(jsonString);
//		JSONArray data = obj.getJSONArray("data");
//
//		for (int i = 0; i < data.length(); i++) {
//			if (data.getJSONObject(i).get("id").equals(10)) {
//				Assert.assertTrue(data.getJSONObject(i).get("first_name").equals("Byron"));
//			}
//		}

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

		Assert.assertFalse(response.toString().isEmpty());
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

	@Given("creat new user")
	public void creatUser() {

		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();

		request.header("Content-Type", "application/json");

		String ReqestBody = "{\"name\":\"Bryant\",\"job\":\"BA\"}";

		response = request.body(ReqestBody).post();

		String jsonString = response.asString();
		token = JsonPath.from(jsonString).get("token");

		StatusCode = response.getStatusCode();

	}

	@Then("Verify if the ID is generate")
	public void checkTheJson() {

		String jsonString = response.asString();

		JSONObject obj = new JSONObject(jsonString);
		Object data = obj.get("id");

		Assert.assertFalse(data.toString().isEmpty());

	}

	@Then("Verify the response json scheme")
	public void checkTheJsonscheme() {

		String jsonString = response.asString();

		JSONObject obj = new JSONObject(jsonString);

		Assert.assertFalse(obj.isNull("id"));
	}

}
