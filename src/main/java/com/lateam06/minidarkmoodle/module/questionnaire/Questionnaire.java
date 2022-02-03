package com.lateam06.minidarkmoodle.module.questionnaire;

import com.lateam06.minidarkmoodle.module.AbstractRessource;
import com.lateam06.minidarkmoodle.module.Ressource;

import java.util.ArrayList;
import java.util.List;

public class Questionnaire extends AbstractRessource {
    private final String name;
    private final String desc;
    private final int id;

  private final List<Question> questions;
    public Questionnaire(String name, String desc, int id) {
        super(name,desc,id);
        this.name = name;
        this.desc = desc;
        this.id = id;

        this.questions = new ArrayList<>();
    }


    public List<Question> getQuestions() {
        return questions;
    }
    public void addQuestion(Question q){
        questions.add(q);
    }
    public void removeQuestion(Question q ){
        questions.remove(q);
    }


}
