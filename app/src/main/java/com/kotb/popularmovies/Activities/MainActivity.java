package com.kotb.popularmovies.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kotb.popularmovies.Adapters.MoviesAdapter;
import com.kotb.popularmovies.Data.MovieContract;
import com.kotb.popularmovies.Model.Movie;
import com.kotb.popularmovies.Network.FetchMoviesTask;
import com.kotb.popularmovies.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.onClickItem {

    private MoviesAdapter moviesAdapter;
    private RecyclerView moviesList;
    private SharedPreferences preferences;
    private ImageView offlineImage;
    private TextView offlineText;
    private ArrayList<Movie> movieArrayList;
    private String top_rated, popular, MoviesList, favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        popular = getResources().getString(R.string.popular);
        top_rated = getResources().getString(R.string.top_rated);
        favorite = getResources().getString(R.string.userFavourite);
        MoviesList = getResources().getString(R.string.MoviesList);

        if (savedInstanceState == null || !savedInstanceState.containsKey(MoviesList)) {
            movieArrayList = new ArrayList<>();
        } else {
            movieArrayList = savedInstanceState.getParcelableArrayList(MoviesList);
        }

        //Number Of Columns For RecyclerView
        int numberOfColumns = 2;
        moviesList = findViewById(R.id.moviesRecyclerView);
        //To Change Style Of RecyclerView
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, numberOfColumns);
        moviesList.setLayoutManager(manager);

        moviesAdapter = new MoviesAdapter(this, this);
        moviesList.setAdapter(moviesAdapter);

        offlineImage = findViewById(R.id.offlineModeImage);
        offlineText = findViewById(R.id.offlineModeText);

        ReadMoviesType();

    }

    //Save In Shared Preferences
    private void SaveMoviesType(String type) {
        if (type == null) {
            type = popular;
        }
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.sharedKey), type).apply();
    }

    private void ReadMoviesType() {
        preferences = getPreferences(MODE_PRIVATE);
        String MoviesType = preferences.getString(getString(R.string.sharedKey), popular);
        if (MoviesType.equals(popular)) {
            getSupportActionBar().setTitle(getResources().getString(R.string.menuMostPopular));
            loadMostPopularMovies(MoviesType);
        } else if (MoviesType.equals(top_rated)) {
            getSupportActionBar().setTitle(getResources().getString(R.string.menuTopRated));
            loadMostPopularMovies(MoviesType);
        } else if (MoviesType.equals(favorite)) {
            movieArrayList = getFavouriteMovies();
            moviesAdapter.setMoviesList(movieArrayList);
            getSupportActionBar().setTitle(getResources().getString(R.string.favouriteMenu));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ReadMoviesType();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MoviesList, movieArrayList);
    }

    //Load Data From API
    @SuppressLint("StaticFieldLeak")
    private void loadMostPopularMovies(String URL) {
        if (movieArrayList.size() == 0) {
            if (isNetworkConnected()) {
                FetchMoviesTask fetchMoviesTask = new FetchMoviesTask() {
                    @Override
                    public void onPostExecute(ArrayList<Movie> movies) {
                        movieArrayList = movies;
                        moviesAdapter.setMoviesList(movies);
                    }
                };
                fetchMoviesTask.execute(URL);
                offlineImage.setVisibility(View.GONE);
                offlineText.setVisibility(View.GONE);
                moviesList.setVisibility(View.VISIBLE);
            } else {
                offlineImage.setVisibility(View.VISIBLE);
                offlineText.setVisibility(View.VISIBLE);
                moviesList.setVisibility(View.GONE);
            }
        } else {
            moviesAdapter.setMoviesList(movieArrayList);
        }
    }

    //Create Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //When Select item In Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mostPopular) {
            //Save Most Popular in preferences
            clearState();
            loadMostPopularMovies(popular);
            SaveMoviesType(popular);
            getSupportActionBar().setTitle(getResources().getString(R.string.menuMostPopular));
        } else if (item.getItemId() == R.id.topRated) {
            //Save Top Rated in preferences
            clearState();
            loadMostPopularMovies(top_rated);
            SaveMoviesType(top_rated);
            getSupportActionBar().setTitle(getResources().getString(R.string.menuTopRated));
        } else if (item.getItemId() == R.id.favourite) {
            clearState();
            SaveMoviesType(favorite);
            movieArrayList = getFavouriteMovies();
            moviesAdapter.setMoviesList(movieArrayList);
            getSupportActionBar().setTitle(getResources().getString(R.string.favouriteMenu));
        }
        return true;
    }

    private void clearState() {
        movieArrayList.clear();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null) != null;
    }

    @Override
    public void onClick(Movie movie) {
        Intent showDetails = new Intent(this, DetailsActivity.class);
        showDetails.putExtra(getResources().getString(R.string.Movie), movie);
        startActivity(showDetails);
    }

    private ArrayList<Movie> getFavouriteMovies() {
        ArrayList<Movie> movies = new ArrayList<>();
        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setMovieID(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID)));
                movie.setMoviePoster(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)));
                movie.setMovieBackground(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKGROUND)));
                movie.setMovieTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME)));
                movie.setMovieOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
                movie.setMovieReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
                movie.setMovieAverage(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_AVERAGE)));
                movies.add(movie);
            } while (cursor.moveToNext());
        }
        return movies;
    }

}
