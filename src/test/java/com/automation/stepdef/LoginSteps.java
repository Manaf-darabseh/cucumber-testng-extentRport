package com.automation.stepdef;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.testng.Assert;

import com.automation.Pages.BasePage;
import com.automation.Pages.Login_Page;
import com.automation.Utilities.TestContext;

public class LoginSteps {
    TestContext testContext;
    Login_Page loginPage;
    BasePage basePage;

    public LoginSteps(TestContext context) {
        testContext = context;
        loginPage = testContext.getPageObjectManager().getLoginPage();
        basePage = testContext.getPageObjectManager().getbasePage();
    }

    @Given("Verify that Login page is displayed")
    public void LoginPageIsDisplayed() {
//        Assert.assertTrue(loginPage.LoginIsDisplayed());
        Assert.assertTrue(basePage.elementDisplaysSimple("UserName"));
    }

    @When("enter user name {string} and password {string}")
    public void enterUserNameAndPassword(String UserName, String Password) {
//        loginPage.EnterUserName(UserName);
//        loginPage.EnterPassword(Password);
    	basePage.fillTextBox("UserName", UserName);
    	basePage.fillTextBox("Password", Password);
    }

    @And("Click Login button")
    public void clickLoginButton() throws InterruptedException {
    	basePage.waitVisibilityOfWebelement("LoginBtn");
    	basePage.clickBy("LoginBtn");
//        Thread.sleep(5000);
//        loginPage.ClickLoginBtn();
    }
}
