Feature: Register Student

  Background:
    Given a teacher named "Steve"
    And a student named "Bruce"
    And "Steve" is connected
    And "Bruce" is connected
    And a module named "Gestion de projet"
    And "Steve" is the teacher registered to the module "Gestion de projet"

  Scenario: Register Student
    When "Steve" has registered "Bruce" on the module "Gestion de projet"
    Then "Bruce" is registered to module "Gestion de projet"

  Scenario: Student register himself
    When "Bruce" tries to register himself to the module "Gestion de projet"
    Then the last request status is 403
    And "Bruce" is not registered to module "Gestion de projet"
