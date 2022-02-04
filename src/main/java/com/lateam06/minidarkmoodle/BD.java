package com.lateam06.minidarkmoodle;

import com.lateam06.minidarkmoodle.module.Module;
import com.lateam06.minidarkmoodle.user.Teacher;

import java.util.ArrayList;
import java.util.List;

public class BD {

    private List<Module> modules;
    private List<Teacher> teachers;

    public BD() {
        this.modules = new ArrayList<>();
        this.teachers = new ArrayList<>();
    }

    public void addModule(Module m) {
        this.modules.add(m);
    }

    public void removeModule(Module m) {
        this.modules.remove(m);
    }

    public Module getModuleByID(int id) {
        for (Module m : modules) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    public void addTeacher(Teacher t) {
        this.teachers.add(t);
    }

    public void removeTeacher(Teacher t) {
        this.teachers.remove(t);
    }

    public Teacher getTeacherByID(int id) {
        for (Teacher t : teachers) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }
}
