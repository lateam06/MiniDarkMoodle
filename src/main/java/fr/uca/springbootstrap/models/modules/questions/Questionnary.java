package fr.uca.springbootstrap.models.modules.questions;

import fr.uca.springbootstrap.models.modules.Resources;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Questionnary extends Resources {

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "question_questionnaries",
            joinColumns = @JoinColumn(name = "questionnary_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private Set<Question> questionSet;

    public Questionnary(String name) {
        super(name);
    }

    public Questionnary() {
    }

    public Set<Question> getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(Set<Question> questionSet) {
        this.questionSet = questionSet;
    }
}
