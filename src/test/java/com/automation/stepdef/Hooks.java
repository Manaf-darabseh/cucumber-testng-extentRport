package com.automation.stepdef;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import java.net.MalformedURLException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.automation.Pages.BasePage;
import com.automation.Utilities.TestContext;
import com.automation.manager.FileReaderManager;
import com.automation.manager.MobileDriverProviderCreator;
import com.automation.manager.DriverManager;

/**
 * Hooks class for managing test lifecycle events in the Cucumber framework.
 * 
 * DEPENDENCY INJECTION:
 * This class uses PicoContainer for dependency injection, which is Cucumber's
 * built-in DI container. The injection works as follows:
 * 
 * 1. Constructor Injection:
 *    - TestContext is automatically injected via constructor
 *    - No explicit configuration needed
 *    - PicoContainer manages object lifecycle
 * 
 * 2. Shared State:
 *    - TestContext is shared between step definitions
 *    - Same instance used across the scenario
 *    - Enables data sharing between steps
 * 
 * 3. Lifecycle:
 *    - New instance per scenario
 *    - Automatic cleanup after scenario
 *    - Thread-safe for parallel execution
 *
 
 * This class is responsible for:
 * 1. Setting up test environments before scenarios
 * 2. Cleaning up resources after scenarios
 * 3. Capturing screenshots on test failures
 * 4. Managing different driver types (Web, Mobile)
 * 
 * The class uses dependency injection to receive the TestContext,
 * which provides access to shared test resources and state.
 * 
 * Supported test types:
 * - Web UI tests (tagged with @Web)
 *   - Uses Selenium WebDriver
 *   - Supports Chrome, Firefox, Edge, Safari
 *   - Handles automatic driver management
 *   - Takes screenshots on failure
 * 
 * - Mobile tests (tagged with @mobile)
 *   - Uses Appium for mobile automation
 *   - Supports both Android and iOS
 *   - Handles port allocation for Appium server
 *   - Manages device UUIDs and capabilities
 * 
 * Features:
 * - Automatic WebDriver/Appium initialization
 * - Screenshot capture on test failure
 * - Clean resource cleanup
 * - URL configuration via FileReaderManager
 * - Cross-platform test execution
 * 
 * Configuration:
 * - Web tests: Configure in configuration.properties
 * - Mobile tests: Configure device capabilities in mobile.properties
 * - Screenshots: Automatically attached to test reports
 */
public class Hooks {
    /**
     * TestContext instance for managing shared test state.
     * Injected by PicoContainer to enable:
     * - Sharing state between step definitions
     * - Managing WebDriver lifecycle
     * - Accessing configuration and test data
     * - Coordinating cleanup operations
     */
    private final TestContext testContext;
    
    /**
     * WebDriver manager instance obtained from TestContext.
     * Responsible for:
     * - Browser instance creation and configuration
     * - Driver lifecycle management
     * - Browser cleanup and resource management
     */
    private final DriverManager driverManager;
    
    /**
     * Mobile driver manager for Appium integration.
     * Handles:
     * - Appium server management
     * - Device detection and selection
     * - Mobile driver configuration
     * - Platform-specific capabilities
     */
    private final MobileDriverProviderCreator mobileDriver;

    /**
     * Constructor using dependency injection to receive test context.
     * 
     * @param context TestContext instance containing shared test state and resources
     */
    public Hooks(TestContext context) {
        this.testContext = context;
        this.driverManager = context.getDriverManager();
        this.mobileDriver = context.getMobileDriverManager();
    }

    /**
     * Setup hook for Web UI tests.
     * Runs before each scenario tagged with @Web.
     * This hook prepares the test environment for web UI testing.
     * 
     * Initialization Process:
     * 1. WebDriver Setup
     *    - Creates new driver instance if none exists
     *    - Configures browser options and capabilities
     *    - Sets up timeouts and window size
     * 
     * 2. Environment Configuration
     *    - Reads base URL from configuration
     *    - Supports multiple environments (dev, staging, prod)
     *    - Handles secure and non-secure URLs
     * 
     * 3. Browser Configuration
     *    - Maximizes window for consistent testing
     *    - Sets implicit wait timeout
     *    - Configures JavaScript and cookie settings
     * 
     * Error Handling:
     * - Validates configuration parameters
     * - Provides clear error messages for missing config
     * - Retries driver creation on common startup issues
     * 
     * Best Practices:
     * - Lazy initialization of WebDriver
     * - Environment-aware configuration
     * - Clean session for each test
     * - Proper resource management
     */
    @Before("@Web")
    public void setUp() {
        try {
            // Get or create WebDriver instance
            WebDriver driver = driverManager.getDriver();
            
            // Navigate to the configured base URL
            String baseUrl = FileReaderManager.getInstance()
                .getConfigFileReader()
                .getUrl();
            
            driver.get(baseUrl);
        } catch (Exception e) {
            // Log setup errors with context
            throw new RuntimeException(
                String.format("Failed to initialize web test environment: %s", 
                    e.getMessage()), e);
        }
    }

