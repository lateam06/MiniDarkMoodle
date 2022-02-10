package fr.uca.springbootstrap.payload.request;

import fr.uca.springbootstrap.models.modules.courses.Text;
import fr.uca.springbootstrap.models.modules.questions.Question;

import javax.validation.constraints.NotBlank;
import java.util.Set;

public class ResourceRequest {

    @NotBlank
    private String name;


    @NotBlank
    private String type;

    private String description;

    @NotBlank
    private Boolean visibility;


    private Set<Text> texts;

    private Set<Question> questionSet;


    public ResourceRequest(String name, String type, String description, Boolean visibility, Set<Text> texts, Set<Question> questionSet) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.visibility = visibility;
        this.texts = texts;
        this.questionSet = questionSet;
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

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }


    public String getType() {
        return type;
    }

    public Set<Text> getTexts() {
        return texts;
    }

    public Set<Question> getQuestionSet() {
        return questionSet;
    }
}
