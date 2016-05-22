package com.example.caseyjones.mydictionary.rest;

import com.example.caseyjones.mydictionary.models.user.User;
import com.example.caseyjones.mydictionary.models.word.WordJS;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by CaseyJones on 10.05.2016.
 */
public interface ApiInterfaceKinveyLink {


//  Sign Up
    @POST("/user/kid_WkU96kIvzZ/")
    @Headers("Authorization: Basic a2lkX1drVTk2a0l2elo6YmNkNzM0NTk5ZGI0NGYxMDg3NTJkNGVjY2FhODBmZDA=")
    Call <User> signUp(@Body User user);

//  Login
    @POST("/user/kid_WkU96kIvzZ/login")
    @Headers("Authorization: Basic a2lkX1drVTk2a0l2elo6YmNkNzM0NTk5ZGI0NGYxMDg3NTJkNGVjY2FhODBmZDA=")
    Call <User> login(@Body User user);

//  Delete
    @DELETE("/user/kid_WkU96kIvzZ/{id}")
    Call<User> deleteItem(@Path("id") String itemId, @Header("Authorization") String authorization,@Header("X-Kinvey-API-Version") int num);

//  Log Out
    @POST ("/user/kid_WkU96kIvzZ/_logout")
    Call<User> logOut(@Header("Authorization") String authorization,@Header("X-Kinvey-API-Version") int num);

//  Email Verification
    @POST("/rpc/kid_WkU96kIvzZ/{username}/user-email-verification-initiate")
    @Headers("Authorization: Basic a2lkX1drVTk2a0l2elo6YmNkNzM0NTk5ZGI0NGYxMDg3NTJkNGVjY2FhODBmZDA=")
    Call<Object> emailVertification(@Path("username")String username);

//  Password Reset
    @POST("/rpc/kid_WkU96kIvzZ/{username}/user-password-reset-initiate")
    @Headers("Authorization: Basic a2lkX1drVTk2a0l2elo6YmNkNzM0NTk5ZGI0NGYxMDg3NTJkNGVjY2FhODBmZDA=")
    Call<User>passwordReset(@Path("username")String username);

//  Add word
    @Headers("Content-Type: application/json")
    @POST("/appdata/kid_WkU96kIvzZ/{collectionName}")
    Call<WordJS> addWord(@Header("Authorization") String authorization, @Header("X-Kinvey-API-Version") int num, @Body WordJS word, @Path("collectionName")String collectionName);

//  Get Collection
    @Headers("Content-Type: application/json")
    @GET("/appdata/kid_WkU96kIvzZ/{collectionName}")
    Call<ArrayList<WordJS>> getCollection(@Header("Authorization") String authorization, @Header("X-Kinvey-API-Version") int num, @Path("collectionName")String collectionName);




}
