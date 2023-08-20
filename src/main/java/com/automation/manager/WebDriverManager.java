package com.automation.manager;

import java.util.Arrays;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.automation.Enums.DriverType;
import com.automation.Enums.EnvironmentType;

public class WebDriverManager   {
	private WebDriver driver;
	private static DriverType driverType;
	private static EnvironmentType environmentType;
	private static final String CHROME_DRIVER_PROPERTY = "webdriver.chrome.driver";
	//private AppiumDriver mobileDriver;




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
			case LOCAL:
				driver = createLocalDriver();
				break;
			case REMOTE:
				driver = createRemoteDriver();
				break;
			case Mobile:
				break;
			case API:
				break;
			default:
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
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--allow-insecure-localhost");

		options.setExperimentalOption("excludeSwitches",Arrays.asList("mixed-forms-interstitial@2"));



		options.addArguments("--incognito");
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--ignore-urlfetcher-cert-requests");
		options.addArguments("--allow-insecure-localhost");
		options.addArguments("--ignore-ssl-errors=yes");
		options.addArguments("--disable-background-networking");
		options.addArguments("--disable-client-side-phishing-detection ");
		options.addArguments("--disable-default-apps");
		options.addArguments("--disable-hang-monitor");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--disable-prompt-on-repost");
		options.addArguments("--disable-sync");
		options.addArguments("--disable-web-resources");
		options.addArguments("--enable-automation");
		options.addArguments("--enable-logging");
		options.addArguments("--force-fieldtrials=SiteIsolationExtensions/Control");
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--log-level=0");
		options.addArguments("--metrics-recording-only");
		options.addArguments("--no-first-run");
		options.addArguments("--password-store=basic");
		options.addArguments("--safebrowsing-disable-auto-update");
		options.addArguments("--test-type=webdriver");
		options.addArguments("--use-mock-keychain");

//		options.addArguments("headless","disable-gpu");


//         HashMap<String, Object> chromeLocalStatePrefs = new HashMap<String, Object>();
//         List<String> experimentalFlags = new ArrayList<String>();
//
//         experimentalFlags.add("same-site-by-default-cookies@2");
//         experimentalFlags.add("cookies-without-same-site-must-be-secure@2");
//         experimentalFlags.add("enable-removing-all-third-party-cookies@2");
//         chromeLocalStatePrefs.put("browser.enabled_labs_experiments", experimentalFlags);
//         options.setExperimentalOption("localState", chromeLocalStatePrefs);

//		ChromeOptions capability = new ChromeOptions();
//		capability.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
//
//			capability.setCapability(ChromeOptions.CAPABILITY, options);

		return new ChromeDriver(options);

	}

	public void closeDriver() {
		driver.close();
		driver.quit();
	}


}
