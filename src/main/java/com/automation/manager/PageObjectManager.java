package com.automation.manager;

import com.automation.Pages.BasePage;
import com.automation.Pages.HomePage;
import com.automation.Pages.Login_Page;
import org.openqa.selenium.WebDriver;

/**
 * Manages the lifecycle of Page Objects in the framework.
 * Implements the Page Object Factory pattern to create page instances lazily.
 */
public class PageObjectManager {

    private final WebDriver driver;
    private HomePage homePage;
    private Login_Page loginPage;
    private BasePage basePage;

    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Gets the HomePage instance, creating it if necessary
     * @return HomePage instance
     */
    public HomePage getHomePage() {

        return (homePage == null) ? homePage = new HomePage(driver) : homePage;
    }
    
    /**
     * Gets the LoginPage instance, creating it if necessary
     * @return LoginPage instance
     */
    public Login_Page getLoginPage() {
        return (loginPage == null) ? loginPage = new Login_Page(driver) : loginPage;
    }
    
    /**
     * Gets the BasePage instance, creating it if necessary
     * @return BasePage instance
     */
    public BasePage getBasePage() {

        return (basePage == null) ? basePage = new BasePage(driver) : basePage;
    }


}

