Feature: Answering to question

  Background:
    Given a teacher named "Marcel"
    And a student named "Yann"
    And "Marcel" is connected
    And "Yann" is connected
    And a module named "Module de questionnaire"
    And "Marcel" is the teacher registered to the module "Module de questionnaire"
    And a questionnaire named "Un questionnaire" in "Module de questionnaire"
    And a QCM named "test" with correct answer "response 1" in "Un questionnaire"
    And two responses "response 1" and "response 2" in the qcm "test"


  Scenario: A student answer to a QCM
    Given "Marcel" has registered "Yann" on the module "Module de questionnaire"
    When "Yann" wants to answer "response 1" in the qcm "test" of the questionnaire "Un questionnaire" of the module "Module de questionnaire"
    Then "Yann" responsed "response 1" to the qcm "test" of the questionnaire "Un questionnaire" of the module "Module de questionnaire"