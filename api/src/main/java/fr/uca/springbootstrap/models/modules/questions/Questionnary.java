package fr.uca.springbootstrap.models.modules.questions;

import fr.uca.springbootstrap.models.modules.Resource;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("questionnaries")
public class Questionnary extends Resource {

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "question_questionnaries",
            joinColumns = @JoinColumn(name = "questionnary_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private Set<Question> questionSet;

    @OneToMany(targetEntity = Result.class, mappedBy = "questionnaryId", fetch = FetchType.LAZY)
    private List<Result> results;

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

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public static String generateUrl(Long moduleId, Long questionnaryId) {
        return "http://localhost:8080/api/modules/" + moduleId + "/resources/" + questionnaryId;
    }
}
