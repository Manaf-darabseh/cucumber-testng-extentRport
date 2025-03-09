package com.automation.manager;

import java.time.Duration;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.automation.Enums.DriverType;
import com.automation.Enums.EnvironmentType;

import io.github.bonigarcia.wdm.WebDriverManager;


public class DriverManager {
	private WebDriver driver;
	private static DriverType driverType;
	private static EnvironmentType environmentType;
	private static final String CHROME_DRIVER_PROPERTY = "webdriver.chrome.driver";




	public DriverManager() {
		driverType = FileReaderManager.getInstance().getConfigFileReader().getBrowser();
		environmentType = FileReaderManager.getInstance().getConfigFileReader().getEnvironment();
	}

	public WebDriver getDriver() {
		if(driver == null) {
			driver = createDriver();
		}
		return driver;
	}


	private WebDriver createDriver() {
		switch (environmentType) {
			case LOCAL:
				driver = createLocalDriver();
				break;
			case Mobile:
				driver = createMobileDriver();
				break;
			case API:
				// No driver needed for API testing
				break;
			default:
				throw new RuntimeException("Unsupported environment type: " + environmentType);
		}
		return driver;
	}


	private WebDriver createLocalDriver() {
		try {
			WebDriver localDriver;
			switch (driverType) {
				case FIREFOX:
					// Use WebDriverManager's automatic version management for Firefox
					WebDriverManager.firefoxdriver()
						.clearDriverCache()
						.clearResolutionCache()
						.driverVersion("latest")
						.setup();
					localDriver = new FirefoxDriver();
					break;
				case CHROME:
					localDriver = createChromeDriver();
					break;
				case EDGE:
					// Use WebDriverManager's automatic version management for Edge
					WebDriverManager.edgedriver()
						.clearDriverCache()
						.clearResolutionCache()
						.driverVersion("latest")
						.setup();
					localDriver = new EdgeDriver();
					break;
				case SAFARI:
					// Safari doesn't need WebDriverManager setup
					localDriver = new SafariDriver();
					break;
				default:
					throw new RuntimeException("Unsupported browser type: " + driverType);
			}

			if (localDriver == null) {
				throw new RuntimeException("Failed to create WebDriver instance");
			}

			// Configure the driver before assigning to the class field
			localDriver.manage().window().maximize();
			localDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
			
			// Only assign to class field after successful configuration
			driver = localDriver;


			return driver;
		} catch (Exception e) {
			throw new RuntimeException("Failed to create local driver: " + e.getMessage(), e);
		}
	}


	protected WebDriver createChromeDriver() {
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

		try {
			// Let WebDriverManager handle Chrome/ChromeDriver compatibility
			WebDriverManager.chromedriver()
				.clearDriverCache()
				.clearResolutionCache()
				.driverVersion("latest")
				.setup();

			WebDriver chromeDriver = new ChromeDriver(options);
			if (chromeDriver == null) {
				throw new RuntimeException("Failed to create Chrome driver");
			}
			return chromeDriver;
		} catch (Exception e) {
			throw new RuntimeException("Failed to create Chrome driver: " + e.getMessage(), e);
		}

	}

	public void closeDriver() {
		try {
			if (driver != null) {
				// Close all associated windows first
				for (String handle : driver.getWindowHandles()) {
					driver.switchTo().window(handle).close();
				}
				// Then quit the driver
				driver.quit();
			}
		} catch (Exception e) {
			System.err.println("Error during driver cleanup: " + e.getMessage());
		} finally {
			// Always null out the driver reference
			driver = null;
		}
	}

	private WebDriver createMobileDriver() {
		// TODO: Implement mobile driver creation based on DriverType
		throw new UnsupportedOperationException("Mobile driver creation not yet implemented");
	}


}
