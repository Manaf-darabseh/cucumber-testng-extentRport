package com.automation.manager;

/**
 * 
 * @author Manaf Al-Darabseh
 */

import com.automation.DataProviders.ConfigFileReader;
import com.automation.Utilities.PathManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Manager class for handling file reading operations across the framework.
 * Uses PathManager for cross-platform path resolution and implements
 * singleton pattern for efficient resource management.
 */
public class FileReaderManager {

    private static final FileReaderManager fileReaderManager = new FileReaderManager();
    private static ConfigFileReader configFileReader;
    private static Properties mobileProperties;
    private static Properties locatorProperties;
    private static Properties extentProperties;
    private static Properties testDataProperties;
    private static Properties loggerProperties;

    private FileReaderManager() {
        // Private constructor for singleton pattern
    }

    /**
     * Gets the singleton instance of FileReaderManager
     * @return The FileReaderManager instance
     */
    public static FileReaderManager getInstance() {
        return fileReaderManager;
    }

    /**
     * Gets the ConfigFileReader instance, creating it if necessary
     * @return The ConfigFileReader instance
     */
    public ConfigFileReader getConfigFileReader() {
        return (configFileReader == null) ? new ConfigFileReader() : configFileReader;
    }

    /**
     * Gets mobile configuration properties
     * @return Properties object containing mobile configuration
     */
    public Properties getMobileProperties() {
        if (mobileProperties == null) {
            mobileProperties = loadProperties("mobile.properties");
        }
        return mobileProperties;
    }

    /**
     * Gets locator configuration properties
     * @return Properties object containing element locators
     */
    public Properties getLocatorProperties() {
        if (locatorProperties == null) {
            locatorProperties = loadProperties("locators.properties");
        }
        return locatorProperties;
    }

    /**
     * Gets extent report configuration properties
     * @return Properties object containing report configuration
     */
    public Properties getExtentProperties() {
        if (extentProperties == null) {
            extentProperties = loadProperties("extent.properties");
        }
        return extentProperties;
    }

    /**
     * Gets test data configuration properties
     * @return Properties object containing test data
     */
    public Properties getTestDataProperties() {
        if (testDataProperties == null) {
            testDataProperties = loadProperties("testdata.properties");
        }
        return testDataProperties;
    }

    /**
     * Gets logger configuration properties
     * @return Properties object containing logger settings
     */
    public Properties getLoggerProperties() {
        if (loggerProperties == null) {
            loggerProperties = loadProperties("log4j2.properties");
        }
        return loggerProperties;
    }

    /**
     * Reloads all properties files
     * This is useful when properties are modified during test execution
     */
    public void reloadAllProperties() {
        configFileReader = new ConfigFileReader();
        mobileProperties = loadProperties("mobile.properties");
        locatorProperties = loadProperties("locators.properties");
        extentProperties = loadProperties("extent.properties");
        testDataProperties = loadProperties("testdata.properties");
        loggerProperties = loadProperties("log4j2.properties");
    }

    /**
     * Gets a property value from any configuration file
     * @param propertyName The name of the property to retrieve
     * @param defaultValue The default value if property is not found
     * @return The property value or default if not found
     */
    public String getProperty(String propertyName, String defaultValue) {
        String value = null;
        
        // Check each properties file in order of priority
        if (configFileReader != null) {
            try {
                // Try to get the property using the specific getter methods first
                if ("url".equals(propertyName)) {
                    return configFileReader.getUrl();
                } else if ("timeout".equals(propertyName)) {
                    return String.valueOf(configFileReader.getTime());
                } else if ("browser".equals(propertyName)) {
                    return configFileReader.getBrowser().toString();
                } else if ("environment".equals(propertyName)) {
                    return configFileReader.getEnvironment().toString();
                } else if ("Headless".equals(propertyName)) {
                    return String.valueOf(configFileReader.isHeadless());
                }
            } catch (RuntimeException e) {
                // If specific getter fails, continue with other property sources
                System.out.println(String.format("Warning: Failed to get property '%s' from ConfigFileReader: %s", 
                    propertyName, e.getMessage()));
            }
        }
        if (value == null && mobileProperties != null) {
            value = mobileProperties.getProperty(propertyName);
        }
        if (value == null && locatorProperties != null) {
            value = locatorProperties.getProperty(propertyName);
        }
        if (value == null && extentProperties != null) {
            value = extentProperties.getProperty(propertyName);
        }
        if (value == null && testDataProperties != null) {
            value = testDataProperties.getProperty(propertyName);
        }
        if (value == null && loggerProperties != null) {
            value = loggerProperties.getProperty(propertyName);
        }
        
        return value != null ? value.trim() : defaultValue;
    }

    /**
     * Gets a property value from any configuration file
     * @param propertyName The name of the property to retrieve
     * @return The property value or null if not found
     */
    public String getProperty(String propertyName) {
        return getProperty(propertyName, null);
    }

    /**
     * Loads properties from a configuration file
     * @param fileName Name of the properties file
     * @return Properties object containing the file contents
     */
    /**
     * Loads properties from a configuration file
     * @param fileName Name of the properties file
     * @return Properties object containing the file contents
     * @throws RuntimeException if file cannot be loaded
     */
    private Properties loadProperties(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Properties file name cannot be null or empty");
        }
        
        Properties properties = new Properties();
        String filePath = PathManager.getConfigPath(fileName);
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println(
                String.format("Warning: Properties file '%s' not found at '%s'. Using empty properties.", 
                    fileName, filePath));
            return properties;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(
                String.format("Failed to load properties file '%s': %s", 
                    fileName, e.getMessage()));
        }

        return properties;
    }
}
