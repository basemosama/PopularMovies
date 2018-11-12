package com.basemosama.popularmovies.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basemosama.popularmovies.Objects.Review;
import com.basemosama.popularmovies.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.viewHOlder> {
    private ArrayList<Review> reviews;
    public ReviewAdapter(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }



    @NonNull
    @Override
    public viewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
      View view= layoutInflater.inflate(R.layout.review_item,parent,false);

        return new viewHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHOlder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }



    class viewHOlder extends RecyclerView.ViewHolder {

        TextView reviewAuthor;
        TextView review;
        public viewHOlder(View itemView) {
            super(itemView);
            reviewAuthor=(TextView) itemView.findViewById(R.id.review_author);
            review=(TextView) itemView.findViewById(R.id.review);


        }

        public  void bind(int position){
            reviewAuthor.setText("Written By : "+reviews.get(position).getAuthor());
            review.setText(reviews.get(position).getReview());

        }


    }
}
