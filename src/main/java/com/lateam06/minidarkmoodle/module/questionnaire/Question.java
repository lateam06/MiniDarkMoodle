package com.lateam06.minidarkmoodle.module.questionnaire;

public class Question {
    private final int id;
    private final int num;
    private final String desc;


    public Question(int id, int num, String description) {
        this.id = id;
        this.num = num;
        this.desc = description;
    }


    public int getId() {
        return id;
    }

    public int getNum() {
        return num;
    }

    public String getDesc() {
        return desc;
    }
}
