package com.lateam06.minidarkmoodle.module.cours;

import com.lateam06.minidarkmoodle.BD;
import com.lateam06.minidarkmoodle.module.Ressource;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import com.lateam06.minidarkmoodle.module.Module;
import com.lateam06.minidarkmoodle.user.Teacher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AddCourseStepDefsTest {

    private Module m1;
    private Module m2;
    private Teacher marcel;
    private Course c;
    private BD bd;

    public AddCourseStepDefsTest() {

    }

    @Given("a teacher named {string} with ID {int}")
    public void aTeacherNamedWithID(String arg0, int arg1) {
        this.bd = new BD();
        this.marcel = new Teacher(arg0, "123", arg1);
        this.bd.addTeacher(this.marcel);
    }


    @And("a module with ID {int}")
    public void aModuleWithID(int arg0) {
        this.m1 = new Module("module1", "first module test", arg0);
        this.bd.addModule(this.m1);
    }

    @And("a course with ID {int}")
    public void aCourseWithID(int arg0) {
        this.m2 = new Module("module2", "second module test", arg0);
        this.bd.addModule(this.m2);
    }

    @Given("Marcel {int} is the teacher registered to the module {int}")
    public void marcelIsTheTeacherRegisteredToTheModule(int arg0, int arg1) {
        Module m = this.bd.getModuleByID(arg1);
        Teacher t = this.bd.getTeacherByID(arg0);
        m.setTeacherRegister(t);
    }

    @When("Marcel {int} want to add the course {int} to the module {int}")
    public void marcelWantToAddTheCourseToTheModule(int arg0, int arg1, int arg2) {
        Module m = this.bd.getModuleByID(arg2);
        Course c = new Course("Test", "test cours fou", arg1);
        Teacher t = this.bd.getTeacherByID(arg0);
        m.addResource(t, c);
    }

    @Then("The course {int} is added to the module {int}")
    public void theCourseIsAddedToTheModule(int arg0, int arg1) {
        Module m = this.bd.getModuleByID(arg1);
        List<Ressource> resourceList = m.getResources();
        Ressource r = null;
        for (Ressource course : resourceList) {
            if (course.getId() == arg0) {
                r = course;
                break;
            }
        }
        assertNotNull(r);
        assertEquals(r.getId(), arg0);
    }


    @Given("the module has a teacher already registered to the module {int}")
    public void theModuleHasATeacherAlreadyRegisteredToTheModule(int arg0) {
        Teacher t = new Teacher("null", "111", 999);
        this.bd.addTeacher(t);
        Module m = this.bd.getModuleByID(arg0);
        m.removeTeacherRegistered();
        m.setTeacherRegister(t);
    }

    @And("Marcel {int} isn't registered as the teacher of this module {int}")
    public void marcelIsnTRegisteredAsTheTeacherOfThisModule(int arg0, int arg1) {
        Teacher marcel = this.bd.getTeacherByID(arg0);
        Module m = this.bd.getModuleByID(arg1);
        Teacher registeredTeacher = m.getTeacherRegistered();
        if (registeredTeacher != null) {
            assertNotEquals(marcel.getId(), registeredTeacher.getId());
        }
    }

    @When("Marcel {int} want to add the course {int} in the module {int}")
    public void marcelWantToAddTheCourseInTheModule(int arg0, int arg1, int arg2) {
        Teacher marcel = this.bd.getTeacherByID(arg0);
        Module m = this.bd.getModuleByID(arg2);
        Course course = new Course("test", "coucou", arg1);
        m.addResource(marcel, course);
    }

    @Then("The course {int} isn't added to the module {int}")
    public void theCourseIsnTAddedToTheModule(int arg0, int arg1) {
        Course course = new Course("test", "coucou", arg0);
        Module m = this.bd.getModuleByID(arg1);
        assertNull(m.getRessourceByID(arg0));
    }

    @Given("the module {int} has no teacher registered")
    public void theModuleHasNoTeacherRegistered(int arg0) {
        Module m = this.bd.getModuleByID(arg0);
        m.removeTeacherRegistered();
    }
}
