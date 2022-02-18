Feature: AddQuestion

  Background:
    Given a teacher named "Marcel"
    And a student named "Louis"
    And "Marcel" is connected
    And "Louis" is connected
    And a module named "le C pour les nuls"
    And "Marcel" is the teacher registered to the module "le C pour les nuls"
    And a questionnaire with name "Examens Pointeurs"
    And "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"

# ADD
  Scenario: Teacher add a question on a module where he isn't registered
    Given a module named "le C++ pour les nuls"
    And a teacher named "Enrico"
    And "Enrico" is connected
    And "Enrico" is the teacher registered to the module "le C++ pour les nuls"
    And a questionnaire with name "Examens Pointeurs Intelligents"
    And "Enrico" has registered the questionnaire "Examens Pointeurs Intelligents" to the module "le C++ pour les nuls"
    And "Marcel" is not a teacher registered to the module "le C++ pour les nuls"
    When "Marcel" wants to add a QCM "Syntaxe" to the questionnaire "Examens Pointeurs Intelligents" of the module "le C++ pour les nuls"
    Then the QCM "Syntaxe" is not added to the questionnaire "Examens Pointeurs Intelligents" and the return status of the request is error

  Scenario: Teacher want to add a QCM on a questionnaire of his module
    Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
    When "Marcel" wants to add a QCM "Syntaxe" to the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
    Then the QCM "Syntaxe" is added to the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"

  Scenario: Teacher want to add an Open on a questionnaire of his module
    Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
    When "Marcel" wants to add an Open "Integer size" to the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
    Then the Open "Integer size" is added to the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"

  Scenario: Student add a QCM on a questionnaire of his module
    Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
    And "Marcel" has registered "Louis" on the module "le C pour les nuls"
    When "Louis" wants to add a QCM "Syntaxe" to the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
    Then the QCM "Syntaxe" is not added to the questionnaire "Examens Pointeurs Intelligents" and the return status of the request is error

  Scenario: Student want to add an Open on a questionnaire of his module
    Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
    And "Marcel" has registered "Louis" on the module "le C pour les nuls"
    When "Louis" wants to add an Open "Integer size" to the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
    Then the Open "Integer size" is not added to the questionnaire "Examens Pointeurs Intelligents" and the return status of the request is error




# DELETE
    Scenario: Teacher want to delete a QCM on a questionnaire of his module
      Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
      And "Marcel" has already registered a QCM "Syntaxe" to the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
      When "Marcel" wants to delete a Question "Syntaxe" from the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
      Then the Question "Syntaxe" is deleted from the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"

    Scenario: Teacher want to delete an Open on a questionnaire of his module
      Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
      And "Marcel" has already registered an Open "Integer Size" to the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
      When "Marcel" wants to delete a Question "Integer Size" from the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
      Then the Question "Integer Size" is deleted from the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"

    Scenario: Student want to delete a QCM on a questionnaire of his module
      Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
      And "Marcel" has already registered a QCM "Syntaxe" to the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
      When "Louis" wants to delete a Question "Syntaxe" from the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
      Then the Question "Syntaxe" is not deleted from the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"

    Scenario: Student want to delete an Open on a questionnaire of his module*/
      Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
      And "Marcel" has already registered an Open "Integer Size" to the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
      When "Louis" wants to delete a Question "Integer size" from the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
      Then the Question "Integer size" is not deleted from the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"

    Scenario: Teacher want to delete a QCM on a questionnaire of a module where he is not registered
      Given a module named "le C++ pour les nuls"
      And a teacher named "Enrico"
      And "Enrico" is connected
      And "Enrico" is the teacher registered to the module "le C++ pour les nuls"
      And "Enrico" has registered the questionnaire "Examens Pointeurs Intelligents" to the module "le C++ pour les nuls"
      And "Enrico" has already registered a QCM "Unique Ptr" to the questionnaire "Examens Pointeurs Intelligents" of the module "le C++ pour les nuls"
      When "Marcel" wants to delete a Question "Unique Ptr" from the questionnaire "Examens Pointeurs Intelligents" of the module "le C++ pour les nuls"
      Then the Question "Unique Ptr" is not deleted from the questionnaire "Examens Pointeurs Intelligents" of the module "le C++ pour les nuls"



