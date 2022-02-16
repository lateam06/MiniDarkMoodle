package fr.uca.springbootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.questions.CodeRunner;
import fr.uca.springbootstrap.models.modules.questions.EQuestion;
import fr.uca.springbootstrap.models.modules.questions.Question;
import fr.uca.springbootstrap.models.modules.questions.Questionnary;
import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.payload.request.CreateQuestionRequest;
import fr.uca.springbootstrap.repository.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

public class AddCodeRunnerStepDefs extends SpringIntegration {
    private static final String PASSWORD = "password";
    private static final String BASE_URL = "http://localhost:8080/api/";

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
    CodeRunnerRepository codeRunnerRepository;

    @Autowired
    QuestionnaryRepository questionnaryRepository;

    @Autowired
    QuestionRepository questionRepository;

    public static String generateQuestionnaryUrl(long moduleId, long questionnaryId) {
        return BASE_URL + "modules/" + moduleId + "/resources/" + questionnaryId;
    }

    @When("{string} wants to add the code question named {string} with testCode {string} and response {string} on {string} of {string}")
    public void wantsToAddTheCodeQuestionNamedWithTestCodeAndResponseOnOf(String userName, String crName, String testCode, String testResponse, String questionnaireName, String moduleName) throws IOException {
        Module module = moduleRepository.findByName(moduleName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();

        var cqr = new CreateQuestionRequest(crName, crName + " description", testResponse, EQuestion.CODE, testCode);

        String token = SpringIntegration.tokenHashMap.get(userName);
        String url = generateQuestionnaryUrl(module.getId(), questionnary.getId()) + "/questions";

        executePost(url, cqr, token);
    }

    @Then("the code question {string} is added on {string} of {string}")
    public void theCodeQuestionIsAddedOf(String crName, String questionnaireName, String moduleName) {
        Module module = moduleRepository.findByName(moduleName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Optional<CodeRunner> codeRunnerOptional = codeRunnerRepository.findByName(crName);

        assertTrue(codeRunnerOptional.isPresent());
        assertTrue(questionnary.getQuestionSet().contains(codeRunnerOptional.get()));
    }

    @Then("the code question {string} is not added on {string} of {string}")
    public void theCodeQuestionIsNotAddedOnOf(String crName, String questionnaireName, String moduleName) {
        assertTrue(codeRunnerRepository.findByName(crName).isEmpty());
        assertTrue(questionRepository.findByName(crName).isEmpty());
    }

    @Given("{string} has already registered a code question code question named {string} with testCode {string} and response {string} on {string} of {string}")
    public void hasAlreadyRegisteredACodeQuestionCodeQuestionNamedWithTestCodeAndResponseOnOf(String userName, String crName, String testCode, String testResponse, String questionnaireName, String moduleName) throws IOException {
        wantsToAddTheCodeQuestionNamedWithTestCodeAndResponseOnOf(userName, crName, testCode, testResponse, questionnaireName, moduleName);
    }

    @When("{string} wants to delete the code question {string} from {string} of {string}")
    public void wantsToDeleteTheCodeQuestionFromOf(String userName, String crName, String questionnaireName, String moduleName) throws IOException {
        Module module = moduleRepository.findByName(moduleName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        CodeRunner cr = codeRunnerRepository.findByName(crName).get();

        String token = SpringIntegration.tokenHashMap.get(userName);
        String url = generateQuestionnaryUrl(module.getId(), questionnary.getId()) + "/questions/" + cr.getId();

        executeDelete(url, token);
    }

    @Then("the code question {string} is deleted from {string} of {string}")
    public void theCodeQuestionIsDeletedFromOf(String crName, String questionnaireName, String moduleName) {
        assertTrue(questionRepository.findByName(crName).isEmpty());
        assertTrue(codeRunnerRepository.findByName(crName).isEmpty());
    }

    @Then("the code question {string} is not deleted from {string} of {string}")
    public void theCodeQuestionIsNotDeletedFromOf(String crName, String questionnaireName, String moduleName) {
        assertTrue(questionRepository.findByName(crName).isPresent());
        assertTrue(codeRunnerRepository.findByName(crName).isPresent());
    }

    @When("{string} wants to get the code question {string} from {string} of {string}")
    public void wantsToGetTheCodeQuestionFromOf(String userName, String crName, String questionnaireName, String moduleName) throws IOException {
        Module module = moduleRepository.findByName(moduleName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        CodeRunner cr = codeRunnerRepository.findByName(crName).get();

        String token = SpringIntegration.tokenHashMap.get(userName);
        String url = generateQuestionnaryUrl(module.getId(), questionnary.getId()) + "/questions/" + cr.getId();

        executeGet(url, token);
    }

    @Then("{string} successfully got the code question {string} from {string} of {string}")
    public void succesfullyGotTheCodeQuestionFromOf(String userName, String crName, String questionnaireName, String moduleName) throws JsonProcessingException {
        assertEquals(200, latestHttpResponse.getStatusLine().getStatusCode());
        var crr = ObjMapper.readValue(latestJson, CreateQuestionRequest.class);
        assertTrue(crr.getName().equalsIgnoreCase(crName));
    }

    @Then("{string} can not get the code question {string} from {string} of {string}")
    public void canNotGetTheCodeQuestionFromOf(String userName, String crName, String questionnaireName, String moduleName) {
        assertTrue(latestHttpResponse.getStatusLine().getStatusCode() >= 400);
    }

    @When("{string} wants to modify the code question {string} from {string} of {string} and change the response to {string}")
    public void wantsToModifyTheCodeQuestionFromOfAndChangeTheResponseTo(String userName, String crName, String questionnaireName, String moduleName, String newResponse) throws IOException {
        Module module = moduleRepository.findByName(moduleName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        CodeRunner cr = codeRunnerRepository.findByName(crName).get();

        String token = SpringIntegration.tokenHashMap.get(userName);
        String url = generateQuestionnaryUrl(module.getId(), questionnary.getId()) + "/questions/" + cr.getId();

        var cqr = new CreateQuestionRequest(cr.getName(), cr.getDescription(), newResponse, EQuestion.CODE, cr.getTestCode());
        executePut(url, cqr, token);
    }

    @When("{string} wants to modify the code question {string} from {string} of {string} and change the testCode to {string}")
    public void wantsToModifyTheCodeQuestionFromOfAndChangeTheTestCodeTo(String userName, String crName, String questionnaireName, String moduleName, String newTestCode) throws IOException {
        Module module = moduleRepository.findByName(moduleName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        CodeRunner cr = codeRunnerRepository.findByName(crName).get();

        String token = SpringIntegration.tokenHashMap.get(userName);
        String url = generateQuestionnaryUrl(module.getId(), questionnary.getId()) + "/questions/" + cr.getId();

        var cqr = new CreateQuestionRequest(cr.getName(), cr.getDescription(), cr.getResponse(), EQuestion.CODE, newTestCode);
        executePut(url, cqr, token);
    }

    @Then("the code question {string} from {string} of {string} response is {string}")
    public void theCodeQuestionFromOfIsModifiedAndTheResponseIsNow(String crName, String questionnaireName, String moduleName, String newResponse) {
        Module module = moduleRepository.findByName(moduleName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        CodeRunner cr = codeRunnerRepository.findByName(crName).get();

        assertEquals(newResponse, cr.getResponse());
    }

    @Then("the code question {string} from {string} of {string} testCode is {string}")
    public void theCodeQuestionFromOfTestCodeIs(String crName, String questionnaireName, String moduleName, String newTestCode) {
        Module module = moduleRepository.findByName(moduleName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        CodeRunner cr = codeRunnerRepository.findByName(crName).get();

        assertEquals(newTestCode, cr.getTestCode());
    }
}
