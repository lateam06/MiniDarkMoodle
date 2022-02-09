package fr.uca.springbootstrap.models.modules.questions;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@DiscriminatorValue("qcms")
public class QCM extends Question {

    @NotNull
    private String response;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "response_qcms",
            joinColumns = @JoinColumn(name = "qcm_id"),
            inverseJoinColumns = @JoinColumn(name = "response_id"))
    private Set<QCMResponse> responses;

    public QCM(String name, String description, String response) {
        super(name, description);
        this.response = response;
    }

    public QCM() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Set<QCMResponse> getResponses() {
        return responses;
    }

    public void setResponses(Set<QCMResponse> responses) {
        this.responses = responses;
    }
}
