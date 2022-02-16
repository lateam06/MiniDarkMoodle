package fr.uca.springbootstrap.payload.response;

import java.util.ArrayList;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.Resource;

import javax.persistence.DiscriminatorValue;

public class AllResourcesResponse {
    public ArrayList<ResourceResponse> resources;

    public AllResourcesResponse() {
        this.resources = new ArrayList<>();
    }

    public AllResourcesResponse(Module module) {
        this();

        for (Resource r : module.getResources()) {
            getResources().add(new ResourceResponse(r.getId(), r.getName(), r.getDescription(), r.getClass().getAnnotation(DiscriminatorValue.class).value()));
        }
    }

    public ArrayList<ResourceResponse> getResources() {
        return resources;
    }

    public void setRessources(ArrayList<ResourceResponse> resources) {
        this.resources = resources;
    }
}
