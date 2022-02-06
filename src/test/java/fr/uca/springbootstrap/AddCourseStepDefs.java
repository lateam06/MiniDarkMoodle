package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.RessourcesRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.HashSet;

public class AddCourseStepDefs extends SpringIntegration{
    private static final String PASSWORD = "password";

    @Autowired
    RessourcesRepository ressourcesRepository;

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

    @Given("a teacher named {string} with ID {int}")
    public void aTeacherNamedWithID(String arg0, int arg1) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>(){{ add(roleRepository.findByName(ERole.ROLE_TEACHER).
                orElseThrow(() -> new RuntimeException("Error: Role is not found."))); }});
        userRepository.save(user);




    }


    @And("a module with ID {string}")
    public void aModuleWithID(String arg0) {
        Module module = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        module.setParticipants(new HashSet<>());
        moduleRepository.save(module);
    }

    @And("a course with name {string}")
    public void aCourseWithName(String arg0) throws IOException {
        Ressources r =   ressourcesRepository.findByName(arg0).orElse(new Ressources(arg0, EType.COURSE));
//        Module module = moduleRepository.findByName(arg1).orElse(new Module(arg0));

        //executePost("http://localhost:8080/api/module/"+module.getId()+"/ressources/"+r.getId(),"token");
        ressourcesRepository.save(r);
//        moduleRepository.save(module);

    }



    @Given("{string} is the teacher registered to the module {string}")
    public void isTheTeacherRegisteredToTheModule(String arg0, String arg1) throws IOException {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/module/"+module.getId()+"/participants/"+user.getId(),jwt);

    }

    @When("{string} want to add the course {string}  to the module {string}")
    public void wantToAddTheCourseToTheModule(String arg0, String arg1, String arg2) throws IOException {
        User user = userRepository.findByUsername(arg0).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        Ressources course = ressourcesRepository.findByName(arg1).get();
        Module module = moduleRepository.findByName(arg2).get();
        executePost("http://localhost:8080/api/module/"+module.getId()+"/ressources/"+course.getId(),jwt);



    }

    @Then("The course {string} is added to the module {string}")
    public void theCourseIsAddedToTheModule(String arg0, String arg1) {
        Ressources course = ressourcesRepository.findByName(arg0).get();
        Module module = moduleRepository.findByName(arg1).get();
        System.out.println(module.getRessources());
        assertTrue(module.getRessources().contains(course));
    }
}
