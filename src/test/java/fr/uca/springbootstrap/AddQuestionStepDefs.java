package fr.uca.springbootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.questions.OpenQuestion;
import fr.uca.springbootstrap.models.modules.questions.QCM;
import fr.uca.springbootstrap.models.modules.questions.Questionnary;
import fr.uca.springbootstrap.models.users.User;
import fr.uca.springbootstrap.payload.request.CreateNewOpenRequest;
import fr.uca.springbootstrap.payload.request.CreateNewQCMRequest;
import fr.uca.springbootstrap.repository.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

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
    UserRepository userRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    QuestionnaryRepository questionnaryRepository;

    @Autowired
    QCMRepository qcmRepository;

    @Autowired
    OpenQuestionRepository openQuestionRepository;

    @And("{string} has registered the questionnaire {string} to the module {string}")
    public void hasRegisteredTheQuestionnaireToTheModule(String teacherName, String questionnaireName, String moduleName) throws IOException {
        User teacher = userRepository.findByUsername(teacherName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();

        String token = authController.generateJwt(teacherName, PASSWORD);
        executePost(Questionnary.generateUrl(module.getId(), questionnary.getId()), token);
        EntityUtils.consume(latestHttpResponse.getEntity());

        module = moduleRepository.findByName(moduleName).get();
        assertTrue(module.getResources().contains(questionnary));
    }

    @When("{string} wants to add a QCM {string} to the questionnaire {string} of the module {string}")
    public void wantsToAddAQCMToTheQuestionnaireOfTheModule(String userName, String qcmName, String questionnaireName, String moduleName) throws IOException {
        User user = userRepository.findByUsername(userName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();

        CreateNewQCMRequest qcm = new CreateNewQCMRequest(qcmName, "description du cours", "reponse a la question");
        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId() + "/newQcm";
        String token = authController.generateJwt(user.getUsername(), PASSWORD);
        executePost(url, qcm, token);
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
        User user = userRepository.findByUsername(userName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();

        CreateNewOpenRequest openRequest = new CreateNewOpenRequest(openName, "description du cours", "reponse a la question");
        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId() + "/newOpen";
        String token = authController.generateJwt(user.getUsername(), PASSWORD);
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

    @And("{string} is a student registered to the module {string}")
    public void isAStudentRegisteredToTheModule(String userName, String moduleName) throws IOException {
        Module module = moduleRepository.findByName(moduleName).get();
        User user = userRepository.findByUsername(userName).get();
        String jwt = authController.generateJwt(userName, PASSWORD);
        executePost("http://localhost:8080/api/module/" + module.getId() + "/participants/" + user.getId(), jwt);
        EntityUtils.consume(latestHttpResponse.getEntity());
    }

    @Then("the Open {string} is not added to the questionnaire {string} and the return status of the request is error")
    public void theOpenIsNotAddedToTheQuestionnaireAndTheReturnStatusOfTheRequestIsError(String arg0, String arg1) {
        assertTrue(latestHttpResponse.getStatusLine().getStatusCode() >= 400);
    }

    @When("{string} wants to get a QCM {string} from the questionnaire {string} of the module {string}")
    public void wantsToGetAQCMFromTheQuestionnaireOfTheModule(String userName, String qcmName, String questionnaireName, String moduleName) throws IOException {
        User user = userRepository.findByUsername(userName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();
        QCM qcm = qcmRepository.findByName(qcmName).get();

        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId() + "/" + qcm.getId();
        String token = authController.generateJwt(userName, PASSWORD);
        executeGet2(url, token);
        EntityUtils.consume(latestHttpResponse.getEntity());
    }

    @Then("{string} can succesfully get the QCM {string} from the questionnaire {string} of the module {string}")
    public void canSuccesfullyGetTheQCMFromTheQuestionnaireOfTheModule(String arg0, String arg1, String arg2, String arg3) throws JsonProcessingException {
        var qcm = ObjMapper.readValue(latestJson, CreateNewQCMRequest.class);
        assertTrue(qcm.getName().equalsIgnoreCase(arg1));
    }

    @When("{string} wants to get an OpenQuestion {string} from the questionnaire {string} of the module {string}")
    public void wantsToGetAnOpenQuestionFromTheQuestionnaireOfTheModule(String userName, String openName, String questionnaireName, String moduleName) throws IOException {
        User user = userRepository.findByUsername(userName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();
        OpenQuestion open = openQuestionRepository.findByName(openName).get();

        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId() + "/" + open.getId();
        String token = authController.generateJwt(userName, PASSWORD);
        executeGet2(url, token);
        EntityUtils.consume(latestHttpResponse.getEntity());
    }

    @Then("{string} can succesfully get the OpenQuestion {string} from the questionnaire {string} of the module {string}")
    public void canSuccesfullyGetTheOpenQuestionFromTheQuestionnaireOfTheModule(String arg0, String arg1, String arg2, String arg3) throws JsonProcessingException {
        var open = ObjMapper.readValue(latestJson, CreateNewOpenRequest.class);
        assertTrue(open.getName().equalsIgnoreCase(arg1));
    }

    @And("{string} has registered {string} on the module {string}")
    public void hasRegisteredOnTheModule(String teacherName, String userName, String moduleName) throws IOException {
        Module module = moduleRepository.findByName(moduleName).get();
        User user = userRepository.findByUsername(userName).get();
        String jwt = authController.generateJwt(teacherName, PASSWORD);
        executePost("http://localhost:8080/api/module/" + module.getId() + "/participants/" + user.getId(), jwt);
    }

    @When("{string} wants to delete a QCM {string} from the questionnaire {string} of the module {string}")
    public void wantsToDeleteAQCMFromTheQuestionnaireOfTheModule(String userName, String qcmName, String questionnaireName, String moduleName) throws IOException {
        User user = userRepository.findByUsername(userName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();
        QCM qcm = qcmRepository.findByName(qcmName).get();

        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId() + "/" + qcm.getId();
        String token = authController.generateJwt(userName, PASSWORD);
        executeDelete(url, token);
        EntityUtils.consume(latestHttpResponse.getEntity());
    }

    @Then("the QCM {string} is deleted from the questionnaire {string} of the module {string}")
    public void theQCMIsDeletedFromTheQuestionnaireOfTheModule(String qcmName, String questionnaireName, String moduleName) {
        assertFalse(qcmRepository.findByName(qcmName).isPresent());
    }

    @And("{string} has already registered a QCM {string} to the questionnaire {string} of the module {string}")
    public void hasAlreadyRegisteredAQCMToTheQuestionnaireOfTheModule(String userName, String qcmName, String questionnaireName, String moduleName) throws IOException {
        User user = userRepository.findByUsername(userName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();

        CreateNewQCMRequest qcm = new CreateNewQCMRequest(qcmName, "description du cours", "reponse a la question");
        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId() + "/newQcm";
        String token = authController.generateJwt(user.getUsername(), PASSWORD);
        executePost(url, qcm, token);
        System.out.println("http: "+  EntityUtils.toString(latestHttpResponse.getEntity()));
        EntityUtils.consume(latestHttpResponse.getEntity());
    }

    @And("{string} has already registered an Open {string} to the questionnaire {string} of the module {string}")
    public void hasAlreadyRegisteredAnOpenToTheQuestionnaireOfTheModule(String userName, String openName, String questionnaireName, String moduleName) throws IOException {
        User user = userRepository.findByUsername(userName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();

        CreateNewOpenRequest openRequest = new CreateNewOpenRequest(openName, "description du cours", "reponse a la question");
        String url = "http://localhost:8080/api/module/" + module.getId() + "/resources/" + questionnary.getId() + "/newOpen";
        String token = authController.generateJwt(user.getUsername(), PASSWORD);
        executePost(url, openRequest, token);
        System.out.println("http: "+  EntityUtils.toString(latestHttpResponse.getEntity()));
        EntityUtils.consume(latestHttpResponse.getEntity());
    }
}
