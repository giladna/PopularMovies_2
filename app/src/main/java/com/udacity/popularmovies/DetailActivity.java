package com.udacity.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.model.MovieDetailsMetadata;
import com.udacity.popularmovies.model.MovieMetadata;
import com.udacity.popularmovies.utilities.MovieAPI;
import com.udacity.popularmovies.utilities.NetworkClient;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailActivity extends AppCompatActivity {
    public static final String MOVIE_DETAILS = "movie_details";

    private ImageView poster_iv;
    private TextView title_tv;
    private TextView overview_tv;
    private TextView release_date_tv;
    private TextView duration_tv;
    private TextView rating_tv;
    private MovieMetadata movieMetadata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        poster_iv = findViewById(R.id.poster_iv);
        title_tv = findViewById(R.id.title_tv);
        overview_tv = findViewById(R.id.overview_tv);
        release_date_tv = findViewById(R.id.release_date_tv);
        duration_tv = findViewById(R.id.duration_tv);
        rating_tv = findViewById(R.id.rating_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }
        MovieMetadata movieMetadata = (MovieMetadata) intent.getParcelableExtra(MOVIE_DETAILS);
        if (movieMetadata == null) {
            closeOnError();
            return;
        }
        populateUI(movieMetadata);
        loadPoster(movieMetadata);
    }

    private void loadPoster(MovieMetadata movieMetadata) {
        Picasso.with(this)
                .load(movieMetadata.getPosterFullPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(poster_iv, new Callback() {
                    @Override
                    public void onSuccess() {
                        // remove spinner
                    }

                    @Override
                    public void onError() {
                        // remove spinner
                    }
                });
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(MovieMetadata movieMetadata) {
        Long movieId = movieMetadata.getId();
        String originalTitle = movieMetadata.getOriginalTitle();
        String plotSynopsis = movieMetadata.getOverview();
        Double userRating = movieMetadata.getVoteAverage();
        String releaseDate = movieMetadata.getReleaseDate();

        if (originalTitle != null) {
            title_tv.setText(originalTitle);
        }

        if (plotSynopsis != null) {
            overview_tv.setText(plotSynopsis);
        }

        if (userRating != null) {
            rating_tv.setText(String.valueOf(userRating) + "/10");
        }

        if (releaseDate != null) {
            release_date_tv.setText(releaseDate);
        }
        if (movieId != null) {
            fetchMovieDetails(movieId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                if (movieMetadata != null) {
                    String title = "Share this movie with friends!";
                    String subject = "You must watch this movie!";
                    ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(this)
                            .setChooserTitle(title)
                            .setSubject(subject)
                            .setText(("https://www.themoviedb.org/movie/" + movieMetadata.getId()))
                            .setType("text/plain");
                    try {
                        intentBuilder.startChooser();
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(this, R.string.sharing_blocked, Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchMovieDetails(Long movieId) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();

        MovieAPI movieAPI = retrofit.create(MovieAPI.class);
        Call<MovieDetailsMetadata> call = movieAPI.getMovieDetails(new BigDecimal(movieId).intValueExact(), BuildConfig.MOVIE_DB_API_KEY);

        if (call == null) {
            return;
        }

        //mLoadingIndicator.setVisibility(View.VISIBLE);
        call.enqueue(new retrofit2.Callback<MovieDetailsMetadata>() {
            @Override
            public void onResponse(Call<MovieDetailsMetadata> call,
                                   Response<MovieDetailsMetadata> response) {

                if (response.body() != null) {
                    final MovieDetailsMetadata movieDetailsMetadata = response.body();
                    //mLoadingIndicator.setVisibility(View.INVISIBLE);
                    if (movieDetailsMetadata != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                duration_tv.setText(String.valueOf(movieDetailsMetadata.getRuntime()));
                            }
                        });
                    } else {
                        //showErrorMessage();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable exception) {
                //showErrorMessage();
            }
        });

    }
}
