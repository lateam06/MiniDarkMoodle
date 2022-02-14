Feature: Student get teachers

  Background:
    Given a teacher named "Marcel"
    And the teacher "Marcel" is connected
    And a student named "Louis"
    And a module named "le C pour les nuls"
    And "Marcel" is the teacher registered to the module "le C pour les nuls"

  Scenario: student access to module's teacher
    Given "Marcel" has registered "Louis" on the module "le C pour les nuls"
    When "Louis" wants to access the teacher of the module "le C pour les nuls"
    Then "Louis" sees that the teacher is "Marcel"

  Scenario: student can't access module's teacher
    When "Louis" wants to access the teacher of the module "le C pour les nuls"
    Then the list of teacher is not send and the return status of the request is error