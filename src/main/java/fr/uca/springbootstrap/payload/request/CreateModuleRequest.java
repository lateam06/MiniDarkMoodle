package fr.uca.springbootstrap.payload.request;

import javax.validation.constraints.NotBlank;

public class CreateModuleRequest {

    public CreateModuleRequest(String name) {
        this.name = name;
    }

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
