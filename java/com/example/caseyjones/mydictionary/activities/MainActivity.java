package com.example.caseyjones.mydictionary.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.caseyjones.mydictionary.R;
import com.example.caseyjones.mydictionary.database.DataBaseSQLHelper;
import com.example.caseyjones.mydictionary.dialogs.SearchDialog;
import com.example.caseyjones.mydictionary.fragments.DictionaryFragment;
import com.example.caseyjones.mydictionary.fragments.NavigationFragment;
import com.example.caseyjones.mydictionary.fragments.WordFragment;
import com.example.caseyjones.mydictionary.models.user.User;
import com.example.caseyjones.mydictionary.models.word.PosWord;
import com.example.caseyjones.mydictionary.models.word.TranslateWord;
import com.example.caseyjones.mydictionary.models.word.WordJS;
import com.example.caseyjones.mydictionary.rest.ApiInterfaceKinveyLink;
import com.example.caseyjones.mydictionary.rest.ApiKinveyConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {


    private Gson gson;
    private Retrofit retrofit;

    private final String NAMECOLLECTION = "collection";

    private final static String APP_PREFERENCES = "App Preference";
    private final static String REMEMBER_USER = "Remember User";

    final Uri CONTACT_URI = Uri
            .parse("content://com.example.caseyjones.mydictionary.contentprovider.wordstranslate/words");

    private ApiInterfaceKinveyLink apiInterfaceLink;


    private static final int HEADERX_KINVEY_API_DELETE = 1;
    private static final int HEADERX_KINVEY_LOAD_COLLECTION = 3;


    private Call<User> call;

    private User user;

    private ArrayList<WordJS> words = new ArrayList<>();


    private DictionaryFragment dictionaryFragment;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private NavigationFragment navigationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        gson = new GsonBuilder().create();
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiKinveyConstant.BASE_URL)
                .build();
        apiInterfaceLink = retrofit.create(ApiInterfaceKinveyLink.class);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        if (user != null) {
            loadCollection(user);
        } else {
            dictionaryFragment = DictionaryFragment.newInstance(loadCollectionFromBD());
            replaceFragment(dictionaryFragment, false);
        }
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_fragment);
    }


    private String jsonInString(ArrayList<PosWord> poswords) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String wordjs = gson.toJson(poswords);
        return wordjs;
    }

    private ArrayList<PosWord> stringInWord(String posWord) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        ArrayList<PosWord> pos = gson.fromJson(posWord, new TypeToken<List<PosWord>>() {
        }.getType());
        return pos;
    }

    private ArrayList<WordJS> loadCollection(final User user) {
        Call<ArrayList<WordJS>> call = apiInterfaceLink.getCollection("Kinvey " + user.getKmd().getAuthtoken(), HEADERX_KINVEY_LOAD_COLLECTION, NAMECOLLECTION + user.getUsername());
        call.enqueue(new Callback<ArrayList<WordJS>>() {
            @Override
            public void onResponse(Call<ArrayList<WordJS>> call, Response<ArrayList<WordJS>> response) {
                if (response.isSuccessful()) {
                    words = response.body();
                    saveCollection(words);
                    navigationFragment.loadUser(user);
                    dictionaryFragment = DictionaryFragment.newInstance(words);
                    replaceFragment(dictionaryFragment, false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<WordJS>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No network. Check your network connection!", Toast.LENGTH_LONG).show();
            }
        });
        return words;
    }

    private ArrayList<WordJS> loadCollectionFromBD() {
        ArrayList<WordJS> words = new ArrayList<>();
        SharedPreferences sPref = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sPref.getString(REMEMBER_USER, "");
        user = gson.fromJson(json, User.class);
        Cursor cursor = getContentResolver().query(CONTACT_URI, null, null, null, null);
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
                wordJS.setId(idWord);
                wordJS.setIdKinvey(idKinveyWord);
                wordJS.setDef(def);
                words.add(wordJS);
            } while (cursor.moveToNext());
        }
        return words;
    }


    private void saveCollection(ArrayList<WordJS> words) {
        ArrayList<ContentValues> contentV = new ArrayList();
        for (int i = 0; i < words.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(DataBaseSQLHelper.KEY_KINVEY_ID, words.get(i).getIdKinvey());
            cv.put(DataBaseSQLHelper.KEY_WORD, jsonInString(words.get(i).getDef()));
            contentV.add(cv);
        }
        ContentValues[] values = new ContentValues[contentV.size()];
        values = contentV.toArray(values);
        getContentResolver().bulkInsert(CONTACT_URI, values);

    }


    @Override
    public void saveWord(WordJS word) {
        ContentValues cv = new ContentValues();
        cv.put(DataBaseSQLHelper.KEY_WORD, jsonInString(word.getDef()));

        getContentResolver().insert(CONTACT_URI, cv);
        dictionaryFragment.updateCollection(word);
    }

    @Override
    public void exitApp() {
        finish();
    }

    @Override
    public void logOut() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("logout", true);
        startActivity(intent);
        getContentResolver().delete(CONTACT_URI, null, null);
        finish();
    }

    @Override
    public void passwordReset() {
        call = apiInterfaceLink.passwordReset(user.getUsername());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(getBaseContext(), "Reset your password via email", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                clear();
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No network. Check your network connection!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void deleteUser() {
        Call<User> call = apiInterfaceLink.login(
                user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                deleteUserFinal(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No network. Check your network connection!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteUserFinal(final User user) {
        call = apiInterfaceLink.deleteItem(user.getId(), "Kinvey " + user.getKmd().getAuthtoken(), HEADERX_KINVEY_API_DELETE);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(getBaseContext(), "Delete " + user.getUsername(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                clear();
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No network. Check your network connection!", Toast.LENGTH_LONG).show();
            }
        });
    }






    public void replaceFragment(Fragment fragment, boolean isBool) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        if (isBool) {
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void choseWord(WordJS word) {
        WordFragment wordFragment = WordFragment.newInstance(word);
        replaceFragment(wordFragment, true);
    }

    private void clear() {
        SharedPreferences sPref = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.clear();
        ed.commit();
    }


}
