package com.lateam06.minidarkmoodle.module.cours;

public class Text {
    private int num;
    private String content;

    public Text(int num, String content) {
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
