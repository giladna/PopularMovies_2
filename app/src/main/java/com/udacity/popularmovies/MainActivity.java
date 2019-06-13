package com.udacity.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.udacity.popularmovies.database.AppDatabase;
import com.udacity.popularmovies.model.DiscoverMoviesResponse;
import com.udacity.popularmovies.model.MovieDetailsMetadata;
import com.udacity.popularmovies.model.MovieMetadata;
import com.udacity.popularmovies.utilities.MovieAPI;
import com.udacity.popularmovies.utilities.NetworkClient;
import com.udacity.popularmovies.utilities.NetworkUtils;
import com.udacity.popularmovies.viewmodel.MainViewModel;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.udacity.popularmovies.utilities.NetworkUtils.FAVORITES;
import static com.udacity.popularmovies.utilities.NetworkUtils.NOW_PLAYING;
import static com.udacity.popularmovies.utilities.NetworkUtils.POPULARITY;
import static com.udacity.popularmovies.utilities.NetworkUtils.VOTE_AVARAGE;

public class MainActivity extends AppCompatActivity implements MovieImageGridAdapter.MovieImageGridOnClickHandler {

    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private int numOfCols;
    private AppDatabase mDatabase;
    private MainViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private MovieImageGridAdapter mMovieImageGridAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numOfCols = 3;//getNumOfCols();
        mDatabase = AppDatabase.getDatabaseInstance(this);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel .class);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mRecyclerView = findViewById(R.id.movies_recyclerview);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numOfCols));
        //StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView.setLayoutManager(sglm);
        mRecyclerView.addItemDecoration(new GridItemDecoration(this));
        mMovieImageGridAdapter = new MovieImageGridAdapter(this, this);
        mRecyclerView.setAdapter(mMovieImageGridAdapter);
        //mViewModel.loadMovies(FAVORITES, 0);
        loadMoviesData(POPULARITY);
    }

    private void loadMoviesData(String filterType) {
        showWMoviesDataView();
        //Using Async
        //new FetchMoviesTask().execute(filterType);

        //Using Retrofit
        fetchMovies(filterType);
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
        fetchMovieDetailsAndStart(movieMetadata);
    }

    private void fetchMovieDetailsAndStart(final MovieMetadata movieMetadata) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();

        MovieAPI movieAPI = retrofit.create(MovieAPI.class);
        Call<MovieDetailsMetadata> call = movieAPI.getMovieDetails(new BigDecimal(movieMetadata.getId()).intValueExact(), BuildConfig.MOVIE_DB_API_KEY);

        if (call == null) {
            return;
        }

        mLoadingIndicator.setVisibility(View.VISIBLE);
        call.enqueue(new retrofit2.Callback<MovieDetailsMetadata>() {
            @Override
            public void onResponse(Call<MovieDetailsMetadata> call,
                                   Response<MovieDetailsMetadata> response) {

                if (response.body() != null) {
                    final MovieDetailsMetadata movieDetailsMetadata = response.body();
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    if (movieDetailsMetadata != null) {
                        Context context = MainActivity.this;
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra(DetailActivity.MOVIE_METADATA, movieMetadata);
                        intent.putExtra(DetailActivity.MOVIE_DETAILS, movieDetailsMetadata);
                        context.startActivity(intent);
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
        }  else if (id == R.id.favorits_menu) {
            item.setChecked(true);

            //Room
            fetchMovies(FAVORITES);
        }
        return super.onOptionsItemSelected(item);
    }

    //Retrofit
    private void fetchMovies(String filterType, Integer... movieId) {

        if (FAVORITES.equals(filterType)) {
           new Thread(new Runnable() {
               @Override
               public void run() {
                   final List<MovieMetadata> movieMetadataList = mDatabase.movieMetadataDao().selectAll();
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           mMovieImageGridAdapter.setMoviesData(movieMetadataList);
                       }
                   });
               }
           }).start();

            return;
        }

        Retrofit retrofit = NetworkClient.getRetrofitClient();

        MovieAPI movieAPI = retrofit.create(MovieAPI.class);
        Call<DiscoverMoviesResponse<MovieMetadata>> call = null;

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
        call.enqueue(new Callback<DiscoverMoviesResponse<MovieMetadata>>() {
            @Override
            public void onResponse(Call<DiscoverMoviesResponse<MovieMetadata>> call,
                                   Response<DiscoverMoviesResponse<MovieMetadata>> response) {

                if (response.body() != null) {
                    List<MovieMetadata> discoverMoviesResponse = response.body().getResults();
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    if (discoverMoviesResponse != null) {
                        showWMoviesDataView();
                        mMovieImageGridAdapter.setMoviesData(discoverMoviesResponse);
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
                JsonParser parser = new JsonParser();
                JsonObject jsonMoviesResponseJsonObject = parser.parse(jsonMoviesResponse).getAsJsonObject();
                JsonArray resultsJsonArray = jsonMoviesResponseJsonObject.get("results").getAsJsonArray();
                Gson gson = new Gson();
                //MovieMetadata[] mcArray = gson.fromJson(resultsJsonArray.getAsString(), MovieMetadata[].class);
                Type listType = new TypeToken<List<MovieMetadata>>(){}.getType();
                List<MovieMetadata> discoverMoviesResponse = new Gson().fromJson(resultsJsonArray, listType);
                //DiscoverMoviesResponse<MovieMetadata> discoverMoviesResponse = gson.fromJson(jsonMoviesResponse, DiscoverMoviesResponse.class);
                return discoverMoviesResponse;
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

    private int getNumOfCols() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        if (getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
            if (width > 1000) {
                return 3;
            } else {
                return 2;
            }
        } else {
            if (width > 1700) {
                return 5;
            } else if (width > 1200) {
                return 4;
            } else {
                return 3;
            }
        }
    }
}
