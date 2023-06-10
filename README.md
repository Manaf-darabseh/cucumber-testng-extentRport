# testng-cucmber
An example automation framework using Selenium, TestNg &amp; Cucumber

## Preconditions
- Install Java JDK8: https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html
- Install Maven 3: https://maven.apache.org/download.cgi
- Google Chrome
- IDE Intellij IDEA. Require installing plugin: Cucumber.

## Run test
##### Method 1: Use Maven command
Run command ```mvn test```

##### Method 2: Use IDE for execution or debugging
Run Main.java

## Project structure
```$xslt
📦testng-cucumber
 ┣ 📂src
 ┃ ┗ 📂main
 ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┗ 📂org
 ┃ ┃ ┃ ┃ ┗ 📂example
 ┃ ┃ ┃ ┃ ┃ ┣ 📂pages
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BasePage.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜HomePage.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LoginPage.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂stepdef
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoryStepDef.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommonStepDef.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LoginStepDef.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂utils
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BaseStepDef.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Contexts.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StepContext.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜Main.java
 ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┣ 📂features
 ┃ ┃ ┃ ┃ ┣ 📜Category.feature
 ┃ ┃ ┃ ┃ ┗ 📜Login.feature
 ┃ ┃ ┃ ┣ 📜cucumber.properties
 ┃ ┃ ┃ ┗ 📜log4j2.xml
 ┣ 📜.gitignore
 ┣ 📜pom.xml
 ┣ 📜README.md
 ┗ 📜testng.xml
```
