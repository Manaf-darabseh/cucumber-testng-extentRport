package com.automation.Pages;

import com.automation.manager.MYSQLConnectionManager;
import com.automation.utils.UtilProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Base page class that provides common functionality for all page objects.
 * Implements the Page Object pattern with useful utility methods for web interactions.
 */
public class BasePage {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(60);
    final Logger logger = LogManager.getLogger(BasePage.class);
    
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final Actions actions;
    protected final Properties locators;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
        this.actions = new Actions(driver);
        this.locators = loadLocators();
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Loads locators from properties file
     * @return Properties object containing locators
     */
    private Properties loadLocators() {
        Properties props = new Properties();
        try {
            String locatorsPath = "src/test/resources/Configurations/locators.properties";
            FileInputStream input = new FileInputStream(locatorsPath);
            props.load(input);
            logger.debug("Loaded locators from {}", locatorsPath);
        } catch (IOException e) {
            logger.error("Failed to load locators.properties", e);
        }
        return props;
    }
    
    /**
     * Gets a locator string from properties and converts it to a By object
     * @param key Property key
     * @return By locator
     */
    protected By getLocator(String key) {
        String locator = locators.getProperty(key);
        if (locator == null) {
            throw new IllegalArgumentException("Locator not found for key: " + key);
        }
        
        if (locator.startsWith("//")) {
            return By.xpath(locator);
        } else {
            return By.cssSelector(locator);
        }
    }
	
	/**
	 * Navigates to the specified URL and maximizes the browser window
	 * @param url URL to navigate to
	 */
	public void go(String url) {
		try {
			driver.get(url);
			driver.manage().window().maximize();
			logger.debug("Navigated to URL: {}", url);
		} catch (Exception e) {
			logger.error("Failed to navigate to URL: {}", url, e);
			throw new RuntimeException("Failed to navigate to URL: " + url, e);
		}
	}

	/**
	 * Fills a text box with the specified value
	 * @param element element locator from properties file
	 * @param value value to enter
	 * @throws RuntimeException if element cannot be found or interacted with
	 */
	public void fillTextBox(String element, String value) {
		try {
			By locator = getLocator(element);
			WebElement textBox = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			textBox.clear();
			textBox.sendKeys(value);
			logger.debug("Filled text box {} with value: {}", element, value);
		} catch (Exception e) {
			logger.error("Failed to fill text box {} with value: {}", element, value, e);
			throw new RuntimeException("Failed to fill text box: " + element, e);
		}
	}

	/**
	 * Clicks on an element after ensuring it is clickable
	 * @param selector element locator from properties file
	 * @throws RuntimeException if element cannot be found or clicked
	 */
	public void clickBy(String selector) {
		try {
			By locator = getLocator(selector);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			element.click();
			logger.debug("Clicked element: {}", selector);
		} catch (Exception e) {
			logger.error("Failed to click element: {}", selector, e);
			throw new RuntimeException("Failed to click element: " + selector, e);
		}
	}

	/**
	 * Clicks on an element using JavaScript or Actions
	 * @param selector element locator from properties file
	 * @throws RuntimeException if element cannot be found or clicked
	 */
	public void clickByJS(String selector) {
		try {
			By locator = getLocator(selector);
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			try {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
				logger.debug("Clicked element using JavaScript: {}", selector);
			} catch (Exception e) {
				logger.debug("JavaScript click failed, trying Actions click for: {}", selector);
				actions.moveToElement(element).click().perform();
				logger.debug("Clicked element using Actions: {}", selector);
			}
		} catch (Exception e) {
			logger.error("Failed to click element using JS/Actions: {}", selector, e);
			throw new RuntimeException("Failed to click element: " + selector, e);
		}
	}

	/**
	 * Checks if an element is displayed
	 * @param selector element locator from properties file
	 * @return true if element is displayed and visible, false otherwise
	 */
	public boolean elementDisplays(String selector) {
		try {
			By locator = getLocator(selector);
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			boolean isDisplayed = element.isDisplayed();
			logger.debug("Element {} display status: {}", selector, isDisplayed);
			return isDisplayed;
		} catch (Exception e) {
			logger.debug("Element {} is not displayed or not found", selector);
			return false;
		}
	}

