Feature: get all questions
  Background:
    Given a teacher named "Marcel"
    And "Marcel" is connected
    And a module named "Module de questionnaire"
    And "Marcel" is the teacher registered to the module "Module de questionnaire"
    And a questionnaire with name "Un questionnaire"
    And "Marcel" has registered the questionnaire "Un questionnaire" to the module "Module de questionnaire"

  Scenario: Teacher get all the questions of the questionnary
    Given "Marcel" has already registered a QCM "test" to the questionnaire "Un questionnaire" of the module "Module de questionnaire"
    When "Marcel" wants to get all questions from the questionnaire "Un questionnaire" of the module "Module de questionnaire"
    Then "Marcel" sees the question "test" in the list of questions returned
