package com.udacity.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DiscoverMoviesResponse {

    @SerializedName("page")
    @Expose
    private Long page;

    @SerializedName("total_results")
    @Expose
    private Long totalResults;

    @SerializedName("total_pages")
    @Expose
    private Long totalPages;

    @SerializedName("results")
    @Expose
    private List<MovieMetadata> results;

    public Long getPage() {
        return page;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public List<MovieMetadata> getResults() {
        return results;
    }
}
