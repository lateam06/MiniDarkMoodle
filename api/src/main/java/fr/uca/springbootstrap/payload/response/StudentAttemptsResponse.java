package fr.uca.springbootstrap.payload.response;

import java.util.List;

public class StudentAttemptsResponse {
    List<String> studentAttempts;

    public StudentAttemptsResponse() {
    }

    public List<String> getStudentAttempts() {
        return studentAttempts;
    }

    public void setStudentAttempts(List<String> studentAttempts) {
        this.studentAttempts = studentAttempts;
    }
}
