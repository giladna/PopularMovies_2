package com.udacity.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoMetadata implements Parcelable {

    @SerializedName("key")
    @Expose
    public String videoUrl;
    @SerializedName("name")
    @Expose
    public String videoName;

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideoName() {
        return videoName;
    }

    private VideoMetadata(Parcel in) {
        videoUrl = in.readString();
        videoName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(videoUrl);
        parcel.writeString(videoName);
    }

    public static final Parcelable.Creator<VideoMetadata> CREATOR = new Parcelable.Creator<VideoMetadata>() {
        public VideoMetadata createFromParcel(Parcel in) {
            return new VideoMetadata(in);
        }

        public VideoMetadata[] newArray(int size) {
            return new VideoMetadata[size];
        }
    };
}