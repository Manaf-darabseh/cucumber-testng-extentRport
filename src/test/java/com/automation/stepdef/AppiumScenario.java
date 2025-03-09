package com.automation.stepdef;

import org.openqa.selenium.By;
import org.testng.Assert;

import com.automation.Pages.BasePage;
import com.automation.Pages.Login_Page;
import com.automation.Utilities.TestContext;
import com.automation.manager.MobileDriverProviderCreator;

import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AppiumScenario {
	TestContext testContext;
	AppiumDriver mobileDriver;
   

    public AppiumScenario(TestContext context) {
        testContext = context;
        mobileDriver = testContext.getMobileDriverManager().getCurrentDriver();
       
    }
	
	@Given("^I open the application$")
	public void i_open_the_application() throws Throwable {
	    Assert.assertTrue(mobileDriver.findElement(By.xpath("//*[@text='Accessibility']")).isDisplayed());   
	}

	@When("^I tap on Accessibility$")
	public void i_tap_on_Accessibility() throws Throwable {
		mobileDriver.findElement(By.xpath("//*[@text='Accessibility']")).click();
	}

	@Then("^I validate Custom View$")
	public void i_validate_Custom_View() throws Throwable {
	  Assert.assertTrue(mobileDriver.findElement(By.xpath("//*[@text='Custom View']")).isDisplayed(), "Custom View is not displayed");
	  
	}
}
