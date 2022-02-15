package fr.uca.springbootstrap.models.modules.questions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("code_runners")
public class CodeRunner extends Question{

    private String testCode;

    private String testResponse;

    private String studentResponse;

    public CodeRunner(String name, String description) {
        super(name, description);
    }

    public CodeRunner() {
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public String getTestResponse() {
        return testResponse;
    }

    public void setTestResponse(String testResponse) {
        this.testResponse = testResponse;
    }

    public String getStudentResponse() {
        return studentResponse;
    }

    public void setStudentResponse(String studentResponse) {
        this.studentResponse = studentResponse;
    }



}
