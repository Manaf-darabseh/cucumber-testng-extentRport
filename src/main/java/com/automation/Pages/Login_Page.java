package com.automation.Pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import com.automation.manager.WebDriverManager;

public class Login_Page {
	
    public Login_Page(WebDriverManager webDriver) {
        PageFactory.initElements(new AjaxElementLocatorFactory(webDriver.getDriver(), 15), this);
    }

    @FindBy(id = "//input[@id='user-name']")
    private WebElement UserName;

    @FindBy(id = "//input[@id='password']")
    private WebElement Password;

    @FindBy(id = "//input[@id='login-button']")
    private WebElement LoginBtn;

    public boolean LoginIsDisplayed() {
        UserName.isDisplayed();
        Password.isDisplayed();
        return true;
    }

    public void EnterUserName(String Username){
        UserName.sendKeys(Username);
    }

    public void EnterPassword(String PW){
        Password.sendKeys(PW);
    }

    public void ClickLoginBtn(){
        LoginBtn.click();
    }
}
