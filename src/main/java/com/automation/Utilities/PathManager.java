package com.automation.Utilities;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for managing file paths across different operating systems.
 * This class provides methods to handle path-related operations in a platform-independent way.
 */
public class PathManager {
    // System properties
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static final String PROJECT_ROOT = System.getProperty("user.dir");
    private static final String USER_HOME = System.getProperty("user.home");
    
    // Common directory names
    private static final String TEST_RESOURCES = "src/test/resources";
    private static final String MAIN_RESOURCES = "src/main/resources";
    private static final String TEST_OUTPUT = "test-output";
    private static final String CONFIGURATIONS = "Configurations";
    private static final String FEATURES = "features";
    private static final String DRIVERS = "drivers";
    private static final String DOWNLOADS = "downloads";
    
    // File extensions
    private static final String PROPERTIES_EXT = ".properties";
    private static final String FEATURE_EXT = ".feature";
    private static final String LOG_EXT = ".log";
    private static final String HTML_EXT = ".html";
    
    /**
     * Checks if the current operating system is Windows
     * @return true if running on Windows, false otherwise
     */
    public static boolean isWindows() {
        return OS_NAME.contains("win");
    }
    
    /**
     * Checks if the current operating system is Mac OS
     * @return true if running on Mac OS, false otherwise
     */
    public static boolean isMac() {
        return OS_NAME.contains("mac");
    }
    
    /**
     * Checks if the current operating system is Linux/Unix
     * @return true if running on Linux/Unix, false otherwise
     */
    public static boolean isUnix() {
        return OS_NAME.contains("nix") || OS_NAME.contains("nux") || OS_NAME.contains("aix");
    }
    
    /**
     * Converts a path to the correct format for the current operating system
     * @param path The path to convert
     * @return The converted path string
     */
    /**
     * Converts a path to the correct format for the current operating system
     * @param path The path to convert
     * @return The converted path string
     */
    public static String getOsPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }
        return path.replace("/", File.separator).replace("\\", File.separator);
    }
    
    /**
     * Gets the absolute path for a resource relative to the project root
     * @param relativePath The path relative to the project root
     * @return The absolute path as a string
     */
    /**
     * Gets the absolute path for a resource relative to the project root
     * @param relativePath The path relative to the project root
     * @return The absolute path as a string
     */
    public static String getAbsolutePath(String relativePath) {
        if (relativePath == null || relativePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Relative path cannot be null or empty");
        }
        Path path = Paths.get(PROJECT_ROOT, relativePath.split("/"));
        return path.normalize().toString();
    }
    
    /**
     * Gets the path to a test resource file
     * @param resourceName The name of the resource file
     * @return The absolute path to the resource file
     */
    public static String getTestResourcePath(String resourceName) {
        return getAbsolutePath(combinePath(TEST_RESOURCES, resourceName));
    }
    
    /**
     * Gets the path to a main resource file
     * @param resourceName The name of the resource file
     * @return The absolute path to the resource file
     */
    public static String getMainResourcePath(String resourceName) {
        return getAbsolutePath(combinePath(MAIN_RESOURCES, resourceName));
    }
    
    /**
     * Gets the path to a log file
     * @param logFileName The name of the log file without extension
     * @return The absolute path to the log file
     */
    public static String getLogPath(String logFileName) {
        String logsDir = getAbsolutePath(combinePath(TEST_OUTPUT, "logs"));
        new File(logsDir).mkdirs();
        return Paths.get(logsDir, logFileName + LOG_EXT).toString();
    }
    
    /**
     * Gets the path to a download directory
     * @return The absolute path to the download directory
     */
    public static String getDownloadPath() {
        String downloadDir = getAbsolutePath(DOWNLOADS);
        new File(downloadDir).mkdirs();
        return downloadDir;
    }
    
    /**
     * Gets the path to the user's home directory
     * @return The absolute path to the user's home directory
     */
    public static String getUserHomePath() {
        return USER_HOME;
    }
    
    /**
     * Creates a directory if it doesn't exist
     * @param directoryPath The path to the directory to create
     * @return true if directory exists or was created, false otherwise
     */
    public static boolean ensureDirectoryExists(String directoryPath) {
        File directory = new File(directoryPath);
        return directory.exists() || directory.mkdirs();
    }
    
    /**
     * Gets a temporary file path in the test output directory
     * @param prefix The prefix for the temporary file
     * @param suffix The suffix/extension for the temporary file
     * @return The absolute path to the temporary file
     */
    public static String getTempFilePath(String prefix, String suffix) {
        String tempDir = getAbsolutePath(combinePath(TEST_OUTPUT, "temp"));
        ensureDirectoryExists(tempDir);
        return Paths.get(tempDir, prefix + "-" + System.currentTimeMillis() + suffix).toString();
    }
    
    /**
     * Gets the path to a configuration file
     * @param configFileName The name of the configuration file
     * @return The absolute path to the configuration file
     */
    public static String getConfigPath(String configFileName) {
        return getAbsolutePath("src/test/resources/Configurations/" + configFileName);
    }
    
    /**
     * Gets the path to a feature file
     * @param featureFileName The name of the feature file
     * @return The absolute path to the feature file
     */
    public static String getFeaturePath(String featureFileName) {
        return getAbsolutePath("src/test/resources/features/" + featureFileName);
    }
    
    /**
     * Gets the path to a test data file
     * @param dataFileName The name of the test data file
     * @return The absolute path to the test data file
     */
    public static String getTestDataPath(String dataFileName) {
        return getAbsolutePath("src/test/resources/testdata/" + dataFileName);
    }
    
    /**
     * Gets the path to a driver executable
     * @param driverFileName The name of the driver file
     * @return The absolute path to the driver executable
     */
    public static String getDriverPath(String driverFileName) {
        return getAbsolutePath("src/test/resources/drivers/" + driverFileName);
    }
    
    /**
     * Gets the path to a screenshot file
     * @param screenshotName The name of the screenshot file
     * @return The absolute path where the screenshot should be saved
     */
    public static String getScreenshotPath(String screenshotName) {
        String screenshotsDir = getAbsolutePath("test-output/screenshots");
        new File(screenshotsDir).mkdirs(); // Ensure directory exists
        return Paths.get(screenshotsDir, screenshotName + ".png").toString();
    }
    
    /**
     * Gets the path to the reports directory
     * @return The absolute path to the reports directory
     */
    public static String getReportsPath() {
        String reportsDir = getAbsolutePath("test-output/reports");
        new File(reportsDir).mkdirs(); // Ensure directory exists
        return reportsDir;
    }
    
    /**
     * Gets the path separator for the current operating system
     * @return The system-specific path separator
     */
    public static String getPathSeparator() {
        return File.separator;
    }
    
    /**
     * Combines multiple path elements into a single path
     * @param elements The path elements to combine
     * @return The combined path as a string
     */
    public static String combinePath(String... elements) {
        return String.join(File.separator, elements);
    }
}
