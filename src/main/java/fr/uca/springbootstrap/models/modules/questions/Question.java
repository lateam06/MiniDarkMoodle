package fr.uca.springbootstrap.models.modules.questions;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "questions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "question_type")
@DiscriminatorValue("question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String name;

    @NotBlank
    @Size(max = 256)
    private String desc;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "question_questionnaries",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "questionnary_id"))
    private Questionnary questionnary;

    public Question(String name, String desc) {
        this.name = name;
        this.desc = desc;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
