package com.example.caseyjones.mydictionary.models.word;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CaseyJones on 14.05.2016.
 */
public class WordJS implements Serializable {

    @SerializedName("_id")
    private String idKinvey;

    @SerializedName("def")
    private ArrayList<PosWord>def;

    private int id;

    public WordJS(){

    }

    public String getIdKinvey() {
        return idKinvey;
    }

    public void setIdKinvey(String idKinvey) {
        this.idKinvey = idKinvey;
    }

    public ArrayList<PosWord> getDef() {
        return def;
    }

    public void setDef(ArrayList<PosWord> def) {
        this.def = def;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
