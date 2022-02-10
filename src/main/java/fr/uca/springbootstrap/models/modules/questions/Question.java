package fr.uca.springbootstrap.models.modules.questions;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "question_type")
@DiscriminatorValue("question")
@Table(name = "questions", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String name;

    @NotBlank
    @Size(max = 256)
    private String description;

    @OneToMany(targetEntity = Result.class, mappedBy = "userId", fetch = FetchType.EAGER)
    private List<Result> results;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "question_questionnary",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "questionnary_id")
        )
    private Questionnary questionnary;

    public Question(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Question() {

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

    public Questionnary getQuestionnary() {
        return questionnary;
    }

    public void setQuestionnary(Questionnary questionnary) {
        this.questionnary = questionnary;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id) && Objects.equals(name, question.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
