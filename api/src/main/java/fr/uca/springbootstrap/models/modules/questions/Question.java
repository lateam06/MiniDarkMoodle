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

    @OneToMany(fetch = FetchType.EAGER)
    private List<Attempt> attempts = new java.util.ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "question_questionnary",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "questionnary_id")
        )
    private Questionnary questionnary;

    public void setAttempts(List<Attempt> attempts) {
        this.attempts = attempts;
    }

    public List<Attempt> getAttempts() {
        return attempts;
    }

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
