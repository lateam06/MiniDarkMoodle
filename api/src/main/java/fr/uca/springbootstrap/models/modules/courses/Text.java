package fr.uca.springbootstrap.models.modules.courses;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "texts")
public class Text {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Text text = (Text) o;
        return Objects.equals(id, text.id) && Objects.equals(paragraph, text.paragraph);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paragraph);
    }
}
