package com.kotb.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Movies.db";

    public static final int DATABASE_VERSION = 2;

    private static final String CREATE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME
            + " ( " + MovieContract.MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY,"
            + MovieContract.MovieEntry.COLUMN_NAME + " TEXT NOT NULL, "
            + MovieContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, "
            + MovieContract.MovieEntry.COLUMN_BACKGROUND + " TEXT NOT NULL, "
            + MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "
            + MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
            + MovieContract.MovieEntry.COLUMN_AVERAGE + " REAL);";

    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME;

    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
