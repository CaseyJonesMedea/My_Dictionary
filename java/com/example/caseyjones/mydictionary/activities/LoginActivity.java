package com.example.caseyjones.mydictionary.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caseyjones.mydictionary.R;
import com.example.caseyjones.mydictionary.models.user.User;
import com.example.caseyjones.mydictionary.rest.ApiInterfaceKinveyLink;
import com.example.caseyjones.mydictionary.rest.ApiKinveyConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CaseyJones on 10.05.2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private Button btnReg;
    private EditText login;
    private EditText password;


    private SharedPreferences sPref;
    private SharedPreferences.Editor ed;

    private Gson gson;
    private Retrofit retrofit;

    private User userSave;


    private ApiInterfaceKinveyLink apiInterfaceLink;

    private final static String REMEMBER_USER = "Remember User";
    private final static String APP_PREFERENCES = "App Preference";

    private boolean logIn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent loadintent = getIntent();
        logIn = loadintent.getBooleanExtra("logout", false);
        if (logIn) {
            clear();
        }
        sPref = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sPref.getString(REMEMBER_USER, "");
        userSave = gson.fromJson(json, User.class);
        if (userSave != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_login);
            initViews();
            Intent intent = getIntent();
            User user = (User) intent.getSerializableExtra("user");
            if (user != null) {
                login.setText(user.getUsername());
                password.setText(user.getPassword());
            }
            gson = new GsonBuilder().create();
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(ApiKinveyConstant.BASE_URL)
                    .build();
            apiInterfaceLink = retrofit.create(ApiInterfaceKinveyLink.class);
            btnLogin.setOnClickListener(this);
            btnReg.setOnClickListener(this);
        }
    }

    private void initViews() {
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReg = (Button) findViewById(R.id.registration);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                fnclogin();
                break;
            case R.id.registration:
                fncregistration();
                break;
        }
    }


    private void fncregistration() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    private void fnclogin() {
        String loginSave = login.getText().toString();
        final String passwordSave = password.getText().toString();
        User userSave = new User(loginSave, passwordSave);
        Call<User> call = apiInterfaceLink.login(
                userSave);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    User user = response.body();
                    user.setPassword(passwordSave);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    saveNaP(user);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect login or password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No network. Check your network connection!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveNaP(User user) {
        sPref = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
        ed = sPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        ed.putString(REMEMBER_USER, json);
        ed.commit();
    }


    private void clear() {
        sPref = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
        ed = sPref.edit();
        ed.clear();
        ed.commit();
    }
}