# GET
  Scenario: Teacher want to get a QCM from his questionnaire of his module
    Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
    And "Marcel" has already registered a QCM "Syntaxe" to the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
    When "Marcel" wants to get a QCM "Syntaxe" from the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
    Then "Marcel" can succesfully get the QCM "Syntaxe" from the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"

  Scenario: Student want to get a QCM from his questionnaire of his module
    Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
    And "Marcel" has already registered a QCM "Syntaxe" to the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
    And "Marcel" has registered "Louis" on the module "le C pour les nuls"
    When "Louis" wants to get a QCM "Syntaxe" from the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
    Then "Louis" can succesfully get the QCM "Syntaxe" from the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"

  Scenario: Teacher want to get an OpenQuestion from his questionnaire of his module
    Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
    And "Marcel" has already registered an Open "Integer size" to the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
    When "Marcel" wants to get an OpenQuestion "Integer size" from the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
    Then "Marcel" can succesfully get the OpenQuestion "Integer size" from the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"

  Scenario: Student want to get an OpenQuestion from his questionnaire of his module
    Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
    And "Marcel" has already registered an Open "Integer size" to the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
    And "Marcel" has registered "Louis" on the module "le C pour les nuls"
    When "Louis" wants to get an OpenQuestion "Integer size" from the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
    Then "Louis" can succesfully get the OpenQuestion "Integer size" from the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"

  Scenario: Student want to get a QCM from a questionnaire of a module where he is not registered
    Given a module named "le C++ pour les nuls"
    And a teacher named "Enrico"
    And "Enrico" is connected
    And "Enrico" is the teacher registered to the module "le C++ pour les nuls"
    And "Enrico" has registered the questionnaire "Examens Pointeurs Intelligents" to the module "le C++ pour les nuls"
    And "Enrico" has already registered a QCM "Unique Ptr" to the questionnaire "Examens Pointeurs Intelligents" of the module "le C++ pour les nuls"
    Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
    And "Marcel" has already registered a QCM "Syntaxe" to the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
    And "Marcel" has registered "Louis" on the module "le C pour les nuls"
    When "Louis" wants to get a QCM "Unique Ptr" from the questionnaire "Examens Pointeurs Intelligents" of the module "le C++ pour les nuls"
    Then "Louis" can not get the QCM "Unique Ptr" from the questionnaire "Examens Pointeurs Intelligents" of the module "le C++ pour les nuls"

  Scenario: Teacher want to get a QCM from a questionnaire of a module where he is not registered
    Given a module named "le C++ pour les nuls"
    And a teacher named "Enrico"
    And "Enrico" is connected
    And "Enrico" is the teacher registered to the module "le C++ pour les nuls"
    And "Enrico" has registered the questionnaire "Examens Pointeurs Intelligents" to the module "le C++ pour les nuls"
    And "Enrico" has already registered a QCM "Unique Ptr" to the questionnaire "Examens Pointeurs Intelligents" of the module "le C++ pour les nuls"
    Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
    And "Marcel" has already registered a QCM "Syntaxe" to the questionnaire "Examens Pointeurs" of the module "le C pour les nuls"
    When "Marcel" wants to get a QCM "Unique Ptr" from the questionnaire "Examens Pointeurs Intelligents" of the module "le C++ pour les nuls"
    Then "Marcel" can not get the QCM "Unique Ptr" from the questionnaire "Examens Pointeurs Intelligents" of the module "le C++ pour les nuls"

