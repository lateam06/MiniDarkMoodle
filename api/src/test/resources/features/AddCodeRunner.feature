# Created by Antoine at 10/02/2022
Feature: CodeRunner
  # Enter feature description here
  Background:
    Given a teacher named "Marcel"
    And a student named "Louis"
    And "Marcel" is connected
    And "Louis" is connected
    And a module named "le C pour les nuls"
    And a questionnaire with name "questionnaire"
    And a CodeRunner Question "factoriel en python"



  Scenario: the teacher add the Student tot the module, add the Questionnary and the question
    Given "Marcel" is the teacher registered to the module "le C pour les nuls"
    When "Marcel" wants to add the questionnaire "questionnaire" to the module "le C pour les nuls"
    And "Marcel" wants to add a CodeRunner "factoriel en python" to the questionnaire "questionnaire" from the module "le C pour les nuls"
    And "Louis" wants to answer the CodeRunner "factoriel en python" of "questionnaire" from "le C pour les nuls"