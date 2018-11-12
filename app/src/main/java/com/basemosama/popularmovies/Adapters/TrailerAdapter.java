package com.basemosama.popularmovies.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basemosama.popularmovies.Objects.Trailer;
import com.basemosama.popularmovies.R;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.viewHOlder> {
    private ArrayList<Trailer> trailers;
    private TrailerClickListener trailerClickListener;
    public TrailerAdapter(ArrayList<Trailer> trailers,TrailerClickListener trailerClickListener) {
        this.trailers = trailers;

        this.trailerClickListener=trailerClickListener;
    }

    public interface TrailerClickListener{
        void onItemClickListener(int position);
    }


    @NonNull
    @Override
    public viewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
      View view= layoutInflater.inflate(R.layout.trailer_item,parent,false);

        return new viewHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHOlder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }



    class viewHOlder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView trailerName;
        public viewHOlder(View itemView) {
            super(itemView);
            trailerName=(TextView) itemView.findViewById(R.id.trailer_name);
            itemView.setOnClickListener(this);

        }

        public  void bind(int position){
            trailerName.setText(trailers.get(position).getName());

        }

        @Override
        public void onClick(View view) {

            int position=getAdapterPosition();
            trailerClickListener.onItemClickListener(position);

        }
    }
}
