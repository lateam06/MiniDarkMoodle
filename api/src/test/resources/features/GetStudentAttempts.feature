Feature: get student attempts

  Background:
    Given a teacher named "Mario"
    And a student named "James"
    And a student named "Jack"
    And "Mario" is connected
    And "James" is connected
    And "Jack" is connected
    And a module named "REST APIs"
    And "Mario" is the teacher registered to the module "REST APIs"
    And a questionnaire named "is it restful" in "REST APIs"
    And a QCM named "verb or noun" with correct answer "verb" in "is it restful"
    And two responses "verb" and "noun" in the qcm "verb or noun"
    And a QCM named "docker" with correct answer "desktop" in "is it restful"
    And two responses "desktop" and "cmd" in the qcm "docker"

    Scenario: teacher get attempts of one student
      Given "Mario" has registered "James" on the module "REST APIs"
      And "James" responded "verb" in the qcm "verb or noun" of the questionnaire "is it restful" of the module "REST APIs"
      And "James" responded "cmd" in the qcm "docker" of the questionnaire "is it restful" of the module "REST APIs"
      When "Mario" wants to get attempts of "James" to the questionnaire "is it restful" of the module "REST APIs"
      Then "Mario" sees that "James" responded "verb" to the qcm

    Scenario: teacher get attempts of all students
      Given "Mario" has registered "James" on the module "REST APIs"
      And "Mario" has registered "Jack" on the module "REST APIs"
      And "James" responded "noun" in the qcm "verb or noun" of the questionnaire "is it restful" of the module "REST APIs"
      And "Jack" responded "verb" in the qcm "verb or noun" of the questionnaire "is it restful" of the module "REST APIs"
      And "James" responded "cmd" in the qcm "docker" of the questionnaire "is it restful" of the module "REST APIs"
      And "Jack" responded "desktop" in the qcm "docker" of the questionnaire "is it restful" of the module "REST APIs"
      When "Mario" wants to get attempts of all students to the questionnaire "is it restful" of the module "REST APIs"
      Then "Mario" sees that "James" and "Jack" responded to the qcm

