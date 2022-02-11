package fr.uca.springbootstrap.models.modules.questions;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
    private Long questionnaryId;

    private Integer rate;

    public Result() {
    }

    public Result(Long userId, Long questionnaryId, Integer rate) {
        this.userId = userId;
        this.questionnaryId = questionnaryId;
        this.rate = rate;
    }

    public Long getQuestionnaryId() {
        return questionnaryId;
    }

    public void setQuestionnaryId(Long questionnaryId) {
        this.questionnaryId = questionnaryId;
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

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}
