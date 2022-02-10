Feature: AddQuestionnaire
  Background:
    Given a teacher named "Marcel" with ID 123456
    And a Student named "Louis"
    And a module with ID "le C pour les nuls"
    And a Questionnaire with name "questionnaire pour les très très nuls"



    Scenario: a teacher register the questionnary to the module
      Given "Marcel" is the teacher registered to the module "le C pour les nuls"
      When "Marcel" wants to add the questionnaire "questionnaire pour les très très nuls" to the module "le C pour les nuls"
      Then The questionnaire "questionnaire pour les très très nuls" is added to the module "le C pour les nuls"
