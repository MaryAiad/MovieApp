package com.example.mkany.movieapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

/**
 * Created by Mery on 11/7/2017.
 */
public class DetailActivity extends AppCompatActivity {
    TextView nameOfMovie, plotSynopsis, releaseDate;
    ImageView imageView;
    RatingBar ratingBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView  = (ImageView) findViewById(R.id.image_header);
        nameOfMovie = (TextView) findViewById(R.id.movie_title1);
        plotSynopsis = (TextView) findViewById(R.id.plotsyn);
        releaseDate = (TextView) findViewById(R.id.release_date);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        Intent intentTheStartedThisActivity = getIntent();
        if(intentTheStartedThisActivity.hasExtra("original_title")){
            String thubnail = getIntent().getExtras().getString("poster_path");
            String movieName = getIntent().getExtras().getString("original_title");
            String overview = getIntent().getExtras().getString("overview");
            String rating = getIntent().getExtras().getString("vote_average");
            String dateRelease = getIntent().getExtras().getString("release_date");
            Glide.with(this).load(thubnail)
                    .into(imageView);
            nameOfMovie.setText(movieName);
            plotSynopsis.setText(overview);
            releaseDate.setText(dateRelease);
            ratingBar.setRating(Float.parseFloat(rating)/2);
        }
        else
        {
            Toast.makeText(this, "No API data", Toast.LENGTH_SHORT).show();
        }
        intialCollapsingToolbar();
    }

    private void intialCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scrollRange == -1)
                {
                    scrollRange= appBarLayout.getTotalScrollRange();
                }
                if(scrollRange + verticalOffset == 0)
                {
                    collapsingToolbarLayout.setTitle(getString(R.string.movie_details));
                    isShow = true;
                }
                else if(isShow)
                {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}
