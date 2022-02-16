Feature: get student attempts

  Background:
    Given a teacher named "Mario"
    And a student named "James"
    And "Mario" is connected
    And "James" is connected
    And a module named "REST APIs"
    And "Mario" is the teacher registered to the module "REST APIs"
    And a questionnaire named "is it restful" in "REST APIs"
    And a QCM named "verb or noun" with correct answer "verb" in "is it restful"
    And a QCM named "requests" with correct answer "get" in "is it restful"
    And two responses "verb" and "noun" in the qcm "verb or noun"
    And two responses "get" and "post" in the qcm "requests"

    Scenario: teacher get attempts of one student
      Given "Mario" has registered "James" on the module "REST APIs"
      And "James" responded "verb" in the qcm "verb or noun" of the questionnaire "is it restful" of the module "REST APIs"
      And "James" responded "post" in the qcm "requests" of the questionnaire "is it restful" of the module "REST APIs"
      When "Mario" wants to get attempts of "James" to the questionnary "is it restful" of the module "REST APIs"
      Then "Mario" sees that "James" responded "verb" for first qcm and "post" for the second