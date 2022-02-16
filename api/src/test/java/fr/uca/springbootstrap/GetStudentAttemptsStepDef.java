package fr.uca.springbootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.questions.*;
import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.payload.request.AnswerQuestionRequest;
import fr.uca.springbootstrap.payload.response.StudentAttemptsCollectionResponse;
import fr.uca.springbootstrap.payload.response.StudentAttemptsResponse;
import fr.uca.springbootstrap.payload.response.TeacherResponse;
import fr.uca.springbootstrap.repository.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class GetStudentAttemptsStepDef extends SpringIntegration{

    @Autowired
    ResourcesRepository resourcesRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserApiRepository userApiRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    QuestionnaryRepository questionnaryRepository;

    @Autowired
    QCMRepository qcmRepository;

    @Autowired
    QCMResponseRepository qcmResponseRepository;

    @Autowired
    QCMAttemptRepository qcmAttemptRepository;


    private final static String PASSWORD = "password";

    @And("{string} responded {string} in the qcm {string} of the questionnaire {string} of the module {string}")
    public void respondedInTheQcmOfTheQuestionnaireOfTheModule(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        QCM qcm = qcmRepository.findByName(arg2).get();
        QCMResponse resp = qcmResponseRepository.findByDescription(arg1).get();
        Questionnary questionnary = questionnaryRepository.findByName(arg3).get();
        Module module = moduleRepository.findByName(arg4).get();
        String token = SpringIntegration.tokenHashMap.get(arg0);

        String url = "http://localhost:8080/api"
                + "/modules/" + module.getId()
                + "/resources/" + questionnary.getId()
                + "/questions/" + qcm.getId();
        AnswerQuestionRequest re = new AnswerQuestionRequest(resp.getId(), resp.getDescription(), EQuestion.QCM);
        executePost(url, re, token);


    }

    @When("{string} wants to get attempts of {string} to the questionnary {string} of the module {string}")
    public void wantsToGetAttemptsOfToTheQuestionnaryOfTheModule(String arg0, String arg1, String arg2, String arg3) throws IOException {
       UserApi student = userApiRepository.findByUsername(arg1).get();
       Questionnary questionnary = questionnaryRepository.findByName(arg2).get();
       Module module = moduleRepository.findByName(arg3).get();
       String token = SpringIntegration.tokenHashMap.get(arg0);

       String url = "http://localhost:8080/api"
               + "/modules/" + module.getId()
               + "/resources/" + questionnary.getId()
               + "/attempts/" + student.getId();
       executeGet(url, token);
    }

    @Then("{string} sees that {string} responded {string} for first qcm and {string} for the second")
    public void seesThatRespondedForFirstQcmAndForTheSecond(String teacher, String student, String response1, String response2) throws JsonProcessingException {
        var resp = ObjMapper.readValue(latestJson, StudentAttemptsResponse.class);

        assertEquals(0, resp.getStudentName().compareTo(student));
    }

    @When("{string} wants to get attempts of all students to the questionnary {string} of the module {string}")
    public void wantsToGetAttemptsOfAllStudentsToTheQuestionnaryOfTheModule(String arg0, String arg1, String arg2) throws IOException {
        Questionnary questionnary = questionnaryRepository.findByName(arg1).get();
        Module module = moduleRepository.findByName(arg2).get();
        String token = SpringIntegration.tokenHashMap.get(arg0);

        String url = "http://localhost:8080/api"
                + "/modules/" + module.getId()
                + "/resources/" + questionnary.getId()
                + "/attempts";
        executeGet(url, token);
    }

    @Then("{string} sees that {string} and {string} responded to all of the qcms")
    public void seesThatAndRespondedToAllOfTheQcms(String arg0, String arg1, String arg2) throws JsonProcessingException {
        var resp = ObjMapper.readValue(latestJson, StudentAttemptsCollectionResponse.class);
        UserApi James = userApiRepository.findByUsername(arg1).get();
        UserApi Jack = userApiRepository.findByUsername(arg2).get();

        assertTrue(resp.getStudentsNames().contains(James.getUsername()));
        assertTrue(resp.getStudentsNames().contains(Jack.getUsername()));
        System.out.println(resp.getStudentAttemptsResponseList());
        System.out.println(resp.getStudentsNames());
    }
}
