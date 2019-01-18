package com.kotb.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Video implements Parcelable {

    private String VideoID;
    private String VideoKey;

    public Video() {
    }

    protected Video(Parcel in) {
        VideoID = in.readString();
        VideoKey = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public String getVideoID() {
        return VideoID;
    }

    public void setVideoID(String videoID) {
        VideoID = videoID;
    }

    public String getVideoKey() {
        return VideoKey;
    }

    public void setVideoKey(String videoKey) {
        VideoKey = videoKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(VideoID);
        parcel.writeString(VideoKey);
    }

    public ArrayList<Video> VideosParsing(String Json) {
        ArrayList<Video> list = new ArrayList<>();
        Video video;
        try {
            JSONObject root = new JSONObject(Json);
            JSONArray results = root.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                video = new Video();
                JSONObject object = results.getJSONObject(i);
                video.setVideoID(object.getString("id"));
                video.setVideoKey(object.getString("key"));
                list.add(video);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
