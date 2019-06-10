package com.udacity.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.model.DiscoverMoviesResponse;
import com.udacity.popularmovies.model.MovieDetailsMetadata;
import com.udacity.popularmovies.model.MovieMetadata;
import com.udacity.popularmovies.model.ReviewMetadata;
import com.udacity.popularmovies.model.VideoMetadata;
import com.udacity.popularmovies.utilities.MovieAPI;
import com.udacity.popularmovies.utilities.NetworkClient;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.udacity.popularmovies.utilities.NetworkUtils.NOW_PLAYING;

public class DetailActivity extends AppCompatActivity {
    public static final String MOVIE_METADATA = "movie_metadata";
    public static final String MOVIE_DETAILS  = "movie_details";

    private ImageView poster_iv;
    private TextView title_tv;
    private TextView overview_tv;
    private TextView release_date_tv;
    private TextView duration_tv;
    private TextView rating_tv;
    private MovieMetadata movieMetadata;
    private MovieDetailsMetadata movieDetailsMetadata;
    private RecyclerView mVideosRecyclerView;
    private RecyclerView mReviewsRecyclerView;
    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;

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
        movieMetadata = (MovieMetadata) intent.getParcelableExtra(MOVIE_METADATA);
        movieDetailsMetadata = (MovieDetailsMetadata) intent.getParcelableExtra(MOVIE_DETAILS);
        if (movieMetadata == null || movieDetailsMetadata == null) {
            closeOnError();
            return;
        }
        populateUI(movieMetadata, movieDetailsMetadata);
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

    private void populateUI(MovieMetadata movieMetadata, MovieDetailsMetadata movieDetailsMetadata) {
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

        duration_tv.setText(String.valueOf(movieDetailsMetadata.getRuntime()));

        if (releaseDate != null) {
            release_date_tv.setText(releaseDate);
        }
        fetchMovieTrailers(movieId);
        fetchMovieReviews(movieId);
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
                    String text = (!TextUtils.isEmpty(movieDetailsMetadata.getHomepage()) ?
                                   movieDetailsMetadata.getHomepage() : "https://www.themoviedb.org/movie/" + movieMetadata.getId());

                    ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(this)
                            .setChooserTitle(title)
                            .setSubject(subject)
                            .setText(text)
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

    private void fetchMovieTrailers(Long movieId) {
        mVideosRecyclerView = findViewById(R.id.videos_recyclerview);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mVideosRecyclerView.setLayoutManager(layoutManager);
        mVideosRecyclerView.setHasFixedSize(true);
        mVideosRecyclerView.setNestedScrollingEnabled(false);

        RecyclerView.ItemDecoration itemDecoration = new HorizontalItemDecoration(this);
        mVideosRecyclerView.addItemDecoration(itemDecoration);

        mVideoAdapter = new VideoAdapter(this);
        mVideosRecyclerView.setAdapter(mVideoAdapter);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        MovieAPI movieAPI = retrofit.create(MovieAPI.class);

        Call<DiscoverMoviesResponse<VideoMetadata>> call = movieAPI.getTrailers(new BigDecimal(movieMetadata.getId()).intValueExact(), BuildConfig.MOVIE_DB_API_KEY);
        if (call == null) {
            return;
        }
        call.enqueue(new retrofit2.Callback<DiscoverMoviesResponse<VideoMetadata>>() {
            @Override
            public void onResponse(Call<DiscoverMoviesResponse<VideoMetadata>> call,
                                   Response<DiscoverMoviesResponse<VideoMetadata>> response) {

                if (response.body() != null) {
                    List<VideoMetadata> discoverVideosResponse =  response.body().getResults();
                    //mLoadingIndicator.setVisibility(View.INVISIBLE);
                    if (discoverVideosResponse != null) {
                        List<VideoMetadata> videoMetadataResults = response.body().getResults();
                        mVideoAdapter.addVideosList(videoMetadataResults);
                        if (videoMetadataResults.size() == 0) {
                            mVideosRecyclerView.setVisibility(View.GONE);
                        }
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

    private void showErrorMessage() {
        Toast.makeText(DetailActivity.this, getString(R.string.error_message), Toast.LENGTH_LONG).show();
    }

    private void fetchMovieReviews(Long movieId) {

        mReviewsRecyclerView = findViewById(R.id.reviews_recyclerview);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mReviewsRecyclerView.setLayoutManager(layoutManager);
        mReviewsRecyclerView.setHasFixedSize(true);
        mReviewsRecyclerView.setNestedScrollingEnabled(false);

        RecyclerView.ItemDecoration itemDecoration = new HorizontalItemDecoration(this);
        mReviewsRecyclerView.addItemDecoration(itemDecoration);

        mReviewAdapter = new ReviewAdapter(this);
        mReviewsRecyclerView.setAdapter(mReviewAdapter);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        MovieAPI movieAPI = retrofit.create(MovieAPI.class);

        Call<DiscoverMoviesResponse<ReviewMetadata>> call = movieAPI.getReviews(new BigDecimal(movieMetadata.getId()).intValueExact(), BuildConfig.MOVIE_DB_API_KEY);
        if (call == null) {
            return;
        }
        call.enqueue(new retrofit2.Callback<DiscoverMoviesResponse<ReviewMetadata>>() {
            @Override
            public void onResponse(Call<DiscoverMoviesResponse<ReviewMetadata>> call,
                                   Response<DiscoverMoviesResponse<ReviewMetadata>> response) {

                if (response.body() != null) {
                    List<ReviewMetadata> reviewMetadataResults =  response.body().getResults();
                    mReviewAdapter.addReviewsList(reviewMetadataResults);
                    if (reviewMetadataResults.size() == 0) {
                        mReviewsRecyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable exception) {
                showErrorMessage();
            }
        });
    }

    public void onReviewClicked(View view, ReviewMetadata reviewMetadata) {
        if (reviewMetadata != null && !TextUtils.isEmpty(reviewMetadata.getUrl())) {

            String url = reviewMetadata.getUrl();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(url));
            if (browserIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(browserIntent);
            }
        }
    }
}
