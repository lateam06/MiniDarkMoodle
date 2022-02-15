package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;

import fr.uca.springbootstrap.models.modules.Module;

import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserApiRepository;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterStudentStepDefs  extends  SpringIntegration{
    private final static String PASSWORD = "password";

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserApiRepository userRepository;

    @Autowired
    AuthController authController;


    @Then("the last request status is {int}")
    public void isRegistered(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @When("{string} tries to register himself to the module {string}")
    public void triesToRegisterHimselfToTheModule(String arg0, String arg1) throws Exception{
        Module module = moduleRepository.findByName(arg1).get();
        UserApi userApi = userRepository.findByUsername(arg0).get();
        String jwt = SpringIntegration.tokenHashMap.get(arg0);

        executePost("http://localhost:8080/api/module/"+module.getId()+"/participants/"+ userApi.getId(), jwt);
    }

}
