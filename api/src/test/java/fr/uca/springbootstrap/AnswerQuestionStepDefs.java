package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.questions.QCM;
import fr.uca.springbootstrap.models.modules.questions.QCMResponse;
import fr.uca.springbootstrap.models.modules.questions.Questionnary;
import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.payload.request.AnswerQCMRequest;
import fr.uca.springbootstrap.repository.*;
import io.cucumber.java.bs.A;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;


public class AnswerQuestionStepDefs extends SpringIntegration {

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
    CourseRepository courseRepository;

    @Autowired
    QuestionnaryRepository questionnaryRepository;

    @Autowired
    QCMRepository qcmRepository;

    @Autowired
    OpenQuestionRepository openQuestionRepository;

    @Autowired
    QCMResponseRepository qcmResponseRepository;

    @Autowired
    QCMAttemptRepository qcmAttemptRepository;


    private final static String PASSWORD = "password";

    @And("a questionnaire named {string} in {string}")
    public void aQuestionnaireWithNameIn(String arg0, String arg1) {
        Questionnary questionnary = questionnaryRepository.findByName(arg0)
                .orElse(new Questionnary(arg0));
        Module module = moduleRepository.findByName(arg1)
                .orElse(new Module(arg1));
        module.getResources().add(questionnary);

        questionnaryRepository.save(questionnary);
        moduleRepository.save(module);

    }

    @And("a QCM named {string} with correct answer {string} in {string}")
    public void aQCMNamedWithCorrectAnswerIn(String arg0, String arg1, String arg2) {
        QCM qcm = qcmRepository.findByName(arg0)
                .orElse(new QCM(arg0, "test", arg1));
        Questionnary questionnary = questionnaryRepository.findByName(arg2).get();
        questionnary.getQuestionSet().add(qcm);
        qcmRepository.save(qcm);
        questionnaryRepository.save(questionnary);
    }

    @And("two responses {string} and {string} in the qcm {string}")
    public void twoResponsesAndInTheQcm(String arg0, String arg1, String arg2) {
        QCMResponse resp1 = qcmResponseRepository.findByDescription(arg0)
                .orElse(new QCMResponse(arg0));
        QCMResponse resp2 = qcmResponseRepository.findByDescription(arg1)
                .orElse(new QCMResponse(arg1));
        QCM qcm = qcmRepository.findByName(arg2).get();
        qcm.getResponses().add(resp1);
        qcm.getResponses().add(resp2);

        qcmResponseRepository.save(resp1);
        qcmResponseRepository.save(resp2);
        qcmRepository.save(qcm);
    }

    @When("{string} wants to answer {string} in the qcm {string} of the questionnaire {string} of the module {string}")
    public void wantsToAnswerInTheQcmOfTheQuestionnaireOfTheModule(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        QCMResponse qcmResponse = qcmResponseRepository.findByDescription(arg1).get();
        QCM qcm = qcmRepository.findByName(arg2).get();
        Questionnary questionnary = questionnaryRepository.findByName(arg3).get();
        Module module = moduleRepository.findByName(arg4).get();
        String token  = SpringIntegration.tokenHashMap.get(arg0);

        String url = "http://localhost:8080/api"
                + "/module/" + module.getId()
                + "/resources/" + questionnary.getId()
                + "/qcm/" + qcm.getId();
        AnswerQCMRequest re = new AnswerQCMRequest(qcmResponse.getId(), qcmResponse.getDescription());
        executePost(url, re, token);

        System.out.println(EntityUtils.toString(latestHttpResponse.getEntity()));
    }

    @Then("{string} responsed {string} to the qcm {string} of the questionnaire {string} of the module {string}")
    public void responsedToTheQcmOfTheQuestionnaireOfTheModule(String arg0, String arg1, String arg2, String arg3, String arg4) {

    }
}