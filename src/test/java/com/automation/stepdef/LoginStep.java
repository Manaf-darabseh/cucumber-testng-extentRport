package com.automation.stepdef;

import com.automation.Pages.BasePage;
import com.automation.manager.DriverManager;
import org.openqa.selenium.WebDriver;

public class LoginStep extends BasePage {
    private final DriverManager driverManager;

    public LoginStep(DriverManager driverManager) {
        super(driverManager.getDriver());
        this.driverManager = driverManager;
    }

//    @Given("I have URL web page as {string}")
//    public void i_have_url_web_page_as(String string) {
//        // Write code here that turns the phrase above into concrete actions
//    	driver.getDriver().get(string);
//        throw new io.cucumber.java.PendingException();
//    }
    
}
