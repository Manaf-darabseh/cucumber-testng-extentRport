package com.automation.manager;

import com.automation.Pages.BasePage;
import com.automation.Pages.HomePage;
import com.automation.Pages.Login_Page;


public class PageObjectManager {

    private final WebDriverManager webDriver;



    private HomePage homePage;
    private Login_Page loginPage;
    private  BasePage basePage ;



    public PageObjectManager(WebDriverManager webDriver) {
        this.webDriver = webDriver;
    }

    //Short Hand If...Else




    public HomePage getHomePage() {
        return (homePage == null) ? homePage = new HomePage(webDriver) : homePage;
    }
    
    public Login_Page getLoginPage() {
		return (loginPage == null) ? loginPage = new Login_Page(webDriver) : loginPage;
    }
    
    public BasePage getbasePage() {
		return (basePage == null) ? basePage = new BasePage(webDriver) : basePage;
    }


}

