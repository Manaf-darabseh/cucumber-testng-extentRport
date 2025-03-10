package com.automation.manager;

import com.automation.Utilities.PathManager;
import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DirectoryManager {

    private static final Logger logger = LogManager.getLogger(DirectoryManager.class);

    /**
     * Initializes required test output directories.
     * Uses PathManager to ensure cross-platform compatibility.
     */
    public void initializeTestDirectories() {
        String screenshotPath = PathManager.getScreenshotPath("").replace(".png", "");
        String reportsPath = PathManager.getReportsPath();

        // Create screenshots directory
        logger.debug("Creating screenshots directory: {}", screenshotPath);
        new File(screenshotPath).mkdirs();

        // Create reports directory
        logger.debug("Creating reports directory: {}", reportsPath);
        new File(reportsPath).mkdirs();
    }
}