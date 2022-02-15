package com.lateam.payload.response;

public class UserApiResponse {

    private Long id;

    private String username;

    public UserApiResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public UserApiResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
