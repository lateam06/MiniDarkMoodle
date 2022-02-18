package fr.lateam.auth.stepdefs;

import com.lateam.payload.request.LoginRequest;
import com.lateam.payload.request.SignupRequest;
import com.lateam.payload.response.JwtResponse;
import fr.lateam.auth.springbootsrap.models.users.User;
import fr.lateam.auth.springbootsrap.repository.RoleRepository;
import fr.lateam.auth.springbootsrap.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserInteractionsStepDefs extends SpringRequests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public static final String BASE_URL = "http://localhost:8081/api/auth";

    @Given("{string} \\(mail : {string}) is already registered with password {string} and role {string}")
    public void mailIsAlreadyRegistered(String userName, String userMail, String password, String role) {
        User user = userRepository.findByUsernameAndEmail(userName, userMail)
                .orElse(new User(userName, userMail, password));
        userRepository.save(user);
    }

    @Given("{string} \\(mail : {string}) is not already registered")
    public void mailIsNotAlreadyRegistered(String userName, String userMail) {
        Optional<User> userOptional = userRepository.findByUsernameAndEmail(userName, userMail);
        userOptional.ifPresent(user -> userRepository.delete(user));
        userOptional = userRepository.findByUsernameAndEmail(userName, userMail);
        assertTrue(userOptional.isEmpty());
    }

    @When("{string} \\(mail : {string}) want to signup with password {string} and role {string}")
    public void mailWantToSignupWithPasswordAndRole(String userName, String userMail, String password, String role) throws IOException {
        var signup = new SignupRequest(userName, userMail, Collections.singleton(role), password);

        String url = BASE_URL + "/signup";
        executePost(url, signup, null);
    }

    @Then("{string} \\(mail : {string}) is successfully registered with role {string}")
    public void mailIsSuccessfullyRegisteredWithPasswordAndRole(String userName, String userMail, String role) {
        Optional<User> optionalUser = userRepository.findByUsernameAndEmail(userName, userMail);

        assertTrue(optionalUser.isPresent());

        User user = optionalUser.get();
    }

    @And("response status is {int}")
    public void responseStatusIs(int status) {
        assertEquals(status, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Then("{string} \\(mail : {string}) is not registered")
    public void mailIsNotRegistered(String userName, String userMail) {
        assertTrue(userRepository.findByUsernameAndEmail(userName, userMail).isEmpty());
    }

    @Then("{string} is connected")
    public void isConnected(String userName) {
        assertEquals(userName, lastJwtReponse.getUsername());
    }

    @When("{string} wants to sign in with password {string}")
    public void wantsToSignInWithPassword(String userName, String password) throws IOException {
        LoginRequest loginRequest = new LoginRequest(userName, password);

        String url = "http://localhost:8081/api/auth/signin";
        executePost(url, loginRequest, null);

        if(latestHttpResponse.getStatusLine().getStatusCode() == 200)
        {
            String bodyResponseAuthServer = EntityUtils.toString(latestHttpResponse.getEntity());
            lastJwtReponse = ObjMapper.readValue(bodyResponseAuthServer, JwtResponse.class);
        }
    }

    @Then("{string} is not connected")
    public void isNotConnected(String arg0) {
        assertTrue(latestHttpResponse.getStatusLine().getStatusCode() >= 400);
    }
}