//add your own feature ec use a db instead of 
//just making an array of pokemon objects
//also makes clearing everything easier
//and watchlist doesnt clear on app restart only when the clear button is pressed
package com.example.csc2990_pokeapi;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class PokemonDBProvider extends ContentProvider {

    public static final String DB_NAME = "PokemonData";
    public static final String TABLE_NAME = "PokemonStats";

    public static final String COLUMN_ONE = "Name";
    public static final String COLUMN_TWO = "Number";
    public static final String COLUMN_THREE = "Height";
    public static final String COLUMN_FOUR = "Weight";
    public static final String COLUMN_FIVE = "XP";


    public static final String AUTHORITY = "com.example.csc2990_pokeapi.db";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    private static final String SQL_CREATE_MAIN =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "_ID INTEGER PRIMARY KEY, " +
                    COLUMN_ONE + " TEXT," +
                    COLUMN_TWO + " TEXT," +
                    COLUMN_THREE + " TEXT," +
                    COLUMN_FOUR + " TEXT," +
                    COLUMN_FIVE + " TEXT" +
                    ")";

    protected static final class MainDatabaseHelper extends SQLiteOpenHelper {

        MainDatabaseHelper(Context context) {
            super(context, DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_MAIN);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    private MainDatabaseHelper mOpenHelper;

    public PokemonDBProvider() { }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MainDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String first = values.getAsString(COLUMN_ONE).trim();
        String second = values.getAsString(COLUMN_TWO).trim();
        String third = values.getAsString(COLUMN_THREE).trim();
        String fourth = values.getAsString(COLUMN_FOUR).trim();
        String fifth = values.getAsString(COLUMN_FIVE).trim();

        if (first.equals("") || second.equals("") ||
                third.equals("") || fourth.equals("") || fifth.equals("")) {
            return null;
        }

        long id = mOpenHelper.getWritableDatabase()
                .insert(TABLE_NAME, null, values);

        return Uri.withAppendedPath(CONTENT_URI, "" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return mOpenHelper.getWritableDatabase()
                .delete(TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return mOpenHelper.getReadableDatabase()
                .query(TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        return mOpenHelper.getWritableDatabase()
                .update(TABLE_NAME, values, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
