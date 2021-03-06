Feature: CodeRunner

  Background:
    Given a teacher named "Arnaud"
    * "Arnaud" is connected
    * a teacher named "Marcel"
    * "Marcel" is connected
    And a module named "le C pour les nuls"
    And a module named "Programming Challenge"
    And a questionnaire with name "questionnaire"
    * "Arnaud" is the teacher registered to the module "Programming Challenge"
    * a questionnaire with name "Algos de graphes"
    * "Arnaud" has registered the questionnaire "Algos de graphes" to the module "Programming Challenge"
    * a CodeRunner Question "factoriel en python"
    And a student named "Sami"
    * "Sami" is connected
    And a student named "Louis"
    * "Louis" is connected


  # ADD
    Scenario: Teacher add a code question on a questionnaire of his module
      When "Arnaud" wants to add the code question named "Find with DFS" with testCode "print(dfs(L))" and response "10" on "Algos de graphes" of "Programming Challenge"
      Then the code question "Find with DFS" is added on "Algos de graphes" of "Programming Challenge"

    Scenario: Teacher add a code question on a module where he isn't registered
      Given a teacher named "Enrico"
      * "Enrico" is connected
      When "Enrico" wants to add the code question named "Find with BFS" with testCode "print(bfs(L))" and response "5" on "Algos de graphes" of "Programming Challenge"
      But "Enrico" is not a teacher registered to the module "Programming Challenge"
      Then the code question "Find with BFS" is not added on "Algos de graphes" of "Programming Challenge"

    Scenario: Student add a code question
      Given "Arnaud" has registered "Sami" on the module "Programming Challenge"
      When "Sami" wants to add the code question named "Find with BFS" with testCode "print(bfs(L))" and response "5" on "Algos de graphes" of "Programming Challenge"
      Then the code question "Find with BFS" is not added on "Algos de graphes" of "Programming Challenge"

  # GET
    Scenario: Teacher get a code question on a questionnaire of his module
      Given "Arnaud" has already registered a code question code question named "Find with Djikstra" with testCode "print(djikstra(L))" and response "15" on "Algos de graphes" of "Programming Challenge"
      When "Arnaud" wants to get the code question "Find with Djikstra" from "Algos de graphes" of "Programming Challenge"
      Then "Arnaud" successfully got the code question "Find with Djikstra" from "Algos de graphes" of "Programming Challenge"

    Scenario: Teacher get a code question on a module where he isn't registered
      Given a teacher named "Enrico"
      * "Enrico" is connected
      Given "Arnaud" has already registered a code question code question named "Find with Djikstra" with testCode "print(djikstra(L))" and response "15" on "Algos de graphes" of "Programming Challenge"
      When "Enrico" wants to get the code question "Find with Djikstra" from "Algos de graphes" of "Programming Challenge"
      But "Enrico" is not a teacher registered to the module "Programming Challenge"
      Then "Enrico" can not get the code question "Find with Djikstra" from "Algos de graphes" of "Programming Challenge"

    Scenario: Student get a code question on a questionnaire of his module
      Given "Arnaud" has already registered a code question code question named "Find with Djikstra" with testCode "print(djikstra(L))" and response "15" on "Algos de graphes" of "Programming Challenge"
      And "Arnaud" has registered "Sami" on the module "Programming Challenge"
      When "Sami" wants to get the code question "Find with Djikstra" from "Algos de graphes" of "Programming Challenge"
      Then "Sami" successfully got the code question "Find with Djikstra" from "Algos de graphes" of "Programming Challenge"

    Scenario: Student get a code question on a module where he isn't registered
      Given a student named "Margaux"
      * "Margaux" is connected
      Given "Arnaud" has already registered a code question code question named "Find with Djikstra" with testCode "print(djikstra(L))" and response "15" on "Algos de graphes" of "Programming Challenge"
      And "Arnaud" has registered "Sami" on the module "Programming Challenge"
      When "Margaux" wants to get the code question "Find with Djikstra" from "Algos de graphes" of "Programming Challenge"
      But "Margaux" is not registered to the module "Programming Challenge"
      Then "Margaux" can not get the code question "Find with Djikstra" from "Algos de graphes" of "Programming Challenge"

  # DELETE
    Scenario: Teacher delete a code question on a questionnaire of his module
      Given "Arnaud" has already registered a code question code question named "Find with Djikstra" with testCode "print(djikstra(L))" and response "15" on "Algos de graphes" of "Programming Challenge"
      When "Arnaud" wants to delete the code question "Find with Djikstra" from "Algos de graphes" of "Programming Challenge"
      Then the code question "Find with Djikstra" is deleted from "Algos de graphes" of "Programming Challenge"

    Scenario: Teacher delete a code question on a module where he isn't registered
      Given a teacher named "Enrico"
      * "Enrico" is connected
      Given "Arnaud" has already registered a code question code question named "Find with Djikstra" with testCode "print(djikstra(L))" and response "15" on "Algos de graphes" of "Programming Challenge"
      When "Enrico" wants to delete the code question "Find with Djikstra" from "Algos de graphes" of "Programming Challenge"
      But "Enrico" is not a teacher registered to the module "Programming Challenge"
      Then the code question "Find with Djikstra" is not deleted from "Algos de graphes" of "Programming Challenge"

    Scenario: Student delete a code question
      Given "Arnaud" has already registered a code question code question named "Find with Djikstra" with testCode "print(djikstra(L))" and response "15" on "Algos de graphes" of "Programming Challenge"
      When "Sami" wants to delete the code question "Find with Djikstra" from "Algos de graphes" of "Programming Challenge"
      Then the code question "Find with Djikstra" is not deleted from "Algos de graphes" of "Programming Challenge"

  # MODIFY
    Scenario: Teacher modify a code question response on a questionnaire of his module
      Given "Arnaud" has already registered a code question code question named "Find Bellman-Ford" with testCode "print(bm(L))" and response "20" on "Algos de graphes" of "Programming Challenge"
      When "Arnaud" wants to modify the code question "Find Bellman-Ford" from "Algos de graphes" of "Programming Challenge" and change the response to "25"
      Then the code question "Find Bellman-Ford" from "Algos de graphes" of "Programming Challenge" response is "25"

    Scenario: Teacher modify a code question testCode on a questionnaire of his module
      Given "Arnaud" has already registered a code question code question named "Find Bellman-Ford" with testCode "print(bm(L))" and response "20" on "Algos de graphes" of "Programming Challenge"
      When "Arnaud" wants to modify the code question "Find Bellman-Ford" from "Algos de graphes" of "Programming Challenge" and change the testCode to "print(bford(L))"
      Then the code question "Find Bellman-Ford" from "Algos de graphes" of "Programming Challenge" testCode is "print(bford(L))"

    Scenario: Teacher modify a code question on a module where he isn't registered
      Given a teacher named "Enrico"
      * "Enrico" is connected
      And "Arnaud" has already registered a code question code question named "Find Bellman-Ford" with testCode "print(bm(L))" and response "20" on "Algos de graphes" of "Programming Challenge"
      When "Enrico" wants to modify the code question "Find Bellman-Ford" from "Algos de graphes" of "Programming Challenge" and change the response to "25"
      But "Enrico" is not a teacher registered to the module "Programming Challenge"
      Then the code question "Find Bellman-Ford" from "Algos de graphes" of "Programming Challenge" response is "20"

    Scenario: Student modify a code question response
      Given "Arnaud" has already registered a code question code question named "Find Bellman-Ford" with testCode "print(bm(L))" and response "20" on "Algos de graphes" of "Programming Challenge"
      When "Sami" wants to modify the code question "Find Bellman-Ford" from "Algos de graphes" of "Programming Challenge" and change the response to "25"
      Then the code question "Find Bellman-Ford" from "Algos de graphes" of "Programming Challenge" response is "20"

    Scenario: Student modify a code question testCode
      Given "Arnaud" has already registered a code question code question named "Find Bellman-Ford" with testCode "print(bm(L))" and response "20" on "Algos de graphes" of "Programming Challenge"
      When "Sami" wants to modify the code question "Find Bellman-Ford" from "Algos de graphes" of "Programming Challenge" and change the testCode to "print(bford(L))"
      Then the code question "Find Bellman-Ford" from "Algos de graphes" of "Programming Challenge" testCode is "print(bm(L))"


# ATTEMPT CODE RUNNER
  Scenario: the teacher add the Student to the module, add the Questionnary and the question
    Given "Marcel" is the teacher registered to the module "le C pour les nuls"
    And "Marcel" has registered "Louis" on the module "le C pour les nuls"
    When "Marcel" wants to add the questionnaire "questionnaire" to the module "le C pour les nuls"
    And "Marcel" wants to add a CodeRunner "factoriel en python" to the questionnaire "questionnaire" from the module "le C pour les nuls"
    And "Louis" wants to answer the CodeRunner "factoriel en python" of "questionnaire" from "le C pour les nuls"
    When "Louis" validate his questionnary  with the code runner "questionnaire" of the module "le C pour les nuls"
    Then "Louis" get a 1 because his reponse is true
