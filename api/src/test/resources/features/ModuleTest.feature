Feature: Module Creation and Managing

  Background:
    Given a teacher named "Ernesto"
    And a student named "Yann"
    And "Ernesto" is connected
    And "Yann" is connected
    And a module named "le C pour les nuls"

  Scenario: Teacher create a new module and assign himself on it
    Given "Ernesto" is a teacher not registered to any module
    When "Ernesto" wants to create a new Module "Un super Module d'enfer"
#    Then The module "Un super module d'enfer" is created
#    But "Ernesto wants to add "Yann" to the module "Un super module d'enfer"