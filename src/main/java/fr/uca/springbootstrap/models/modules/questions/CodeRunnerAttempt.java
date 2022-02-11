package fr.uca.springbootstrap.models.modules.questions;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("runner_attempt")
public class CodeRunnerAttempt extends Attempt{

    @Override
    public boolean computeResult() {
        return true;
    }
}