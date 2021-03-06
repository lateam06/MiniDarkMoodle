package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.questions.Questionnary;
import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.repository.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;

public class AddQuestionnaireStepdefs extends SpringIntegration {
    private static final String PASSWORD = "password";

    @Autowired
    ResourcesRepository resourcesRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserApiRepository userRepository;

    @Autowired
    AuthController authController;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    QuestionnaryRepository questionnaryRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    CodeRunnerRepository codeRunnerRepository;

    @And("a questionnaire with name {string}")
    public void aQuestionnaireWithName(String arg0) {
        Questionnary questionnary = questionnaryRepository.findByName(arg0).orElse(new Questionnary(arg0));
        questionnary.setVisibility(true);
        resourcesRepository.save(questionnary);
    }


    @When("{string} wants to add the questionnaire {string} to the module {string}")
    public void wantsToAddTheQuestionnaireToTheModule(String arg0, String arg1, String arg2) throws IOException {
        UserApi userApi = userRepository.findByUsername(arg0).get();
        String jwt = SpringIntegration.tokenHashMap.get(arg0);
        Module module = moduleRepository.findByName(arg2).get();
        Questionnary questionnary = questionnaryRepository.findByName(arg1).get();
        executePut("http://localhost:8080/api/modules/" + module.getId() + "/resources/" + questionnary.getId(), jwt);
    }

    @Then("The questionnaire {string} is added to the module {string}")
    public void theQuestionnaireIsAddedToTheModule(String arg0, String arg1) {

        Questionnary questionnary = questionnaryRepository.findByName(arg0).get();
        Module module = moduleRepository.findByName(arg1).get();
        assertTrue(module.getResources().contains(questionnary));


    }

    @And("another questionnaire with name {string} and description {string}")
    public void anotherQuestionnaireWithNameAndDescription(String arg0, String arg1) {
        Questionnary questionnary = questionnaryRepository.findByName(arg0).orElse(new Questionnary(arg0));
        questionnary.setDescription(arg1);
        resourcesRepository.save(questionnary);
    }


    @Then("{string} checks if the questionnaire {string} from {string} has a description according to {string} with a get")
    public void checksIfTheQuestionnaireFromHasADescriptionAccordingToWithAGet(String arg0, String arg1, String arg2, String arg3) throws IOException {
        Questionnary questionnary = questionnaryRepository.findByName(arg1).get();
        Module module = moduleRepository.findByName(arg2).get();
        String jwt = SpringIntegration.tokenHashMap.get(arg0);
        String url = "http://localhost:8080/api/modules/" + module.getId() + "/resources/" + questionnary.getId();

        executeGet(url, jwt);
        Questionnary resp = ObjMapper.readValue(latestJson, Questionnary.class);

        assertEquals(arg3.compareTo(resp.getDescription()), 0);
    }


}
