Feature: Text of a course

  Background:
    Given a teacher named "Marcel"
    And a student named "Louis"
    And "Marcel" is connected
    And "Louis" is connected
    And a module named "le C pour les nuls"
    And a course with name "introduction"

  Scenario: Teacher add Text
    Given "Marcel" is the teacher registered to the module "le C pour les nuls"
    And the course "introduction" has been added by "Marcel" into "le C pour les nuls"
    When "Marcel" add the text "Les pointeurs en C c'est vraiment dur" to the course "introduction" in the module "le C pour les nuls"
    Then the text "Les pointeurs en C c'est vraiment dur" has been added to the course "introduction"

  Scenario: Teacher modify a text
    Given "Marcel" is the teacher registered to the module "le C pour les nuls"
    And the course "introduction" has been added by "Marcel" into "le C pour les nuls"
    And the text "text before" is in the course "introduction"
    When "Marcel" replace the text "text before" of "introduction" in "text after" in the module "le C pour les nuls"
    Then the text "text before" does not exist
    And the text "text after" is now in the course "introduction"

  Scenario: Teacher delete a text
    Given "Marcel" is the teacher registered to the module "le C pour les nuls"
    And the course "introduction" has been added by "Marcel" into "le C pour les nuls"
    And the text "text before" is in the course "introduction"
    When "Marcel" delete the text "text before" of the course "introduction" in the module "le C pour les nuls"
    Then the text "text before" does not exist in the course "introduction"