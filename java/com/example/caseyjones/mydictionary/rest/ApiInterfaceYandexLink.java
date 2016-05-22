package com.example.caseyjones.mydictionary.rest;


import com.example.caseyjones.mydictionary.models.word.WordJS;

import java.util.Map;

import retrofit2.Call;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;

import retrofit2.http.POST;


/**
 * Created by CaseyJones on 14.05.2016.
 */
public interface ApiInterfaceYandexLink {

    // Translate
    @FormUrlEncoded
    @POST("/api/v1/dicservice.json/lookup")
    Call<WordJS> translate(@FieldMap Map<String, String> map);


}
