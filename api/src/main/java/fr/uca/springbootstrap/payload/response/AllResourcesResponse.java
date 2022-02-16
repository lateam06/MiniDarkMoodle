package fr.uca.springbootstrap.payload.response;

import java.util.ArrayList;

public class AllResourcesResponse {
    public ArrayList<ResourceResponse> resources;

    public AllResourcesResponse() {
        this.resources = new ArrayList<>();
    }

    public ArrayList<ResourceResponse> getResources() {
        return resources;
    }

    public void setRessources(ArrayList<ResourceResponse> resources) {
        this.resources = resources;
    }
}
