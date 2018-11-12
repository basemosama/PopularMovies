package com.basemosama.popularmovies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.basemosama.popularmovies.DataBase.Movie;
import com.basemosama.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder>{

    private ArrayList<Movie> movieArrayList;
    private MovieClickListener movieClickListener;
    public MoviesAdapter(ArrayList<Movie> arrayList,MovieClickListener mClickListener){
        this.movieArrayList=arrayList;
        this.movieClickListener=mClickListener;
    }

    public interface MovieClickListener{

        void onItemClickListener(int position);
    }

    public void updateAdapter(List<Movie> movies){
        movieArrayList.clear();
        movieArrayList.addAll(movies);
        notifyDataSetChanged();
    }
public ArrayList<Movie> getMovies(){
        return movieArrayList;
}


    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context=parent.getContext();

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.movie_item,parent,false);


        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{
        ImageView moviesimage;
        public MovieHolder(View itemView) {
            super(itemView);

            moviesimage=(ImageView)itemView.findViewById(R.id.movieimage);
            itemView.setOnClickListener(this);
        }

        public void bind(int position){
            String url=movieArrayList.get(position).getImagePath();
            Picasso.get()
                    .load(url)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(moviesimage);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            movieClickListener.onItemClickListener(clickedPosition);
        }
    }
}
