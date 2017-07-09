package com.example.android.popularmoviesstageone;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by bembengcs on 6/29/2017.
 */

public class ImageAdapter extends BaseAdapter{

    private static final String LOG_TAG = ImageAdapter.class.getSimpleName();
    private final Context mContext;
    private final Movie[] mMovies;


    public ImageAdapter(Context context, Movie[] movies){
        mContext = context;
        mMovies = movies;
    }

    @Override
    public int getCount() {
        if (mMovies == null || mMovies.length == 0){
            return -1;
        }
        return mMovies.length;
    }

    @Override
    public Object getItem(int position) {
        if (mMovies == null || mMovies.length == 0) {
            return null;
        }
        return mMovies[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if(convertView == null){
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext)
                .load(mMovies[position].getPosterPath())
                .resize(mContext.getResources().getInteger(R.integer.tmdb_poster_w185_width),
                        mContext.getResources().getInteger(R.integer.tmdb_poster_w185_height))
                .error(R.drawable.error_not_found)
                .placeholder(R.drawable.searching)
                .into(imageView);

        return imageView;
    }

}
