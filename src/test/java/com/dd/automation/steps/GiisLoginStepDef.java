package com.dd.automation.steps;

import com.dd.automation.base.BaseUtil;
import com.dd.automation.pages.GiisLoginPage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GiisLoginStepDef {
  private BaseUtil base;
  private GiisLoginPage giisLoginPage;

  public GiisLoginStepDef(BaseUtil base) {
    this.base = base;
    this.giisLoginPage = new GiisLoginPage(this.base.Driver);
  }

  @Given("I have entered valid {string}, {string} and {string}")
  public void i_have_entered_valid_and(String username, String password, String campus)  {
    giisLoginPage.enterEmail1(username);
    giisLoginPage.enterPassword(password);
    giisLoginPage.selectCampus(campus);
  }

  @When("I click on login")
  public void i_click_on_login()  {
    giisLoginPage.clickLoginButton();
  }

  @Then("I should be able to see the GIIS home page")
  public void i_should_be_able_to_see_the_GIIS_home_page() {
    giisLoginPage.landOnHomepage();
  }
}
