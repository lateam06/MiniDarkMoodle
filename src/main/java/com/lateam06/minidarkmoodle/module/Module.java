package com.lateam06.minidarkmoodle.module;

import com.lateam06.minidarkmoodle.user.Teacher;

import java.util.ArrayList;
import java.util.List;

public class Module {
    private final String name;
    private final String desc;
    private final int id;
    private final List<Ressource> resources;
    private List<Teacher> teachers;

    public Module(String name, String desc, int id) {
        this.name = name;
        this.desc = desc;
        this.id = id;
        this.resources = new ArrayList<>();
        this.teachers = new ArrayList<>();
    }

    public List<Ressource> getCourseList() {
        return new ArrayList<>(resources);
    }

    public Teacher getTeacherRegistered() {
        Teacher t = null;
        if (!teachers.isEmpty()) {
            t = teachers.get(0);
        }
        return t;
    }

    public Ressource getRessourceByID(int id) {
        Ressource r = null;
        for (Ressource res : this.resources) {
            if (res.getId() == id) {
                r = res;
            }
        }
        return r;
    }

    public boolean isTeacherRegistered(Teacher t) {
        if (teachers.isEmpty()) {
            return false;
        }
        else {
            return teachers.get(0).equals(t);
        }
    }

    public void setTeacherRegister(Teacher t) {
        if (teachers.isEmpty()) {
            teachers.add(t);
        }
        else {
            throw new IllegalCallerException(String.format("The teacher %s is already registered", teachers.get(0)));
        }
    }

    public void addResource(Teacher t, Ressource r) {
        if (teachers.contains(t)){
            resources.add(r);
        }
    }

    public void removeRessource(Ressource r) {
        resources.remove(r);
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getId() {
        return id;
    }

    public List<Ressource> getResources() {
        return resources;
    }

    public void removeTeacherRegistered() {
        this.teachers.clear();
    }
}
