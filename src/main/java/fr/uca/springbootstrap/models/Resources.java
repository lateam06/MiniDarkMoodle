package fr.uca.springbootstrap.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "resources",uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")}
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_resource")
@DiscriminatorValue("resource")
public class Resources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(max = 20)
    private String name;

    @Size(max = 256)
    private String desc;

    //private EType type;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "resources_modules",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id"))
    private Set<Module> modules = new HashSet<>();



    public Resources() {
    }

    public Resources(String name/*, EType type*/) {
        //TODO ajouter le text ou la question associé etc ...
        this.name = name;
//        this.type = type;
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
        Resources resources = (Resources) o;
        return id == resources.id && name.equals(resources.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name/*, type*/);
    }
}
