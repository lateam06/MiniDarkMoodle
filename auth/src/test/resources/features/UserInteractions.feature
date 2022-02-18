Feature: UserInteraction

#SIGN UP
  Scenario: User signup
    Given "Eric Ruommez" (mail : "ruommez@test.fr") is not already registered
    When "Eric Ruommez" (mail : "ruommez@test.fr") want to signup with password "password123" and role "teacher"
    Then "Eric Ruommez" (mail : "ruommez@test.fr") is successfully registered with role "teacher"
    And response status is 200

  Scenario: Same email
    Given "Nice Ruommez" (mail : "ruommez@test.fr") is not already registered
    But "Eric Ruommez" (mail : "ruommez@test.fr") is already registered with password "password123" and role "teacher"
    When "Nice Ruommez" (mail : "ruommez@test.fr") want to signup with password "password123" and role "teacher"
    Then "Nice Ruommez" (mail : "ruommez@test.fr") is not registered
    And response status is 400

  Scenario: Same name
    Given "Eric Ruommez" (mail : "ruommezdenice@test.fr") is not already registered
    But "Eric Ruommez" (mail : "ruommez@test.fr") is already registered with password "password123" and role "teacher"
    When "Eric Ruommez" (mail : "ruommezdenice@test.fr") want to signup with password "password123" and role "teacher"
    Then "Eric Ruommez" (mail : "ruommezdenice@test.fr") is not registered
    And response status is 400

#SIGN IN
  Scenario: User signin
    Given "Eric Ruommez" (mail : "ruommez@test.fr") is already registered with password "password123" and role "teacher"
    When "Eric Ruommez" wants to sign in with password "password123"
    Then "Eric Ruommez" is connected
    And response status is 200

  Scenario: User signin with wrong password
    Given "Eric Ruommez" (mail : "ruommez@test.fr") is already registered with password "password123" and role "teacher"
    When "Eric Ruommez" wants to sign in with password "password1234"
    Then "Eric Ruommez" is not connected

  Scenario: User signin with username not known
    Given "Mister C" (mail : "misterc@test.fr") is not already registered
    When "Mister C" wants to sign in with password "123456"
    Then "Mister C" is not connected

  Scenario: Connect multiple times
    Given "Eric Ruommez" (mail : "ruommez@test.fr") is already registered with password "password123" and role "teacher"
    When "Eric Ruommez" wants to sign in with password "password123"
    Then "Eric Ruommez" is connected
    And response status is 200
    When "Eric Ruommez" wants to sign in with password "password123"
    Then "Eric Ruommez" is connected
    And response status is 200
    When "Eric Ruommez" wants to sign in with password "password123"
    When "Eric Ruommez" wants to sign in with password "password123"
    Then "Eric Ruommez" is connected
    And response status is 200

