package com.automation.stepdef;

import io.cucumber.java.en.*;
import org.testng.Assert;

import com.automation.Pages.Login_Page;
import com.automation.Utilities.TestContext;
import com.automation.manager.FileReaderManager;

/**
 * Step definitions for Login feature
 */
public class LoginSteps {
    private final TestContext testContext;
    private final Login_Page loginPage;

    public LoginSteps(TestContext context) {
        this.testContext = context;
        this.loginPage = testContext.getPageObjectManager().getLoginPage();
    }

    @Given("I am on the login page")
    public void navigateToLoginPage() {
        loginPage.go(FileReaderManager.getInstance().getConfigFileReader().getUrl());
    }

    @And("the login form is displayed")
    public void verifyLoginFormDisplayed() {
        Assert.assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed");
    }

    @When("I enter username {string}")
    public void enterUsername(String username) {
        loginPage.enterUsername(username);
    }

    @And("I enter password {string}")
    public void enterPassword(String password) {
        loginPage.enterPassword(password);
    }

    @And("I click the login button")
    public void clickLoginButton() {
        loginPage.clickLoginButton();
    }

    @Then("I should be logged in successfully")
    public void verifySuccessfulLogin() {
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.isEmpty(), 
            "Expected successful login but got error: " + errorMessage);
    }

    @Then("I should see the error message {string}")
    public void verifyErrorMessage(String expectedError) {
        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, expectedError, 
            "Expected error message does not match actual");
    }

    @Then("the password should be masked")
    public void verifyPasswordMasked() {
        Assert.assertTrue(loginPage.isPasswordMasked(), 
            "Password field should be masked");
    }

    @Then("the login page should load within {int} seconds")
    public void verifyPageLoadTime(int seconds) {
        Assert.assertTrue(loginPage.verifyPageLoadTime(seconds), 
            "Login page should load within " + seconds + " seconds");
    }

    @And("all login elements should be interactive")
    public void verifyElementsInteractive() {
        Assert.assertTrue(loginPage.areAllElementsInteractive(), 
            "All login elements should be interactive");
    }
}
