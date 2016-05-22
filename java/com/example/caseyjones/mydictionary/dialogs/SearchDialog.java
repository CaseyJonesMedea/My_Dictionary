package com.example.caseyjones.mydictionary.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.caseyjones.mydictionary.R;

import com.example.caseyjones.mydictionary.activities.OnFragmentInteractionListener;
import com.example.caseyjones.mydictionary.models.word.WordJS;
import com.example.caseyjones.mydictionary.rest.ApiInterfaceYandexLink;

import com.example.caseyjones.mydictionary.rest.ApiYandexConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CaseyJones on 14.05.2016.
 */
public class SearchDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private View form = null;

    private OnFragmentInteractionListener onFragmentInteractionListener;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        onFragmentInteractionListener = (OnFragmentInteractionListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        form = getActivity().getLayoutInflater().inflate(R.layout.dialog_search, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return (builder.setTitle("Search").setView(form)
                .setPositiveButton(android.R.string.ok, this)
                .create());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        EditText editWord = (EditText) form.findViewById(R.id.editWord);
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiYandexConstant.BASE_URL_YANDEX)
                .build();
        ApiInterfaceYandexLink apiInterfaceYandexLink = retrofit.create(ApiInterfaceYandexLink.class);
        Map<String, String> mapJson = new HashMap();

        mapJson.put("key", ApiYandexConstant.KEY);
        mapJson.put("text", editWord.getText().toString());
        mapJson.put("lang", ApiYandexConstant.LANG);


        Call<WordJS> call = apiInterfaceYandexLink.translate(mapJson);
        call.enqueue(new Callback<WordJS>() {
            @Override
            public void onResponse(Call<WordJS> call, Response<WordJS> response) {
                WordJS wordJS = response.body();
                if (wordJS.getDef().size() == 0) {
                    Toast.makeText(context, "No found word!", Toast.LENGTH_LONG).show();
                } else {
                    onFragmentInteractionListener.choseWord(wordJS);
                    onFragmentInteractionListener.saveWord(wordJS);
                }
            }

            @Override
            public void onFailure(Call<WordJS> call, Throwable t) {
                Toast.makeText(context, "No network. Check your network connection!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onDismiss(DialogInterface unused) {
        super.onDismiss(unused);
    }
}
