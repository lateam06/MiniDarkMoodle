package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.Resource;
import fr.uca.springbootstrap.models.modules.courses.Course;
import fr.uca.springbootstrap.models.modules.questions.Questionnary;
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

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AddQuestionnaireStepdefs extends  SpringIntegration {
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
    QuestionnaryRepository questionnaryRepository;



    @And("a Questionnaire with name {string}")
    public void aQuestionnaireWithName(String arg0) {
        Questionnary questionnary = questionnaryRepository.findByName(arg0).orElse(new Questionnary(arg0));
        resourcesRepository.save(questionnary);
    }


    @When("{string} wants to add the questionnaire {string} to the module {string}")
    public void wantsToAddTheQuestionnaireToTheModule(String arg0, String arg1, String arg2) throws IOException {
        User user = userRepository.findByUsername(arg0).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        Module module = moduleRepository.findByName(arg2).get();
        Questionnary questionnary = questionnaryRepository.findByName(arg1).get();
        executePost("http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId(), jwt);
    }

    @Then("The questionnaire {string} is added to the module {string}")
    public void theQuestionnaireIsAddedToTheModule(String arg0, String arg1) {

        Questionnary questionnary = questionnaryRepository.findByName(arg0).get();
        Module module = moduleRepository.findByName(arg1).get();
        assertTrue(module.getResources().contains(questionnary));



    }

    @And("another Questionnaire with name {string} and descritpion {string}")
    public void anotherQuestionnaireWithNameAndDescritpion(String arg0, String arg1) {
        Questionnary questionnary = questionnaryRepository.findByName(arg0).orElse(new Questionnary(arg0));
        questionnary.setDescription(arg1);
        resourcesRepository.save(questionnary);
    }


    @Then("{string} checks if the Questionnary {string} from {string} has a description according to {string} with a get")
    public void checksIfTheQuestionnaryFromHasADescriptionAccordingToWithAGet(String arg0, String arg1, String arg2, String arg3) throws IOException {
        Questionnary questionnary = questionnaryRepository.findByName(arg1).get();
        Module module = moduleRepository.findByName(arg2).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId();

        executeGet(url,jwt);
        Questionnary resp = ObjMapper.readValue(latestJson,Questionnary.class);

//        ResponseEntity<Resource> resp = (ResponseEntity<Resource>)
        assertEquals(arg3.compareTo(resp.getDescription()), 0);
    }

    @And("a CodeRunner Question {string}")
    public void aCodeRunnerQuestion(String arg0) {

    }
}
