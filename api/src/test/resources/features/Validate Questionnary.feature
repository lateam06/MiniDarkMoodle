# Created by Antoine at 11/02/2022
Feature: Validate questionnary

  Background:
    Given a teacher named "Marcel"
    And a student named "Louis"
    And a module named "le Python pour les nuls"
    And a questionnaire with name "Examens Python"
    And "Marcel" is the teacher registered to the module "le Python pour les nuls"
    And "Marcel" has registered the questionnaire "Examens Python" to the module "le Python pour les nuls"
    And "Marcel" has already registered a QCM "question12" to the questionnaire "Examens Python" of the module "le Python pour les nuls"

  Scenario: a student validate his questionnary
    Given "Marcel" has registered "Louis" on the module "le Python pour les nuls"
    When "Louis" validate his questionnary "Examens Python" of the module "le Python pour les nuls"
    Then he gets a 0 because he's bad