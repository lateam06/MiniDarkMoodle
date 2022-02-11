package fr.uca.springbootstrap.payload.response;

public class ResultResponse {
    private int grade;

    public ResultResponse(int grade) {
        this.grade = grade;
    }

    public ResultResponse() {
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
