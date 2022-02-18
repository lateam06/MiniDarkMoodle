package fr.uca.springbootstrap.payload.response;
import fr.uca.springbootstrap.models.modules.Module;

import java.util.ArrayList;
import java.util.List;

public class ModulesResponse {
    private List<String> modules = new ArrayList<>();

    public ModulesResponse(List<Module> modules) {
        for (Module module : modules) {

            this.modules.add(module.getName() + String.format(" id : %d",module.getId()));}

        }




    public ModulesResponse() {
    }

    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

}
