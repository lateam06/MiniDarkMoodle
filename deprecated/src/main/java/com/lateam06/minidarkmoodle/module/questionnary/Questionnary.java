package com.lateam06.minidarkmoodle.module.questionnary;

import com.lateam06.minidarkmoodle.module.AbstractRessource;

import java.util.ArrayList;
import java.util.List;

public class Questionnary extends AbstractRessource {
  private final List<Question> questions;
    public Questionnary(String name, String desc, int id) {
        super(name,desc,id);
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
