package fr.uca.springbootstrap.models.modules.questions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("code_runner")
public class CodeRunner extends Question{

    private String testCode;

    private String testResponse;

    private String studentResponse;

    public CodeRunner(String name, String desc) {
        super(name, desc);
    }

    public CodeRunner() {
    }
}
