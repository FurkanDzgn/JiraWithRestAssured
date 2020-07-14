Feature: Jira with API

  Scenario: Create a story
    Given the user write restassured steps
    When the user goes to the website
    When the user click last created one
    Then click viewWorkflow
    And validate information
