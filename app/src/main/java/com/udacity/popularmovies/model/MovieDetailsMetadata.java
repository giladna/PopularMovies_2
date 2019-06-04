package com.udacity.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieDetailsMetadata implements Parcelable {
    @Expose
    @SerializedName("budget")
    private long budget;
    @Expose
    @SerializedName("homepage")
    private String homepage;
    @Expose
    @SerializedName("revenue")
    private long revenue;
    @Expose
    @SerializedName("runtime")
    private int runtime;
    @Expose
    @SerializedName("tagline")
    private String tagline;

    public MovieDetailsMetadata() {
    }

    public long getBudget() {
        return budget;
    }

    public String getHomepage() {
        return homepage;
    }

    public long getRevenue() {
        return revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getTagline() {
        return tagline;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.budget);
        dest.writeString(this.homepage);
        dest.writeLong(this.revenue);
        dest.writeInt(this.runtime);
        dest.writeString(this.tagline);
    }

    protected MovieDetailsMetadata(Parcel in) {
        this.budget = in.readLong();
        this.homepage = in.readString();
        this.revenue = in.readLong();
        this.runtime = in.readInt();
        this.tagline = in.readString();
    }

    public static final Creator<MovieDetailsMetadata> CREATOR = new Creator<MovieDetailsMetadata>() {
        @Override
        public MovieDetailsMetadata createFromParcel(Parcel source) {
            return new MovieDetailsMetadata(source);
        }

        @Override
        public MovieDetailsMetadata[] newArray(int size) {
            return new MovieDetailsMetadata[size];
        }
    };
}