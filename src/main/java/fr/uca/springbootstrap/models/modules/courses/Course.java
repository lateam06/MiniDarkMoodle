package fr.uca.springbootstrap.models.modules.courses;

import fr.uca.springbootstrap.models.modules.Resources;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue("course")
public class Course extends Resources {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "text_course",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "text_id"))
    private Set<Text> texts;

    public Course(String name) {
        super(name);
    }

    public Course() {
    }
}
