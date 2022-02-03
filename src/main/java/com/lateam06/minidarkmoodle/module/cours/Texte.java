package com.lateam06.minidarkmoodle.module.cours;

public class Texte {
    private int num;
    private String content;

    public Texte(int num, String content) {
        this.num = num;
        this.content = content;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
}
