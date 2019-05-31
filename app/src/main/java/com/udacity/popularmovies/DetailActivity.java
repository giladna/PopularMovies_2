package com.udacity.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.model.MovieMetadata;


public class DetailActivity extends AppCompatActivity {
    public static final String MOVIE_DETAILS = "movie_details";

    ImageView poster_iv;
    TextView title_tv;
    TextView overview_tv;
    TextView release_date_tv;
    TextView rating_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        poster_iv = findViewById(R.id.poster_iv);
        title_tv = findViewById(R.id.title_tv);
        overview_tv = findViewById(R.id.overview_tv);
        release_date_tv = findViewById(R.id.release_date_tv);
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
    }
}
