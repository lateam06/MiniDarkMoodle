package fr.uca.springbootstrap.payload.request;

public class TextRequest {

    private String paragraph;

    public TextRequest(String paragraph) {
        this.paragraph = paragraph;
    }

    public TextRequest() {
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }
}
