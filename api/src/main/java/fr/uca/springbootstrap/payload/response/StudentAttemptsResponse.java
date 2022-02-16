package fr.uca.springbootstrap.payload.response;

import fr.uca.springbootstrap.models.users.UserApi;

import java.util.List;

public class StudentAttemptsResponse {

    String studentName;
    List<String> studentAttempts;

    public StudentAttemptsResponse() {
    }

    public List<String> getStudentAttempts() {
        return studentAttempts;
    }

    public void setStudentAttempts(List<String> studentAttempts) {
        this.studentAttempts = studentAttempts;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
