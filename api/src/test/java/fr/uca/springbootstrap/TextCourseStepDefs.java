package fr.uca.springbootstrap;

import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.courses.Course;
import fr.uca.springbootstrap.models.modules.courses.Text;
import fr.uca.springbootstrap.payload.request.TextRequest;
import fr.uca.springbootstrap.repository.CourseRepository;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.TextRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.util.EntityUtils;
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
                + "/module/" + module.getId()
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

    @And("the text {string} is in the course {string}")
    public void theTextIsInTheCourse(String arg0, String arg1) {
        Course course = courseRepository.findByName(arg1).get();
        Text text = textRepository.findByParagraph(arg0)
                .orElse(new Text(arg0));
        textRepository.save(text);
        if (!course.getTexts().contains(text)) {
            course.getTexts().add(text);
            courseRepository.save(course);
        }
    }

    @When("{string} replace the text {string} of {string} in {string} in the module {string}")
    public void replaceTheTextOfInInTheModule(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        String token = SpringIntegration.tokenHashMap.get(arg0);
        Course course = courseRepository.findByName(arg2).get();
        Module module = moduleRepository.findByName(arg4).get();
        Text textbefore = textRepository.findByParagraph(arg1).get();
        TextRequest re = new TextRequest(arg3);

        String url = "http://localhost:8080/api"
                + "/module/" + module.getId()
                + "/resource/" + course.getId()
                + "/text/" + textbefore.getId();

        executePut(url, re, token);
    }

    @Then("the text {string} does not exist")
    public void theTextDoesNotExist(String arg0) {
        var otext =  textRepository.findByParagraph(arg0);
        assertTrue(otext.isEmpty());
    }

    @Then("the text {string} is now in the course {string}")
    public void theTextIsNowInTheCourse(String arg0, String arg1) {
        var otext =  textRepository.findByParagraph(arg0);
        assertTrue(otext.isPresent());
        Text text = otext.get();

        Course course = courseRepository.findByName(arg1).get();
        assertTrue(course.getTexts().contains(text));
    }

    @When("{string} delete the text {string} of the course {string} in the module {string}")
    public void deleteTheText(String arg0, String arg1, String arg2, String arg3) throws IOException {
        String token = SpringIntegration.tokenHashMap.get(arg0);
        Text text = textRepository.findByParagraph(arg1).get();
        Course course = courseRepository.findByName(arg2).get();
        Module module = moduleRepository.findByName(arg3).get();

        String url = "http://localhost:8080/api"
                + "/module/" + module.getId()
                + "/resource/" + course.getId()
                + "/text/" + text.getId();

        executeDelete(url, token);
    }

    @Then("the text {string} does not exist in the course {string}")
    public void theTextDoesNotExistInTheCourse(String arg0, String arg1) {
        var otext = textRepository.findByParagraph(arg0);
        assertTrue(otext.isEmpty());
        Course course = courseRepository.findByName(arg1).get();
        assertFalse(course.getTexts().contains(new Text(arg1)));
    }
}
