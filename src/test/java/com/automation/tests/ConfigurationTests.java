package com.automation.tests;

import com.automation.Enums.DriverType;
import com.automation.Enums.EnvironmentType;
import com.automation.Utilities.PathManager;
import com.automation.manager.FileReaderManager;
import com.automation.DataProviders.ConfigFileReader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigurationTests {
    private String testConfigPath;
    private Properties testProperties;
    
    @BeforeClass
    public void setup() throws IOException {
        // Create a test configuration file
        testConfigPath = PathManager.getConfigPath("test-configuration.properties");
        testProperties = new Properties();
        testProperties.setProperty("url", "https://www.example.com");
        testProperties.setProperty("timeout", "30");
        testProperties.setProperty("browser", "chrome");
        testProperties.setProperty("environment", "local");
        testProperties.setProperty("Headless", "false");
        
        try (FileWriter writer = new FileWriter(testConfigPath)) {
            testProperties.store(writer, "Test Configuration");
        }
    }
    
    @Test
    public void testPathManagerBasics() {
        // Test OS detection
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        Assert.assertEquals(PathManager.isWindows(), isWindows, "Windows detection failed");
        
        // Test path conversion
        String testPath = "path/to/file";
        String osPath = PathManager.getOsPath(testPath);
        Assert.assertTrue(
            osPath.contains(File.separator),
            "Path not converted to OS-specific format"
        );
        
        // Test config path resolution
        String configPath = PathManager.getConfigPath("config.properties");
        Assert.assertTrue(
            configPath.endsWith("config.properties"),
            "Config path resolution failed"
        );
        
        // Test directory creation
        String testDir = PathManager.getAbsolutePath("test-output/test-dir");
        boolean created = PathManager.ensureDirectoryExists(testDir);
        Assert.assertTrue(created, "Directory creation failed");
        Assert.assertTrue(new File(testDir).exists(), "Directory does not exist");
    }
    
    @Test
    public void testFileReaderManager() {
        FileReaderManager manager = FileReaderManager.getInstance();
        
        // Test singleton pattern
        Assert.assertEquals(
            manager, FileReaderManager.getInstance(),
            "Singleton pattern failed"
        );
        
        // Test property loading
        Properties mobileProps = manager.getMobileProperties();
        Assert.assertNotNull(mobileProps, "Mobile properties should not be null");
        
        // Test property retrieval with default
        String testProp = manager.getProperty("test.property", "default");
        Assert.assertEquals(testProp, "default", "Default property value not returned");
    }
    
    @Test
    public void testConfigFileReader() {
        ConfigFileReader reader = new ConfigFileReader();
        
        // Test URL validation
        String url = reader.getUrl();
        Assert.assertTrue(
            url.startsWith("http"),
            "URL validation failed"
        );
        
        // Test timeout parsing
        long timeout = reader.getTime();
        Assert.assertTrue(timeout > 0, "Invalid timeout value");
        
        // Test browser type parsing
        DriverType browser = reader.getBrowser();
        Assert.assertNotNull(browser, "Browser type is null");
        
        // Test environment type parsing
        EnvironmentType env = reader.getEnvironment();
        Assert.assertNotNull(env, "Environment type is null");
        
        // Test headless mode
        boolean headless = reader.isHeadless();
        Assert.assertFalse(headless, "Headless mode should be false by default");
    }
    
    @Test(expectedExceptions = RuntimeException.class)
    public void testInvalidUrl() throws IOException {
        // Create config with invalid URL
        Properties invalidProps = new Properties();
        invalidProps.setProperty("url", "invalid-url");
        invalidProps.setProperty("timeout", "30");
        invalidProps.setProperty("browser", "chrome");
        invalidProps.setProperty("environment", "local");
        invalidProps.setProperty("Headless", "false");
        
        String invalidConfigPath = PathManager.getConfigPath("configuration.properties");
        try (FileWriter writer = new FileWriter(invalidConfigPath)) {
            invalidProps.store(writer, "Invalid Configuration");
        }
        
        // This should throw an exception
        new ConfigFileReader();
    }
    
    @Test(expectedExceptions = RuntimeException.class)
    public void testInvalidTimeout() throws IOException {
        // Create config with invalid timeout
        Properties invalidProps = new Properties();
        invalidProps.setProperty("url", "https://www.example.com");
        invalidProps.setProperty("timeout", "-1");
        invalidProps.setProperty("browser", "chrome");
        invalidProps.setProperty("environment", "local");
        invalidProps.setProperty("Headless", "false");
        
        String invalidConfigPath = PathManager.getConfigPath("configuration.properties");
        try (FileWriter writer = new FileWriter(invalidConfigPath)) {
            invalidProps.store(writer, "Invalid Configuration");
        }
        
        // This should throw an exception
        new ConfigFileReader();
    }
}
