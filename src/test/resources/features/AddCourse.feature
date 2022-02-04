Feature: AddCourse

  Background:
    Given a teacher named "Marcel" with ID 123456
    And a module with ID 123
    And a course with ID 246

  Scenario: Marcel is the teacher registered to the module 123
    Given Marcel 123456 is the teacher registered to the module 123
    When Marcel 123456 want to add the course 246 to the module 123
    Then The course 246 is added to the module 123

  Scenario: One other teacher already registered in the module
    Given the module has a teacher already registered to the module 123
    And Marcel 123456 isn't registered as the teacher of this module 123
    When Marcel 123456 want to add the course 246 in the module 123
    Then The course 246 isn't added to the module 123

  Scenario: No teacher registered in the module yet
    Given the module 123 has no teacher registered
    And Marcel 123456 isn't registered as the teacher of this module 123
    When Marcel 123456 want to add the course 246 in the module 123
    Then The course 246 isn't added to the module 123