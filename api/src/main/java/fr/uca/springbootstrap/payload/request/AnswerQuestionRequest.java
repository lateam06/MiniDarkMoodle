package fr.uca.springbootstrap.payload.request;

import fr.uca.springbootstrap.models.modules.questions.EQuestion;

public class AnswerQuestionRequest {

    private Long id;

    private String response;

    private EQuestion questionType;

    public AnswerQuestionRequest(Long id, String response, EQuestion questionType) {
        this.response = response;
        this.id = id;
        this.questionType = questionType;
    }

    public AnswerQuestionRequest() {
    }

    public EQuestion getQuestionType() {
        return questionType;
    }

    public void setQuestionType(EQuestion questionType) {
        this.questionType = questionType;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}