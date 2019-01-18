package com.kotb.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie implements Parcelable {

    private int movieID;
    private Double movieAverage;
    private String movieTitle;
    private String moviePoster;
    private String movieReleaseDate;
    private String movieOverview;
    private String movieBackground;

    public Movie() {
    }

    protected Movie(Parcel in) {
        movieID = in.readInt();
        if (in.readByte() == 0) {
            movieAverage = null;
        } else {
            movieAverage = in.readDouble();
        }
        movieTitle = in.readString();
        moviePoster = in.readString();
        movieReleaseDate = in.readString();
        movieOverview = in.readString();
        movieBackground = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public Double getMovieAverage() {
        return movieAverage;
    }

    public void setMovieAverage(Double movieAverage) {
        this.movieAverage = movieAverage;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public String getMovieBackground() {
        return movieBackground;
    }

    public void setMovieBackground(String movieBackground) {
        this.movieBackground = movieBackground;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }


    //Movies Parsing To Retrieve List Of Movies
    public ArrayList<Movie> MoviesParsing(String Json) {
        ArrayList<Movie> list = new ArrayList<>();
        Movie movie;
        try {
            JSONObject root = new JSONObject(Json);
            JSONArray results = root.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                movie = new Movie();
                JSONObject movies = results.getJSONObject(i);
                movie.setMovieID(movies.getInt("id"));
                movie.setMovieAverage(movies.getDouble("vote_average"));
                movie.setMovieTitle(movies.getString("title"));
                movie.setMoviePoster(movies.getString("poster_path"));
                movie.setMovieOverview(movies.getString("overview"));
                movie.setMovieReleaseDate(movies.getString("release_date"));
                movie.setMovieBackground(movies.getString("backdrop_path"));
                list.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieID);
        if (movieAverage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(movieAverage);
        }
        dest.writeString(movieTitle);
        dest.writeString(moviePoster);
        dest.writeString(movieReleaseDate);
        dest.writeString(movieOverview);
        dest.writeString(movieBackground);
    }
}
