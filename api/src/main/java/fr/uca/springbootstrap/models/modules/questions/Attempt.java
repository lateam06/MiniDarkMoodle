package fr.uca.springbootstrap.models.modules.questions;

import javax.persistence.*;
import java.util.Objects;

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

    protected Long userId;

    @ManyToOne
    @JoinColumn(name = "question_id")
    protected Question question;
    protected String studentAttempt;

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

    public String getStudentAttempt() {
        return studentAttempt;
    }

    public void setStudentAttempt(String student_attempt) {
        this.studentAttempt = student_attempt;
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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attempt attempt = (Attempt) o;
        return Objects.equals(id, attempt.id) && Objects.equals(userId, attempt.userId) && Objects.equals(question, attempt.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, question);
    }
}