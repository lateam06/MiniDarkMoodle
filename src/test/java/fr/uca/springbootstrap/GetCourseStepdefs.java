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
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashSet;

public class GetCourseStepdefs extends SpringIntegration  {
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

    @Autowired
    RestTemplate restTemplate;

    @And("a course with name {string}")
    public void aCourseWithName(String arg0) throws IOException {
        Course course = courseRepository.findByName(arg0).orElse(new Course(arg0));
        courseRepository.save(course);

    }


    @And("the course {string} has been added by {string} into {string}")
    public void theCourseHasBeenAddedByInto(String arg0, String arg1, String arg2) throws IOException {
        User user = userRepository.findByUsername(arg1).get();
        String jwt = authController.generateJwt(arg1, PASSWORD);
        Module module = moduleRepository.findByName(arg2).get();
        Course course = courseRepository.findByName(arg0).get();
        executePost("http://localhost:8080/api/module/" + module.getId() + "/resources/" + course.getId(), jwt);




    }

    @When("{string} wants to add the course a second time {string} to the module {string}")
    public void wantsToAddTheCourseASecondTimeToTheModule(String arg0, String arg1, String arg2) throws IOException {
        User user = userRepository.findByUsername(arg0).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        Module module = moduleRepository.findByName(arg2).get();
        Course course = courseRepository.findByName(arg1).get();
        executePost("http://localhost:8080/api/module/" + module.getId() + "/resources/" + course.getId(), jwt);
    }
    @Then("the course is not added and the return status of the request is {int}")
    public void theCourseIsNotAddedAndTheReturnStatusOfTheRequestIs(int status) {
        assertEquals(status, latestHttpResponse.getStatusLine().getStatusCode());
    }


    @And("a course {string} has already been added by {string} in the module {string}")
    public void aCourseHasAlreadyBeenAddedByInTheModule(String arg0, String arg1, String arg2) {
        Course course = courseRepository.findByName(arg0).get();
        Module module = moduleRepository.findByName(arg2).get();
        System.out.println(module.getResources());
        assertTrue(module.getResources().contains(course));
    }

    @And("{string} wants to get the course {string} from {string} and make sure the description is {string}")
    public void wantsToGetTheCourseFromAndMakeSureTheDescriptionIs(String arg0, String arg1, String arg2, String arg3) throws IOException {
        Course course = courseRepository.findByName(arg1).get();
        Module module = moduleRepository.findByName(arg2).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);

        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + course.getId();



        ResponseEntity<Resource> resp = (ResponseEntity<Resource>) executeGet(url,jwt,Resource.class);


        System.out.println("status : " + resp.getStatusCode());
        System.out.println("id : " + resp.getBody().getId());
        System.out.println(resp.getBody().getName());


        assertEquals(arg3.compareTo(resp.getBody().getDescription()), 0);
    }

    @And("the teacher {string} is connected")
    public void theTeacherIsConnected(String arg0) throws IOException {
        String jwt = authController.generateJwt(arg0, PASSWORD);
        //executePost("http://localhost:8080/api/auth/signin",jwt);


    }
    Course cours;
    @And("{string} wants to get the course {string} from {string}")
    public void wantsToGetTheCourseFrom(String arg0, String arg1, String arg2) {
        Course course = courseRepository.findByName(arg1).get();
        Module module = moduleRepository.findByName(arg2).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + course.getId();

        ResponseEntity<Course> resp = (ResponseEntity<Course>) executeGet(url,jwt,Course.class);
        System.out.println("visibility : "+ course.isVisibility());
        cours = resp.getBody();
    }


    @And("{string} wants to change the visibility fo the course {string} of {string} to true")
    public void wantsToChangeTheVisibilityFoTheCourseOfToTrue(String arg0, String arg1, String arg2) throws IOException {
        cours.setVisibility(true);
        String jwt = authController.generateJwt(arg0, PASSWORD);
        Module module = moduleRepository.findByName(arg2).get();
        courseRepository.save(cours);
        executePost("http://localhost:8080/api/module/" + module.getId() + "/resources/" + cours.getId(),jwt);
    }


    @And("{string} gets the course {string} of {string} and make sur the visibility is to true")
    public void getsTheCourseOfAndMakeSurTheVisibilityIsToTrue(String arg0, String arg1, String arg2) {
        Course course = courseRepository.findByName(arg1).get();
        Module module = moduleRepository.findByName(arg2).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + course.getId();

        ResponseEntity<Resource> resp = (ResponseEntity<Resource>) executeGet(url,jwt,Resource.class);
        assertTrue(resp.getBody().isVisibility());

    }
}
