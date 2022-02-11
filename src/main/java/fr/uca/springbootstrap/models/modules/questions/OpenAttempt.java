package fr.uca.springbootstrap.models.modules.questions;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("open_attempt")
public class OpenAttempt extends Attempt{

    @Override
    public boolean computeResult() {
        return true;
    }
}