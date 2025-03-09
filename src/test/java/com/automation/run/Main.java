package com.automation.run;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.automation.manager.DriverManager;

/**
 * Main test runner class for API tests.
 * This class is responsible for executing all API-related test scenarios.
 * It extends AbstractTestNGCucumberTests to integrate Cucumber with TestNG.
 *
 * Configuration:
 * - Features: Located in src/test/resources/features/
 * - Step Definitions: Located in com.automation.stepdef package
 * - Reports: Generated in target/cucumber-reports/
 * - Tags: Only runs scenarios tagged with @API
 *
 * The class uses the following reporting plugins:
 * 1. ExtentReports - For detailed HTML reports with screenshots
 * 2. Pretty - For readable console output
 * 3. HTML - For basic HTML reports
 * 4. JSON - For raw test execution data
 */
@CucumberOptions(
    // Path to feature files
    features = "src/test/resources/features/",
    
    // Package containing step definitions
    glue = "com.automation.stepdef",
    
    // Reporting plugins configuration
    plugin = {
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",  // ExtentReports integration
        "pretty",  // Readable console output
        "html:target/cucumber-reports/cucumber-pretty",  // HTML reports
        "json:target/cucumber-reports/CucumberTestReport.json"  // JSON reports
    },
    
    // Only run API tests in this class
    tags = "@API",  // Run API tests first as they're faster and don't need browser
    
    // Enable better console output formatting
    monochrome = true
)
public class Main extends AbstractTestNGCucumberTests {
    // Logger instance for this class
    private final Logger logger = LogManager.getLogger(Main.class);
    
    // WebDriver manager instance
    private DriverManager driverManager;

    /**
     * Provides test scenarios to TestNG for execution.
     * Overridden to disable parallel execution and ensure sequential test runs.
     *
     * @return Array of test scenarios to be executed
     */
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    /**
     * Method to be run before the test suite.
     * This method is called once before all tests are run.
     * It is used to set up the test environment.
     */
    /**
     * Setup method that runs before the test suite execution begins.
     * Initializes the WebDriver manager and logs the suite start.
     * This method is marked with @BeforeSuite to ensure it runs once before all tests.
     */
    @BeforeSuite
    public void beforeSuite() {
        logger.info("================ STARTING TEST SUITE ================");
        driverManager = new DriverManager();
    }

    /**
     * Method to be run after the test suite.
     * This method is called once after all tests are run.
     * It is used to clean up the test environment.
     */
    /**
     * Cleanup method that runs after the test suite execution completes.
     * Responsible for cleaning up resources and closing the WebDriver.
     * This method is marked with @AfterSuite to ensure it runs once after all tests.
     * 
     * Important: This ensures that all browser instances are properly closed
     * and system resources are released, even if tests fail.
     */
    @AfterSuite
    public void afterSuite() {
        logger.info("================ CLEANING UP TEST SUITE ================");
        if (driverManager != null) {
            driverManager.closeDriver();
        }
    }
}
    
    
