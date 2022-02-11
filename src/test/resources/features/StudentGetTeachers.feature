Feature: Student get teachers

  Background:
    Given a teacher named "Marcel"
    And the teacher "Marcel" is connected
    And a student named "Louis"
    And a module named "le C pour les nuls"
    And "Marcel" is the teacher registered to the module "le C pour les nuls"

  Scenario:
    Given "Marcel" registers "Louis" as a student to module "le C pour les nuls"
    When "Louis" wants to access the teacher of the module "le C pour les nuls"
    Then the list of teachers is send and the return status of the request is 200

  Scenario:
    When "Louis" wants to access the teacher of the module "le C pour les nuls"
    Then his request is rejected and the return status of the request is 403