package fr.lateam.auth.springbootsrap.payload.request;

import javax.validation.constraints.NotBlank;

public class CodeRequest {


    @NotBlank
    private String code;

    public CodeRequest(String code) {
        this.code = code;
    }

    public CodeRequest() {
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
