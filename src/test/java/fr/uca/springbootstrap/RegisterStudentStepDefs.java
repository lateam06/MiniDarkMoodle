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

    @Given("a new teacher with login {string}")
    public void aTeacherWithLogin(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>(){{ add(roleRepository.findByName(ERole.ROLE_TEACHER).
                orElseThrow(() -> new RuntimeException("Error: Role is not found"))); }});
        userRepository.save(user);
    }

    @And("a student with login {string}")
    public void aStudentWithLogin(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>(){{ add(roleRepository.findByName(ERole.ROLE_STUDENT).
                orElseThrow(() -> new RuntimeException("Error: Role is not found"))); }});
        userRepository.save(user);
    }

    @And("with a module named {string}")
    public void aModuleNamed(String arg0) {
        Module module = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        module.setParticipants(new HashSet<>());
        moduleRepository.save(module);
    }


    @When("{string} registers {string} as a student to module {string}")
    public void registersStudentToModule(String arg0, String arg1, String arg2) throws Exception {
        Module module = moduleRepository.findByName(arg2).get();
        User teacher = userRepository.findByUsername(arg0).get();
        User student = userRepository.findByUsername(arg1).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);

        System.out.println("http://localhost:8080/api/module"+module.getId()+"/participants/"+teacher.getId());
        System.out.println("http://localhost:8080/api/module"+module.getId()+"/participants/"+student.getId());
        executePost("http://localhost:8080/api/module/"+module.getId()+"/participants/"+teacher.getId(), jwt);
        executePost("http://localhost:8080/api/module/"+module.getId()+"/participants/"+student.getId(), jwt);
    }

    @Then("the last valid request status is {int}")
    public void isRegisteredToModule(int status) {
        assertEquals(status, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @And("{string} is registered to module {string} as a student")
    public void isRegisteredToModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        assertTrue(module.getParticipants().contains(user));
    }


    @When("{string} tries to register himself to the module {string}")
    public void triesToRegisterHimselfToTheModule(String arg0, String arg1) throws Exception{
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);

        executePost("http://localhost:8080/api/module/"+module.getId()+"/participants/"+user.getId(), jwt);
    }

    @Then("the last invalid request status is {int}")
    public void isNotRegisteredToModule(int status) {
        assertEquals(status, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @And("{string} is not registered to the module {string}")
    public void isNotRegisteredToTheModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        assertFalse(module.getParticipants().contains(user));
    }
}
