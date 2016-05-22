package com.example.caseyjones.mydictionary.models.word;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by CaseyJones on 14.05.2016.
 */
public class TranslateWord implements Serializable{

    @SerializedName("text")
    private String text;

    @SerializedName("pos")
    private String pos;

    public TranslateWord(String text, String pos) {
        this.text = text;
        this.pos = pos;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
