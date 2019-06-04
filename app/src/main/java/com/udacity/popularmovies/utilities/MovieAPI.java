package com.udacity.popularmovies.utilities;

import android.database.Observable;

import com.udacity.popularmovies.model.DiscoverMoviesResponse;
import com.udacity.popularmovies.model.MovieDetailsMetadata;
import com.udacity.popularmovies.model.MovieMetadata;
import com.udacity.popularmovies.model.ReviewMetadata;
import com.udacity.popularmovies.model.VideoMetadata;

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
//https://api.themoviedb.org/3/movie/299537?api_key=8778f67f11a5a1dcb6ee731a56892416
//https://api.themoviedb.org/3/movie/299537/videos?api_key=8778f67f11a5a1dcb6ee731a56892416
// https://api.themoviedb.org/3/movie/420817/videos?api_key=8778f67f11a5a1dcb6ee731a56892416
// https://api.themoviedb.org/3/movie/420817/reviews?api_key=8778f67f11a5a1dcb6ee731a56892416

//https://api.themoviedb.org/3/movie/popular?language=en-US&page=1&api_key=8778f67f11a5a1dcb6ee731a56892416
//https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=1&api_key=8778f67f11a5a1dcb6ee731a56892416
    //https://img.youtube.com/vi/9g5knnlF7Zo/0.jpg
    @GET("movie/popular")
    Call<DiscoverMoviesResponse<MovieMetadata>> getPopularMovies(@Query(API_KEY) String apiKey, @Query(PARAM_PAGE) int pageId);

    @GET("movie/top_rated")
    Call<DiscoverMoviesResponse<MovieMetadata>> getTopRatedMovies(@Query(API_KEY) String apiKey, @Query(PARAM_PAGE) int pageId);

    @GET("movie/upcoming")
    Call<DiscoverMoviesResponse<MovieMetadata>> getUpcomingMovies(@Query(API_KEY) String apiKey, @Query(PARAM_PAGE) int pageId);

    @GET("movie/now_playing")
    Call<DiscoverMoviesResponse<MovieMetadata>> getNowPlayingMovies(@Query(API_KEY) String apiKey, @Query(PARAM_PAGE) int pageId);

    @GET("movie/{id}/reviews")
    Call<DiscoverMoviesResponse<ReviewMetadata>> getReviews(@Path("id") int movieId, @Query(API_KEY) String apiKey);

    @GET("movie/{id}/videos")
    Call<DiscoverMoviesResponse<VideoMetadata>> getTrailers(@Path("id") int movieId, @Query(API_KEY) String apiKey);

    @GET("movie/{id}")
    Call<MovieDetailsMetadata> getMovieDetails(@Path("id") int movieId, @Query(API_KEY) String apiKey);
}