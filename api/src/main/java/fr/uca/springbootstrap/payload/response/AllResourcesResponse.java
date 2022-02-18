package fr.uca.springbootstrap.payload.response;

import java.util.ArrayList;
import java.util.List;

import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.Resource;

import javax.persistence.DiscriminatorValue;

public class AllResourcesResponse {
    public List<ResourceResponse> resources;

    public AllResourcesResponse() {
        this.resources = new ArrayList<>();
    }

    public AllResourcesResponse(Module module) {
        this();
        this.resources = new ArrayList<>();
        if (module.getResources() != null && !module.getResources().isEmpty()) {
            for (Resource r : module.getResources()) {
                getResources().add(new ResourceResponse(r.getId(), r.getName(), r.getDescription(), r.getClass().getAnnotation(DiscriminatorValue.class).value()));
            }
        }
    }

    public List<ResourceResponse> getResources() {
        return resources;
    }

    public void setRessources(List<ResourceResponse> resources) {
        this.resources = resources;
    }
}
