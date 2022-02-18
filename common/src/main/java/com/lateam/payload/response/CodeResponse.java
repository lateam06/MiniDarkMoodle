package com.lateam.payload.response;

public class CodeResponse {

    private boolean result;

    public CodeResponse(boolean result) {
        this.result = result;
    }

    public CodeResponse() {
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
