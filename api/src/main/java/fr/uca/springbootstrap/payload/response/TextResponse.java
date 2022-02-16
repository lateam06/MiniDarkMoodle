package fr.uca.springbootstrap.payload.response;

public class TextResponse {

    private Long id;

    private String paragraph;

    public TextResponse(Long id, String paragraph) {
        this.id = id;
        this.paragraph = paragraph;
    }

    public TextResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }
}
