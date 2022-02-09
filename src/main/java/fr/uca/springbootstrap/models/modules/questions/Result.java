package fr.uca.springbootstrap.models.modules.questions;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long questionId;

    @NotNull
    private Integer rate;

    public Result() {
    }

    public Result(Long userId, Long questionId, Integer rate) {
        this.userId = userId;
        this.questionId = questionId;
        this.rate = rate;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long question_id) {
        this.questionId = question_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long user_id) {
        this.userId = user_id;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}
