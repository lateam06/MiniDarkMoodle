package fr.uca.springbootstrap.models.modules.questions;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "qcm_responses")
public class QCMResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String description;

    public QCMResponse(String description) {
        this.description = description;
    }

    public QCMResponse() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }
}
