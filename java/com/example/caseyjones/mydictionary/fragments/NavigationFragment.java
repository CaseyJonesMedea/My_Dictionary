package com.example.caseyjones.mydictionary.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.caseyjones.mydictionary.R;
import com.example.caseyjones.mydictionary.activities.LoginActivity;
import com.example.caseyjones.mydictionary.activities.MainActivity;
import com.example.caseyjones.mydictionary.activities.OnFragmentInteractionListener;
import com.example.caseyjones.mydictionary.dialogs.DeleteAccountDialog;
import com.example.caseyjones.mydictionary.models.user.User;
import com.google.gson.Gson;

/**
 * Created by CaseyJones on 22.05.2016.
 */
public class NavigationFragment extends Fragment implements View.OnClickListener {

    private TextView userName;
    private TextView emailUser;
    private TextView passwordReset;
    private TextView logOut;
    private TextView deleteAccount;
    private TextView exitApp;

    private Context context;

    private final static String APP_PREFERENCES = "App Preference";
    private final static String REMEMBER_USER = "Remember User";

    private User userSave;
    private OnFragmentInteractionListener onFragmentInteractionListener;

    final Uri CONTACT_URI = Uri
            .parse("content://com.example.caseyjones.mydictionary.contentprovider.wordstranslate/words");

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        onFragmentInteractionListener = (OnFragmentInteractionListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sPref = context.getSharedPreferences(APP_PREFERENCES, context.MODE_PRIVATE);
        Gson gsonSave = new Gson();
        String json = sPref.getString(REMEMBER_USER, "");
        userSave = gsonSave.fromJson(json, User.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, null);
        initViews(view);
        passwordReset.setOnClickListener(this);
        logOut.setOnClickListener(this);
        deleteAccount.setOnClickListener(this);
        exitApp.setOnClickListener(this);
        if (userSave != null) {
            userName.setText(userSave.getUsername());
            emailUser.setText(userSave.getEmail());
        }
        return view;
    }

    private void initViews(View view) {
        userName = (TextView) view.findViewById(R.id.user_name);
        emailUser = (TextView) view.findViewById(R.id.email_user);
        passwordReset = (TextView) view.findViewById(R.id.password_reset);
        logOut = (TextView) view.findViewById(R.id.log_out_user);
        deleteAccount = (TextView) view.findViewById(R.id.delete_user);
        exitApp = (TextView) view.findViewById(R.id.exit_app);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.password_reset:
                onFragmentInteractionListener.passwordReset();
                break;
            case R.id.log_out_user:
                onFragmentInteractionListener.logOut();
                break;
            case R.id.delete_user:
                DeleteAccountDialog deleteAccountDialog = new DeleteAccountDialog();
                deleteAccountDialog.show(getFragmentManager(), "Delete Account");
                break;
            case R.id.exit_app:
                onFragmentInteractionListener.exitApp();
                break;
        }
    }

    public void loadUser(User user) {
        userSave = user;
        userName.setText(user.getUsername());
        emailUser.setText(user.getEmail());
    }
}
