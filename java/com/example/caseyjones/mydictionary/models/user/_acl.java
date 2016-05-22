package com.example.caseyjones.mydictionary.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by CaseyJones on 11.05.2016.
 */
public class _acl implements Serializable {

    @SerializedName("creator")
    private String creator;

    public _acl(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
