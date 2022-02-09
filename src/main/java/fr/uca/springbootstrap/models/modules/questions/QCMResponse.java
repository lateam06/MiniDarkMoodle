package fr.uca.springbootstrap.models.modules.questions;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "qcm_response")
public class QCMResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String desc;

    public QCMResponse(String desc) {
        this.desc = desc;
    }

    public QCMResponse() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
