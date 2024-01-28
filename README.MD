# A Sample Test Automation Framework

## Overview

The testautom_framework project is an automation framework designed to automate web application testing using Selenium
WebDriver with Java and Cucumber. This framework follows the behavior-driven development (BDD) approach, allowing
testers and developers to write test scenarios in plain language using Gherkin syntax.

## Features

- Automates web application testing using Selenium WebDriver.
- Implements BDD scenarios using Cucumber.
- Provides an easily extendable and maintainable test suite.
- Generates detailed test reports for analysis.

## Prerequisites

Ensure the following prerequisites are met before using the testautom_framework:

- Java Development Kit (JDK) installed
- Maven installed
- The code and pom.xml has WebDriverManager, which automatically downloads the appropriate WebDriver binary based on the
  system configuration and the browser version detected.

## Getting Started

To get started with the testautom_framework project, follow these steps:

1. **Clone the Repository:**

    `git clone <repository_url>`

2. **Navigate to the Project Directory:**

    `cd testautom_framework`

3. **Install Dependencies:**

    `mvn clean install`

4. **Execute the Test Suite:**

    `mvn test`

## Project Structure

The project structure of `A Sample Test Automation Framework` is organized as follows:

`src/main/java`: Contains Java code for implementing page objects and utilities.
`src/test/java`: Contains Cucumber step definitions and test runners.
`src/test/resources`: Contains feature files written in Gherkin syntax.
`target`: Contains generated reports and compiled classes.

## Configuration

`config.properties`: Configurations for WebDriver settings and other properties.
`log4j.properties`: Logging configuration settings.

## Reporting

Test execution results are generated in HTML format and can be found in the `target/cucumber-reports` directory.

## Contributing

Contributions to the `A Sample Test Automation Framework` project are welcome! To contribute, fork the repository, make changes, and
submit a pull request.
