package fr.uca.springbootstrap.payload.response;

import java.util.List;

public class AllQuestionsResponse {
    private List<String> questionNames;

    public AllQuestionsResponse() {
    }

    public List<String> getQuestionNames() {
        return questionNames;
    }

    public void setQuestionNames(List<String> questionNames) {
        this.questionNames = questionNames;
    }
}
