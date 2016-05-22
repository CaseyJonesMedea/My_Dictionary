package com.example.caseyjones.mydictionary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caseyjones.mydictionary.R;
import com.example.caseyjones.mydictionary.models.user.User;
import com.example.caseyjones.mydictionary.rest.ApiInterfaceKinveyLink;
import com.example.caseyjones.mydictionary.rest.ApiKinveyConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CaseyJones on 13.05.2016.
 */
public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText loginReg;
    private EditText emailReg;
    private EditText passwordReg;
    private EditText repeatPasswordReg;
    private Button create;

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAILADDRESS_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        create.setOnClickListener(this);
        emailAddressValidator();
    }

    private void initViews() {
        loginReg = (EditText) findViewById(R.id.login_registration);
        emailReg = (EditText) findViewById(R.id.email_registration);
        passwordReg = (EditText) findViewById(R.id.password_registration);
        repeatPasswordReg = (EditText) findViewById(R.id.repeat_password_registration);
        create = (Button) findViewById(R.id.create);
    }

    @Override
    public void onClick(View v) {
        if (!repeatPasswordReg.getText().toString().equals(passwordReg.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_LONG).show();
        } else if (emailReg.getText().toString().isEmpty() || !validate(emailReg.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Check your email!", Toast.LENGTH_LONG).show();
        } else {
            Gson gson = new GsonBuilder().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(ApiKinveyConstant.BASE_URL)
                    .build();
            ApiInterfaceKinveyLink apiInterfaceLink = retrofit.create(ApiInterfaceKinveyLink.class);
            Call<User> call = apiInterfaceLink.signUp(
                    new User(loginReg.getText().toString(), passwordReg.getText().toString(), emailReg.getText().toString()));
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();
                    if (user != null) {
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "This username is already taken!", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "No network. Check your network connection!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void emailAddressValidator() {
        pattern = Pattern.compile(EMAILADDRESS_PATTERN);
    }

    public boolean validate(final String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
