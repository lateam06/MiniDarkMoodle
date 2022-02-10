package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.users.ERole;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.users.Role;
import fr.uca.springbootstrap.models.users.User;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterStudentStepDefs  extends  SpringIntegration{
    private final static String PASSWORD = "password";

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    @And("a student with login {string}")
    public void aStudentWithLogin(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>(){{ add(roleRepository.findByName(ERole.ROLE_STUDENT).
                orElseThrow(() -> new RuntimeException("Error: Role is not found"))); }});
        userRepository.save(user);
    }


    @When("{string} registers {string} as a student to module {string}")
    public void registersStudentToModule(String arg0, String arg1, String arg2) throws Exception {
        Module module = moduleRepository.findByName(arg2).get();
        User teacher = userRepository.findByUsername(arg0).get();
        User student = userRepository.findByUsername(arg1).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);

        executePost("http://localhost:8080/api/module/"+module.getId()+"/participants/"+teacher.getId(), jwt);
        EntityUtils.consume(latestHttpResponse.getEntity());
        executePost("http://localhost:8080/api/module/"+module.getId()+"/participants/"+student.getId(), jwt);
    }

    @Then("the last request status is {int}")
    public void isRegistered(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @When("{string} tries to register himself to the module {string}")
    public void triesToRegisterHimselfToTheModule(String arg0, String arg1) throws Exception{
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);

        executePost("http://localhost:8080/api/module/"+module.getId()+"/participants/"+user.getId(), jwt);
    }

}