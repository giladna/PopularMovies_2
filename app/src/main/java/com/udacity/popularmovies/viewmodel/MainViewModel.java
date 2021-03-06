package com.udacity.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import com.udacity.popularmovies.BuildConfig;
import com.udacity.popularmovies.database.AppDatabase;
import com.udacity.popularmovies.model.DiscoverMoviesResponse;
import com.udacity.popularmovies.model.MovieMetadata;
import com.udacity.popularmovies.utilities.MovieAPI;
import com.udacity.popularmovies.utilities.NetworkClient;
import com.udacity.popularmovies.utilities.NetworkUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<List<MovieMetadata>> mostPopularMovies;
    private MutableLiveData<List<MovieMetadata>> topRatedMovies;
    private MutableLiveData<List<MovieMetadata>> nowPlayingMovies;
    private LiveData<List<MovieMetadata>> favoriteMovies;
    private MutableLiveData<Integer> currentFilter;
    private AppDatabase database;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getDatabaseInstance(getApplication());
    }

    public LiveData<List<MovieMetadata>> getMostPopularMovies() {
        if (mostPopularMovies == null) {
            mostPopularMovies = new MutableLiveData<>();
            loadMovies(NetworkUtils.POPULARITY, 1);
        }
        return mostPopularMovies;
    }

    public LiveData<List<MovieMetadata>> getTopRatedMovies() {
        if (topRatedMovies == null) {
            topRatedMovies = new MutableLiveData<>();
            loadMovies(NetworkUtils.VOTE_AVARAGE, 1);
        }
        return topRatedMovies;
    }

    public LiveData<List<MovieMetadata>> getNowPlayingMovies() {
        if (nowPlayingMovies == null) {
            nowPlayingMovies = new MutableLiveData<>();
            loadMovies(NetworkUtils.NOW_PLAYING, 1);
        }
        return nowPlayingMovies;
    }

    public LiveData<List<MovieMetadata>> getFavoriteMovies() {
        if (favoriteMovies == null) {
            favoriteMovies = new MutableLiveData<>();
            getFavoritesFromDatabase();
        }
        return favoriteMovies;
    }

    public MutableLiveData<Integer> getCurrentFilter() {
        if (currentFilter == null) {
            currentFilter = new MutableLiveData<>();
            currentFilter.setValue(0);
        }
        return currentFilter;
    }

    public void loadMovies(String filter, int page) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        MovieAPI movieAPI = retrofit.create(MovieAPI.class);
        if (NetworkUtils.POPULARITY.equals(filter)) {
            Call<DiscoverMoviesResponse<MovieMetadata>> call = movieAPI.getPopularMovies(
                    BuildConfig.MOVIE_DB_API_KEY, page);

            call.enqueue(new Callback<DiscoverMoviesResponse<MovieMetadata>>() {
                @Override
                public void onResponse(Call<DiscoverMoviesResponse<MovieMetadata>> call,
                                       Response<DiscoverMoviesResponse<MovieMetadata>> response) {
                    if (response.isSuccessful()) {
                        List<MovieMetadata> result = response.body().getResults();
                        List<MovieMetadata> value = mostPopularMovies.getValue();
                        if (value == null || value.isEmpty()) {
                            mostPopularMovies.setValue(result);
                        } else {
                            value.addAll(result);
                            mostPopularMovies.setValue(value);
                        }
                        getCurrentFilter().setValue(0);
                    }
                }

                @Override
                public void onFailure(Call<DiscoverMoviesResponse<MovieMetadata>> call, Throwable t) {
                    mostPopularMovies = null;
                    getCurrentFilter().setValue(-1);
                }
            });
        } else if (NetworkUtils.VOTE_AVARAGE.equals(filter)) {
            Call<DiscoverMoviesResponse<MovieMetadata>> call = movieAPI.getTopRatedMovies(
                    BuildConfig.MOVIE_DB_API_KEY, page);


            call.enqueue(new Callback<DiscoverMoviesResponse<MovieMetadata>>() {
                @Override
                public void onResponse(Call<DiscoverMoviesResponse<MovieMetadata>> call,
                                       Response<DiscoverMoviesResponse<MovieMetadata>> response) {
                    if (response.isSuccessful()) {
                        List<MovieMetadata> result = response.body().getResults();
                        List<MovieMetadata> value = topRatedMovies.getValue();
                        if (value == null || value.isEmpty()) {
                            topRatedMovies.setValue(result);
                        } else {
                            value.addAll(result);
                            topRatedMovies.setValue(value);
                        }
                        getCurrentFilter().setValue(1);
                    }
                }

                @Override
                public void onFailure(Call<DiscoverMoviesResponse<MovieMetadata>> call, Throwable t) {
                    topRatedMovies = null;
                    getCurrentFilter().setValue(-1);
                }
            });
        } else if (NetworkUtils.NOW_PLAYING.equals(filter)) {
            Call<DiscoverMoviesResponse<MovieMetadata>> call = movieAPI.getNowPlayingMovies(
                    BuildConfig.MOVIE_DB_API_KEY, page);


            call.enqueue(new Callback<DiscoverMoviesResponse<MovieMetadata>>() {
                @Override
                public void onResponse(Call<DiscoverMoviesResponse<MovieMetadata>> call,
                                       Response<DiscoverMoviesResponse<MovieMetadata>> response) {
                    if (response.isSuccessful()) {
                        List<MovieMetadata> result = response.body().getResults();
                        List<MovieMetadata> value = nowPlayingMovies.getValue();
                        if (value == null || value.isEmpty()) {
                            nowPlayingMovies.setValue(result);
                        } else {
                            value.addAll(result);
                            nowPlayingMovies.setValue(value);
                        }
                        getCurrentFilter().setValue(2);
                    }
                }

                @Override
                public void onFailure(Call<DiscoverMoviesResponse<MovieMetadata>> call, Throwable t) {
                    topRatedMovies = null;
                    getCurrentFilter().setValue(-1);
                }
            });
        }
    }

    private void getFavoritesFromDatabase() {
        favoriteMovies = database.movieMetadataDao().getAll();
    }
}
