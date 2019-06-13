package com.udacity.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.udacity.popularmovies.model.ReviewMetadata;

public class ReadReviewActivity extends AppCompatActivity {
    public static final String REVIEW_KEY = "REVIEW";
    public static final String TITLE_KEY = "TITLE";

    private TextView reviewAuthor;
    private TextView reviewContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_review);


        Intent intent = getIntent();
        String movieTitle = intent.getStringExtra(TITLE_KEY);
        ReviewMetadata review = intent.getParcelableExtra(REVIEW_KEY);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(movieTitle);
        }
        reviewAuthor = findViewById(R.id.reviewAuthorTv);
        reviewAuthor.setText("A review by " + review.getAuthor());
        reviewContent = findViewById(R.id.reviewContentTv);
        reviewContent.setText(review.getContent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
