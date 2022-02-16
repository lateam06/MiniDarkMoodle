package fr.uca.springbootstrap.payload.response;

import java.util.List;

public class TextCollectionResponse {

    private List<String> textList;

    public TextCollectionResponse(List<String> textList) {
        this.textList = textList;
    }

    public TextCollectionResponse() {
    }

    public List<String> getTextList() {
        return textList;
    }

    public void setTextList(List<String> textList) {
        this.textList = textList;
    }
}
