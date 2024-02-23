package com.dd.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;


public class GiisLoginPage {
    private WebDriver driver;

    // Locators
    private By emailInputLocator = By.name("userId");
    private By passwordInputLocator = By.name("userPassword");
    private By campusDropdownLocator = By.xpath("//span[@id='select2-select-campus-login-container']");
    private By campusTextFieldLocator = By.xpath("//input[@placeholder='Search Campus']");
    private By loginButtonLocator = By.cssSelector("input[value='Login']");
    private By homePageLocator = By.xpath("//a[normalize-space()='HOME']");


    // Constructor
    public GiisLoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Methods
    public void enterEmail1(String email) {
        WebElement emailInput = driver.findElement(emailInputLocator);
        emailInput.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement passwordInput = driver.findElement(passwordInputLocator);
        passwordInput.sendKeys(password);
    }

    public void selectCampus(String campus)  {
        WebElement campusDropdown = driver.findElement(campusDropdownLocator);
        campusDropdown.click();
        WebElement campusTextField = driver.findElement(campusTextFieldLocator);
        campusTextField.sendKeys(campus);
        campusTextField.sendKeys(Keys.RETURN);
    }

    public void clickLoginButton() {
        WebElement loginButton = driver.findElement(loginButtonLocator);
        loginButton.click();
    }

    public void landOnHomepage() {
        WebElement homePage = driver.findElement(homePageLocator);
        homePage.isDisplayed();
    }
}
