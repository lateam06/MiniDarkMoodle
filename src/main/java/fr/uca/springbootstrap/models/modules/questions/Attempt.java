package fr.uca.springbootstrap.models.modules.questions;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "attempt_type")
@DiscriminatorValue("attempt")
@Table(name = "attempts")
public class Attempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "question_id")
    protected Question question;

    private String student_attempt;

    public Attempt(Question question, Long userId) {
        this.question = question;
        this.userId = userId;
    }

    public Attempt() {
    }

    public boolean computeResult() {
        return false;
    }

    public Long getId() {
        return id;
    }

    public String getStudent_attempt() {
        return student_attempt;
    }

    public void setStudent_attempt(String student_attempt) {
        this.student_attempt = student_attempt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}