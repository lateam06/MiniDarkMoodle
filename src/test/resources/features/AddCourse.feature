Feature: AddCourse

  Background:
    Given a teacher named "Marcel" with ID 123456
    And a module with ID "le C pour les nuls"
    And a course with name "introduction"

  Scenario: Marcel is the teacher registered to the module "le C pour les nuls"
    Given "Marcel" is the teacher registered to the module "le C pour les nuls"
    When "Marcel" want to add the course "introduction"  to the module "le C pour les nuls"
    Then The course "introduction" is added to the module "le C pour les nuls"

  Scenario: One other teacher already registered in the module
    Given the module has a teacher already registered to the module "le C pour les nuls"
    And Marcel isn't registered as the teacher of this module "le C pour les nuls"
    When Marcel want to add the course "introduction"  in the module "le C pour les nuls"
    Then The course "introduction"  isn't added to the module "le C pour les nuls"

  Scenario: No teacher registered in the module yet
    Given the module "le C pour les nuls" has no teacher registered
    And "Marcel" isn't registered as the teacher of this module "le C pour les nuls"
    When "Marcel" want to add the course "introduction"  in the module "le C pour les nuls"
    Then The course "introduction"  isn't added to the module "le C pour les nuls"