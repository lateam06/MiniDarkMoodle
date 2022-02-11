Feature: Register Teacher

  Background:
    Given a teacher named "Steve"
    And a teacher named "Sarah"
    And a module named "Gestion de projet"

  Scenario: Register Teacher
    When "Steve" registers to module "Gestion de projet"
    Then last request status is 200
    And "Steve" is registered to module "Gestion de projet"

  Scenario: Register Second Teacher
    When "Sarah" registers to module "Gestion de projet"
    And "Steve" registers to module "Gestion de projet"
    Then last request status is 400
    And "Sarah" is registered to module "Gestion de projet"
    And "Steve" is not registered to module "Gestion de projet"

