package com.udacity.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DiscoverMoviesResponse<T> {

    @SerializedName("results")
    @Expose
    private List<T> results;

    public List<T> getResults() {
        return results;
    }
}
