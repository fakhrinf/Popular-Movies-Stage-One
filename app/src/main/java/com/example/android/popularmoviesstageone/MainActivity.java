package com.example.android.popularmoviesstageone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setOnItemClickListener(moviePosterClickListener);

        if (savedInstanceState == null){
            getMoviesFromTMDB(getSortMethod());
        } else {
            Parcelable[] parcelable = savedInstanceState.getParcelableArray(getString(R.string.parcel_movie));

            if (parcelable != null) {
                int numMovieObjects = parcelable.length;
                Movie[] movies = new Movie[numMovieObjects];
                for (int i = 0; i < numMovieObjects; i++){
                    movies[i] = (Movie) parcelable[i];
                }
                mGridView.setAdapter(new ImageAdapter(this, movies));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        Refresh
        if (id == R.id.action_refresh) {
            getMoviesFromTMDB(getSortMethod());
            return true;
        }

//        Sort by Popular Movies
        if (id == R.string.action_popular_movies) {
            updateSharedPrefs(getString(R.string.path_popular_movie));
            getMoviesFromTMDB(getSortMethod());
            return true;
        }

//        Sort by Top Rated Movies
        if (id == R.string.action_top_rated_movies) {
            updateSharedPrefs(getString(R.string.path_top_rated_movie));
            getMoviesFromTMDB(getSortMethod());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final GridView.OnItemClickListener moviePosterClickListener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Movie movie = (Movie) parent.getItemAtPosition(position);

            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(getResources().getString(R.string.parcel_movie), movie);

            startActivity(intent);
        }
    };

    public boolean networkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getMoviesFromTMDB(String sortMethod){
        if (networkAvailable()) {
            // Key needed to get data from TMDb
            String apiKey = getString(R.string.api_key);

            // Listener for when AsyncTask is ready to update UI
            OnTaskCompleted taskCompleted = new OnTaskCompleted() {
                @Override
                public void onFetchMoviesTaskCompleted(Movie[] movies) {
                    mGridView.setAdapter(new ImageAdapter(getApplicationContext(), movies));
                }
            };

            // Execute task
            FetchMovieAsyncTask movieTask = new FetchMovieAsyncTask(taskCompleted, apiKey);
            movieTask.execute(sortMethod);
        } else {
            Toast.makeText(this, getString(R.string.error_need_internet_conncetion), Toast.LENGTH_LONG).show();
        }
    }

    public String getSortMethod() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(getString(R.string.pref_sort_method_key),
                getString(R.string.path_popular_movie));
    }

    private void updateSharedPrefs(String sortMethod) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_sort_method_key), sortMethod);
        editor.apply();
    }

}
