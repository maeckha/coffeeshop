Feature: Homepage
  Scenario: client calls the start page
    When the client calls /
    Then the client receives status code of 200