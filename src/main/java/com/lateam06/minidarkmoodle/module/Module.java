package com.lateam06.minidarkmoodle.module;

import java.util.ArrayList;
import java.util.List;

public class Module {
    private final String name;
    private final String desc;
    private final int id;
    private final List<Ressource> ressources;

    public Module(String name, String desc, int id) {
        this.name = name;
        this.desc = desc;
        this.id = id;
        this.ressources = new ArrayList();
    }
    public void addRessource(Ressource r){
        ressources.add(r);
    }
    public void removeRessource(Ressource r ){
        ressources.remove(r);
    }



}
