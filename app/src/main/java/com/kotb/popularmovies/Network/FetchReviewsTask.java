package com.kotb.popularmovies.Network;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.kotb.popularmovies.BuildConfig;
import com.kotb.popularmovies.Model.Review;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public abstract class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<Review>> {

    private final Review review = new Review();

    public FetchReviewsTask() {
    }

    @Override
    protected ArrayList<Review> doInBackground(String... strings) {
        if (strings.length == 0) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String ReviewJsonStr;
        try {
            String REVIEW_URL = strings[0];
            String REVIEW_ID = strings[1];
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(REVIEW_ID)
                    .appendPath(REVIEW_URL)
                    .appendQueryParameter("api_key", BuildConfig.MY_MOVIE_DB_API_KEY);
            String myUrl = builder.build().toString();
            URL url = new URL(myUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            ReviewJsonStr = buffer.toString();
        } catch (IOException e) {
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.d("", e.getMessage());
                }
            }
        }
        return review.ReviewsParsing(ReviewJsonStr);
    }

    @Override
    protected abstract void onPostExecute(ArrayList<Review> reviews);
}
