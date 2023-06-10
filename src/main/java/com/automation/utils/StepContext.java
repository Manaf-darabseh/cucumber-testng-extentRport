package com.automation.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.Map;

public class StepContext {
    private final WebDriver driver;
    private final Map<String, Object> contextList = new HashMap<>();

    public StepContext() {
        this.driver = new ChromeDriver();
    }

    public Object getContext(String key) {
        return contextList.get(key);
    }

    public void setContext(String key, Object data) {
        contextList.put(key, data);
    }

    public Boolean isContains(String key) {
        return contextList.containsKey(key);
    }

    public WebDriver getDriver() {
        return driver;
    }
}
