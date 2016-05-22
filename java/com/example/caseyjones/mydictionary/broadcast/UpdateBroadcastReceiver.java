package com.example.caseyjones.mydictionary.broadcast;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.example.caseyjones.mydictionary.database.DataBaseSQLHelper;
import com.example.caseyjones.mydictionary.models.user.User;
import com.example.caseyjones.mydictionary.models.word.PosWord;
import com.example.caseyjones.mydictionary.models.word.WordJS;
import com.example.caseyjones.mydictionary.rest.ApiInterfaceKinveyLink;
import com.example.caseyjones.mydictionary.rest.ApiKinveyConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CaseyJones on 21.05.2016.
 */
public class UpdateBroadcastReceiver extends BroadcastReceiver {

    private Gson gson;
    private Retrofit retrofit;
    private ApiInterfaceKinveyLink apiInterfaceLink;

    private final static String APP_PREFERENCES = "App Preference";
    private final static String REMEMBER_USER = "Remember User";
    private final static String NAMECOLLECTION = "collection";

    private Context context;
    private Call<User> call;


    final Uri CONTACT_URI = Uri
            .parse("content://com.example.caseyjones.mydictionary.contentprovider.wordstranslate/words");

    private static final int HEADERX_KINVEY_ADD_WORD = 3;
    private static final int HEADERX_KINVEY_API_LOG_OUT = 1;

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        gson = new GsonBuilder().create();
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiKinveyConstant.BASE_URL)
                .build();
        apiInterfaceLink = retrofit.create(ApiInterfaceKinveyLink.class);
        SharedPreferences sPref = context.getSharedPreferences(APP_PREFERENCES, context.MODE_PRIVATE);
        Gson gsonSave = new Gson();
        String json = sPref.getString(REMEMBER_USER, "");
        User userSave = gsonSave.fromJson(json, User.class);
        if (userSave != null) {
            call = apiInterfaceLink.login(userSave);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();
                    addWordsInCollection(onUpdateKinveyBase(), user);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }


    public ArrayList<WordJS> onUpdateKinveyBase() {
        ArrayList<WordJS> words = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(CONTACT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int id = cursor.getColumnIndex(DataBaseSQLHelper.KEY_ROWID);
            int idKinvey = cursor.getColumnIndex(DataBaseSQLHelper.KEY_KINVEY_ID);
            int word = cursor.getColumnIndex(DataBaseSQLHelper.KEY_WORD);
            do {
                int idWord = cursor.getInt(id);
                String idKinveyWord = cursor.getString(idKinvey);
                String defWord = cursor.getString(word);
                ArrayList<PosWord> def = stringInWord(defWord);
                WordJS wordJS = new WordJS();
                wordJS.setIdKinvey(idKinveyWord);
                wordJS.setDef(def);
                wordJS.setId(idWord);
                words.add(wordJS);
            } while (cursor.moveToNext());
        }
        return words;
    }


    private ArrayList<PosWord> stringInWord(String posWord) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        ArrayList<PosWord> pos = gson.fromJson(posWord, new TypeToken<List<PosWord>>() {
        }.getType());
        return pos;
    }


    public void addWordsInCollection(ArrayList<WordJS> words, User user) {
       for (int i = 0; i < words.size(); i++) {
          if (words.get(i).getIdKinvey() == null) {
                Call<WordJS> call = apiInterfaceLink.addWord("Kinvey " + user.getKmd().getAuthtoken(), HEADERX_KINVEY_ADD_WORD, words.get(i), NAMECOLLECTION + user.getUsername());
                call.enqueue(new Callback<WordJS>() {
                    @Override
                    public void onResponse(Call<WordJS> call, Response<WordJS> response) {
                        onUpdateDataBase(response.body());
                    }

                    @Override
                    public void onFailure(Call<WordJS> call, Throwable t) {

                    }
                });
            }
        }
//        logout(user);
    }

    private void onUpdateDataBase(WordJS word) {
            ContentValues cv = new ContentValues();
            int id = word.getId();
            cv.put(DataBaseSQLHelper.KEY_KINVEY_ID, word.getIdKinvey());
            cv.put(DataBaseSQLHelper.KEY_WORD, jsonInString(word.getDef()));
            context.getContentResolver().update(CONTACT_URI, cv,"_id = ?",new String[] {String.valueOf(id)});
        }


    private void logout(User user) {
        Call<User> call = apiInterfaceLink.logOut("Kinvey " + user.getKmd().getAuthtoken(), HEADERX_KINVEY_API_LOG_OUT);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "No network. Check your network connection!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String jsonInString(ArrayList<PosWord> poswords) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String wordjs = gson.toJson(poswords);
        return wordjs;
    }
}
