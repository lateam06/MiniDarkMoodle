package fr.uca.springbootstrap.payload.response;

import java.util.List;

public class AllUserResponse {
    List<String> names;
    List<Long> iDs;
    List<String> roles;

    public AllUserResponse() {
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<Long> getiDs() {
        return iDs;
    }

    public void setiDs(List<Long> iDs) {
        this.iDs = iDs;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
