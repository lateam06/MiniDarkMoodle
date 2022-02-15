package fr.uca.springbootstrap.models.modules.questions;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("qcm_attempt")
public class QCMAttempt extends Attempt{

    public QCMAttempt(Long questionId, Long userId) {
        super(questionId, userId);
    }

    public QCMAttempt() {
    }

    @Override
    public boolean computeResult() {
        return true;
    }
}
