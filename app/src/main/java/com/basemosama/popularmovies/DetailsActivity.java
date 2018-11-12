package com.basemosama.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.basemosama.popularmovies.Adapters.ReviewAdapter;
import com.basemosama.popularmovies.Adapters.TrailerAdapter;
import com.basemosama.popularmovies.Contants.Constant;
import com.basemosama.popularmovies.DataBase.AppExcuters;
import com.basemosama.popularmovies.DataBase.CheckMovieViewModel;
import com.basemosama.popularmovies.DataBase.CheckMovieViewModelFactory;
import com.basemosama.popularmovies.DataBase.Movie;
import com.basemosama.popularmovies.DataBase.MovieDatabase;
import com.basemosama.popularmovies.Objects.Review;
import com.basemosama.popularmovies.Objects.Trailer;
import com.basemosama.popularmovies.Utilities.JsonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailsActivity extends AppCompatActivity implements TrailerAdapter.TrailerClickListener, LoaderManager.LoaderCallbacks<String> {

    TextView titleTv;
    TextView ratingTv;
    TextView releaseDateTv;
    TextView plotTv;
    ImageView imageView;
    TextView noReviews;
    Boolean isFavourited =true;
    MovieDatabase movieDatabase;
    Movie currentMovie;
    AppExcuters appExcuters;
    MenuItem menuItem;
    LoaderManager loaderManager;
    RecyclerView trailersRecyclerView;
    RecyclerView reviewsRecyclerView;
    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;
    ArrayList<Trailer> trailers=new ArrayList<>();
    ArrayList<Review> reviews=new ArrayList<>();


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isRotated",true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        appExcuters=AppExcuters.getExcuter();

       currentMovie= getIntent().getParcelableExtra(Constant.MOVIE_INTENT_NAME_EXTRA);

       setUpUI(currentMovie);


       Log.i("myMovie", String.valueOf(currentMovie.getDatabaseId()));
        movieDatabase=MovieDatabase.getInstance(getApplicationContext());
         trailersRecyclerView = (RecyclerView) findViewById(R.id.trailers_rv);
         reviewsRecyclerView=(RecyclerView) findViewById(R.id.reviews_rv);


        String videoJsonUrl = Constant.TMDB_BASE_URL+currentMovie.getId()+Constant.TRAILER_STRING;
        String reviewJsonUrl = Constant.TMDB_BASE_URL+currentMovie.getId()+Constant.REVIEWS_STRING;



        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        trailersRecyclerView.setLayoutManager(linearLayoutManager);
        trailersRecyclerView.setHasFixedSize(true);
        trailerAdapter=new TrailerAdapter(trailers,this);
        trailersRecyclerView.setAdapter(trailerAdapter);
        LinearLayoutManager reviewlinearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        reviewsRecyclerView.setLayoutManager(reviewlinearLayoutManager);
        reviewsRecyclerView.setHasFixedSize(true);
        reviewAdapter=new ReviewAdapter(reviews);
        reviewsRecyclerView.setAdapter(reviewAdapter);

        if(savedInstanceState==null){
            setUpLoader(videoJsonUrl,Constant.TRAILER_URL_EXTRA);
            setUpLoader(reviewJsonUrl,Constant.REVIEW_URL_EXTRA);

        }else {
            getSupportLoaderManager().initLoader(Constant.TRAILER_LOADER, null, this);
            getSupportLoaderManager().initLoader(Constant.REVIEW_LOADER, null, this);

        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        menu.findItem(R.id.popular).setVisible(false);
        menu.findItem(R.id.topRated).setVisible(false);
        menuItem=menu.findItem(R.id.favourite);
        chekIfInFavourites();

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.favourite){
            addOrRemoveMovie();
        }

        return super.onOptionsItemSelected(item);

    }


    private void addOrRemoveMovie(){


        appExcuters.getDiskIo().execute(new Runnable() {
            @Override
            public void run() {

                if(isFavourited){

                    movieDatabase.movieDao().deleteMovieById(currentMovie.getId());


                }else{
                    movieDatabase.movieDao().insertMovie(currentMovie);

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isFavourited){
                            Toast.makeText(DetailsActivity.this,currentMovie.getTitle()+getString(R.string.removed_from_favourites),Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(DetailsActivity.this,currentMovie.getTitle()+getString(R.string.added_to_favourites),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        });



    }




    private void chekIfInFavourites(){
        CheckMovieViewModelFactory checkMovieViewModelFactory=new CheckMovieViewModelFactory(movieDatabase,currentMovie.getId());
        final CheckMovieViewModel checkMovieViewModel=ViewModelProviders.of(this,checkMovieViewModelFactory).get(CheckMovieViewModel.class);
        checkMovieViewModel.IsInFavourite().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isInFavourite) {
              //  checkMovieViewModel.IsInFavourite().removeObserver(this);
              isFavourited=isInFavourite;
                if(isInFavourite){
                    menuItem.setIcon(R.drawable.favorite2);

                }else{
                    menuItem.setIcon(R.drawable.favorite);

                }
            }
        });
            }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle bundle) {


        return MainActivity.getAsyncTaskLoader(this,bundle,Constant.TRAILER_AND_REVIEW_URL_EXTRA);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        int id=loader.getId();
        if(id==Constant.TRAILER_LOADER) {
            if (trailers.size() == 0) {
                trailers.addAll(JsonUtils.getTrailers(data));
                trailerAdapter.notifyDataSetChanged();
            }

        }
        else if(id==Constant.REVIEW_LOADER){
            if (reviews.size() == 0) {
                reviews.addAll(JsonUtils.getReviews(data));
                reviewAdapter.notifyDataSetChanged();
            }

            if(reviews.size()>0){
                noReviews.setVisibility(View.GONE);
            }else {
                noReviews.setVisibility(View.VISIBLE);
            }        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }



    public void setUpLoader (String url,String key){


            Bundle bundle=new Bundle();
            bundle.putString(Constant.TRAILER_AND_REVIEW_URL_EXTRA,url);
            int loaderId = 22;
            if(key.equals(Constant.TRAILER_URL_EXTRA)){
                loaderId=Constant.TRAILER_LOADER;
            }else if(key.equals(Constant.REVIEW_URL_EXTRA)){
                loaderId=Constant.REVIEW_LOADER;
            }

            if(MainActivity.checkNetworkConnection(this)){


                loaderManager=getSupportLoaderManager();
                Loader<String> myLoader = loaderManager.getLoader(loaderId);
                if (myLoader==null){
                    loaderManager.initLoader(loaderId,bundle,this);
                }else {
                    loaderManager.restartLoader(loaderId,bundle,this);
                }
            }else {
                Toast.makeText(this, getString(R.string.network_error_message2), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClickListener(int position) {

        String videoId=trailers.get(position).getKey();
        Intent appIntent=new Intent(Intent.ACTION_VIEW,Uri.parse(Constant.YOUTUBE_APP_BASE_URI + videoId));
        Intent webIntent=new Intent(Intent.ACTION_VIEW,Uri.parse(Constant.YOUTUBE_VIDEO_BASE_URL + videoId));
        try {
            startActivity(appIntent);
        }catch (ActivityNotFoundException e){
            startActivity(webIntent);
        }

    }

    public void setUpUI(Movie currentMovie){
        titleTv=(TextView)findViewById(R.id.title1);
        ratingTv=(TextView)findViewById(R.id.rating1);
        releaseDateTv=(TextView)findViewById(R.id.releasedate1);
        plotTv=(TextView)findViewById(R.id.plot1);
        imageView=(ImageView) findViewById(R.id.detailimage);
        noReviews=(TextView)findViewById(R.id.no_reviews);

        String ratingText=currentMovie.getRating() +" /10";

        setTitle(currentMovie.getTitle());
        titleTv.setText(currentMovie.getTitle());
        ratingTv.setText(ratingText);
        releaseDateTv.setText(currentMovie.getReleaseDate());
        plotTv.setText(currentMovie.getPlot());
        Picasso.get()
                .load(currentMovie.getImagePath())
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);

    }
}
