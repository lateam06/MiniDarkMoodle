Feature: Register Student

  Background:
    Given a new teacher with login "steve"
    And a student with login "marcel"
    And with a module named "Gestion de projet"

  Scenario: Register Student
    When "steve" registers "marcel" as a student to module "Gestion de projet"
    Then the last valid request status is 200
    And "marcel" is registered to module "Gestion de projet" as a student

  Scenario: Student register himself
    When "marcel" tries to register himself to the module "Gestion de projet"
    Then the last invalid request status is 403
    And "marcel" is not registered to the module "Gestion de projet"
