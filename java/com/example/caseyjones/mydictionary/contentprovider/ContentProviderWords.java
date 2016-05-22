package com.example.caseyjones.mydictionary.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.caseyjones.mydictionary.database.DataBaseSQLHelper;

/**
 * Created by CaseyJones on 21.05.2016.
 */
public class ContentProviderWords extends ContentProvider {

    // // Uri
    // authority
    static final String AUTHORITY = "com.example.caseyjones.mydictionary.contentprovider.wordstranslate";

    // path
    static final String WORDS_PATH = "words";

    // Общий Uri
    public static final Uri CONTACT_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + WORDS_PATH);

    // Типы данных
    // набор строк
    static final String CONTACT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + WORDS_PATH;

    // одна строка
    static final String CONTACT_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + WORDS_PATH;

    //// UriMatcher
    // общий Uri
    static final int URI_WORDS = 1;

    // Uri с указанным ID
    static final int URI_WORDS_ID = 2;

    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, WORDS_PATH, URI_WORDS);
        uriMatcher.addURI(AUTHORITY, WORDS_PATH + "/#", URI_WORDS_ID);
    }

    private DataBaseSQLHelper dbHelper;
    private SQLiteDatabase db;


    @Override
    public boolean onCreate() {
        dbHelper = new DataBaseSQLHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (uriMatcher.match(uri) == URI_WORDS) {
            db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query(DataBaseSQLHelper.DATABASE_TABLE_WORDS, projection, selection,
                    selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(),
                    CONTACT_CONTENT_URI);
            return cursor;
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_WORDS:
                return CONTACT_CONTENT_TYPE;
            case URI_WORDS_ID:
                return CONTACT_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public synchronized Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != URI_WORDS)
            throw new IllegalArgumentException("Wrong URI: " + uri);
        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(DataBaseSQLHelper.DATABASE_TABLE_WORDS, null, values);
        Uri resultUri = ContentUris.withAppendedId(CONTACT_CONTENT_URI, rowID);
        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public synchronized int bulkInsert(Uri uri, ContentValues[] values) {
        if (uriMatcher.match(uri) != URI_WORDS)
            throw new IllegalArgumentException("Wrong URI: " + uri);
        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues value : values) {
                db.insert(DataBaseSQLHelper.DATABASE_TABLE_WORDS, null, value);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) != URI_WORDS)
            throw new IllegalArgumentException("Wrong URI: " + uri);
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(DataBaseSQLHelper.DATABASE_TABLE_WORDS, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) != URI_WORDS)
            throw new IllegalArgumentException("Wrong URI: " + uri);
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(DataBaseSQLHelper.DATABASE_TABLE_WORDS, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }
}
