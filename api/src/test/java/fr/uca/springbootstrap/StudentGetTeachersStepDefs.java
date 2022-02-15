package fr.uca.springbootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.uca.springbootstrap.payload.response.TeacherResponse;
import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.users.User;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StudentGetTeachersStepDefs extends SpringIntegration{
    private static final String PASSWORD = "password";


    @Autowired
    UserRepository userRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    AuthController authController;

    @When("{string} wants to access the teacher of the module {string}")
    public void wantsToAccessTheTeacherOfTheModule(String arg0, String arg1) throws IOException {
        User student = userRepository.findByUsername(arg0).get();
        Module module = moduleRepository.findByName(arg1).get();

        String url = "http://localhost:8080/api/module/" + module.getId() + "/teachers";
        String jwt = authController.generateJwt(student.getUsername(), PASSWORD);

        executeGet(url, jwt);

    }

    @Then("the return status for the request is {int}")
    public void theReturnStatusForTheRequestIs(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Then("{string} sees that the teacher is {string}")
    public void seesThatTheTeacherIs(String arg0, String arg1) throws JsonProcessingException {
        var resp = ObjMapper.readValue(latestJson, TeacherResponse.class);
        assertTrue(resp.getTeachers().contains(arg1));
    }

    @Then("the list of teacher is not send and the return status of the request is error")
    public void theListOfTeacherIsNotSendAndTheReturnStatusOfTheRequestIsError() {
        assertTrue(latestHttpResponse.getStatusLine().getStatusCode() >= 400);
    }



}
