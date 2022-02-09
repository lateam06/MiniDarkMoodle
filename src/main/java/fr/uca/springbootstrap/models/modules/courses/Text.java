package fr.uca.springbootstrap.models.modules.courses;

import javax.persistence.*;

@Entity
@Table(name = "text")
public class Text {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paragraph;

    public Text(String paragraph) {
        this.paragraph = paragraph;
    }

    public Text() {
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
