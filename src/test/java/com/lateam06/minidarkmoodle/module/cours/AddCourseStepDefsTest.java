package com.lateam06.minidarkmoodle.module.cours;

import io.cucumber.java.en.Given;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import com.lateam06.minidarkmoodle.module.Module;
import com.lateam06.minidarkmoodle.user.Teacher;

class AddCourseStepDefsTest {

    private Module m;
    private Teacher marcel;
    private Course c;

    public AddCourseStepDefsTest() {

    }

    @Given("a teacher named {string} and with teacher ID {int}")
    public void givenATeacher(String teacherName, int teacherId) {
        this.m = new Module("test", "test module for gherkin", 0);
        this.marcel = new Teacher(teacherName, "123", teacherId);
    }

}