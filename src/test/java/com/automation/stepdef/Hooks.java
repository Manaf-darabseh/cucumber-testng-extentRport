package com.automation.stepdef;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

import com.automation.Pages.BasePage;
import com.automation.Utilities.TestContext;
import com.automation.manager.FileReaderManager;
import com.automation.manager.WebDriverManager;

public class Hooks extends BasePage{

    TestContext testContext;
    WebDriverManager webDriver;

    public Hooks(WebDriverManager driver , TestContext context) {
        super(driver);
        testContext = context;
    }

    @Before
    public void setUp() {
        webDriver = testContext.getDriverManager();
        webDriver.getDriver().get(FileReaderManager.getInstance().getConfigFileReader().getUrl());
    }

    @After(order=1)
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
}
