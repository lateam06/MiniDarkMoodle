package com.lateam06.minidarkmoodle.user;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends AbstractUser {

    private List<Module> modules;

    public Teacher(String name, String password, int id) {
        super(name, password, id);
        this.setModules(new ArrayList<>());
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

}
