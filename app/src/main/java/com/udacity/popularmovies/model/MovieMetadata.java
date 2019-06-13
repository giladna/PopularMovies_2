package com.udacity.popularmovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movie")
public class MovieMetadata implements Parcelable {
    private static final String POSTER_URL_PREFIX = "https://image.tmdb.org/t/p/w342";//w185";

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    public Long id;
    @SerializedName("popularity")
    @Expose
    public Double popularity;
    @SerializedName("video")
    @Expose
    public Boolean video;
    @SerializedName("vote_count")
    @Expose
    public Long voteCount;
    @SerializedName("vote_average")
    @Expose
    public Double voteAverage;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("release_date")
    @Expose
    public String releaseDate;
    @SerializedName("original_language")
    @Expose
    public String originalLanguage;
    @SerializedName("original_title")
    @Expose
    public String originalTitle;
    @SerializedName("genre_ids")
    @Expose
    public List<Long> genreIds = null;
    @SerializedName("backdrop_path")
    @Expose
    public String backdropPath;
    @SerializedName("adult")
    @Expose
    public Boolean adult;
    @SerializedName("overview")
    @Expose
    public String overview;
    @SerializedName("poster_path")
    @Expose
    public String posterPath;

    @Ignore
    boolean isInFavorites;

    public MovieMetadata() {
    }

    @Ignore
    public MovieMetadata(Long id, Double popularity, Boolean video, Long voteCount, Double voteAverage, String title, String releaseDate, String originalLanguage, String originalTitle, List<Long> genreIds, String backdropPath, Boolean adult, String overview, String posterPath, boolean isInFavorites) {
        this.popularity = popularity;
        this.id = id;
        this.video = video;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.title = title;
        this.releaseDate = releaseDate;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.genreIds = genreIds;
        this.backdropPath = backdropPath;
        this.adult = adult;
        this.overview = overview;
        this.posterPath = posterPath;
        this.isInFavorites = isInFavorites;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Long getId() {
        return id;
    }

    public Boolean getVideo() {
        return video;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getPosterFullPath() {
        if(TextUtils.isEmpty(posterPath)) {
            return null;
        }
        return POSTER_URL_PREFIX + posterPath;
    }

    public String getFallbackPosterFullPath() {
        if(TextUtils.isEmpty(posterPath)) {
            return null;
        }
        return POSTER_URL_PREFIX + getBackdropPath();
    }

    public boolean isInFavorites() {
        return isInFavorites;
    }

    public void setInFavorites(boolean inFavorites) {
        isInFavorites = inFavorites;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.popularity);
        dest.writeValue(this.video);
        dest.writeValue(this.voteCount);
        dest.writeValue(this.voteAverage);
        dest.writeString(this.title);
        dest.writeString(this.releaseDate);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.originalTitle);
        dest.writeList(this.genreIds);
        dest.writeString(this.backdropPath);
        dest.writeValue(this.adult);
        dest.writeString(this.overview);
        dest.writeString(this.posterPath);
        dest.writeByte(this.isInFavorites ? (byte) 1 : (byte) 0);
    }

    protected MovieMetadata(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.video = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.voteCount = (Long) in.readValue(Long.class.getClassLoader());
        this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.genreIds = new ArrayList<Long>();
        in.readList(this.genreIds, Long.class.getClassLoader());
        this.backdropPath = in.readString();
        this.adult = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.isInFavorites = in.readByte() != 0;
    }

    public static final Creator<MovieMetadata> CREATOR = new Creator<MovieMetadata>() {
        @Override
        public MovieMetadata createFromParcel(Parcel source) {
            return new MovieMetadata(source);
        }

        @Override
        public MovieMetadata[] newArray(int size) {
            return new MovieMetadata[size];
        }
    };
}