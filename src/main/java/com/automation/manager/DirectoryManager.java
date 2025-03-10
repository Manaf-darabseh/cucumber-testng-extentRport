package com.automation.manager;

import com.automation.Utilities.PathManager;
import java.io.File;

public class DirectoryManager {

    /**
     * Initializes required test output directories.
     * Uses PathManager to ensure cross-platform compatibility.
     */
    public void initializeTestDirectories() {
        // Create screenshots directory
        new File(PathManager.getScreenshotPath("").replace(".png", "")).mkdirs();

        // Create reports directory
        new File(PathManager.getReportsPath()).mkdirs();
    }
}