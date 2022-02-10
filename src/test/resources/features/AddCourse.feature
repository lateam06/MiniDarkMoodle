Feature: AddCourse

  Background:
    Given a teacher named "Marcel" with ID 123456
    And a Student named "Louis"
    And a module with ID "le C pour les nuls"
    And a course with name "introduction"

  Scenario: Teacher want to add a course in his module
    Given "Marcel" is the teacher registered to the module "le C pour les nuls"
    When "Marcel" wants to add the course "introduction" to the module "le C pour les nuls"
    Then The course "introduction" is added to the module "le C pour les nuls"

    Scenario: Student wants to add a course
      Given "Marcel" is the teacher registered to the module "le C pour les nuls"
      When  "Louis" wants to add the course "introduction" to the module "le C pour les nuls"
      Then the course is not added and the return status of the request is 403 forbidden


      Scenario: Teacher deletes course in his module
        Given "Marcel" is the teacher registered to the module "le C pour les nuls"
        When "Marcel" wants to delete the course "introduction" from the module "le C pour les nuls"
        Then the course "introduction" is deleted from the module "le C pour les nuls"

      Scenario: Student wants to delete a course
        Given "Marcel" is the teacher registered to the module "le C pour les nuls"
        When  "Louis" wants to delete the course "introduction" to the module "le C pour les nuls"
        Then the course is not deleted and the return status of the request is 403


Scenario: Teacher add a Course to the module
  Given "Marcel" is the teacher registered to the module "le C pour les nuls"
  When "Marcel" adds the course "Cours 2" to the post request to the module "le C pour les nuls"
  Then "Marcel" check that the "Cours 2" course has been added correcty in "le C pour les nuls"







#
#  Scenario: One other teacher already registered in the module
#    Given the module has a teacher already registered to the module "le C pour les nuls"
#    And Marcel isn't registered as the teacher of this module "le C pour les nuls"
#    When Marcel want to add the course "introduction"  in the module "le C pour les nuls"
#    Then The course "introduction"  isn't added to the module "le C pour les nuls"**
#
#  Scenario: No teacher registered in the module yet
#    Given the module "le C pour les nuls" has no teacher registered
#    And "Marcel" isn't registered as the teacher of this module "le C pour les nuls"
#    When "Marcel" want to add the course "introduction"  in the module "le C pour les nuls"
#    Then The course "introduction"  isn't added to the module "le C pour les nuls"