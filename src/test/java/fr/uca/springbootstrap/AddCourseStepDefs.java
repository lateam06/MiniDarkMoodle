package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.Resource;
import fr.uca.springbootstrap.models.modules.courses.Course;
import fr.uca.springbootstrap.models.users.ERole;
import fr.uca.springbootstrap.models.users.Role;
import fr.uca.springbootstrap.models.users.User;
import fr.uca.springbootstrap.repository.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashSet;

public class AddCourseStepDefs extends SpringIntegration {
    private static final String PASSWORD = "password";

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



    @Given("a teacher named {string} with ID {int}")
    public void aTeacherNamedWithID(String arg0, int arg1) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);


    }

    @And("a Student named {string}")
    public void aStudentNamed(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>() {{
            add(roleRepository.findByName(ERole.ROLE_STUDENT).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);

    }


    @And("a module with ID {string}")
    public void aModuleWithID(String arg0) {
        Module module = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        module.setParticipants(new HashSet<>());
        moduleRepository.save(module);
    }

    @And("a course with name {string} with a description {string}")
    public void aCourseWithName(String arg0,String arg1) throws IOException {
        Course course = courseRepository.findByName(arg0).orElse(new Course(arg0));
        course.setDescription(arg1);
        courseRepository.save(course);
//        resources r = resourcesRepository.findByName(arg0).orElse(new resources(arg0, EType.COURSE));
//        resourcesRepository.save(r);

    }


    @Given("{string} is the teacher registered to the module {string}")
    public void isTheTeacherRegisteredToTheModule(String arg0, String arg1) throws IOException {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/module/" + module.getId() + "/participants/" + user.getId(), jwt);
//        System.out.println("ajout du prof , debug " + latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Then("The course {string} is added to the module {string}")
    public void theCourseIsAddedToTheModule(String arg0, String arg1) {
        Course course = courseRepository.findByName(arg0).get();
        Module module = moduleRepository.findByName(arg1).get();
        System.out.println(module.getResources());
        assertTrue(module.getResources().contains(course));
    }
    @When("{string} wants to add the course {string} to the module {string}")
    public void wantsToAddTheCourseToTheModule(String arg0, String arg1, String arg2) throws IOException {
        User user = userRepository.findByUsername(arg0).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        Module module = moduleRepository.findByName(arg2).get();
        Course course = courseRepository.findByName(arg1).get();
        executePost("http://localhost:8080/api/module/" + module.getId() + "/resources/" + course.getId(), jwt);
    }




    @When("{string} wants to delete the course {string} from the module {string}")
    public void wantsToDeleteTheCourseFromTheModule(String arg0, String arg1, String arg2) throws IOException {
        User user = userRepository.findByUsername(arg0).get();
        String jwtStudent = authController.generateJwt(arg0, PASSWORD);
        Module module = moduleRepository.findByName(arg2).get();
        Resource course = resourcesRepository.findByName(arg1).get();
        executeDelete("http://localhost:8080/api/module/" + module.getId() + "/resources/" + course.getId(), jwtStudent);


    }

    @Then("the course {string} is deleted from the module {string}")
    public void theCourseIsDeletedFromTheModule(String arg0, String arg1) {
        Resource course = resourcesRepository.findByName(arg0).get();
        Module module = moduleRepository.findByName(arg1).get();
        assertFalse(module.getResources().contains(course));

    }

    @When("{string} wants to delete the course {string} to the module {string}")
    public void wantsToDeleteTheCourseToTheModule(String arg0, String arg1, String arg2) throws IOException {
        User user = userRepository.findByUsername(arg0).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        Module module = moduleRepository.findByName(arg2).get();
        Resource course = resourcesRepository.findByName(arg1).get();
        executeDelete("http://localhost:8080/api/module/" + module.getId() + "/resources/" + course.getId(), jwt);




    }


    @Then("the course is not added and the return status of the request is {int} forbidden")
    public void theCourseIsNotAddedAndTheReturnStatusOfTheRequestIs(int status) {
        assertEquals(status, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Then("the course is not deleted and the return status of the request is {int}")
    public void theCourseIsNotDeletedAndTheReturnStatusOfTheRequestIs(int status) {
        assertEquals(status, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @And("{string} is not the teacher registered to the module {string}")
    public void isNotTheTeacherRegisteredToTheModule(String teacherName, String moduleName) {

    }

    @And("{string} is not a teacher registered to the module {string}")
    public void isNotATeacherRegisteredToTheModule(String teacherName, String moduleName) {
        User teacher = userRepository.findByUsername(teacherName).get();
        Module module = moduleRepository.findByName(moduleName).get();

        assertFalse(module.getParticipants().contains(teacher));
    }


//    @And("a course {string} has already been added by {string} in the module {string}")
//    public void aCourseHasAlreadyBeenAddedByInTheModule(String courseName, String teacherName, String moduleName) throws IOException {
//        /*User user = userRepository.findByUsername(teacherName).get();
//        //String jwt = authController.generateJwt(teacherName, PASSWORD);
//        Module module = moduleRepository.findByName(moduleName).get();
//        resources course = resourcesRepository.findByName(courseName).orElse(new resources(courseName, EType.COURSE));
//        resourcesRepository.save(course);
//
//        executePost("http://localhost:8080/api/module/" + module.getId() + "/resources/" + course.getId(), jwt);
//
//        //module = moduleRepository.findByName(moduleName).get();
//        //assertTrue(module.getresources().contains(course));*/
//    }
}
