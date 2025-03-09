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
 * WebTests runner class for Web UI tests.
 * This class is responsible for executing all Web-related test scenarios.
 * It extends AbstractTestNGCucumberTests to integrate Cucumber with TestNG.
 *
 * Configuration:
 * - Features: Located in src/test/resources/features/
 * - Step Definitions: Located in com.automation.stepdef package
 * - Reports: Generated in target/cucumber-reports/
 * - Tags: Only runs scenarios tagged with @Web
 *
 * Key Responsibilities:
 * 1. Browser Management: Handles WebDriver lifecycle
 * 2. Test Execution: Runs web UI tests sequentially
 * 3. Resource Cleanup: Ensures proper cleanup of browser instances
 *
 * The class uses the following reporting plugins:
 * 1. ExtentReports - For detailed HTML reports with screenshots
 * 2. Pretty - For readable console output
 * 3. HTML - For basic HTML reports
 * 4. JSON - For raw test execution data (separate from API tests)
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
        "json:target/cucumber-reports/WebTestReport.json"  // JSON reports (separate from API)
    },
    
    // Only run Web UI tests in this class
    tags = "@Web",
    
    // Enable better console output formatting
    monochrome = true
)
public class WebTests extends AbstractTestNGCucumberTests {
    // Logger instance for this class
    private final Logger logger = LogManager.getLogger(WebTests.class);
    
    // WebDriver manager instance for browser control
    private DriverManager driverManager;

    /**
     * Provides test scenarios to TestNG for execution.
     * Overridden to disable parallel execution and ensure sequential test runs.
     * This is important for web tests to prevent multiple browser instances
     * from interfering with each other.
     *
     * @return Array of test scenarios to be executed
     */
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    /**
     * Setup method that runs before the web test suite execution begins.
     * Initializes the WebDriver manager for browser control and logs the suite start.
     * This method is marked with @BeforeSuite to ensure it runs once before all web tests.
     *
     * Note: This is separate from API test initialization to maintain clean separation
     * of concerns between API and Web testing infrastructure.
     */
    @BeforeSuite
    public void beforeSuite() {
        logger.info("================ STARTING WEB TEST SUITE ================");
        driverManager = new DriverManager();
    }

    /**
     * Cleanup method that runs after the web test suite execution completes.
     * Responsible for cleaning up resources and closing any remaining browser windows.
     * This method is marked with @AfterSuite to ensure it runs once after all web tests.
     * 
     * Important: This ensures that all browser instances are properly closed
     * and system resources are released, even if tests fail. This prevents
     * orphaned browser processes from consuming system resources.
     */
    @AfterSuite
    public void afterSuite() {
        logger.info("================ CLEANING UP WEB TEST SUITE ================");
        if (driverManager != null) {
            driverManager.closeDriver();
        }
    }
}
