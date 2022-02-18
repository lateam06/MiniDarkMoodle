Feature: Text of a course

  Background:
    Given a teacher named "Marcel"
    Given a student named "Antoine"
    And "Marcel" is connected
    And "Antoine" is connected
    And a module named "cours avec texte"
    And a course with name "premier cours"
    And "Marcel" is the teacher registered to the module "cours avec texte"
    And the course "premier cours" has been added by "Marcel" into "cours avec texte"

  Scenario: Student can't read the texts
    Given a student named "Bernard"
    And "Bernard" is connected
    When "Bernard" read the course "premier cours" of the module "cours avec texte"
    Then he can't read the course

  Scenario: Student can read the texts
    Given "Marcel" has registered "Antoine" on the module "cours avec texte"
    When "Antoine" read the course "premier cours" of the module "cours avec texte"
    Then he can read the course

  Scenario: Teacher add Text
    When "Marcel" add the text "Les pointeurs en C c'est vraiment dur" to the course "premier cours" in the module "cours avec texte"
    Then the text "Les pointeurs en C c'est vraiment dur" has been added to the course "premier cours"

  Scenario: Teacher modify a text
    Given "Marcel" has already registered the text "text before" in the course "premier cours" of the module "cours avec texte"
    When "Marcel" replace the text "text before" of "premier cours" in "text after" in the module "cours avec texte"
    Then the text "text before" in course "premier cours" is now "text after"

  Scenario: Teacher delete a text
    Given "Marcel" has already registered the text "text before" in the course "premier cours" of the module "cours avec texte"
    When "Marcel" delete the text "text before" of the course "premier cours" in the module "cours avec texte"
    Then the text "text before" does not exist in the course "premier cours"