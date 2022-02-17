package fr.uca.springbootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.courses.Course;
import fr.uca.springbootstrap.models.modules.courses.Text;
import fr.uca.springbootstrap.payload.request.TextRequest;
import fr.uca.springbootstrap.payload.response.TextResponse;
import fr.uca.springbootstrap.repository.CourseRepository;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.TextRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TextCourseStepDefs extends SpringIntegration {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    TextRepository textRepository;

    @When("{string} add the text {string} to the course {string} in the module {string}")
    public void addTheTextToTheCourse(String teacherName, String paragraph, String courseName, String moduleName) throws IOException {
        String token = SpringIntegration.tokenHashMap.get(teacherName);
        Course course = courseRepository.findByName(courseName).get();
        TextRequest textRequest = new TextRequest(paragraph);
        Module module = moduleRepository.findByName(moduleName).get();

        String url = "http://localhost:8080/api"
                + "/modules/" + module.getId()
                + "/resource/" + course.getId()
                + "/text";

        executePost(url, textRequest, token);
    }

    @Then("the text {string} has been added to the course {string}")
    public void theTextHasBeenAddedToTheCourse(String paragraph, String courseName) {
        var otext = textRepository.findByParagraph(paragraph);
        Course course = courseRepository.findByName(courseName).get();
        assertTrue(otext.isPresent());

        Text text = otext.get();
        assertTrue(course.getTexts().contains(text));
    }

    @Given("{string} has already registered the text {string} in the course {string} of the module {string}")
    public void hasAlreadyRegisteredTheTextInTheCourseOfTheModule(String teacherName, String textBefore, String courseName, String moduleName) throws IOException {
        String token = tokenHashMap.get(teacherName);
        Course course = courseRepository.findByName(courseName).get();
        Module module = moduleRepository.findByName(moduleName).get();

        TextRequest re = new TextRequest(textBefore);

        String url = "http://localhost:8080/api"
                + "/modules/" + module.getId()
                + "/resource/" + course.getId()
                + "/text";

        executePost(url, re, token);
    }

    @When("{string} replace the text {string} of {string} in {string} in the module {string}")
    public void replaceTheTextIdOfInInTheModule(String teacherName, String oldText, String courseName, String newText, String moduleName) throws IOException {
        assertEquals(202, latestHttpResponse.getStatusLine().getStatusCode());
        var tr = ObjMapper.readValue(latestJson, TextResponse.class);

        Text text = textRepository.findById(tr.getId()).get();
        String token = tokenHashMap.get(teacherName);
        Course course = courseRepository.findByName(courseName).get();
        Module module = moduleRepository.findByName(moduleName).get();

        String url = "http://localhost:8080/api"
                + "/modules/" + module.getId()
                + "/resource/" + course.getId()
                + "/text/" + text.getId();

        TextRequest textRequest = new TextRequest(newText);
        executePut(url, textRequest, token);
    }

    @Then("the text {string} in course {string} is now {string}")
    public void theTextInCourseIsNow(String textBefore, String courseName, String textAfter) throws JsonProcessingException {
        assertEquals(202, latestHttpResponse.getStatusLine().getStatusCode());

        var tr = ObjMapper.readValue(latestJson, TextResponse.class);
        var otext = textRepository.findById(tr.getId());

        assertTrue(otext.isPresent());
        Text text = otext.get();

        assertEquals(textAfter, text.getParagraph());
        assertTrue(courseRepository.findByName(courseName).get().getTexts().contains(text));
    }

    @When("{string} delete the text {string} of the course {string} in the module {string}")
    public void deleteTheTextOfTheCourseInTheModule(String teacherName, String textPar, String courseName, String moduleName) throws IOException {
        assertEquals(202, latestHttpResponse.getStatusLine().getStatusCode());

        var tr = ObjMapper.readValue(latestJson, TextResponse.class);
        var text = textRepository.findById(tr.getId()).get();

        Course course = courseRepository.findByName(courseName).get();
        Module module = moduleRepository.findByName(moduleName).get();
        String token = tokenHashMap.get(teacherName);

        String url = "http://localhost:8080/api"
                + "/modules/" + module.getId()
                + "/resource/" + course.getId()
                + "/text/" + text.getId();

        executeDelete(url, token);
    }


    @Then("the text {string} does not exist in the course {string}")
    public void theTextDoesNotExistInTheCourse(String textPar, String courseName) throws IOException {
        assertEquals(202, latestHttpResponse.getStatusLine().getStatusCode());

        var tr = ObjMapper.readValue(latestJson, TextResponse.class);
        assertTrue(textRepository.findById(tr.getId()).isEmpty());

        Course course = courseRepository.findByName(courseName).get();
        boolean verif = false;

        for (Text text : course.getTexts()) {
            if (text.getId().equals(tr.getId())) {
                verif = true;
                break;
            }
        }

        assertFalse(verif);
    }

    @When("{string} read the course {string} of the module {string}")
    public void readTheCourseOfTheModule(String studentName, String courseName, String moduleName) throws IOException {
        Course course = courseRepository.findByName(courseName).get();
        Module module = moduleRepository.findByName(moduleName).get();
        String token = tokenHashMap.get(studentName);

        String url = "http://localhost:8080/api"
                + "/modules/" + module.getId()
                + "/resource/" + course.getId()
                + "/text";

        executeGet(url, token);
    }

    @Then("he can't read the course")
    public void heCanTReadTheCourse() {
        assertEquals(400, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Then("he can read the course")
    public void heCanReadTheCourse() {
        assertEquals(200, latestHttpResponse.getStatusLine().getStatusCode());
    }
}
