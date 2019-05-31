package com.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.udacity.popularmovies.model.DiscoverMoviesResponse;
import com.udacity.popularmovies.model.MovieMetadata;
import com.udacity.popularmovies.utilities.MovieAPI;
import com.udacity.popularmovies.utilities.NetworkClient;
import com.udacity.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.udacity.popularmovies.utilities.NetworkUtils.NOW_PLAYING;
import static com.udacity.popularmovies.utilities.NetworkUtils.POPULARITY;
import static com.udacity.popularmovies.utilities.NetworkUtils.VOTE_AVARAGE;

public class MainActivity extends AppCompatActivity implements MovieImageGridAdapter.MovieImageGridOnClickHandler {

    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static final int NUM_OF_COL = 2;
    private RecyclerView mRecyclerView;
    private MovieImageGridAdapter mMovieImageGridAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mRecyclerView = findViewById(R.id.movies_recyclerview);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, NUM_OF_COL));
        //StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView.setLayoutManager(sglm);
        mRecyclerView.addItemDecoration(new GridItemDecoration(10, 2));
        mMovieImageGridAdapter = new MovieImageGridAdapter(this, this);
        mRecyclerView.setAdapter(mMovieImageGridAdapter);

        loadMoviesData(POPULARITY);
    }

    private void loadMoviesData(String filterType) {
        showWMoviesDataView();
        //Using Async
        new FetchMoviesTask().execute(filterType);

        //Using Retrofit
        //fetchMovies(filterType);
    }

    @Override
    public void onClick(MovieMetadata movieMetadata) {
        long id = movieMetadata.getId();
        //Toast.makeText(MainActivity.this, "ID = " + id , Toast.LENGTH_SHORT).show();
        launchDetailActivity(movieMetadata);
    }

    private void showWMoviesDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void launchDetailActivity(MovieMetadata movieMetadata) {
        Context context = MainActivity.this;
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE_DETAILS, movieMetadata);
        context.startActivity(intent);
    }

    // This method creates the menu on the app
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Called when a options menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // We check what menu item was clicked and show a Toast
        if (id == R.id.most_popular) {
            item.setChecked(true);
            //Async
            //loadMoviesData(POPULARITY);

            //Retrofit
            fetchMovies(POPULARITY);
            return true;

            // If exit was clicked close the app
        } else if (id == R.id.top_rated) {
            item.setChecked(true);
            //Async
            //loadMoviesData(VOTE_AVARAGE);

            //Retrofit
            fetchMovies(VOTE_AVARAGE);
            return true;
        } else if (id == R.id.now_playing) {
            item.setChecked(true);

            //Async
            //loadMoviesData(NOW_PLAYING);

            //Retrofit
            fetchMovies(NOW_PLAYING);
        }
        return super.onOptionsItemSelected(item);
    }

    //Retrofit
    private void fetchMovies(String filterType) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();

        MovieAPI movieAPI = retrofit.create(MovieAPI.class);
        Call<DiscoverMoviesResponse> call = null;

        switch (filterType) {
            case NOW_PLAYING:
                call = movieAPI.getNowPlayingMovies(BuildConfig.MOVIE_DB_API_KEY, 1);
                break;
            case VOTE_AVARAGE:
                call = movieAPI.getTopRatedMovies(BuildConfig.MOVIE_DB_API_KEY, 1);
                break;
            case POPULARITY:
            default:
                call = movieAPI.getPopularMovies(BuildConfig.MOVIE_DB_API_KEY, 1);
        }
        if (call == null) {
            return;
        }

        mLoadingIndicator.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<DiscoverMoviesResponse>() {
            @Override
            public void onResponse(Call call, Response response) {

                if (response.body() != null) {
                    DiscoverMoviesResponse discoverMoviesResponse = (DiscoverMoviesResponse) response.body();
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    if (discoverMoviesResponse.getResults() != null) {
                        showWMoviesDataView();
                        mMovieImageGridAdapter.setMoviesData(discoverMoviesResponse.getResults());
                    } else {
                        showErrorMessage();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable exception) {
                showErrorMessage();
            }
        });
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, List<MovieMetadata>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MovieMetadata> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String sortByCondition = params[0];
            URL moviesRequestUrl = NetworkUtils.buildUrl(sortByCondition, 1);

            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
                Gson gson = new Gson();
                DiscoverMoviesResponse discoverMoviesResponse = gson.fromJson(jsonMoviesResponse, DiscoverMoviesResponse.class);
                return discoverMoviesResponse.getResults();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<MovieMetadata> moviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                showWMoviesDataView();
                mMovieImageGridAdapter.setMoviesData(moviesData);
            } else {
                showErrorMessage();
            }
        }
    }
}
