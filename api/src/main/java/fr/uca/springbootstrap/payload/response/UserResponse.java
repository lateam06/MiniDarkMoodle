package fr.uca.springbootstrap.payload.response;

import fr.uca.springbootstrap.models.users.Role;

import java.util.List;

public class UserResponse {
    String name;
    Long id;
    List<String> roles;

    public UserResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
