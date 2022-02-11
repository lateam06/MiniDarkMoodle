package fr.uca.springbootstrap.models.modules.questions;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("qcm_attempt")
public class QCMAttempt extends Attempt{

    @Override
    public boolean computeResult() {
        return true;
    }
}
