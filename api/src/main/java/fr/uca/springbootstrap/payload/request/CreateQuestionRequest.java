package fr.uca.springbootstrap.payload.request;

import fr.uca.springbootstrap.models.modules.questions.EQuestion;
import fr.uca.springbootstrap.models.modules.questions.QCMResponse;
import fr.uca.springbootstrap.models.modules.questions.Question;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

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

    private Set<QCMResponse> qcmResponses;

    public CreateQuestionRequest() {

    }

    public CreateQuestionRequest(String name, String description, String response, EQuestion questionType, Set<String> qcmResponses) {
        this.name = name;
        this.description = description;
        this.response = response;
        this.questionType = questionType;
        this.qcmResponses = new HashSet<>();
        for (String qcmRespons : qcmResponses) {
            this.qcmResponses.add(new QCMResponse(qcmRespons));
        }
    }

    public CreateQuestionRequest(String name, String description, String response, EQuestion questionType, String testCode) {
        this.name = name;
        this.description = description;
        this.response = response;
        this.questionType = questionType;
        this.testCode = testCode;
        this.qcmResponses = new HashSet<>();
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

    public Set<QCMResponse> getQcmResponses() {
        return qcmResponses;
    }

    public void setQcmResponses(Set<String> qcmResponses) {
        this.qcmResponses = new HashSet<>();
        for (String qcmRespons : qcmResponses) {
            this.qcmResponses.add(new QCMResponse(qcmRespons));
        }
    }
}
