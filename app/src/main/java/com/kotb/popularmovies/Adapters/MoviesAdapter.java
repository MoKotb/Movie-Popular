package com.kotb.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kotb.popularmovies.Model.Movie;
import com.kotb.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private ArrayList<Movie> movieArrayList;
    private final Context context;
    private final onClickItem listener;

    public MoviesAdapter(Context context, onClickItem listener) {
        this.context = context;
        this.listener = listener;
    }


    public interface onClickItem {
        void onClick(Movie movie);
    }

    //Load Item
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    //Set Data In item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Image Size
        String imageSize = "w342/";
        //Image URL
        String movieImage = "http://image.tmdb.org/t/p/" + imageSize + movieArrayList.get(position).getMoviePoster();
        //Picasso Library To Cache Image
        if (movieArrayList.get(position).getMoviePoster().isEmpty()) {
            Picasso.with(context).load(R.drawable.placeholder).into(holder.moviePoster);
        } else {
            Picasso.with(context).load(movieImage).placeholder(R.drawable.placeholder).into(holder.moviePoster);
        }
    }


    //Number Of Items In List
    @Override
    public int getItemCount() {
        if (movieArrayList == null) {
            return 0;
        }
        return movieArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView moviePoster;

        public ViewHolder(View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movieItemImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(movieArrayList.get(getAdapterPosition()));
        }
    }

    public void setMoviesList(ArrayList<Movie> movies) {
        this.movieArrayList = movies;
        notifyDataSetChanged();
    }

}
