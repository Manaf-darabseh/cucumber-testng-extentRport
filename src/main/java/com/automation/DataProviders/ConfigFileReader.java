package com.automation.DataProviders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.automation.Enums.DriverType;
import com.automation.Enums.EnvironmentType;
import com.automation.Utilities.PathManager;

/**
 * Configuration reader that loads and provides access to configuration properties.
 * This class handles the main configuration file and provides type-safe access
 * to common configuration properties with validation and error handling.
 */
public class ConfigFileReader {

    private final Properties properties;
    private final String configPath;
    
    // Default values
    private static final long DEFAULT_TIMEOUT = 30;
    private static final String DEFAULT_BROWSER = "chrome";
    private static final String DEFAULT_ENVIRONMENT = "local";
    private static final String DEFAULT_HEADLESS = "false";

    /**
     * Initializes the configuration reader by loading properties from the configuration file.
     * Uses PathManager to resolve the file path in a cross-platform manner.
     * 
     * @throws RuntimeException if the configuration file cannot be loaded
     */
    /**
     * Validates a URL string
     * @param url The URL to validate
     * @throws RuntimeException if URL is invalid
     */
    private void validateUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new RuntimeException("URL cannot be null or empty");
        }
        
        url = url.trim().toLowerCase();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new RuntimeException(
                String.format("Invalid URL: '%s'. URL must start with http:// or https://", url));
        }
        
        // Basic URL format validation
        if (!url.matches("^https?://[\\w.-]+(:\\d+)?(/.*)?$")) {
            throw new RuntimeException(
                String.format("Invalid URL format: '%s'", url));
        }
    }
    
    /**
     * Validates a timeout value
     * @param timeout The timeout value to validate
     * @throws RuntimeException if timeout is invalid
     */
    private void validateTimeout(long timeout) {
        if (timeout <= 0) {
            throw new RuntimeException(
                String.format("Invalid timeout value: %d. Must be greater than 0", timeout));
        }
        if (timeout > 300) { // 5 minutes max timeout
            throw new RuntimeException(
                String.format("Invalid timeout value: %d. Must not exceed 300 seconds", timeout));
        }
    }
    
    /**
     * Validates a browser type string
     * @param browser The browser type to validate
     * @throws RuntimeException if browser type is invalid
     */
    private void validateBrowser(String browser) {
        if (browser == null || browser.trim().isEmpty()) {
            throw new RuntimeException("Browser type cannot be null or empty");
        }
        
        String validBrowsers = String.join(", ", "chrome", "firefox", "edge", "safari");
        if (!validBrowsers.contains(browser.trim().toLowerCase())) {
            throw new RuntimeException(
                String.format("Invalid browser type: '%s'. Must be one of: %s", 
                    browser, validBrowsers));
        }
    }
    
    /**
     * Validates an environment type string
     * @param environment The environment type to validate
     * @throws RuntimeException if environment type is invalid
     */
    private void validateEnvironment(String environment) {
        if (environment == null || environment.trim().isEmpty()) {
            throw new RuntimeException("Environment type cannot be null or empty");
        }
        
        String validEnvironments = String.join(", ", "local", "remote", "mobile", "api");
        if (!validEnvironments.contains(environment.trim().toLowerCase())) {
            throw new RuntimeException(
                String.format("Invalid environment type: '%s'. Must be one of: %s", 
                    environment, validEnvironments));
        }
    }
    
    public ConfigFileReader() {
        properties = new Properties();
        this.configPath = PathManager.getConfigPath("configuration.properties");
        File configFile = new File(this.configPath);
        
        // Validate file existence and permissions
        if (!configFile.exists()) {
            throw new RuntimeException(
                String.format("Configuration file not found at '%s'", configPath));
        }
        
        if (!configFile.canRead()) {
            throw new RuntimeException(
                String.format("Cannot read configuration file at '%s'. Check file permissions.", configPath));
        }
        
        try (FileInputStream fis = new FileInputStream(configFile)) {
            properties.load(fis);
            
            // Validate critical properties immediately
            validateUrl(getUrl());
            validateTimeout(getTime());
            validateBrowser(properties.getProperty("browser", DEFAULT_BROWSER));
            validateEnvironment(properties.getProperty("environment", DEFAULT_ENVIRONMENT));
        } catch (IOException e) {
            throw new RuntimeException(
                String.format("Failed to load configuration.properties from '%s': %s", 
                    configPath, e.getMessage()));
        }
    }

    /**
     * Gets the application URL from configuration
     * @return The configured URL
     * @throws RuntimeException if URL is not specified
     */
    /**
     * Gets the base URL for the application under test
     * @return The configured URL
     * @throws RuntimeException if URL is not specified or invalid
     */
    public String getUrl() {
        String url = properties.getProperty("url");
        if (url == null || url.trim().isEmpty()) {
            throw new RuntimeException(
                String.format("URL not specified in configuration file: %s", configPath));
        }
        
        url = url.trim();
        validateUrl(url);
        return url;
    }

    /**
     * Gets the timeout value from configuration
     * @return The configured timeout in milliseconds
     * @throws RuntimeException if timeout is not specified or invalid
     */
    /**
     * Gets the timeout value in seconds
     * @return The configured timeout, or default if not specified
     * @throws RuntimeException if timeout value is invalid
     */
    public long getTime() {
        String timeout = properties.getProperty("timeout");
        if (timeout == null || timeout.trim().isEmpty()) {
            System.out.println(
                String.format("Warning: Timeout not specified in %s. Using default: %d seconds", 
                    configPath, DEFAULT_TIMEOUT));
            return DEFAULT_TIMEOUT;
        }
        
        try {
            long timeoutValue = Long.parseLong(timeout.trim());
            validateTimeout(timeoutValue);
            return timeoutValue;
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                String.format("Invalid timeout value in %s: '%s'. Must be a valid number", 
                    configPath, timeout));
        } catch (RuntimeException e) {
            // Re-throw validation exceptions
            throw e;
        }
    }

    /**
     * Gets the browser type from configuration
     * @return The configured DriverType
     * @throws RuntimeException if browser type is not specified or invalid
     */
    /**
     * Gets the browser type to use for testing
     * @return The configured DriverType, or default if not specified
     * @throws RuntimeException if browser type is invalid
     */
    public DriverType getBrowser() {
        String browserName = properties.getProperty("browser");
        if (browserName == null || browserName.trim().isEmpty()) {
            System.out.println(
                String.format("Warning: Browser type not specified in %s. Using default: %s", 
                    configPath, DEFAULT_BROWSER));
            browserName = DEFAULT_BROWSER;
        }
        
        browserName = browserName.trim().toLowerCase();
        validateBrowser(browserName);
        
        switch (browserName) {
            case "chrome":
                return DriverType.CHROME;
            case "firefox":
                return DriverType.FIREFOX;
            case "edge":
                return DriverType.EDGE;
            case "safari":
                return DriverType.SAFARI;
            default:
                // This should never happen due to validation
                throw new RuntimeException(
                    String.format("Unsupported browser type '%s' in %s", browserName, configPath));
        }
    }

    /**
     * Gets the environment type from configuration
     * @return The configured EnvironmentType
     * @throws RuntimeException if environment type is not specified or invalid
     */
    /**
     * Gets the environment type to use for testing
     * @return The configured EnvironmentType, or default if not specified
     * @throws RuntimeException if environment type is invalid
     */
    public EnvironmentType getEnvironment() {
        String environmentName = properties.getProperty("environment");
        if (environmentName == null || environmentName.trim().isEmpty()) {
            System.out.println(
                String.format("Warning: Environment type not specified in %s. Using default: %s", 
                    configPath, DEFAULT_ENVIRONMENT));
            environmentName = DEFAULT_ENVIRONMENT;
        }
        
        environmentName = environmentName.trim().toLowerCase();
        validateEnvironment(environmentName);
        
        switch (environmentName) {
            case "local":
                return EnvironmentType.LOCAL;
            case "remote":
                return EnvironmentType.REMOTE;
            case "mobile":
                return EnvironmentType.Mobile;
            case "api":
                return EnvironmentType.API;
            default:
                // This should never happen due to validation
                throw new RuntimeException(
                    String.format("Unsupported environment type '%s' in %s", 
                        environmentName, configPath));
        }
    }

    /**
     * Checks if headless mode is enabled in configuration
     * @return "true" if headless mode is enabled, "false" otherwise
     */
    /**
     * Checks if headless mode is enabled
     * @return true if headless mode is enabled, false otherwise
     */
    public boolean isHeadless() {
        String headless = properties.getProperty("Headless", DEFAULT_HEADLESS);
        if (headless == null) {
            return Boolean.parseBoolean(DEFAULT_HEADLESS);
        }
        
        headless = headless.trim().toLowerCase();
        if (!headless.equals("true") && !headless.equals("false")) {
            throw new RuntimeException(
                String.format("Invalid headless value in %s: '%s'. Must be 'true' or 'false'", 
                    configPath, headless));
        }
        
        return Boolean.parseBoolean(headless);
    }
    
    /**
     * Gets a property value by name
     * @param propertyName The name of the property
     * @param defaultValue Default value if property is not found
     * @return The property value or default if not found
     */
    public String getProperty(String propertyName, String defaultValue) {
        if (propertyName == null || propertyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Property name cannot be null or empty");
        }
        
        String value = properties.getProperty(propertyName);
        return value != null ? value.trim() : defaultValue;
    }
    
    /**
     * Gets a property value by name
     * @param propertyName The name of the property
     * @return The property value or null if not found
     */
    public String getProperty(String propertyName) {
        return getProperty(propertyName, null);
    }
}
