package com.example.caseyjones.mydictionary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CaseyJones on 17.05.2016.
 */
public class DataBaseSQLHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "WordContract";
    public static final String DATABASE_TABLE_WORDS = "words";
    private static final int DATABASE_VERSION = 1;

    public static final String KEY_ROWID = "_id";
    public static final String KEY_KINVEY_ID = "idkinvey";
    public static final String KEY_WORD = "word";

    private static final String DATABASE_CREATE_TABLE_STUDENTS =
            "create table " + DATABASE_TABLE_WORDS + " ("
                    + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_KINVEY_ID + " string , "
                    + KEY_WORD + " string );";


    public DataBaseSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_TABLE_STUDENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_WORDS);
        onCreate(db);
    }
}
