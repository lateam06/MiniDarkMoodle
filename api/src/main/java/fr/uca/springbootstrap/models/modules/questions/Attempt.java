package fr.uca.springbootstrap.models.modules.questions;

import javax.persistence.*;

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

    private Long userId;

    protected Long questionId;

    private String studentAttempt;

    public Attempt(Long questionId, Long userId) {
        this.questionId = questionId;
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

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

}