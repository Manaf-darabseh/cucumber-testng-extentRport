package com.automation.run;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@CucumberOptions(features = {"src/test/resources/features/Login.feature"}
		,glue="com.automation.stepdef"
		,plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
)


public class Main extends AbstractTestNGCucumberTests {
    private final Logger logger = LogManager.getLogger(Main.class);

//    public Main() {
//        WebDriverManager.chromedriver().setup();
//        logger.info("Starting test");
//    }

//    @Override
//    @DataProvider(name = "TestCase" , parallel = true)
//    public Object[][] scenarios() {
//        return super.scenarios();
//    }
    @BeforeSuite
    public void beforeSuite() {
        System.out.println("================ BEFORE SUITE ================");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("================ AFTER SUITE ================");
    }
    
    @Test
    public void startTest() {
    	
    }
}
    
    