	/**
	 * Gets text from an element
	 * @param selector element locator from properties file
	 * @return text content of the element
	 * @throws RuntimeException if element cannot be found or text cannot be retrieved
	 */
	public String getText(String selector) {
		try {
			By locator = getLocator(selector);
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			String text = element.getText();
			logger.debug("Got text from {}: {}", selector, text);
			return text;
		} catch (Exception e) {
			logger.error("Failed to get text from element: {}", selector, e);
			throw new RuntimeException("Failed to get text from element: " + selector, e);
		}
	}

	/**
	 * Gets the value of an attribute from an element
	 * @param selector element locator from properties file
	 * @param attribute name of the attribute
	 * @return value of the attribute or empty string if attribute doesn't exist
	 * @throws RuntimeException if element cannot be found
	 */
	public String getAttribute(String selector, String attribute) {
		try {
			By locator = getLocator(selector);
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			String value = element.getAttribute(attribute);
			logger.debug("Got attribute '{}' from {}: {}", attribute, selector, value);
			return value != null ? value : "";
		} catch (Exception e) {
			logger.error("Failed to get attribute '{}' from element: {}", attribute, selector, e);
			throw new RuntimeException(
				String.format("Failed to get attribute '%s' from element: %s", attribute, selector), e);
		}
	}
	/*
	 * End of function
	 */

	/*
	 * The following function can get the attribute value
	 */
	/**
	 * Gets the value attribute from an element
	 * @param selector element locator from properties file
	 * @return value of the value attribute in lowercase
	 */
	public String getAttributeValue(String selector) {
		String value = driver.findElement(getLocator(selector)).getAttribute("value");
		return value != null ? value.toLowerCase() : "";
	}
	/*
	 * End of function
	 */

	
	
	/*
	 * The following function can get the attribute value
	 */
	/**
	 * Checks if an element is selected based on its class attribute
	 * @param selector element locator from properties file
	 * @return true if element has 'selected' class, false otherwise
	 */
	public boolean isSelected(String selector) {
		String classAttribute = getAttribute(selector, "class");
		return classAttribute != null && classAttribute.contains("selected");
	}
	/*
	 * End of function
	 */	
	
	
	
	/*
	 * The following function can get the attribute value Method is duplicated
	 */
	public WebElement getElementById(String selector) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); 

		if (UtilProperties.getInstance().getProperty(selector).contains("//")) {
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(UtilProperties.getInstance().getProperty(selector))));
			return driver.findElement(By.xpath(UtilProperties.getInstance().getProperty(selector)));
		} else {
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.cssSelector(UtilProperties.getInstance().getProperty(selector))));
			return driver.findElement(By.cssSelector(UtilProperties.getInstance().getProperty(selector)));
		}
	}
	/*
	 * End of function
	 */

	/*
	 * The following function is used to switch between tabs
	 */
