Feature: AddCourse

  Background:
    Given a teacher named "Marcel" and with teacher ID 123456
    And a module with an ID 123

  Scenario: Marcel is the teacher registered to the module
    Given "Marcel" is the teacher registered to the module
    When "Marcel" want to add a course to the module
    Then The course is added to the module

  Scenario: One other teacher registered in the module
    Given the module has a teacher already registered to the module
    And "Marcel" isn't registered as the teacher of this module
    When "Marcel" want to add a course in the module
    Then The course isn't added

  Scenario: No teacher registered in the module yet
    Given the module has no teacher registered to the module
    And "Marcel" isn't registered as the teacher of this module
    When "Marcel" want to add a course in the module
    Then The course is added to the module
    And "Marcel" is registered as the teacher of this module