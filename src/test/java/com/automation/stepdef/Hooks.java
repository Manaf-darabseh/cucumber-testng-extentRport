package com.automation.stepdef;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;


import java.net.MalformedURLException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.automation.Pages.BasePage;
import com.automation.Utilities.TestContext;
import com.automation.manager.FileReaderManager;
import com.automation.manager.MobileDriverProviderCreator;
import com.automation.manager.WebDriverManager;
import com.automation.utils.UtilProperties;
import org.testng.annotations.BeforeMethod;

import javax.management.Query;

public class Hooks extends BasePage{

    TestContext testContext;
    WebDriverManager webDriver;
    MobileDriverProviderCreator mobileDriver;

    public Hooks(WebDriverManager driver , TestContext context) {
        super(driver);
        testContext = context;
    }

    @Before("@web")
    public void setUp() {
        webDriver = testContext.getDriverManager();
        webDriver.getDriver().get(FileReaderManager.getInstance().getConfigFileReader().getUrl());
    }

    @Before("@mobile")
    public void setUpAppium() throws MalformedURLException
    {

        mobileDriver = testContext.getmobileDriverManager();
        mobileDriver.initializePortsAndUUIDs();
        //	mobileDriver.getCurrentDriver();
        mobileDriver = testContext.getmobileDriverManager();
    }

    @After("@web")
    public void tearDown(Scenario scenario) {

        testContext.getDriverManager().closeDriver();
    }

	@After(order = 2) // Cucumber After Hook with order 1
	public void takeScreenShotOnFailedScenario(Scenario scenario) {

		System.out.println("Taking screenshot from Cucumber After hook with order=2 if the scenario fails");
		if ((scenario.isFailed())) {
			final byte[] screenshot = ((TakesScreenshot) webDriver.getDriver()).getScreenshotAs(OutputType.BYTES);
			scenario.attach(screenshot, "image/png", scenario.getName());
		}
	}

//	    @BeforeMethod
//	    public void beforeScenario(Scenario scenario) {
//	        // Create a new Extent Test with the scenario name
//	        extentTest = extentReports.createTest(scenario.getName());
//	    }

	    
	    	

}
