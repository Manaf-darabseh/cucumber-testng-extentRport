package com.automation.Pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Login page class representing the login functionality.
 * Uses property-based selectors for element locators.
 */
public class Login_Page extends BasePage {

    // Selector keys from properties file
    private static final String USERNAME_FIELD = "login.username.field";
    private static final String PASSWORD_FIELD = "login.password.field";
    private static final String LOGIN_BUTTON = "login.submit.button";
    private static final String ERROR_MESSAGE = "login.error.message";
    
    // Error messages
    private static final String USERNAME_REQUIRED = "Username is required";
    private static final String PASSWORD_REQUIRED = "Password is required";
    private static final String LOGIN_FAILED = "Login failed. Please check your credentials.";
    private static final String PAGE_NOT_LOADED = "Login page is not loaded properly.";
    private static final String ELEMENT_NOT_FOUND = "Login form element not found: %s";
    private static final String PAGE_LOAD_TIMEOUT = "Login page did not load within specified timeout: %d seconds";

    /**
     * Constructor for Login_Page
     * @param driver WebDriver instance
     */
    public Login_Page(WebDriver driver) {
        super(driver);
    }

    /**
     * Checks if login form is displayed and ready for interaction
     * @return true if both username and password fields are displayed and enabled
     * @throws RuntimeException if the login page is not loaded properly
     */
    public boolean isLoginFormDisplayed() {
        try {
            boolean usernameReady = wait.until(ExpectedConditions.elementToBeClickable(getLocator(USERNAME_FIELD))).isEnabled();
            boolean passwordReady = wait.until(ExpectedConditions.elementToBeClickable(getLocator(PASSWORD_FIELD))).isEnabled();
            boolean buttonReady = wait.until(ExpectedConditions.elementToBeClickable(getLocator(LOGIN_BUTTON))).isEnabled();
            
            boolean isReady = usernameReady && passwordReady && buttonReady;
            logger.debug("Login form ready status: {}", isReady);
            return isReady;
        } catch (TimeoutException e) {
            logger.error("Login form elements not ready within timeout");
            throw new RuntimeException(PAGE_NOT_LOADED, e);
        } catch (NoSuchElementException e) {
            logger.error("Login form elements not found");
            throw new RuntimeException(PAGE_NOT_LOADED, e);
        } catch (Exception e) {
            logger.error("Unexpected error checking login form status", e);
            throw new RuntimeException("Failed to verify login form status", e);
        }
    }

    /**
     * Verifies if the login page loads within the specified timeout
     * @param timeoutSeconds maximum time to wait for page load in seconds
     * @return true if page loads within timeout, false otherwise
     * @throws RuntimeException if timeout occurs
     */
    public boolean verifyPageLoadTime(int timeoutSeconds) {
        try {
            long startTime = System.currentTimeMillis();
            boolean isLoaded = isLoginFormDisplayed();
            long loadTime = (System.currentTimeMillis() - startTime) / 1000;
            
            logger.debug("Login page load time: {} seconds", loadTime);
            
            if (loadTime > timeoutSeconds) {
                throw new TimeoutException(String.format(PAGE_LOAD_TIMEOUT, timeoutSeconds));
            }
            
            return isLoaded;
        } catch (Exception e) {
            logger.error("Failed to verify page load time", e);
            throw new RuntimeException("Failed to verify page load time", e);
        }
    }

    /**
     * Verifies if the password field masks the input
     * @return true if password is masked, false otherwise
     */
    public boolean isPasswordMasked() {
        try {
            String inputType = getAttribute(PASSWORD_FIELD, "type");
            boolean isMasked = "password".equals(inputType);
            logger.debug("Password field mask status: {}", isMasked);
            return isMasked;
        } catch (Exception e) {
            logger.error("Failed to verify password masking", e);
            throw new RuntimeException("Failed to verify password masking", e);
        }
    }

    /**
     * Verifies if all login elements are interactive
     * @return true if all elements are interactive, false otherwise
     */
    public boolean areAllElementsInteractive() {
        try {
            return isLoginFormDisplayed() && 
                   driver.findElement(getLocator(USERNAME_FIELD)).isEnabled() &&
                   driver.findElement(getLocator(PASSWORD_FIELD)).isEnabled() &&
                   driver.findElement(getLocator(LOGIN_BUTTON)).isEnabled();
        } catch (Exception e) {
            logger.error("Failed to verify elements interactivity", e);
            return false;
        }
    }

    /**
     * Enters username in the login form
     * @param username username to enter
     * @return Login_Page instance for method chaining
     */
    public Login_Page enterUsername(String username) {
        try {
            fillTextBox(USERNAME_FIELD, username);
            return this;
        } catch (Exception e) {
            logger.error("Failed to enter username: {}", username, e);
            throw new RuntimeException("Failed to enter username", e);
        }
    }

    /**
     * Enters password in the login form
     * @param password password to enter
     * @return Login_Page instance for method chaining
     */
    public Login_Page enterPassword(String password) {
        try {
            fillTextBox(PASSWORD_FIELD, password);
            return this;
        } catch (Exception e) {
            logger.error("Failed to enter password", e);
            throw new RuntimeException("Failed to enter password", e);
        }
    }

    /**
     * Clicks the login button
     * @return Login_Page instance for method chaining
     */
    public Login_Page clickLoginButton() {
        try {
            clickBy(LOGIN_BUTTON);
            return this;
        } catch (Exception e) {
            logger.error("Failed to click login button", e);
            throw new RuntimeException("Failed to click login button", e);
        }
    }

    /**
     * Validates login credentials before attempting login
     * @param username username to validate
     * @param password password to validate
     * @throws IllegalArgumentException if credentials are invalid
     */
    private void validateCredentials(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException(USERNAME_REQUIRED);
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException(PASSWORD_REQUIRED);
        }
    }

    /**
     * Checks if there is an error message displayed
     * @return the error message if present, empty string otherwise
     */
    public String getErrorMessage() {
        try {
            if (elementDisplays(ERROR_MESSAGE)) {
                String error = getText(ERROR_MESSAGE);
                logger.debug("Login error message: {}", error);
                return error;
            }
            return "";
        } catch (Exception e) {
            logger.debug("No error message found");
            return "";
        }
    }

    /**
     * Performs login with given credentials
     * @param username username to login with
     * @param password password to login with
     * @return Login_Page instance for method chaining
     * @throws IllegalArgumentException if credentials are invalid
     * @throws RuntimeException if login fails
     */
    public Login_Page login(String username, String password) {
        try {
            validateCredentials(username, password);
            
            if (!isLoginFormDisplayed()) {
                throw new RuntimeException(PAGE_NOT_LOADED);
            }
            
            logger.debug("Attempting login with username: {}", username);
            enterUsername(username)
                .enterPassword(password)
                .clickLoginButton();
            
            String error = getErrorMessage();
            if (!error.isEmpty()) {
                throw new RuntimeException(error);
            }
            
            return this;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid login credentials: {}", e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            logger.error("Login failed: {}", e.getMessage());
            throw new RuntimeException(LOGIN_FAILED, e);
        }
    }
}
