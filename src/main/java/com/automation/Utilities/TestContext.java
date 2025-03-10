package com.automation.Utilities;

import com.automation.manager.DriverManager;
import com.automation.manager.MobileDriverProviderCreator;
import com.automation.manager.PageObjectManager;
import com.automation.utils.ScenarioContext;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.automation.manager.DirectoryManager;

/**
 * @author Manaf Al-Darabseh
 */

/**
 * TestContext class that manages the lifecycle of various managers and contexts
 * used throughout the test execution.
 *
 * This class is responsible for:
 * 1. Managing WebDriver and Mobile driver instances
 * 2. Handling page objects and scenario context
 * 3. Coordinating test resources and cleanup
 *
 * Path management is handled through the PathManager utility to ensure
 * cross-platform compatibility.
 */
public class TestContext {

    private final DriverManager driverManager;
    private final PageObjectManager pageObjectManager;
    private final MobileDriverProviderCreator mobileDriverManager;
    private final ScenarioContext scenarioContext;
    private final DirectoryManager directoryManager;

    public TestContext() {
        // Initialize directory manager
        directoryManager = new DirectoryManager();

        // Initialize test output directories
        directoryManager.initializeTestDirectories();

        // Initialize managers
        driverManager = DriverManager.getInstance();
        pageObjectManager = new PageObjectManager(driverManager.getDriver());
        mobileDriverManager = new MobileDriverProviderCreator();
        scenarioContext = new ScenarioContext();
    }

    public WebDriver getDriver() {
        return driverManager.getDriver();
    }

    public PageObjectManager getPageObjectManager() {
        return pageObjectManager;
    }

    public ScenarioContext getScenarioContext() {
        return scenarioContext;
    }

    public MobileDriverProviderCreator getMobileDriverManager() {
        return mobileDriverManager;
    }

    /**
     * Gets the DriverManager instance
     * @return The DriverManager instance used for WebDriver management
     */
    public DriverManager getDriverManager() {
        return driverManager;
    }

    /**
     * Cleans up resources after test execution.
     * This includes:
     * 1. Closing and quitting WebDriver instances
     * 2. Cleaning up temporary files
     * 3. Resetting context state
     */
    public void tearDown() {
        try {
            // Clean up WebDriver resources
            if (driverManager != null) {
                WebDriver driver = driverManager.getDriver();
                if (driver != null) {
                    driver.quit();
                }
                driverManager.closeDriver();
            }

            // Clear scenario context
            if (scenarioContext != null) {
                scenarioContext.clear();
            }

        } catch (Exception e) {
            System.err.println(String.format(
                "Error during test context cleanup: %s",
                e.getMessage()));
        } finally {
            // Ensure the driver reference is cleared
            if (driverManager != null) {
                driverManager.closeDriver();
            }
        }
    }

}
