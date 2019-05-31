package com.udacity.popularmovies.utilities;

import android.database.Observable;

import com.udacity.popularmovies.model.DiscoverMoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.udacity.popularmovies.utilities.NetworkUtils.API_KEY;
import static com.udacity.popularmovies.utilities.NetworkUtils.PARAM_PAGE;

/**
 * Created by nuhkoca on 2/16/18.
 */

public interface MovieAPI {

    @GET("movie/popular")
    Call<DiscoverMoviesResponse> getPopularMovies(@Query(API_KEY) String apiKey, @Query(PARAM_PAGE) int pageId);

    @GET("movie/top_rated")
    Call<DiscoverMoviesResponse> getTopRatedMovies(@Query(API_KEY) String apiKey, @Query(PARAM_PAGE) int pageId);

    @GET("movie/upcoming")
    Call<DiscoverMoviesResponse> getUpcomingMovies(@Query(API_KEY) String apiKey, @Query(PARAM_PAGE) int pageId);

    @GET("movie/now_playing")
    Call<DiscoverMoviesResponse> getNowPlayingMovies(@Query(API_KEY) String apiKey, @Query(PARAM_PAGE) int pageId);

    @GET("movie/{id}/reviews")
    Call<DiscoverMoviesResponse> getReviews(@Path("id") int movieId, @Query(API_KEY) String apiKey);

    @GET("movie/{id}/videos")
    Call<DiscoverMoviesResponse> getTrailers(@Path("id") int movieId, @Query(API_KEY) String apiKey);

    @GET("movie/{id}")
    Call<DiscoverMoviesResponse> getOtherDetails(@Path("id") int movieId, @Query(API_KEY) String apiKey);
}