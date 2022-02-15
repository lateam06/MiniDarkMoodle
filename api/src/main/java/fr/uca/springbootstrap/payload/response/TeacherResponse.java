package fr.uca.springbootstrap.payload.response;

import java.util.List;

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
