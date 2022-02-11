package fr.uca.springbootstrap.models.modules.courses;

import fr.uca.springbootstrap.models.modules.Resource;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue("courses")
public class Course extends Resource {

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "text_courses",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "text_id"))
    private Set<Text> texts;

    public Course(String name) {
        super(name);
    }

    public Course() {
    }

    public Set<Text> getTexts() {
        return texts;
    }

    public void setTexts(Set<Text> texts) {
        this.texts = texts;
    }
}
