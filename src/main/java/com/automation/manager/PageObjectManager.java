package com.automation.manager;

/**
 *
 * @author Manaf Al-Darabseh
 */

import com.automation.Pages.BasePage;
import com.automation.Pages.HomePage;
import com.automation.Pages.Login_Page;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the lifecycle of Page Objects in the framework.
 * Implements the Page Object Factory pattern to create page instances lazily.
 */
public class PageObjectManager {

    private final WebDriver driver;
    private final Map<Class<?>, Object> pageObjects = new HashMap<>();

    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Gets the HomePage instance, creating it if necessary
     * @return HomePage instance
     */
    public HomePage getHomePage() {
        return getPage(HomePage.class);
    }

    /**
     * Gets the LoginPage instance, creating it if necessary
     * @return LoginPage instance
     */
    public Login_Page getLoginPage() {
        return getPage(Login_Page.class);
    }

    /**
     * Gets the BasePage instance, creating it if necessary
     * @return BasePage instance
     */
    public BasePage getBasePage() {
        return getPage(BasePage.class);
    }

    /**
     * Gets the page object of the specified class, creating it if necessary.
     * @param pageClass The class of the page object to get.
     * @param <T> The type of the page object.
     * @return The page object of the specified class.
     */
    public <T> T getPage(Class<T> pageClass) {
        return (T) pageObjects.computeIfAbsent(pageClass,
                clazz -> {
                    try {
                        return clazz.getDeclaredConstructor(WebDriver.class).newInstance(driver);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to create page object: " + clazz.getName(), e);
                    }
                });
    }
}
