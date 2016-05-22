package com.example.caseyjones.mydictionary.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by CaseyJones on 10.05.2016.
 */
public class User implements Serializable {

    @SerializedName("_id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("_kmd")
    private _kmd kmd;

    @SerializedName("_acl")
    private _acl acl;

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    @SerializedName("description")
    private String description;


    public User(String id, String username, _kmd kmd, _acl acl, String password, String email) {
        this.id = id;
        this.username = username;
        this.kmd = kmd;
        this.acl = acl;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public _kmd getKmd() {
        return kmd;
    }

    public void setKmd(_kmd kmd) {
        this.kmd = kmd;
    }

    public _acl getAcl() {
        return acl;
    }

    public void setAcl(_acl acl) {
        this.acl = acl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

