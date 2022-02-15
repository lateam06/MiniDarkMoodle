package fr.uca.springbootstrap.payload.request;

public class AnswerQCMRequest {

    private Long id;

    private String response;

    public AnswerQCMRequest(Long id, String response) {
        this.response = response;
        this.id = id;
    }

    public AnswerQCMRequest() {
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