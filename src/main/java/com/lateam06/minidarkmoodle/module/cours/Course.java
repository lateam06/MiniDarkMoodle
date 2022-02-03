package com.lateam06.minidarkmoodle.module.cours;

import java.util.ArrayList;
import java.util.List;

import com.lateam06.minidarkmoodle.module.AbstractRessource;

public class Course extends AbstractRessource {

    private List<Text> textList;

    public Course(String name, String desc, int id) {
        super(name, desc, id);
        this.textList = new ArrayList<>();
    }
    
    
}
