package fr.lateam.auth.springbootsrap.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateNewOpenRequest {

    @NotBlank
    @Size(max = 20)
    private String name;

    @NotBlank
    @Size(max = 256)
    private String description;

    @Size(max = 512)
    private String response;

    public CreateNewOpenRequest() {

    }

    public CreateNewOpenRequest(String name, String description, String response) {
        this.name = name;
        this.description = description;
        this.response = response;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
