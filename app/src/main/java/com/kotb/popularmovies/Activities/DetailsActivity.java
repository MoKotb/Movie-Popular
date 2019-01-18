package com.kotb.popularmovies.Activities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kotb.popularmovies.Adapters.VideosAdapter;
import com.kotb.popularmovies.Data.MovieContract;
import com.kotb.popularmovies.Model.Movie;
import com.kotb.popularmovies.Model.Review;
import com.kotb.popularmovies.Model.Video;
import com.kotb.popularmovies.Network.FetchReviewsTask;
import com.kotb.popularmovies.Network.FetchVideosTask;
import com.kotb.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity implements VideosAdapter.onClickItem {

    private ImageView backgroundImage, movieImage;
    private TextView movieTitle, movieDate, movieRate, overview, Textreviews;
    private Button mark;
    private Movie movie;
    private VideosAdapter videosAdapter;
    private RecyclerView recyclerView;
    private String add, remove, videosList, saveReviews;
    private ArrayList<Video> videoArrayList;
    private ArrayList<Review> reviewArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mark = (Button) findViewById(R.id.mark);
        Textreviews = (TextView) findViewById(R.id.reviews);
        videosList = getResources().getString(R.string.videosList);

        add = getResources().getString(R.string.Favorite);
        remove = getResources().getString(R.string.removeFromFavourite);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        recyclerView = (RecyclerView) findViewById(R.id.videosRecyclerView);
        //To Change Style Of RecyclerView
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setFocusable(false);
        videosAdapter = new VideosAdapter(this, this);
        recyclerView.setAdapter(videosAdapter);


        if (savedInstanceState == null) {
            videoArrayList = new ArrayList<>();
            reviewArrayList = new ArrayList<>();
            if (extras != null) {
                if (extras.containsKey(getResources().getString(R.string.Movie))) {
                    movie = extras.getParcelable(getResources().getString(R.string.Movie));
                }
            }
            loadTrailers(movie);
            loadReviews(Integer.toString(movie.getMovieID()));
        } else {
            videoArrayList = savedInstanceState.getParcelableArrayList(videosList);
            reviewArrayList = savedInstanceState.getParcelableArrayList("Momo");
            movie = savedInstanceState.getParcelable("Momo2");
            videosAdapter.setVideosList(videoArrayList);
            StringBuffer buffer = new StringBuffer("");
            if (reviewArrayList.size() != 0) {
                for (int i = 0; i < reviewArrayList.size(); i++) {
                    buffer.append(reviewArrayList.get(i).getReviewAuthor() + " : \n" + reviewArrayList.get(i).getReviewContent());
                }
                Textreviews.setText(buffer);
            } else {
                Textreviews.setText("No Reviews");
            }
        }

        setData(movie);

        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mark.getText().equals(add)) {
                    addFavouriteMovie(movie);
                    mark.setText(remove);
                } else {
                    removeMovie(Integer.toString(movie.getMovieID()));
                }
            }
        });

        getSupportActionBar().setTitle(movie.getMovieTitle());

        if (getFavouriteMovie(Integer.toString(movie.getMovieID()))) {
            mark.setText(remove);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(this, movie.getMovieTitle(), Toast.LENGTH_SHORT).show();
        outState.putParcelableArrayList(videosList, videoArrayList);
        outState.putParcelableArrayList("Momo", reviewArrayList);
        outState.putParcelable("Momo2", movie);
    }

    private void loadTrailers(Movie movie) {
        @SuppressLint("StaticFieldLeak") FetchVideosTask fetchVideosTask = new FetchVideosTask() {
            @Override
            public void onPostExecute(ArrayList<Video> videos) {
                videoArrayList = videos;
                videosAdapter.setVideosList(videos);
            }
        };
        fetchVideosTask.execute(getResources().getString(R.string.videos), Integer.toString(movie.getMovieID()));
    }

    private void loadReviews(String movieID) {
        @SuppressLint("StaticFieldLeak") FetchReviewsTask fetchReviewsTask = new FetchReviewsTask() {
            @Override
            protected void onPostExecute(ArrayList<Review> reviews) {
                reviewArrayList = reviews;
                StringBuffer buffer = new StringBuffer("");
                if (reviewArrayList.size() != 0) {
                    for (int i = 0; i < reviewArrayList.size(); i++) {
                        buffer.append(reviewArrayList.get(i).getReviewAuthor() + " : \n" + reviewArrayList.get(i).getReviewContent());
                    }
                    Textreviews.setText(buffer);
                } else {
                    Textreviews.setText("No Reviews");
                }
            }
        };
        fetchReviewsTask.execute(getResources().getString(R.string.reviews), movieID);
    }

    private void setData(Movie movie) {
        backgroundImage = (ImageView) findViewById(R.id.backgroundImage);
        movieImage = (ImageView) findViewById(R.id.movieImage);
        movieTitle = (TextView) findViewById(R.id.movieTitle);
        movieDate = (TextView) findViewById(R.id.movieDate);
        movieRate = (TextView) findViewById(R.id.movieRate);
        overview = (TextView) findViewById(R.id.overview);
        String imageSize = "w342/";
        //Image URL
        String background = "http://image.tmdb.org/t/p/" + imageSize + movie.getMovieBackground();
        String poster = "http://image.tmdb.org/t/p/" + imageSize + movie.getMoviePoster();

        Picasso.with(this).load(background).placeholder(R.drawable.placeholder).into(backgroundImage);
        Picasso.with(this).load(poster).placeholder(R.drawable.placeholder).into(movieImage);

        String date = getResources().getString(R.string.date) + movie.getMovieReleaseDate();
        String rate = getResources().getString(R.string.rate) + movie.getMovieAverage().toString() + getResources().getString(R.string.ratingNumber);
        movieTitle.setText(movie.getMovieTitle());
        movieDate.setText(date);
        movieRate.setText(rate);
        overview.setText(movie.getMovieOverview());
    }

    private void addFavouriteMovie(Movie movie) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.MovieEntry.COLUMN_ID, movie.getMovieID());
        contentValues.put(MovieContract.MovieEntry.COLUMN_NAME, movie.getMovieTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getMoviePoster());
        contentValues.put(MovieContract.MovieEntry.COLUMN_BACKGROUND, movie.getMovieBackground());
        contentValues.put(MovieContract.MovieEntry.COLUMN_AVERAGE, movie.getMovieAverage());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getMovieOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getMovieReleaseDate());
        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
        if (uri != null) {
            Toast.makeText(this, getResources().getString(R.string.AddedSuccessfully), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(Video video) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video.getVideoKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + video.getVideoKey()));
        if (appIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(appIntent);
        } else {
            startActivity(webIntent);
        }
    }

    private Boolean getFavouriteMovie(String id) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(id).build();
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            int MovieID = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID));
            if (MovieID > 0) {
                return true;
            }
        }
        return false;
    }

    private void removeMovie(String id) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(id).build();
        int numberOfRows = getContentResolver().delete(uri, null, null);
        if (numberOfRows > 0) {
            Toast.makeText(this, getResources().getString(R.string.DeletedSuccessfully), Toast.LENGTH_LONG).show();
            mark.setText(add);
        }
    }
}
