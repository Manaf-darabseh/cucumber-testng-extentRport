package com.automation.manager;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.Enums.DriverType;
import com.automation.Enums.EnvironmentType;

public class WebDriverManager   {
	private WebDriver driver;
	private static DriverType driverType;
	private static EnvironmentType environmentType;
	private static final String CHROME_DRIVER_PROPERTY = "webdriver.chrome.driver";
	
	
	

	public WebDriverManager() {
		driverType = FileReaderManager.getInstance().getConfigFileReader().getBrowser();
		environmentType = FileReaderManager.getInstance().getConfigFileReader().getEnvironment();
	}

	public WebDriver getDriver() {
		if(driver == null) driver = createDriver();
		return driver;
	}


	private WebDriver createDriver() {
		   switch (environmentType) {	    
	        case LOCAL : driver = createLocalDriver();
	        	break;
	        case REMOTE : driver = createRemoteDriver();
	        	break;
		   }
		   return driver;
	}

	private WebDriver createRemoteDriver() {
		throw new RuntimeException("RemoteWebDriver is not yet implemented");
	}

	private WebDriver createLocalDriver() {
        switch (driverType) {	    
        case FIREFOX : driver = new FirefoxDriver();
	    	break;
        case CHROME : 
        	System.setProperty(CHROME_DRIVER_PROPERTY , System.getProperty("user.dir") + "\\webdrivers\\chrome\\chromedriver.exe");
        	driver =  createChromeDriver();
    		break;
        case EDGE : driver = new EdgeDriver();
    		break;
		default:
			break;
        }

        if(FileReaderManager.getInstance().getConfigFileReader() != null) driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		return driver;
	}	

	
	protected ChromeDriver createChromeDriver() {

		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + "\\webdrivers\\chrome\\chromedriver.exe");

		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		options.setExperimentalOption("excludeSwitches",Arrays.asList("disable-popup-blocking"));
		options.addArguments("test-type");
		options.addArguments("--ignore-ssl-errors=yes");
		options.addArguments("--ignore-certificate-errors");
		
				
		
		return new ChromeDriver(options);

	}

	public void closeDriver() {
		driver.close();
		driver.quit();
	}

	
}
