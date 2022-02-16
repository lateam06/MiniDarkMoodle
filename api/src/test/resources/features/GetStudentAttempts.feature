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
    And a QCM named "requests" with correct answer "get" in "is it restful"
    And a QCM named "code runner" with correct answer "python" in "is it restful"
    And a QCM named "containers" with correct answer "multi" in "is it restful"
    And two responses "verb" and "noun" in the qcm "verb or noun"
    And two responses "get" and "post" in the qcm "requests"
    And two responses "python" and "java" in the qcm "code runner"
    And two responses "multi" and "mono" in the qcm "containers"

    Scenario: teacher get attempts of one student
      Given "Mario" has registered "James" on the module "REST APIs"
      And "James" responded "verb" in the qcm "verb or noun" of the questionnaire "is it restful" of the module "REST APIs"
      And "James" responded "post" in the qcm "requests" of the questionnaire "is it restful" of the module "REST APIs"
      When "Mario" wants to get attempts of "James" to the questionnary "is it restful" of the module "REST APIs"
      Then "Mario" sees that "James" responded "verb" for first qcm and "post" for the second

    Scenario: teacher get attempts of all students
      Given "Mario" has registered "James" on the module "REST APIs"
      And "Mario" has registered "Jack" on the module "REST APIs"
      And "James" responded "noun" in the qcm "verb or noun" of the questionnaire "is it restful" of the module "REST APIs"
      And "James" responded "get" in the qcm "requests" of the questionnaire "is it restful" of the module "REST APIs"
      And "James" responded "java" in the qcm "code runner" of the questionnaire "is it restful" of the module "REST APIs"
      And "James" responded "multi" in the qcm "containers" of the questionnaire "is it restful" of the module "REST APIs"
      And "Jack" responded "verb" in the qcm "verb or noun" of the questionnaire "is it restful" of the module "REST APIs"
      And "Jack" responded "post" in the qcm "requests" of the questionnaire "is it restful" of the module "REST APIs"
      And "Jack" responded "python" in the qcm "code runner" of the questionnaire "is it restful" of the module "REST APIs"
      And "Jack" responded "mono" in the qcm "containers" of the questionnaire "is it restful" of the module "REST APIs"
      When "Mario" wants to get attempts of all students to the questionnary "is it restful" of the module "REST APIs"
      Then "Mario" sees that "James" and "Jack" responded to all of the qcms

