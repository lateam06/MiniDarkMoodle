package fr.uca.springbootstrap.models.modules.questions;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("open_attempt")
public class OpenAttempt extends Attempt{

    public OpenAttempt(Question question, Long userId) {
        super(question, userId);
    }

    public OpenAttempt() {
    }

    @Override
    public boolean computeResult() {
        try {
            OpenQuestion openQuestion = (OpenQuestion) question;
            String correct = openQuestion.getResponse();
            String studentResp = getStudentAttempt();

            if (correct.equals(studentResp)) {
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e){
            return false;
        }
    }
}