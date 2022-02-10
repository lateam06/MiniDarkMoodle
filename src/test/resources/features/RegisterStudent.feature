Feature: Register Student

  Background:
    Given a teacher with login "steve"
    And a student with login "Bruce"
    And a module named "Gestion de projet"

  Scenario: Register Student
    When "steve" registers "Bruce" as a student to module "Gestion de projet"
    Then the last request status is 200
    And "Bruce" is registered to module "Gestion de projet"

  Scenario: Student register himself
    When "Bruce" tries to register himself to the module "Gestion de projet"
    Then the last request status is 403
    And "Bruce" is not registered to module "Gestion de projet"
