package com.kotb.popularmovies.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieProvider extends ContentProvider {

    private MovieDatabaseHelper databaseHelper;

    private static final int MOVIES = 100;
    private static final int MOVIES_ID = 101;

    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH, MOVIES);

        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH + "/#", MOVIES_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        databaseHelper = new MovieDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sort) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        int match = URI_MATCHER.match(uri);

        Cursor cursor;

        switch (match) {
            case MOVIES:
                cursor = database.query(MovieContract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sort);
                break;
            case MOVIES_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "Movie_ID=?";
                String[] mArgs = new String[]{id};
                cursor = database.query(MovieContract.MovieEntry.TABLE_NAME, projection, mSelection, mArgs, null, null, sort);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        int match = URI_MATCHER.match(uri);

        Uri matchUri;

        switch (match) {
            case MOVIES:
                long id = sqLiteDatabase.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    matchUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Error In Adding in Db" + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("unknown uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return matchUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        int match = URI_MATCHER.match(uri);

        int deleted;

        switch (match) {
            case MOVIES_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "Movie_ID=?";
                String[] mArgs = new String[]{id};
                deleted = database.delete(MovieContract.MovieEntry.TABLE_NAME, mSelection, mArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }
        if (deleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
