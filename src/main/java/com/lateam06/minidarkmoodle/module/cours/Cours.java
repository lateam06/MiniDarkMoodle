package com.lateam06.minidarkmoodle.module.cours;

import java.util.ArrayList;
import java.util.List;

import com.lateam06.minidarkmoodle.module.AbstractRessource;

public class Cours extends AbstractRessource {

    private List<Texte> textList;

    public Cours(String name, String desc, int id) {
        super(name, desc, id);
        this.textList = new ArrayList<>();
    }
    
    
}
