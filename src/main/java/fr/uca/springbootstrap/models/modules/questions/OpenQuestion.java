package fr.uca.springbootstrap.models.modules.questions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

@Entity
@DiscriminatorValue("open")
public class OpenQuestion extends Question {

    @Size(max = 512)
    private String response;

    public OpenQuestion(String name, String desc, String response) {
        super(name, desc);
        this.response = response;
    }

    public OpenQuestion() {

    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
