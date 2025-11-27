package com.example.csc2990_pokeapi;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class PokemonDBProvider extends ContentProvider {

    public static final String DBName = "PokemonData";

    static final class MainDatabaseHelper extends SQLiteOpenHelper {
        MainDatabaseHelper(Context context){
            super(context, DBName, null, 1);
        }
        public void onCreate(SQLiteDatabase db){
            db.execSQL(SQL_CREATE_MAIN);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}
    }

    public static final String TABLE_NAME = "PokemonStats";
    public static final String COLUMN_ONE   = "Name";
    public static final String COLUMN_TWO   = "Number";
    public static final String COLUMN_THREE = "Height";
    public static final String COLUMN_FOUR  = "Weight";
    public static final String COLUMN_FIVE  = "XP";

    public static final String AUTHORITY = "com.example.csc2990_pokeapi.db";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    private MainDatabaseHelper mOpenHelper;

    private static final String SQL_CREATE_MAIN =
            "CREATE TABLE " + TABLE_NAME +
                    " (_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ONE + " TEXT, " +
                    COLUMN_TWO + " TEXT, " +
                    COLUMN_THREE + " TEXT, " +
                    COLUMN_FOUR + " TEXT, " +
                    COLUMN_FIVE + " TEXT)";

    @Override
    public boolean onCreate() {
        mOpenHelper = new MainDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] args, String sortOrder) {

        return mOpenHelper.getReadableDatabase()
                .query(TABLE_NAME, projection, selection, args,
                        null, null, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long id = mOpenHelper.getWritableDatabase()
                .insert(TABLE_NAME, null, values);

        if (id == -1) return null;

        return Uri.withAppendedPath(CONTENT_URI, "" + id);
    }


    @Override public int delete(Uri uri, String s, String[] s2){ return 0; }
    @Override public int update(Uri uri, ContentValues v,String s,String[] s2){ return 0; }
    @Override public String getType(Uri uri){ return null; }
}
