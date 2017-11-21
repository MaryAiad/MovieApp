package com.example.mkany.movieapp.adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mkany.movieapp.DetailActivity;
import com.example.mkany.movieapp.MainActivity;
import com.example.mkany.movieapp.R;
import com.example.mkany.movieapp.model.Movie;
//import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mery on 11/7/2017.
 */

public class MovieAaptor extends RecyclerView.Adapter<MovieAaptor.MovieViewHolder> {

    private List<Movie> movies;
    private int rowLayout;
    private Context context;

    public MovieAaptor(List<Movie> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        LinearLayout moviesLayout;
        TextView movieTitle;
        TextView rating;
        ImageView movieImage;

        public MovieViewHolder(View v) {
            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            movieImage = (ImageView) v.findViewById(R.id.movie_image);
            movieTitle = (TextView) v.findViewById(R.id.title);
            rating = (TextView) v.findViewById(R.id.rating);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION)
                    {
                        Movie movie = movies.get(pos);
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("original_title", movies.get(pos).getOriginalTitle());
                        intent.putExtra("poster_path", movies.get(pos).getPosterPath());
                        intent.putExtra("overview", movies.get(pos).getOverview());
                        intent.putExtra("vote_average", Double.toString(movies.get(pos).getVoteAverage()));
                        intent.putExtra("release_date", movies.get(pos).getReleaseDate());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        Toast.makeText(v.getContext(), "You Clicked "+movie.getOriginalTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public MovieAaptor.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {

        Glide.with(context).load(movies.get(position).getPosterPath())
                .placeholder(R.drawable.ic_loadingcurved)
                .error(android.R.drawable.sym_def_app_icon)
                .into(holder.movieImage);
        holder.movieTitle.setText(movies.get(position).getTitle());
        holder.rating.setText(Double.toString(movies.get(position).getVoteAverage()));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
