Feature: UserInteraction

#SIGN UP
  Scenario: User signup
    Given "Eric Zemmour" (mail : "zemmour@test.fr") is not already registered
    When "Eric Zemmour" (mail : "zemmour@test.fr") want to signup with password "password123" and role "teacher"
    Then "Eric Zemmour" (mail : "zemmour@test.fr") is successfully registered with role "teacher"
    And response status is 200

  Scenario: Same email
    Given "Nice Zemmour" (mail : "zemmour@test.fr") is not already registered
    But "Eric Zemmour" (mail : "zemmour@test.fr") is already registered with password "password123" and role "teacher"
    When "Nice Zemmour" (mail : "zemmour@test.fr") want to signup with password "password123" and role "teacher"
    Then "Nice Zemmour" (mail : "zemmour@test.fr") is not registered
    And response status is 400

  Scenario: Same name
    Given "Eric Zemmour" (mail : "zemmourdenice@test.fr") is not already registered
    But "Eric Zemmour" (mail : "zemmour@test.fr") is already registered with password "password123" and role "teacher"
    When "Eric Zemmour" (mail : "zemmourdenice@test.fr") want to signup with password "password123" and role "teacher"
    Then "Eric Zemmour" (mail : "zemmourdenice@test.fr") is not registered
    And response status is 400

#SIGN IN
  Scenario: User signin
    Given "Eric Zemmour" (mail : "zemmour@test.fr") is already registered with password "password123" and role "teacher"
    When "Eric Zemmour" wants to sign in with password "password123"
    Then "Eric Zemmour" is connected
    And response status is 200

  Scenario: User signin with wrong password
    Given "Eric Zemmour" (mail : "zemmour@test.fr") is already registered with password "password123" and role "teacher"
    When "Eric Zemmour" wants to sign in with password "password1234"
    Then "Eric Zemmour" is not connected

  Scenario: User signin with username not known
    Given "Mister C" (mail : "misterc@test.fr") is not already registered
    When "Mister C" wants to sign in with password "123456"
    Then "Mister C" is not connected

  Scenario: Connect multiple times
    Given "Eric Zemmour" (mail : "zemmour@test.fr") is already registered with password "password123" and role "teacher"
    When "Eric Zemmour" wants to sign in with password "password123"
    Then "Eric Zemmour" is connected
    And response status is 200
    When "Eric Zemmour" wants to sign in with password "password123"
    Then "Eric Zemmour" is connected
    And response status is 200
    When "Eric Zemmour" wants to sign in with password "password123"
    When "Eric Zemmour" wants to sign in with password "password123"
    Then "Eric Zemmour" is connected
    And response status is 200

