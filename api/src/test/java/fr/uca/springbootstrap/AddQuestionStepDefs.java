package fr.uca.springbootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.uca.springbootstrap.models.modules.questions.*;
import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.payload.request.CreateQuestionRequest;
import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.repository.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Optional;

public class AddQuestionStepDefs extends SpringIntegration {

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
    CourseRepository courseRepository;

    @Autowired
    QuestionnaryRepository questionnaryRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QCMRepository qcmRepository;

    @Autowired
    OpenQuestionRepository openQuestionRepository;

    @And("{string} has registered the questionnaire {string} to the module {string}")
    public void hasRegisteredTheQuestionnaireToTheModule(String teacherName, String questionnaireName, String moduleName) throws IOException {
        UserApi teacher = userRepository.findByUsername(teacherName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();

        String jwt = SpringIntegration.tokenHashMap.get(teacherName);
        executePut(Questionnary.generateUrl(module.getId(), questionnary.getId()), jwt);
        EntityUtils.consume(latestHttpResponse.getEntity());

        module = moduleRepository.findByName(moduleName).get();
        assertTrue(module.getResources().contains(questionnary));
    }

    @When("{string} wants to add a QCM {string} to the questionnaire {string} of the module {string}")
    public void wantsToAddAQCMToTheQuestionnaireOfTheModule(String userName, String qcmName, String questionnaireName, String moduleName) throws IOException {
        UserApi userApi = userRepository.findByUsername(userName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();

        CreateQuestionRequest question = new CreateQuestionRequest(qcmName, "description du cours", "reponse a la question", EQuestion.QCM);
        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId() + "/questions";
        String token = SpringIntegration.tokenHashMap.get(userApi.getUsername());
        executePost(url, question, token);
        System.out.println("http: "+  EntityUtils.toString(latestHttpResponse.getEntity()));
        EntityUtils.consume(latestHttpResponse.getEntity());
    }

    @Then("the QCM {string} is not added to the questionnaire {string} and the return status of the request is error")
    public void theQCMIsNotAddedToTheQuestionnaireAndTheReturnStatusOfTheRequestIsError(String arg0, String arg1) {
        assertTrue(latestHttpResponse.getStatusLine().getStatusCode() >= 400);
    }

    @Then("the QCM {string} is added to the questionnaire {string} of the module {string}")
    public void theQCMIsAddedToTheQuestionnaireOfTheModule(String qcmName, String questionnaireName, String moduleName) {
        Module module = moduleRepository.findByName(moduleName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        QCM qcm = qcmRepository.findByName(qcmName).get();

        assertTrue(module.getResources().contains(questionnary));
        assertTrue(questionnary.getQuestionSet().contains(qcm));
    }

    @When("{string} wants to add an Open {string} to the questionnaire {string} of the module {string}")
    public void wantsToAddAnOpenToTheQuestionnaireOfTheModule(String userName, String openName, String questionnaireName, String moduleName) throws IOException {
        UserApi userApi = userRepository.findByUsername(userName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();

        CreateQuestionRequest openRequest = new CreateQuestionRequest(openName, "description du cours", "reponse a la question", EQuestion.OPEN);
        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId() + "/questions";
        String token = SpringIntegration.tokenHashMap.get(userApi.getUsername());
        executePost(url, openRequest, token);
        System.out.println("http: "+  EntityUtils.toString(latestHttpResponse.getEntity()));
        EntityUtils.consume(latestHttpResponse.getEntity());
    }

    @Then("the Open {string} is added to the questionnaire {string} of the module {string}")
    public void theOpenIsAddedToTheQuestionnaireOfTheModule(String openName, String questionnaireName, String moduleName) {
        Module module = moduleRepository.findByName(moduleName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        OpenQuestion open = openQuestionRepository.findByName(openName).get();

        assertTrue(module.getResources().contains(questionnary));
        assertTrue(questionnary.getQuestionSet().contains(open));
    }

    @Then("the Open {string} is not added to the questionnaire {string} and the return status of the request is error")
    public void theOpenIsNotAddedToTheQuestionnaireAndTheReturnStatusOfTheRequestIsError(String arg0, String arg1) {
        assertTrue(latestHttpResponse.getStatusLine().getStatusCode() >= 400);
    }

    @When("{string} wants to get a QCM {string} from the questionnaire {string} of the module {string}")
    public void wantsToGetAQCMFromTheQuestionnaireOfTheModule(String userName, String qcmName, String questionnaireName, String moduleName) throws IOException {
        UserApi userApi = userRepository.findByUsername(userName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();
        QCM qcm = qcmRepository.findByName(qcmName).get();

        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId() + "/questions/" + qcm.getId();
        String token = SpringIntegration.tokenHashMap.get(userName);
        executeGet(url, token);
        EntityUtils.consume(latestHttpResponse.getEntity());
    }

    @Then("{string} can succesfully get the QCM {string} from the questionnaire {string} of the module {string}")
    public void canSuccesfullyGetTheQCMFromTheQuestionnaireOfTheModule(String arg0, String arg1, String arg2, String arg3) throws JsonProcessingException {
        var qcm = ObjMapper.readValue(latestJson, CreateQuestionRequest.class);
        assertTrue(qcm.getName().equalsIgnoreCase(arg1));
    }

    @When("{string} wants to get an OpenQuestion {string} from the questionnaire {string} of the module {string}")
    public void wantsToGetAnOpenQuestionFromTheQuestionnaireOfTheModule(String userName, String openName, String questionnaireName, String moduleName) throws IOException {
        UserApi userApi = userRepository.findByUsername(userName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();
        OpenQuestion open = openQuestionRepository.findByName(openName).get();

        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId() + "/questions/" + open.getId();
        String token = SpringIntegration.tokenHashMap.get(userName);
        executeGet(url, token);
        EntityUtils.consume(latestHttpResponse.getEntity());
    }

    @Then("{string} can succesfully get the OpenQuestion {string} from the questionnaire {string} of the module {string}")
    public void canSuccesfullyGetTheOpenQuestionFromTheQuestionnaireOfTheModule(String arg0, String arg1, String arg2, String arg3) throws JsonProcessingException {
        var open = ObjMapper.readValue(latestJson, CreateQuestionRequest.class);
        assertTrue(open.getName().equalsIgnoreCase(arg1));
    }

    @When("{string} wants to delete a Question {string} from the questionnaire {string} of the module {string}")
    public void wantsToDeleteAQuestionFromTheQuestionnaireOfTheModule(String userName, String questionName, String questionnaireName, String moduleName) throws IOException {
        UserApi userApi = userRepository.findByUsername(userName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();
        Optional<OpenQuestion> optionalOpenQuestion = openQuestionRepository.findByName(questionName);
        Optional<QCM> optionalQCM = qcmRepository.findByName(questionName);
        Question question = questionRepository.findByName(questionName).get();

        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId() + "/questions/" + question.getId();
        String token = SpringIntegration.tokenHashMap.get(userName);
        executeDelete(url, token);
        System.out.println("http deleted : "+  EntityUtils.toString(latestHttpResponse.getEntity()));
        EntityUtils.consume(latestHttpResponse.getEntity());
    }

    @Then("the Question {string} is deleted from the questionnaire {string} of the module {string}")
    public void theQuestionIsDeletedFromTheQuestionnaireOfTheModule(String questionName, String questionnaireName, String moduleName) {
        Optional<Question> questionOptional = questionRepository.findByName(questionName);
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();
        assertFalse(questionRepository.findByName(questionName).isPresent());
    }

    @And("{string} has already registered a QCM {string} to the questionnaire {string} of the module {string}")
    public void hasAlreadyRegisteredAQCMToTheQuestionnaireOfTheModule(String userName, String qcmName, String questionnaireName, String moduleName) throws IOException {
        UserApi userApi = userRepository.findByUsername(userName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();

        if(questionRepository.findByName(qcmName).isEmpty()) {
            CreateQuestionRequest question = new CreateQuestionRequest(qcmName, "description du cours", "reponse a la question", EQuestion.QCM);
            String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId() + "/questions";
            String token = SpringIntegration.tokenHashMap.get(userName);
            executePost(url, question, token);
            System.out.println("http already : " + EntityUtils.toString(latestHttpResponse.getEntity()));
            EntityUtils.consume(latestHttpResponse.getEntity());
        }
    }

    @And("{string} has already registered an Open {string} to the questionnaire {string} of the module {string}")
    public void hasAlreadyRegisteredAnOpenToTheQuestionnaireOfTheModule(String userName, String openName, String questionnaireName, String moduleName) throws IOException {
        UserApi userApi = userRepository.findByUsername(userName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();

        if(questionRepository.findByName(openName).isEmpty()) {
            CreateQuestionRequest openRequest = new CreateQuestionRequest(openName, "description du cours", "reponse a la question", EQuestion.OPEN);
            String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId() + "/questions";
            String token = SpringIntegration.tokenHashMap.get(userApi.getUsername());
            executePost(url, openRequest, token);
            System.out.println("http already : " + EntityUtils.toString(latestHttpResponse.getEntity()));
            EntityUtils.consume(latestHttpResponse.getEntity());
        }
    }

    @Then("the Question {string} is not deleted from the questionnaire {string} of the module {string}")
    public void theQuestionIsNotDeletedFromTheQuestionnaireOfTheModule(String questionName, String questionnaireName, String moduleName) {
        Optional<Question> questionOptional = questionRepository.findByName(questionName);
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();
        assertTrue(questionRepository.findByName(questionName).isPresent());
        assertTrue(latestHttpResponse.getStatusLine().getStatusCode() >= 400);
    }

    @Then("{string} can not get the QCM {string} from the questionnaire {string} of the module {string}")
    public void canNotGetTheQCMFromTheQuestionnaireOfTheModule(String userName, String qcmName, String questionnaireName, String moduleName) {
        assertTrue(latestHttpResponse.getStatusLine().getStatusCode() >= 400);
    }


}
