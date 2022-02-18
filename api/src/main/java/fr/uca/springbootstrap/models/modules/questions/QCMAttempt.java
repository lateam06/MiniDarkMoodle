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
        try {
            QCM qcm = (QCM) question;
            String correct = qcm.getResponse();
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
