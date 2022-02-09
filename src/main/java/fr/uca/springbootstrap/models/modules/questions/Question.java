package fr.uca.springbootstrap.models.modules.questions;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "question_questionnaries",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "questionnary_id"))
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
}
