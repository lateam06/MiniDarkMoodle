package fr.uca.springbootstrap;
import com.fasterxml.jackson.core.JsonProcessingException;
import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.courses.Course;
import fr.uca.springbootstrap.models.modules.questions.OpenQuestion;
import fr.uca.springbootstrap.models.modules.questions.QCM;
import fr.uca.springbootstrap.models.modules.questions.Questionnary;
import fr.uca.springbootstrap.models.users.User;
import fr.uca.springbootstrap.payload.request.CreateNewOpenRequest;
import fr.uca.springbootstrap.payload.request.CreateNewQCMRequest;
import fr.uca.springbootstrap.payload.response.ResultResponse;
import fr.uca.springbootstrap.repository.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

public class ValidateQuestionnaireStepdefs extends SpringIntegration  {
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




    @When("{string} validate his questionnary {string} of the module {string}")
    public void validateHisQuestionnaryOfTheModule(String arg0, String arg1, String arg2) throws IOException {
        User student = userRepository.findByUsername(arg0).get();
        Questionnary quest = questionnaryRepository.findByName(arg1).get();
        Module mod = moduleRepository.findByName(arg2).get();
        String token = authController.generateJwt(student.getUsername(), PASSWORD);
        executeGet("http://localhost:8080/api/module/"+mod.getId()+"/resources/"+quest.getId()+"/validate",token);

    }



    @Then("he gets a {int} because he's bad")
    public void heGetsABecauseHeSBad(int arg0) throws JsonProcessingException {
        ResultResponse res = ObjMapper.readValue(latestJson, ResultResponse.class);
        assertEquals(arg0,res.getGrade());
    }
}
