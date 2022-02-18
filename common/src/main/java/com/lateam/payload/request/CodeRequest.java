package com.lateam.payload.request;

public class CodeRequest {
    private String studentCode;
    private String test;
    private String testResult;


    public CodeRequest(String studentCode, String test, String testResult) {
        this.studentCode = studentCode;
        this.test = test;
        this.testResult = testResult;
    }

    public CodeRequest() {
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }
}
