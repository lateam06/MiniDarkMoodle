package fr.uca.springbootstrap.models.modules.questions;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("qcm_attempt")
public class QCMAttempt extends Attempt {

    public QCMAttempt(Question question, Long userId) {
        super(question, userId);
    }

    public QCMAttempt() {
    }

    @Override
    public boolean computeResult() {
        return true;
    }
}
