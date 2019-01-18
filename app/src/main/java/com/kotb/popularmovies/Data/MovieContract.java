package com.kotb.popularmovies.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class MovieContract {

    private MovieContract() {
    }

    public static final String AUTHORITY = "com.kotb.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH = "Movie";

    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();

        public static final String TABLE_NAME = "Movie";

        public static final String COLUMN_ID = "Movie_ID";

        public static final String COLUMN_NAME = "Title";

        public static final String COLUMN_POSTER = "Poster";

        public static final String COLUMN_BACKGROUND = "Background";

        public static final String COLUMN_AVERAGE = "Average";

        public static final String COLUMN_OVERVIEW = "Overview";

        public static final String COLUMN_RELEASE_DATE = "Release_Date";

    }

}
