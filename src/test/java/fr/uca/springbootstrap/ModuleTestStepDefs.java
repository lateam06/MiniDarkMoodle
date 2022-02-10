package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.users.User;
import fr.uca.springbootstrap.payload.request.CreateModuleRequest;
import fr.uca.springbootstrap.repository.*;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class ModuleTestStepDefs extends SpringIntegration {

    private final static String PASSWORD = "password";

    @Autowired
    ResourcesRepository resourcesRepository;

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

    @Autowired
    CourseRepository courseRepository;

    @Given("{string} is a teacher not registered to any module")
    public void isATeacherNotRegisteredToAnyModule(String arg0) {
        User user = userRepository.findByUsername(arg0).get();
        List<Module> modules = user.getModules();

        assertTrue(modules.isEmpty());
    }

    @When("{string} wants to create a new Module {string}")
    public void wantsToCreateANewModule(String arg0, String arg1) throws IOException {
        CreateModuleRequest createModuleRequest = new CreateModuleRequest(arg1);
        String jwt = authController.generateJwt(arg0, PASSWORD);

        executePost("http://localhost:8080/api/module/createModule", createModuleRequest, jwt);

        assertTrue(moduleRepository.findByName(arg1).isPresent());
    }

    @Then("The module {string} is created")
    public void theModuleIsCreated(String arg0) {
    }

    @But("{string}Yann{string}Un super module d'enfer\"")
    public void yannUnSuperModuleDEnfer(String arg0, String arg1) throws Throwable {
    }
}
