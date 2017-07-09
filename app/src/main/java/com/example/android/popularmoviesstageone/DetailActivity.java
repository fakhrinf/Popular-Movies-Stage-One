package com.example.android.popularmoviesstageone;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;

public class DetailActivity extends AppCompatActivity {

    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvOriginalTitle = (TextView) findViewById(R.id.tv_original_title);
        ImageView ivPoster = (ImageView) findViewById(R.id.iv_poster);
        TextView tvOverview = (TextView) findViewById(R.id.tv_overview);
        TextView tvVoteAverage = (TextView) findViewById(R.id.tv_vote_average);
        TextView tvReleaseDate = (TextView) findViewById(R.id.tv_release_date);

        Intent intent = getIntent();

        try {
            Movie movies = intent.getParcelableExtra(getString(R.string.parcel_movie));
//            Movie movies = (Movie) getIntent().getSerializableExtra(getString(R.string.parcel_movie));
            tvOriginalTitle.setText(movies.getOriginalTitle());

            Picasso.with(this)
                    .load(movies.getPosterPath())
                    .resize(getResources().getInteger(R.integer.tmdb_poster_w185_width),
                            getResources().getInteger(R.integer.tmdb_poster_w185_height))
                    .error(R.drawable.error_not_found)
                    .placeholder(R.drawable.searching)
                    .into(ivPoster);

            String overview = movies.getOverview();
            if (overview == null) {
                tvOverview.setTypeface(null, Typeface.ITALIC);
                overview = getResources().getString(R.string.no_summary_found);
            }
            tvOverview.setText(overview);

            tvVoteAverage.setText(movies.getDetailedVoteAverage());

            // First get the release date from the object - to be used if something goes wrong with
            // getting localized release date (catch).
            String releaseDate = movies.getReleaseDate();
            if (releaseDate != null) {
                try {
                    releaseDate = DateTimeHelper.getLocalizedDate(this,
                            releaseDate, movies.getDateFormat());
                } catch (ParseException e) {
                    Log.e(LOG_TAG, "Error with parsing movie release date", e);
                }
            } else {
                tvReleaseDate.setTypeface(null, Typeface.ITALIC);
                releaseDate = getResources().getString(R.string.no_release_date_found);
            }
            tvReleaseDate.setText(releaseDate);
        }catch (Exception e){
            Log.d("ONCREATEERROR", "onCreate: "+ e.getMessage());
            Log.d("ERROR", "onCreate: " + e.getLocalizedMessage());
        }
    }
}

