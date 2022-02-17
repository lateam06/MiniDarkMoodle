package fr.uca.springbootstrap.payload.request;

import fr.uca.springbootstrap.models.modules.questions.EQuestion;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateQuestionRequest {

    @NotBlank
    @Size(max = 20)
    private String name;

    @NotBlank
    @Size(max = 256)
    private String description;

    private String response;
    private String testCode;

    private EQuestion questionType;


    public CreateQuestionRequest() {

    }

    public CreateQuestionRequest(String name, String description, String response, EQuestion questionType, String testCode) {
        this.name = name;
        this.description = description;
        this.response = response;
        this.questionType = questionType;
        this.testCode = testCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public EQuestion getQuestionType() {
        return questionType;
    }

    public void setQuestionType(EQuestion questionType) {
        this.questionType = questionType;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }
}
