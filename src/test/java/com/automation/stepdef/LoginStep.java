package com.automation.stepdef;

import com.automation.Pages.BasePage;
import com.automation.manager.WebDriverManager;

public class LoginStep extends BasePage {
	WebDriverManager driver = new WebDriverManager();

    public LoginStep(WebDriverManager driver) {
        super(driver);
    }

//    @Given("I have URL web page as {string}")
//    public void i_have_url_web_page_as(String string) {
//        // Write code here that turns the phrase above into concrete actions
//    	driver.getDriver().get(string);
//        throw new io.cucumber.java.PendingException();
//    }
    
}
