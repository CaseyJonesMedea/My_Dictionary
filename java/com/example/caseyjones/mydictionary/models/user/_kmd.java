package com.example.caseyjones.mydictionary.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by CaseyJones on 11.05.2016.
 */
public class _kmd implements Serializable {

    @SerializedName("lmt")
    private String lmt;

    @SerializedName("ect")
    private String ect;

    @SerializedName("authtoken")
    private String authtoken;

    public _kmd(String lmt, String ect, String authtoken) {
        this.lmt = lmt;
        this.ect = ect;
        this.authtoken = authtoken;
    }

    public String getLmt() {
        return lmt;
    }

    public void setLmt(String lmt) {
        this.lmt = lmt;
    }

    public String getEct() {
        return ect;
    }

    public void setEct(String ect) {
        this.ect = ect;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
}
