Feature: AddQuestionnaire
  Background:
    Given a teacher named "Marcel" with ID 123456
    And a Student named "Louis"
    And a module with ID "le C pour les nuls"
    And a Questionnaire with name "questionnaire"
    And another Questionnaire with name "autre questionnaire" and descritpion "QCM facile"



    Scenario: a teacher register the questionnary to the module
      Given "Marcel" is the teacher registered to the module "le C pour les nuls"
      When "Marcel" wants to add the questionnaire "questionnaire" to the module "le C pour les nuls"
      Then The questionnaire "questionnaire" is added to the module "le C pour les nuls"



      Scenario: a teacher consult the questionnary "autre questionnaire" to check the description
        Given "Marcel" is the teacher registered to the module "le C pour les nuls"
        When "Marcel" wants to add the questionnaire "autre questionnaire" to the module "le C pour les nuls"
        Then "Marcel" checks if the Questionnary "autre questionnaire" from "le C pour les nuls" has a description according to "QCM facile" with a get