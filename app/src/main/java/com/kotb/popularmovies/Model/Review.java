package com.kotb.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Review implements Parcelable {

    private String ReviewID;
    private String ReviewAuthor;
    private String ReviewContent;


    protected Review(Parcel in) {
        ReviewID = in.readString();
        ReviewAuthor = in.readString();
        ReviewContent = in.readString();
    }

    public Review() {
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ReviewID);
        parcel.writeString(ReviewAuthor);
        parcel.writeString(ReviewContent);
    }

    public String getReviewID() {
        return ReviewID;
    }

    public void setReviewID(String reviewID) {
        ReviewID = reviewID;
    }

    public String getReviewAuthor() {
        return ReviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        ReviewAuthor = reviewAuthor;
    }

    public String getReviewContent() {
        return ReviewContent;
    }

    public void setReviewContent(String reviewContent) {
        ReviewContent = reviewContent;
    }

    public ArrayList<Review> ReviewsParsing(String Json) {
        ArrayList<Review> list = new ArrayList<>();
        Review review;
        try {
            JSONObject root = new JSONObject(Json);
            JSONArray results = root.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                review = new Review();
                JSONObject object = results.getJSONObject(i);
                review.setReviewID(object.getString("id"));
                review.setReviewAuthor(object.getString("author"));
                review.setReviewContent(object.getString("content"));
                list.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
