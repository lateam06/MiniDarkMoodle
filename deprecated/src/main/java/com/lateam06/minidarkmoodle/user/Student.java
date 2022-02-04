package com.lateam06.minidarkmoodle.user;

import java.util.ArrayList;
import java.util.List;

public class Student extends AbstractUser {

    private List<Module> modules;

    public Student(String name, String password, int id) {
        super(name, password, id);
        this.modules = new ArrayList<>();
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

}
