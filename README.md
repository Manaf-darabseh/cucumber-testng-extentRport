# Cucumber TestNG Automation Framework

A robust test automation framework combining Cucumber BDD, TestNG, Selenium WebDriver, and Appium. This framework supports both Web UI and Mobile testing with extensive reporting capabilities.

## Features

- **Cross-Platform Testing**
    - Web UI testing with Selenium WebDriver
    - Mobile testing with Appium
    - Support for multiple browsers (Chrome, Firefox, Edge, Safari)

- **BDD Framework**
    - Cucumber for behavior-driven development
    - Gherkin syntax for readable test scenarios
    - Step definition organization

- **Test Management**
    - TestNG for test orchestration
    - Parallel test execution support
    - Test grouping and prioritization

- **Reporting**
    - ExtentReports integration
    - Screenshot capture on failure
    - Detailed HTML reports
    - Test execution logs

- **Framework Features**
    - Page Object Model design pattern
    - Singleton pattern for DriverManager
    - Dependency injection with PicoContainer
    - Automatic WebDriver management
    - Environment-specific configuration
    - Robust error handling

## Prerequisites

- Java JDK 8 or higher
- Maven 3.8.x or higher
- Chrome/Firefox/Edge browser
- IntelliJ IDEA (recommended)
- Appium (for mobile testing)

### Required IntelliJ Plugins
- Cucumber for Java
- Gherkin
- TestNG

## Setup

1.  Clone the repository:
    ```bash
    git clone <repository-url>
    cd cucumber-testng-extentRport
    ```

2.  Install dependencies:
    ```bash
    mvn clean install
    ```

3.  Configure test environment:
    - Update `src/test/resources/Configurations/configuration.properties`
    - Set browser type, URLs, and timeouts
    - Configure mobile capabilities if needed

## Running Tests

### Method 1: Maven Command Line

```bash
# Run all tests
mvn clean test

# Run specific test groups
mvn test -Dgroups=web
mvn test -Dgroups=mobile

# Run with specific browser
mvn test -Dbrowser=chrome
```

### Method 2: TestNG XML

1.  Configure `testng.xml`
2.  Right-click on `testng.xml`
3.  Select 'Run'

### Method 3: Individual Test Classes

- Run `WebTests.java` for Web UI tests
- Run `Main.java` for API tests

## Project Structure

```
📦 cucumber-testng-extentRport
 ┣ 📂src
 ┃ ┣ 📂main/java/com/automation
 ┃ ┃ ┣ 📂Pages            # Page Object classes
 ┃ ┃ ┣ 📂Utilities       # Helper classes and utilities
 ┃ ┃ ┗ 📂manager         # Driver and configuration management
 ┃ ┗ 📂test
 ┃   ┣ 📂java/com/automation
 ┃   ┃ ┣ 📂run           # Test runners (Main.java, WebTests.java)
 ┃   ┃ ┗ 📂stepdef       # Step definitions for Cucumber
 ┃   ┗ 📂resources
 ┃     ┣ 📂Configurations  # Properties files
 ┃     ┣ 📂features       # Cucumber feature files
 ┃     ┗ 📜extent.properties # Reporting configuration
 ┣ 📜pom.xml            # Maven dependencies
 ┗ 📜testng.xml         # TestNG configuration
```

## Key Components

- **Pages**: Page Object classes for web elements and actions
- **Utilities**: Helper classes for common operations
- **Manager**: Driver management and configuration
- **StepDef**: Cucumber step definitions
- **Features**: Gherkin feature files
- **Resources**: Configuration files

## Configuration Management System

The framework implements a robust configuration management system with the following components:

### 1. Configuration Files

#### configuration.properties
```properties
# Base URL for the application under test
url = https://www.example.com

# Timeout in seconds (1-300)
timeout = 30

# Browser type: chrome, firefox, edge, safari
browser = chrome

# Environment: local, remote, mobile, api
environment = local

# Headless mode: true or false
Headless = false
```

#### mobile.properties
- Appium server configuration
- Device capabilities and settings
- Mobile test parameters

#### extent.properties
- Report configuration
- Screenshot settings
- Test result formatting

### 2. Configuration Components

#### FileReaderManager
- Singleton pattern for efficient resource management
- Lazy loading of configuration files
- Support for multiple property files
- Centralized property access with fallback support
- Automatic reload capability

#### ConfigFileReader
- Type-safe property access
- Default values for critical settings
- Robust validation:
    - URL format validation
    - Timeout range checks (1-300 seconds)
    - Browser type validation
    - Environment type validation
    - Headless mode parsing
- Comprehensive error messages

#### PathManager
- Cross-platform path resolution
- Project structure constants
- Directory creation utilities
- Resource path resolution
- OS-specific path handling

### 3. Best Practices

#### Configuration Setup
1. Always use the appropriate getter methods:
    ```java
    String url = FileReaderManager.getInstance().getConfigFileReader().getUrl();
    long timeout = FileReaderManager.getInstance().getConfigFileReader().getTime();
    ```

2. Handle configuration errors:
    ```java
    try {
        String browser = FileReaderManager.getInstance().getConfigFileReader().getBrowser();
    } catch (RuntimeException e) {
        // Handle invalid configuration
    }
    ```

3. Use default values when appropriate:
    ```java
    String value = FileReaderManager.getInstance().getProperty("custom.property", "default");
    ```

#### Configuration Validation
- URLs must start with http:// or https://
- Timeouts must be between 1 and 300 seconds
- Browser must be one of: chrome, firefox, edge, safari
- Environment must be one of: local, remote, mobile, api
- Headless mode must be true or false

#### Error Messages
The system provides clear error messages for common issues:
- Missing configuration files
- Invalid property values
- Permission issues
- File access errors

## Best Practices

1.  **Test Organization**
    - Group related scenarios in feature files
    - Use tags for test categorization
    - Follow BDD naming conventions

2.  **Code Structure**
    - Implement Page Object Model
    - Use dependency injection
    - Maintain clean step definitions

3.  **Error Handling**
    - Implement robust error handling
    - Capture screenshots on failure
    - Provide detailed error messages

4.  **Maintenance**
    - Regular dependency updates
    - Code review guidelines
    - Documentation updates

## Contributing

1.  Fork the repository
2.  Create a feature branch
3.  Commit your changes
4.  Push to the branch
5.  Create a Pull Request

## Support

For support and questions, please create an issue in the repository.
