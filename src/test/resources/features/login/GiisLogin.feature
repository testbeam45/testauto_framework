Feature: Login to GIIS school Website

  Background:
    Given I am on the OpenCart login page

  @Sanity
  Scenario Outline: Successful login with valid credentials
    Given I have entered valid "<username>", "<password>" and "<campus>"
    When I click on login
    Then I should be able to see the GIIS home page

    Examples:
      | username         | password      | campus         |
      | Preethi Konchady | Disha20082016 | PG-SmartCampus |