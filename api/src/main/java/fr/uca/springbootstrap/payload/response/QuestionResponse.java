package fr.uca.springbootstrap.payload.response;

import fr.uca.springbootstrap.models.modules.questions.CodeRunner;
import fr.uca.springbootstrap.models.modules.questions.EQuestion;
import fr.uca.springbootstrap.models.modules.questions.Question;

public class QuestionResponse {
    private Long id;

    private String name;

    private String description;

    private EQuestion type;

    public QuestionResponse(Question question, EQuestion type) {
        this.id = question.getId();
        this.name = question.getName();
        this.description = question.getDescription();
        this.type = type;
    }

    public QuestionResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public EQuestion getType() {
        return type;
    }

    public void setType(EQuestion type) {
        this.type = type;
    }
}
