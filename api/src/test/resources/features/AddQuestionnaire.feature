Feature: AddQuestionnaire

  Background:
    Given a teacher named "Marcel"
    And a student named "Louis"
    And a module named "le C pour les nuls"
    And a questionnaire with name "questionnaire"
    And another questionnaire with name "autre questionnaire" and description "QCM facile"

  Scenario: a teacher register the questionnaire to the module
    Given "Marcel" is the teacher registered to the module "le C pour les nuls"
    When "Marcel" wants to add the questionnaire "questionnaire" to the module "le C pour les nuls"
    Then The questionnaire "questionnaire" is added to the module "le C pour les nuls"

  Scenario: a teacher consult the questionnaire "autre questionnaire" to check the description
    Given "Marcel" is the teacher registered to the module "le C pour les nuls"
    When "Marcel" wants to add the questionnaire "autre questionnaire" to the module "le C pour les nuls"
    Then "Marcel" checks if the questionnaire "autre questionnaire" from "le C pour les nuls" has a description according to "QCM facile" with a get