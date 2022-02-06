package fr.uca.springbootstrap.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "ressources_modules",
            joinColumns = @JoinColumn(name = "ressource_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id"))
    private Set<Module> modules = new HashSet<>();



    public Ressources() {
    }

    public Ressources(String name, EType type) {
        //TODO ajouter le text ou la question associé etc ...
        this.name = name;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ressources ressources = (Ressources) o;
        return id == ressources.id && name.equals(ressources.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }
}