//	public void switchTab() {
//
//		ArrayList<String> tabs2 = new ArrayList<String>(getWindowHandles());
//		switchTo().window(tabs2.get(1));
//	}
	/*
	 * End of function
	 */


	/*
	 * End of function
	 */

	/*
	 * The following function is used to check if table is dispalyed
	 */
	public Boolean tableDisplayed(String selector) {

		try {
			driver.findElement(By.cssSelector(UtilProperties.getInstance().getProperty(selector)));
			return true;
		} catch (org.openqa.selenium.NoSuchElementException e) {
			return false;
		}
	}
	/*
	 * End of function
	 */

	/*
	 * The following function is used to get query value from Database for dynamic
	 * selector
	 */
	public String testDBCPDataSource(String query, String selector, String column) {

		String[] selectors = checkString(selector).split(" -");
		String selector1 = selectors[0];
		// Inside query , replace XX with selector value
		String content = UtilProperties.getInstance().getProperty(query).replace("XX", selector1);

		// Save query value into list
		List<String> r = MYSQLConnectionManager.getInstance().getListData(content, column);
		if (r != null) {
			return String.join(",", r);
		}
		return null;
	}
	/*
	 * End of function
	 */

	/*
	 * The following function is used to get value or text of webelement
	 */
	public String checkString(String Element1) {

		if ((savedWebElement(Element1)).getText().equals("")) {
			return getValue(Element1);
		} else {

			return bringText(Element1);
		}
	}
	/*
	 * End of function
	 */

	/*
	 * The following function is used to get value of webelement
	 */
	public String getValue(String element1) {

		return savedWebElement(element1).getAttribute("value");
	}
	/*
	 * End of function
	 */

	/*
	 * The following function is used to get text of webelement
	 */
	public String bringText(String element1) {

		return savedWebElement(element1).getText();
	}
	/*
	 * End of function
	 */

	/*
	 * The following function is used to retrieve value from database
	 */
	public String testDBCPDataSource(String query, String coulmn) {
		String content = UtilProperties.getInstance().getProperty(query);
		List<String> r = MYSQLConnectionManager.getInstance().getListData(content, coulmn);
		if (r != null) {
			return String.join(",", r).replace("[", "").replace("]", "");
		}
		return null;
	}
	/*
	 * End of function
	 */

	/*
	 * The following function will be used to select item from (Li-ul) list this
	 * method is duplicated
	 */
	public void selectlistItem(String Text, String selector) {
		List<WebElement> listItems;

		// Save found elements in list
		listItems = listwebelement(selector);
		String Zones;
		String selector2 [];
		// Check all list elements
		for (WebElement item : listItems) {
			Zones = item.getText().toString();
			//String[] selectors = (item.getText());
			String selectors = Zones;
			String selector1 = selectors;
			selector2 = selector1.split(" ");
			String splittedValue = selector2[0];
		
			// check if given text match one of the list items ,then select it
			if (Text.equalsIgnoreCase(splittedValue)) {
				try {
					item.click();
					break;
				} catch (StaleElementReferenceException e) {
					item.click();
				}
			}
		}
	}
	/*
	 * End of function
	 */

	/*
	 * The following function will be used to select the splitted value from list
	 */
	public void splitlistItemValue(String Text, String selector) {
		List<WebElement> listItems;

		// Save found elements in list
		listItems = listwebelement(selector);
		String [] selectors;
		// Check all list element
		for (WebElement item : listItems) {
			selectors = item.getText().split(" ");
			String splittedValue = selectors[0];
		
			// check if given text match one of the list items ,then select it
			if (Text.contains(splittedValue))
			{
				try {
					item.click();
					break;
				} catch (StaleElementReferenceException e) {
					item.click();
				}
			}
		}
	}
	/*
	 * End of function
	 */

	
	
	/*
	 * The following function will be used to select item from (select/option) list
	 */
	public void selectListElement(String Text, String selector) {
		Select dropdown = null;

		// Save found element in drop down list
		dropdown = new Select(savedWebElement(selector));

		// Check all drop down list elements
		for (WebElement item : (dropdown.getOptions())) {

			// split the item text after space and save it in string
			String[] selectors = (item.getText()).split(" ");
			String selector1 = selectors[0];

			// If list element equals text , select text
			// if (Text.equalsIgnoreCase(selector1)) {
			if (selector1.toLowerCase().contains(Text.toLowerCase()))
				dropdown.selectByValue(Text);
			break;
		}
	}

	/*
	 * End of function
	 */

	/*
	 * The following function will be used to check item if not checked
	 */
	public void itemchecked(String selector) {

		// check item if item is not checked
		if (savedWebElement(selector).isSelected()) {
			return;
		} else {
			savedWebElement(selector).click();
		}
	}
	/*
	 * End of function
	 */




	/*
	 * The following function will be used to fill textbox with value from Database
	 */
	public String fillTextboxFromDB(String selector, String value, String column) {
		// To get the query
		String query = UtilProperties.getInstance().getProperty(value);

		// To run the query and store the result in variable r
		List<String> r = MYSQLConnectionManager.getInstance().getListData(query, column);

		// To get first AWB
		String result = r.get(0);

		savedWebElement(selector).clear();
		savedWebElement(selector).sendKeys(result);
		return result;
	}
	/*
	 * End of function
	 */

	/*
	 * The following function will be used to pick date
	 */
	public void pikDate(String selector, String value) {
		WebElement dateWidget;
		List<WebElement> columns;

		dateWidget = savedWebElement(selector);
		columns = dateWidget.findElements(By.tagName("a"));

		for (WebElement cell : columns) {
			// Select Date
			if (cell.getText().equals(value)) {
				cell.click();
				break;
			}
		}
	}
	/*
	 * End of function
	 */

	/*
	 * The following function will be used to close the new tap
	 */
	public void CloseNewTap() {

		// Add windows into array, then close the new window and switch back to
		// the
		// previous window
		ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(tabs2.size() - 1));
		driver.close();
		driver.switchTo().window(tabs2.get(0));
	}
	/*
	 * End of function
	 */

	/*
	 * The following function will be used to switch to another frame
	 */
	public void switchToFrame() {

		// Get all frames(tabs) and save them in array list
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());

		// Switch to the previous Frame(tab)
		driver.switchTo().window(tabs2.get(tabs2.size() - 1));
	}
	/*
	 * End of function
	 */
	
	/*
	 * The following function will be used to switch to another pop up
	 */
	public void switchToPopUp() {

		String parentWindowHandler = driver.getWindowHandle(); // Store your parent window
		String subWindowHandler = null;

		Set<String> handles = driver.getWindowHandles(); // get all window handles
		Iterator<String> iterator = handles.iterator();
		while (iterator.hasNext()){
		    subWindowHandler = iterator.next();
		}
		driver.switchTo().window(subWindowHandler); // switch to popup window

		// Now you are in the popup window, perform necessary actions here

		driver.switchTo().window(parentWindowHandler);  // switch back to parent window
	}
	/*
	 * End of function
	 */
	

	/*
	 * The following function will be used to check if WebElement is displayed or
	 * not
	 */
	public WebElement savedWebElement(String selector) {
		WebDriverWait Wait = new WebDriverWait(driver,Duration.ofSeconds(60));
		String elementLocation = UtilProperties.getInstance().getProperty(selector);

		// Save CSS WebEelement in string
		By condition = By.cssSelector(elementLocation);

		// Override the condition string with xpath webElement
		if (elementLocation.contains("//")) {
			condition = By.xpath(elementLocation);
		}

		// Wait until element is visible
		Wait.until(ExpectedConditions.visibilityOfElementLocated(condition));

		// Save found element in WebElement
		WebElement foundElement = driver.findElement(condition);

		// boolean isDisplayed =(foundElement==null) ? false :
		// foundElement.isDisplayed();
		// return isDisplayed;
		return foundElement;
	}
	/*
	 * End of function
	 */

	/*
	 * The following function will be used to check if ListElements is displayed or
	 * not
	 */
	public List<WebElement> listwebelement(String selector) {
		
		WebDriverWait Wait = new WebDriverWait(driver,Duration.ofSeconds(60));
		List<WebElement> listItems;

		String elementLocation = UtilProperties.getInstance().getProperty(selector);

		// Save CSS WebEelement in string
		By condition = By.cssSelector(elementLocation);

		// Override the condition string with xpath webElement
		if (elementLocation.contains("//")) {
			condition = By.xpath(elementLocation);
		}

		// Wait until element is visible
		Wait.until(ExpectedConditions.visibilityOfElementLocated(condition));

		// Save found element in WebElement
		listItems = driver.findElements(condition);

		return listItems;
	}
	/*
	 * The following function will be used to to wait element to be visible
	 * not
	 */
	public void waitVisibilityOfWebelement(String selector) {
		WebDriverWait Wait = new WebDriverWait(driver,Duration.ofSeconds(60));
		String elementLocation = UtilProperties.getInstance().getProperty(selector);

		// Save CSS WebEelement in string
		By condition = By.cssSelector(elementLocation);

		// Override the condition string with xpath webElement
		if (elementLocation.contains("//")) {
			condition = By.xpath(elementLocation);
		}

		// Wait until element is visible
		Wait.until(ExpectedConditions.visibilityOfElementLocated(condition));
	}
	/*
	 * End of function
	 */
	/*
	 * The following function will be used to to wait element to be Clickable
	 * not
	 */
	public void waitElementToBeClickable(String selector) {
		WebDriverWait Wait = new WebDriverWait(driver,Duration.ofSeconds(60));
		String elementLocation = UtilProperties.getInstance().getProperty(selector);

		// Save CSS WebEelement in string
		By condition = By.cssSelector(elementLocation);

		// Override the condition string with xpath webElement
		if (elementLocation.contains("//")) {
			condition = By.xpath(elementLocation);
		}

		// Wait until element is visible
		Wait.until(ExpectedConditions.elementToBeClickable(condition));
	}
	/*
	 * End of function
	 */
	
	/*
	 * The following function will be used to to wait element to be Clickable
	 * not
	 */
	public void waitElementToBeInvisible(String selector) {
		WebDriverWait Wait = new WebDriverWait(driver,Duration.ofSeconds(60));
		String elementLocation = UtilProperties.getInstance().getProperty(selector);

		// Save CSS WebEelement in string
		By condition = By.cssSelector(elementLocation);

		// Override the condition string with xpath webElement
		if (elementLocation.contains("//")) {
			condition = By.xpath(elementLocation);
		}

		// Wait until element is visible
		Wait.until(ExpectedConditions.invisibilityOfElementLocated(condition));
	}
	/*
	 * End of function
	 */
	
	 public void datePickerTest(String datepickerSelector) {
	        //Get Today's number
	         String today = getCurrentDay();
	 
	        //Click and open the datepickers
	        clickBy(datepickerSelector);
	 
	        //This is from date picker table
	        WebElement dateWidgetFrom = savedWebElement("DatePickerTable");
	 
	        //This are the rows of the from date picker table
//	        List<WebElement> rows = dateWidgetFrom.findElements(By.tagName("tr"));
	 
	        //This are the columns of the from date picker table
	        List<WebElement> columns = dateWidgetFrom.findElements(By.tagName("td"));
	 
	        //DatePicker is a table. Thus we can navigate to each cell
	        //and if a cell matches with the current date then we will click it.
	        for (WebElement cell: columns) {
	            /*
	            //If you want to click 18th Date
	            if (cell.getText().equals("18")) {
	            */
	            //Select Today's Date
	            if (cell.getText().equals(today)) {
	                cell.click();
	                break;
	            }
	        }
	 
	    }
	 
	//Get The Current Day
	    public String getCurrentDay(){
	        //Create a Calendar Object
	        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
	 
	        //Get Current Day as a number
	        int todayInt = calendar.get(Calendar.DAY_OF_MONTH);
	        //System.out.println("Today Int: " + todayInt +"\n");
	 
	        //Integer to String Conversion
	        String todayStr = Integer.toString(todayInt);
	        //System.out.println("Today Str: " + todayStr + "\n");
	 
	        return todayStr;
	    }
	    
	    /*
		 * The following function is used to get query List from Database for dynamic
		 * selector
		 */
		public List<String> GetQueryList(String query, String value, String column) {
			
			String content = UtilProperties.getInstance().getProperty(query).replace("XX", value);

			// Save query value into list
			List<String> r = MYSQLConnectionManager.getInstance().getListData(content, column);
			if (r != null) {
				

			}
			return r;
		}
		/*
		 * End of function
		 */
		
	public Boolean elementDisplaysSimple(String selector) {

		WebElement foundElement = savedWebElement(selector);

		// if element is displayed return True else False

		try {
			return foundElement.isDisplayed();
		} catch (NoSuchElementException ignored) {
			return false;
		} catch (StaleElementReferenceException ignored) {
			return false;
		}
	}
	
	public void selctRandonFromDropDownList(String dropDown) {
		// Getting list of options
		List<WebElement> itemsInDropdown =  listwebelement(dropDown);

		// Getting size of options available
		int size = itemsInDropdown.size();

		// Generate a random number with in range
		int randnMumber = ThreadLocalRandom.current().nextInt(0, size);

		
		// Selecting random value
		itemsInDropdown.get(randnMumber).click();
		
	}
		
}
