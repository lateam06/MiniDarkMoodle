package fr.uca.springbootstrap.payload.response;

import fr.uca.springbootstrap.models.users.User;

import java.util.List;
import java.util.Set;

public class TeacherResponse {
    private List<String> teachers;

    public TeacherResponse() {
    }

    public List<String> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<String> teachers) {
        this.teachers = teachers;
    }
}
