Feature: Access student data

  Background:
    Given one teacher with login "alfred"
    And one student with login "bruce"
    And one student with login "clark"
    And a module with name "python"
    And "alfred" is registered as the teacher of the module "python"
    And "bruce" is registered as a student of the module "python"
    And "clark" is registered as a student of the module "python"

  Scenario: Get information about one student of the module
    When "alfred" access information about "bruce"
    Then the request answer is 200
    And "alfred" has access to "bruce" information

  Scenario: Get information about all students of the module
    When "alfred" access information about all students in the module "python"
    Then the request answer is 200
    And "alfred" has access to "bruce" and "clark" information