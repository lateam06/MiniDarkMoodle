package fr.uca.springbootstrap.payload.response;

import fr.uca.springbootstrap.models.modules.Resource;
import fr.uca.springbootstrap.models.modules.Module;

import javax.persistence.DiscriminatorValue;

public class ModuleResponse extends AllResourcesResponse {
    private long id;
    private String name;

    public ModuleResponse() {
        super();
    }

    public ModuleResponse(Module module) {
        super(module);
        this.id= module.getId();
        this.name = module.getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
