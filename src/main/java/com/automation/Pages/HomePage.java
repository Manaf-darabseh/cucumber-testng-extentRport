package com.automation.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Optional;

/**
 * HomePage class representing the home page of the application.
 * Uses property-based selectors for element locators.
 */
public class HomePage extends BasePage {
    // Selector keys from properties file
    private static final String CATEGORY_SELECTOR = "home.category.menu.%s";
    private static final String SUBCATEGORY_SELECTOR = "home.subcategory.menu.%s";
    private static final String BREADCRUMB_SELECTOR = "home.breadcrumb";
    private static final String TOP_MENU_SELECTOR = "home.top.menu";

    /**
     * Constructor for HomePage
     *
     * @param driver WebDriver instance
     */
    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Selects a category from the top menu
     *
     * @param category    main category to select
     * @param subCategory optional subcategory to select
     * @return HomePage instance for method chaining
     */
    public HomePage selectCategory(String category, String subCategory) {
        try {
            String categoryKey = String.format(CATEGORY_SELECTOR, category);
            By categoryLocator = getLocator(categoryKey);
            WebElement categoryElement = driver.findElement(categoryLocator);

            if (subCategory != null && !subCategory.isEmpty()) {
                selectWithSubcategory(category, subCategory, categoryElement);
            } else {
                clickCategory(category, categoryElement);
            }
            return this;
        } catch (Exception e) {
            logger.error("Failed to select category: {} with subcategory: {}", category, subCategory, e);
            throw new RuntimeException(
                    String.format("Failed to select category '%s' with subcategory '%s'", category, subCategory), e);
        }
    }

    /**
     * Selects a category with subcategory
     */
    private void selectWithSubcategory(String category, String subCategory, WebElement categoryElement) {
        // Hover over main category
        wait.until(ExpectedConditions.elementToBeClickable(categoryElement));
        actions.moveToElement(categoryElement).perform();
        logger.debug("Hovering over category: {}", category);

        // Format the selector with the subcategory name and wait for it to be clickable
        String subCategoryKey = String.format(SUBCATEGORY_SELECTOR, subCategory);
        By subCategoryLocator = getLocator(subCategoryKey);
        WebElement subCategoryElement = wait.until(
                ExpectedConditions.elementToBeClickable(driver.findElement(subCategoryLocator)));
        subCategoryElement.click();
        logger.debug("Clicked subcategory: {}", subCategory);
    }

    /**
     * Clicks the main category
     */
    private void clickCategory(String category, WebElement categoryElement) {
        categoryElement.click();
        logger.debug("Clicked category: {}", category);
    }

    /**
     * Gets the current breadcrumb text
     *
     * @return Optional containing breadcrumb text or empty if not found
     */
    public Optional<String> getCurrentBreadcrumb() {
        try {
            By breadcrumbLocator = getLocator(BREADCRUMB_SELECTOR);
            WebElement breadcrumbElement = driver.findElement(breadcrumbLocator);
            wait.until(ExpectedConditions.visibilityOf(breadcrumbElement));
            String text = breadcrumbElement.getText();
            logger.debug("Got breadcrumb text: {}", text);
            return Optional.of(text);
        } catch (Exception e) {
            logger.error("Failed to get breadcrumb text", e);
            return Optional.empty();
        }
    }

    /**
     * Gets the current category from breadcrumb
     *
     * @return Optional containing current category name or empty if not found
     */
    public Optional<String> getCurrentCategory() {
        try {
            return getCurrentBreadcrumb()
                    .filter(text -> !text.isEmpty())
                    .map(breadcrumbText -> {
                        String[] breadcrumbParts = breadcrumbText.split(">");
                        return breadcrumbParts[breadcrumbParts.length - 1].trim();
                    });
        } catch (Exception e) {
            logger.error("Failed to get current category", e);
            return Optional.empty();
        }
    }

    /**
     * Converts a property key to a locator
     *
     * @param propertyKey the key in the properties file
     * @return By locator
     */
    public By getLocator(String propertyKey) {
        // This method would retrieve the locator string from properties
        // and convert it to an appropriate By locator
        // Assuming implementation in the base class or to be implemented
        return By.cssSelector(getPropertyValue(propertyKey));
    }

    /**
     * Gets property value for the given key
     * This is a placeholder for the actual implementation
     */
    private String getPropertyValue(String key) {
        // Placeholder implementation - in real code, this would fetch from properties
        return key; // Simplified for example
    }
}