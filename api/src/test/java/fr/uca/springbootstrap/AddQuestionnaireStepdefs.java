package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.Resource;
import fr.uca.springbootstrap.models.modules.questions.CodeRunner;
import fr.uca.springbootstrap.models.modules.questions.EQuestion;
import fr.uca.springbootstrap.models.modules.questions.Questionnary;
import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.payload.request.CreateQuestionRequest;
import fr.uca.springbootstrap.repository.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Optional;

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
        resourcesRepository.save(questionnary);
    }


    @When("{string} wants to add the questionnaire {string} to the module {string}")
    public void wantsToAddTheQuestionnaireToTheModule(String arg0, String arg1, String arg2) throws IOException {
        UserApi userApi = userRepository.findByUsername(arg0).get();
        String jwt = SpringIntegration.tokenHashMap.get(arg0);
        Module module = moduleRepository.findByName(arg2).get();
        Questionnary questionnary = questionnaryRepository.findByName(arg1).get();
        executePut("http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId(), jwt);
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
        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId();

        executeGet(url, jwt);
        Questionnary resp = ObjMapper.readValue(latestJson, Questionnary.class);

        assertEquals(arg3.compareTo(resp.getDescription()), 0);
    }

    @And("a CodeRunner Question {string}")
    public void aCodeRunnerQuestion(String arg0) {
        Optional<CodeRunner> ocode = codeRunnerRepository.findByName(arg0);
        if (ocode.isEmpty()) {
            CodeRunner cr = new CodeRunner();
            cr.setName(arg0);
            cr.setDescription("Calculez la factoriel de n");
            cr.setTestCode("print(fact(6))");
            cr.setTestResponse("720");
            questionRepository.save(cr);
        }


    }

    @And("{string} wants to add a CodeRunner {string} to the questionnaire {string} from the module {string}")
    public void wantsToAddACodeRunnerToTheQuestionnaireFromTheModule(String arg0, String arg1, String arg2, String arg3) throws IOException {
        Module module = moduleRepository.findByName(arg3).get();
        Resource resource = resourcesRepository.findByName(arg2).get();
        CodeRunner cr = codeRunnerRepository.findByName(arg1).get();
        CreateQuestionRequest request = new CreateQuestionRequest(cr.getName(), cr.getDescription(), cr.getTestResponse(), EQuestion.CODE);
        request.setCodeRunner(cr);
        String jwt = SpringIntegration.tokenHashMap.get(arg0);
        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + resource.getId() + "/questions";

        executePost(url, request, jwt);

        assertEquals(latestHttpResponse.getStatusLine().getStatusCode(),200);
        EntityUtils.consume(latestHttpResponse.getEntity());
    }
}
