package com.udacity.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieMetadata implements Parcelable {


    private static final String POSTER_URL_PREFIX = "https://image.tmdb.org/t/p/w185";
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("vote_count")
    @Expose
    private Long voteCount;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("genre_ids")
    @Expose
    private List<Long> genreIds = null;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("adult")
    @Expose
    private Boolean adult;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.popularity);
        dest.writeValue(this.id);
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
    }

    public MovieMetadata(Double popularity, Long id, Boolean video, Long voteCount, Double voteAverage, String title, String releaseDate, String originalLanguage, String originalTitle, List<Long> genreIds, String backdropPath, Boolean adult, String overview, String posterPath) {
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
    }

    protected MovieMetadata(Parcel in) {
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.id = (Long) in.readValue(Long.class.getClassLoader());
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
    }

    public static final Parcelable.Creator<MovieMetadata> CREATOR = new Parcelable.Creator<MovieMetadata>() {
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