# MODIFY
  Scenario: Teacher want to modify a QCM from his questionnaire of his module
    Given a questionnaire with name "Cpp 17 Ptr"
    Given "Marcel" has registered the questionnaire "Cpp 17 Ptr" to the module "le C pour les nuls"
    And "Marcel" has already registered a QCM "Box" to the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls"
    When "Marcel" wants to modify the QCM "Box" from the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls" and set the response to "les pointeurs c dur"
    Then The response of the QCM "Box" from the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls" is "les pointeurs c dur"

  Scenario: Teacher want to modify a QCM description from his questionnaire of his module
    Given a questionnaire with name "Cpp 17 Ptr"
    Given "Marcel" has registered the questionnaire "Cpp 17 Ptr" to the module "le C pour les nuls"
    And "Marcel" has already registered a QCM "Box" to the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls"
    When "Marcel" wants to modify the QCM "Box" from the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls" and set the description to "Voici ma nouvelle description"
    Then The description of the QCM "Box" from the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls" is "Voici ma nouvelle description"

  Scenario: Teacher want to modify an Open from his questionnaire of his module
    Given a questionnaire with name "Cpp 17 Ptr"
    Given "Marcel" has registered the questionnaire "Cpp 17 Ptr" to the module "le C pour les nuls"
    And "Marcel" has already registered an Open "Box syntaxe" to the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls"
    When "Marcel" wants to modify the Open "Box syntaxe" from the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls" and set the response to "la vraie syntaxe c'est ca"
    Then The response of the Open "Box syntaxe" from the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls" is "la vraie syntaxe c'est ca"

  Scenario: Teacher want to modify an Open from his questionnaire of his module
    Given a questionnaire with name "Cpp 17 Ptr"
    Given "Marcel" has registered the questionnaire "Cpp 17 Ptr" to the module "le C pour les nuls"
    And "Marcel" has already registered an Open "Box syntaxe" to the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls"
    When "Marcel" wants to modify the Open "Box syntaxe" from the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls" and set the description to "la vraie description c'est ca"
    Then The description of the Open "Box syntaxe" from the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls" is "la vraie description c'est ca"

  Scenario: Teacher want to modify a QCM from his questionnaire of a module where he isn't registered
    Given a teacher named "Enrico"
    Given a questionnaire with name "Cpp 17 Ptr"
    Given "Marcel" has registered the questionnaire "Cpp 17 Ptr" to the module "le C pour les nuls"
    And "Marcel" has already registered a QCM "Box" to the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls"
    When "Enrico" wants to modify the QCM "Box" from the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls" and set the response to "les pointeurs c dur"
    But "Enrico" is not a teacher registered to the module "le C pour les nuls"
    Then The response of the QCM "Box" from the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls" is "reponse a la question"

  Scenario: Student want to modify a QCM
    Given a questionnaire with name "Cpp 17 Ptr"
    Given "Marcel" has registered the questionnaire "Cpp 17 Ptr" to the module "le C pour les nuls"
    And "Marcel" has already registered a QCM "Box" to the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls"
    And "Marcel" has registered "Louis" on the module "le C pour les nuls"
    When "Louis" wants to modify the QCM "Box" from the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls" and set the response to "les pointeurs c dur"
    Then The response of the QCM "Box" from the questionnaire "Cpp 17 Ptr" of the module "le C pour les nuls" is "reponse a la question"

#    Scenario: Teacher want to add an answer to a QCM
#      Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
#      And a QCM named "Syntaxe"
#      And "Marcel" has already added the QCM "Syntaxe" to the questionnaire "Examens Pointeurs"
#      When "Marcel" want to answer to the questionnaire "Examens Pointeurs"
#      Then The answer isn't treated

#    Scenario: Student want to add an answer to a QCM
#      Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
#      And a QCM named "Syntaxe"
#      And "Marcel" has already added the QCM "Syntaxe" to the questionnaire "Examens Pointeurs"
#      When "Louis" want to answer to the questionnaire "Examens Pointeurs"
#      Then The answer is added to the QCM "Syntaxe"

#    Scenario: Teacher want to update an answer to a QCM
#      Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
#      And a QCM named "Syntaxe"
#      And "Marcel" has already added the QCM "Syntaxe" to the questionnaire "Examens Pointeurs"
#      When "Marcel" want to update his answer on the questionnaire
#      Then The answer isn't treated

#    Scenario: Student want to update an answer to a QCM
#      Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
#      And a QCM named "Syntaxe"
#      And "Marcel" has already added the QCM "Syntaxe" to the questionnaire "Examens Pointeurs"
#      When "Louis" want to update his answer to the questionnaire "Examens Pointeurs"
#      Then The answer is updated on the QCM "Syntaxe"

#    Scenario: Teacher want to delete his answer from a QCM
#      Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
#      And a QCM named "Syntaxe"
#      And "Marcel" has already added the QCM "Syntaxe" to the questionnaire "Examens Pointeurs"
#      When "Marcel" want to delete his answer from the questionnaire "Examens Pointeurs"
#      Then The answer isn't deleted

#    Scenario: Student want to delete his answer from a QCM
#      Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
#      And a QCM named "Syntaxe"
#      And "Marcel" has already added the QCM "Syntaxe" to the questionnaire "Examens Pointeurs"
#      When "Louis" want to delete his answer from to the questionnaire "Examens Pointeurs"
#      Then The answer is deleted from the QCM "Syntaxe"

#    Scenario: Teacher want to get his answer to a QCM
#      Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
#      And a QCM named "Syntaxe"
#      And "Marcel" has already added the QCM "Syntaxe" to the questionnaire "Examens Pointeurs"
#      When "Marcel" want to delete his answer from the questionnaire "Examens Pointeurs"
#      Then "Marcel" doesn't get his answer from the QCM "Syntaxe"

#    Scenario: Student want to get his answer to a QCM
#      Given "Marcel" has registered the questionnaire "Examens Pointeurs" to the module "le C pour les nuls"
#      And a QCM named "Syntaxe"
#      And "Marcel" has already added the QCM "Syntaxe" to the questionnaire "Examens Pointeurs"
#      When "Louis" want to get his answer from to the questionnaire "Examens Pointeurs"
#      Then "Louis" get his answer from the QCM "Syntaxe"

