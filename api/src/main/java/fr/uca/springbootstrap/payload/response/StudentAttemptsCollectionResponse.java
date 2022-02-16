package fr.uca.springbootstrap.payload.response;

import java.util.List;

public class StudentAttemptsCollectionResponse {
    List<String> studentAttemptsResponseList;
    List<String> studentsNames;

    public StudentAttemptsCollectionResponse() {}

    public List<String> getStudentAttemptsResponseList() {
        return studentAttemptsResponseList;
    }

    public void setStudentAttemptsResponseList(List<String> studentAttemptsResponseList) {
        this.studentAttemptsResponseList = studentAttemptsResponseList;
    }

    public List<String> getStudentsNames() {
        return studentsNames;
    }

    public void setStudentsNames(List<String> studentsNames) {
        this.studentsNames = studentsNames;
    }
}
