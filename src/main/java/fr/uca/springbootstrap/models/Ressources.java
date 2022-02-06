package fr.uca.springbootstrap.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ressources",uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")}
)

public class Ressources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    @Size(max = 20)
    private String name;


    private EType type;





}
