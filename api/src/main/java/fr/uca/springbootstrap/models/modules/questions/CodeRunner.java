package fr.uca.springbootstrap.models.modules.questions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@DiscriminatorValue("code_runners")
public class CodeRunner extends Question{

    private String testCode;

    @NotNull
    private String response;

    public CodeRunner(String name, String description, String response, String testCode) {
        super(name, description);
        this.testCode = testCode;
        this.response = response;
    }

    public CodeRunner() {
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }



}