    /**
     * Setup hook for Mobile tests.
     * Runs before each scenario tagged with @mobile.
     * 
     * This method:
     * 1. Initializes Appium server on dynamic ports
     * 2. Detects and configures connected mobile devices
     * 3. Sets up device capabilities based on platform (iOS/Android)
     * 4. Establishes connection to Appium server
     * 
     * Required Configuration:
     * - Appium server path
     * - Device UDID or emulator name
     * - Platform version
     * - App package and activity (Android)
     * - Bundle ID (iOS)
     * 
     * The configuration is loaded from mobile.properties and can be
     * overridden via environment variables for CI/CD integration.
     * 
     * @throws MalformedURLException if there's an error in Appium URL formation
     * @throws RuntimeException if required mobile device is not found
     */
    @Before("@mobile")
    public void setUpAppium() throws MalformedURLException {
        mobileDriver.initializePortsAndUUIDs();
    }

    /**
     * Cleanup hook for Web UI tests.
     * Runs after each scenario tagged with @Web.
     * This hook ensures proper cleanup of resources and captures failure evidence.
     * 
     * Execution Order:
     * 1. Check scenario status
     * 2. Capture failure evidence if needed
     * 3. Clean up browser resources
     * 4. Reset test context state
     * 
     * Resource Cleanup:
     * - Closes all browser windows
     * - Quits WebDriver instance
     * - Clears thread-local storage
     * - Resets page object references
     * 
     * Failure Handling:
     * - Takes screenshot on test failure
     * - Logs failure details
     * - Preserves error state for reporting
     * 
     * Context Management:
     * - Cleans shared test context
     * - Resets page objects
     * - Clears cached data
     * 
     * Best Practices:
     * - Always runs, even if test fails
     * - Gracefully handles cleanup errors
     * - Prevents resource leaks
     * - Maintains test isolation
     * 
     * @param scenario The current test scenario, containing execution status and details
     */
    @After("@Web")
    public void tearDown(Scenario scenario) {
        try {
            // Capture evidence if test failed
            if (scenario.isFailed()) {
                takeScreenshot(scenario);
            }
            
            // Clean up resources
            testContext.tearDown();
        } catch (Exception e) {
            // Log cleanup errors but don't mask test failures
            System.err.println(String.format(
                "Error during scenario cleanup '%s': %s", 
                scenario.getName(), 
                e.getMessage()));
        }
    }

    /**
     * Utility method to capture and attach screenshots to the test report.
     * This is crucial for debugging test failures by providing visual evidence
     * of the application state at the time of failure.
     * 
     * Screenshot Capture Process:
     * 1. Validates WebDriver screenshot capability
     * 2. Takes full-page screenshot in PNG format
     * 3. Converts to byte array for attachment
     * 4. Attaches to Cucumber scenario report
     * 
     * Screenshot Properties:
     * - Format: PNG (best balance of quality and size)
     * - Scope: Full visible page
     * - Name: Matches scenario name for easy identification
     * - Storage: Embedded in test report
     * 
     * Error Handling:
     * - Gracefully handles driver null cases
     * - Catches and logs screenshot failures
     * - Prevents test interruption on screenshot failure
     * - Provides error details for debugging
     * 
     * Usage in Reports:
     * - Automatically included in Cucumber reports
     * - Viewable in ExtentReports if configured
     * - Accessible in CI/CD pipeline artifacts
     * 
     * Best Practices:
     * - Called only on test failure to minimize overhead
     * - Uses BYTES format to avoid file system operations
     * - Includes scenario name for context
     * 
     * @param scenario The Cucumber scenario context for attaching the screenshot
     */
    private void takeScreenshot(Scenario scenario) {
        try {
            WebDriver driver = driverManager.getDriver();
            if (driver instanceof TakesScreenshot) {
                // Take screenshot as bytes to avoid file I/O
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                
                // Attach to scenario with meaningful name
                scenario.attach(screenshot, "image/png", 
                    String.format("%s - Failure Screenshot", scenario.getName()));
            }
        } catch (Exception e) {
            // Log error but don't fail the test
            System.err.println(String.format(
                "Failed to capture screenshot for scenario '%s': %s", 
                scenario.getName(), 
                e.getMessage()));
        }
    }

    /**
     * Optional ExtentReports integration hook.
     * Uncomment and configure this method to enable detailed HTML reporting.
     * 
     * This method would:
     * 1. Create a new test entry in ExtentReports for each scenario
     * 2. Use the scenario name as the test name for clear reporting
     * 3. Enable detailed logging and reporting capabilities
     * 
     * To use this:
     * 1. Add ExtentReports dependencies to pom.xml
     * 2. Initialize extentReports in @BeforeSuite
     * 3. Configure report settings in extent.properties
     * 4. Flush and close reports in @AfterSuite
     * 
     * Example configuration:
     * <pre>
     * @BeforeMethod
     * public void beforeScenario(Scenario scenario) {
     *     extentTest = extentReports.createTest(scenario.getName());
     * }
     * </pre>
     */

}
