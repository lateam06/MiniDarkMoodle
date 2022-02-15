package fr.uca.springbootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.questions.Questionnary;
import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.payload.response.AllQuestionsResponse;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.QuestionRepository;
import fr.uca.springbootstrap.repository.QuestionnaryRepository;
import fr.uca.springbootstrap.repository.UserApiRepository;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

public class GetAllQuestionsStepDefs extends SpringIntegration{
    private final static String PASSWORD = "password";

    @Autowired
    UserApiRepository userRepository;

    @Autowired
    QuestionnaryRepository questionnaryRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @When("{string} wants to get all questions from the questionnaire {string} of the module {string}")
    public void getAllQuestions(String userName, String questionnaireName, String moduleName) throws IOException {
        UserApi userApi = userRepository.findByUsername(userName).get();
        Questionnary questionnary = questionnaryRepository.findByName(questionnaireName).get();
        Module module = moduleRepository.findByName(moduleName).get();

        String url = "http://localhost:8080/api/module/" + module.getId() +  "/resources/" + questionnary.getId() + "/questions";
        String token = SpringIntegration.tokenHashMap.get(userName);
        executeGet(url, token);
    }


    @Then("{string} sees the question {string} in the list of questions returned")
    public void seesQuestionName(String userName, String questionName) throws JsonProcessingException {
        var resp = ObjMapper.readValue(latestJson, AllQuestionsResponse.class);
        assertTrue(resp.getQuestionNames().contains(questionName));
    }
}
