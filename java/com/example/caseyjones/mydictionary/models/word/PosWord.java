package com.example.caseyjones.mydictionary.models.word;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CaseyJones on 14.05.2016.
 */
public class PosWord implements Serializable{

    @SerializedName("text")
    private String text;

    @SerializedName("pos")
    private String pos;

    @SerializedName("ts")
    private String ts;

    @SerializedName("tr")
    private ArrayList <TranslateWord> tr;

    public PosWord(String text, String pos, String ts, ArrayList<TranslateWord> tr) {
        this.text = text;
        this.pos = pos;
        this.ts = ts;
        this.tr = tr;
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

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public ArrayList<TranslateWord> getTr() {
        return tr;
    }

    public void setTr(ArrayList<TranslateWord> tr) {
        this.tr = tr;
    }
}
