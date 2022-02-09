# Created by Antoine at 09/02/2022
Feature: Get Course
  # Enter feature description here
  Background:
    Given a teacher named "Marcel" with ID 123456
    And a Student named "Louis"
    And a module with ID "le C pour les nuls"
    And a course with name "introduction" with a descritpion "cours d'introduction au c très facile"
    And the course "introduction" has been added by "Marcel" into "le C pour les nuls"

#    this scenario is here because we needed a similar background that was incompatible with addCourse.feature
  Scenario: Teacher add the same course two times in his module
    Given "Marcel" is the teacher registered to the module "le C pour les nuls"
    And a course "introduction" has already been added by "Marcel" in the module "le C pour les nuls"
    When "Marcel" wants to add the course a second time "introduction" to the module "le C pour les nuls"
    Then the course is not added and the return status of the request is 400

  Scenario: Teacher consult a course too see the descritpion
    Given "Marcel" is the teacher registered to the module "le C pour les nuls"
    And "Marcel" wants to get the course "introduction" from "le C pour les nuls" and make sure the description is "cours d'introduction au c très facile"