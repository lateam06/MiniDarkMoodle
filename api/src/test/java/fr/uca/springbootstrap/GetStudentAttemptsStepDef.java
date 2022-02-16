package fr.uca.springbootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.questions.*;
import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.payload.request.AnswerQuestionRequest;
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
                + "/module/" + module.getId()
                + "/resources/" + questionnary.getId()
                + "/questions/" + qcm.getId();
        AnswerQuestionRequest re = new AnswerQuestionRequest(resp.getId(), resp.getDescription(), EQuestion.QCM);
        executePost(url, re, token);

        //// FAIRE AVEC LES REPOS

        /*UserApi user = userApiRepository.findByUsername(arg0).get();
        QCMAttempt qcmAttempt = qcmAttemptRepository.findByQuestionIdAndUserId(qcm.getId(), user.getId()).orElse(new QCMAttempt());
        assertNotNull(qcmAttempt);
        assertEquals(arg1, qcmAttempt.getStudentAttempt());*/
        assertEquals(200, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @When("{string} wants to get attempts of {string} to the questionnary {string} of the module {string}")
    public void wantsToGetAttemptsOfToTheQuestionnaryOfTheModule(String arg0, String arg1, String arg2, String arg3) throws IOException {
       UserApi student = userApiRepository.findByUsername(arg1).get();
       Questionnary questionnary = questionnaryRepository.findByName(arg2).get();
       Module module = moduleRepository.findByName(arg3).get();
       String token = SpringIntegration.tokenHashMap.get(arg0);

       String url = "http://localhost:8080/api"
               + "/module/" + module.getId()
               + "/resources/" + questionnary.getId()
               + "/attempts/" + student.getId();
       executeGet(url, token);
    }

    @Then("{string} sees that {string} responded {string} for first qcm and {string} for the second")
    public void seesThatRespondedForFirstQcmAndForTheSecond(String teacher, String student, String response1, String response2) throws JsonProcessingException {
        var resp = ObjMapper.readValue(latestJson, StudentAttemptsResponse.class);

        assertTrue(resp.getStudentAttempts().contains(response1));
        assertTrue(resp.getStudentAttempts().contains(response2));
        System.out.println(resp.getStudentAttempts());
    }
}
