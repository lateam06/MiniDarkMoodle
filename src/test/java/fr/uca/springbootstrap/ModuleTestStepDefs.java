package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.users.User;
import fr.uca.springbootstrap.payload.request.CreateModuleRequest;
import fr.uca.springbootstrap.repository.*;
import io.cucumber.java.en.*;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.HashSet;
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

    @Given("{string} is the teacher registered to the module {string}")
    public void isTheTeacherRegisteredToTheModule(String arg0, String arg1) throws IOException {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/module/" + module.getId() + "/participants/" + user.getId(), jwt);
        EntityUtils.consume(latestHttpResponse.getEntity());
    }

    @When("{string} wants to create a new Module {string}")
    public void wantsToCreateANewModule(String arg0, String arg1) throws IOException {
        CreateModuleRequest createModuleRequest = new CreateModuleRequest(arg1);
        String jwt = authController.generateJwt(arg0, PASSWORD);

        executePost("http://localhost:8080/api/module/createModule", createModuleRequest, jwt);
        EntityUtils.consume(latestHttpResponse.getEntity());
        assertTrue(moduleRepository.findByName(arg1).isPresent());
    }

    @And("{string} has registered {string} on the module {string}")
    public void hasRegisteredOnTheModule(String teacherName, String userName, String moduleName) throws IOException {
        Module module = moduleRepository.findByName(moduleName).get();
        User user = userRepository.findByUsername(userName).get();
        String jwt = authController.generateJwt(teacherName, PASSWORD);
        executePost("http://localhost:8080/api/module/" + module.getId() + "/participants/" + user.getId(), jwt);
        EntityUtils.consume(latestHttpResponse.getEntity());
    }

}
