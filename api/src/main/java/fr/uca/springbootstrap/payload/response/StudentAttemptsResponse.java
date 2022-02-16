package fr.uca.springbootstrap.payload.response;

import fr.uca.springbootstrap.models.users.UserApi;

import java.util.List;

public class StudentAttemptsResponse {

    UserApi student;
    List<String> studentAttempts;

    public StudentAttemptsResponse() {
    }

    public List<String> getStudentAttempts() {
        return studentAttempts;
    }

    public void setStudentAttempts(List<String> studentAttempts) {
        this.studentAttempts = studentAttempts;
    }

    public UserApi getStudent() {
        return student;
    }

    public void setStudent(UserApi student) {
        this.student = student;
    }
}
