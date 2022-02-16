Feature: Module Creation and Managing

  Background:
    Given a teacher named "Marcel"
    Given a teacher named "Ernesto"
    And a student named "Yann"
    * "Ernesto" is connected
    * "Yann" is connected
    * "Marcel" is connected
    And a module named "le C pour les nuls"
    * "Marcel" is the teacher registered to the module "le C pour les nuls"

  Scenario: Teacher create a new module and assign himself on it
    Given "Ernesto" is a teacher not registered to any module
    When "Ernesto" wants to create a new Module "Un super Module d'enfer"
#    Then The module "Un super module d'enfer" is created
#    But "Ernesto wants to add "Yann" to the module "Un super module d'enfer"
  
  Scenario: Teacher want to get all resources of his module
    Given a course named "Les structures" with a description "Ici on apprend a creer des structures"
    * a course named "Les types" with a description "Apprentissage des types primitifs"
    * a course named "La lib standard" with a description "Quelques fonctions utiles"
    And "Marcel" adds the course "Les structures" to the module "le C pour les nuls"
    * "Marcel" adds the course "Les types" to the module "le C pour les nuls"
    * "Marcel" adds the course "La lib standard" to the module "le C pour les nuls"
    When "Marcel" wants to get all resources of the module "le C pour les nuls"
    Then "Marcel" gets all resources of the module "le C pour les nuls" with at least 3 resources
    And find "Les structures" with a description "Ici on apprend a creer des structures"
    * find "Les types" with a description "Apprentissage des types primitifs"
    * find "La lib standard" with a description "Quelques fonctions utiles"

  Scenario: Teacher want to get all resources of a module where he is not registered
    Given a teacher named "Enrico"
    * "Enrico" is connected
    Given a course named "Les structures" with a description "Ici on apprend a creer des structures"
    * a course named "Les types" with a description "Apprentissage des types primitifs"
    * a course named "La lib standard" with a description "Quelques fonctions utiles"
    And "Marcel" adds the course "Les structures" to the module "le C pour les nuls"
    * "Marcel" adds the course "Les types" to the module "le C pour les nuls"
    * "Marcel" adds the course "La lib standard" to the module "le C pour les nuls"
    When "Enrico" wants to get all resources of the module "le C pour les nuls"
    Then "Enrico" can not get all resources of the module "le C pour les nuls"

  Scenario: Student want to get all resources of his module
    Given a student named "Louis"
    * "Louis" is connected
    * "Marcel" has registered "Louis" on the module "le C pour les nuls"
    Given a course named "Les structures" with a description "Ici on apprend a creer des structures"
    * a course named "Les types" with a description "Apprentissage des types primitifs"
    * a course named "La lib standard" with a description "Quelques fonctions utiles"
    And "Marcel" adds the course "Les structures" to the module "le C pour les nuls"
    * "Marcel" adds the course "Les types" to the module "le C pour les nuls"
    * "Marcel" adds the course "La lib standard" to the module "le C pour les nuls"
    When "Louis" wants to get all resources of the module "le C pour les nuls"
    Then "Louis" gets all resources of the module "le C pour les nuls" with at least 3 resources
    And find "Les structures" with a description "Ici on apprend a creer des structures"
    * find "Les types" with a description "Apprentissage des types primitifs"
    * find "La lib standard" with a description "Quelques fonctions utiles"

  Scenario: Student want to get all resources of a module where he is not registered
    Given a student named "Juliette"
    Given a course named "Les structures" with a description "Ici on apprend a creer des structures"
    * a course named "Les types" with a description "Apprentissage des types primitifs"
    * a course named "La lib standard" with a description "Quelques fonctions utiles"
    And "Marcel" adds the course "Les structures" to the module "le C pour les nuls"
    * "Marcel" adds the course "Les types" to the module "le C pour les nuls"
    * "Marcel" adds the course "La lib standard" to the module "le C pour les nuls"
    When "Juliette" wants to get all resources of the module "le C pour les nuls"
    But "Juliette" is not registered to the module "le C pour les nuls"
    Then "Juliette" can not get all resources of the module "le C pour les nuls"