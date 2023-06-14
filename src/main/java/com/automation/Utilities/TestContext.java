package com.automation.Utilities;


import com.automation.manager.MobileDriverProviderCreator;
import com.automation.manager.PageObjectManager;
import com.automation.manager.WebDriverManager;
import com.automation.utils.ScenarioContext;



public class TestContext {

    private final WebDriverManager driverManager;
    private final PageObjectManager pageObjectManager;
    private final MobileDriverProviderCreator mobileDriverManager;
    public ScenarioContext scenarioContext;

    public TestContext() {
        driverManager = new WebDriverManager();
        pageObjectManager = new PageObjectManager(driverManager);
		mobileDriverManager = new MobileDriverProviderCreator();
        scenarioContext = new ScenarioContext();
    }

    public WebDriverManager getDriverManager() {
        return driverManager;
    }

    public PageObjectManager getPageObjectManager() {
        return pageObjectManager;
    }

    public ScenarioContext getScenarioContext() {
        return scenarioContext;
    }
    public MobileDriverProviderCreator getmobileDriverManager() {
        return mobileDriverManager;
    }

}
