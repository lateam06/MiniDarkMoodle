package fr.uca.springbootstrap.models.modules.questions;


import fr.uca.springbootstrap.repository.CodeRunnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.python.util.PythonInterpreter;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.StringWriter;

@CrossOrigin(origins = "*", maxAge = 3600)

@Entity
@DiscriminatorValue("runner_attempt")
public class CodeRunnerAttempt extends Attempt {

    public CodeRunnerAttempt(Question question, Long userId) {
        super(question, userId);
    }

    public CodeRunnerAttempt() {
        super();
    }

    @Override
    public boolean computeResult() {
        CodeRunner cr = (CodeRunner) question;
        try (PythonInterpreter pyInterp = new PythonInterpreter()) {
            StringWriter output = new StringWriter();
            pyInterp.setOut(output);
            pyInterp.exec(studentAttempt + "\n" + cr.getTestCode());
            return output.toString().trim().equals(cr.getResponse());
        } catch (Exception e) {
            return false;
        }
    }


}