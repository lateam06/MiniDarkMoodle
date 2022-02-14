package fr.uca.springbootstrap.payload.request;

public class CreateTextRequest {

    private String paragraph;

    public CreateTextRequest(String paragraph) {
        this.paragraph = paragraph;
    }

    public CreateTextRequest() {
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }
}
