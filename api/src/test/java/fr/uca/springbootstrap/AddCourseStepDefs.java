package fr.uca.springbootstrap;

import fr.uca.springbootstrap.payload.request.ResourceRequest;
import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.Resource;
import fr.uca.springbootstrap.models.modules.courses.Course;
import fr.uca.springbootstrap.models.users.ERole;
import fr.uca.springbootstrap.models.users.User;
import fr.uca.springbootstrap.repository.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.DiscriminatorValue;

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


    @Given("a teacher named {string}")
    public void aTeacherNamedWithID(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);


    }

    @Given("a student named {string}")
    public void aStudentNamed(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_STUDENT).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);

    }


    @And("a course named {string} with a description {string}")
    public void aCourseWithName(String arg0, String arg1) {
        Course course = courseRepository.findByName(arg0).orElse(new Course(arg0));
        course.setDescription(arg1);
        courseRepository.save(course);
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
        executePut("http://localhost:8080/api/module/" + module.getId() + "/resources/" + course.getId(), jwt);
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


    @When("{string} adds the course {string} to the post request to the module {string}")
    public void addsTheCourseObjectToThePostRequestToTheModule(String arg0, String courseName, String arg1) throws IOException {
        String jwt = authController.generateJwt(arg0, PASSWORD);
        Module mod = moduleRepository.findByName(arg1).get();
        Course course = new Course(courseName);
        String disc = course.getClass().getAnnotation(DiscriminatorValue.class).value();
        ResourceRequest re = new ResourceRequest(course.getName(), disc, "", true, null, null);
        executePost("http://localhost:8080/api/module/" + mod.getId() + "/resources", re, jwt);
    }

    @Then("{string} check that the {string} course has been added correcty in {string}")
    public void checkThatTheCourseHasBeenAddedCorrectyIn(String arg0, String arg1, String arg2) {
        String jwt = authController.generateJwt(arg0, PASSWORD);
        Module mod = moduleRepository.findByName(arg2).get();
        Course course = courseRepository.findByName(arg1).get();
        assertTrue(mod.getResources().contains(course));
    }
}
