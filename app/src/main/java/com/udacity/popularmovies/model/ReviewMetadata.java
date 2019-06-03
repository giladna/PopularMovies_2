package com.udacity.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewMetadata implements Parcelable {

    @SerializedName("author")
    @Expose
    public String author;
    @SerializedName("content")
    @Expose
    public String content;

    public ReviewMetadata() {
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    private ReviewMetadata(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
    }

    public static final Parcelable.Creator<ReviewMetadata> CREATOR = new Parcelable.Creator<ReviewMetadata>() {
        public ReviewMetadata createFromParcel(Parcel in) {
            return new ReviewMetadata(in);
        }

        public ReviewMetadata[] newArray(int size) {
            return new ReviewMetadata[size];
        }
    };
}