package com.basemosama.popularmovies;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.basemosama.popularmovies.Adapters.MoviesAdapter;
import com.basemosama.popularmovies.Contants.Constant;
import com.basemosama.popularmovies.DataBase.MainViewModel;
import com.basemosama.popularmovies.DataBase.Movie;
import com.basemosama.popularmovies.DataBase.MovieDatabase;
import com.basemosama.popularmovies.Utilities.JsonUtils;
import com.basemosama.popularmovies.Utilities.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieClickListener,LoaderManager.LoaderCallbacks<String> {
    String movieUrl;
    SharedPreferences sharedPreference;
    static ArrayList<Movie> movieArrayList=new ArrayList<>();
     ArrayList<String> jsonArrayList=new ArrayList<>();
    RecyclerView recyclerView;
   static MoviesAdapter moviesAdapter;
     LoaderManager loaderManager;

     MovieDatabase movieDatabase;
     String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         sharedPreference=PreferenceManager.getDefaultSharedPreferences(this);
        recyclerView =(RecyclerView)findViewById(R.id.moviesrv);

        // Changing no. of Coloums from 3 to 5 when rotating the device
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(this,5 ));

        }

        recyclerView.setHasFixedSize(true);
        moviesAdapter=new MoviesAdapter(movieArrayList,this);
        recyclerView.setAdapter(moviesAdapter);

        movieUrl=sharedPreference.getString(Constant.MOVIE_URL_KEY,Constant.POPULAR_MOVIES_URL);
        title=sharedPreference.getString(Constant.TITLE_KEY,getString(R.string.popular));

        movieDatabase=MovieDatabase.getInstance(getApplicationContext());


        if(savedInstanceState==null){
            loadMovies(movieUrl);
        }else {
           getSupportLoaderManager().restartLoader(Constant.MOVIES_LOADER, null, this);
             title=savedInstanceState.getString(Constant.CATEGORY_NAME);
        }
        setTitle(title);





        //adding More movies from the api pages into the RecyclerView while Scrolling Down

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(!recyclerView.canScrollVertically(1)){
                    int pageNumber=(movieArrayList.size()+1)/20+1;
                    try {
                        URL murl=new URL(movieUrl+"&page="+pageNumber);
                        loadMovies(murl.toString());

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



    }

    @Override
    public void onItemClickListener(int position) {

        Intent intent =new Intent(this,DetailsActivity.class);
        intent.putExtra(Constant.MOVIE_INTENT_NAME_EXTRA,  moviesAdapter.getMovies().get(position));

        startActivity(intent);

    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public  Loader<String> onCreateLoader(int id, final Bundle bundle) {


        return getAsyncTaskLoader(this,bundle,Constant.MOVIE_URL_EXTRA) ;

    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        if(!jsonArrayList.contains(data) && !getTitle().equals(getString(R.string.favourite_movies))){

            movieArrayList.addAll(JsonUtils.getMovies(data));
            jsonArrayList.add(data);
            moviesAdapter.notifyDataSetChanged();

        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        menu.findItem(R.id.favourite).setVisible(false);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


            if (item.getItemId() == R.id.popular) {
                movieUrl = Constant.POPULAR_MOVIES_URL;
                title = getString(R.string.popular);
                updateRecyclerView(movieUrl,title);

            } else if (item.getItemId() == R.id.topRated) {
                movieUrl = Constant.TOP_RATED_MOVIES_URL;
                title = getString(R.string.topRated);
                updateRecyclerView(movieUrl,title);

            }else if (item.getItemId() == R.id.favouriteMovies) {

                movieUrl=getString(R.string.favourite) ;
                title = getString(R.string.favourite_movies);
                updateRecyclerView(movieUrl,title);


            }



        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constant.CATEGORY_NAME,getTitle().toString());
    }
    public void setUpViewModel( ){

        final MainViewModel mainViewModel=ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getMyMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if(getTitle().equals(getString(R.string.favourite_movies))){
                    moviesAdapter.updateAdapter(movies);
                }
            }


        });

    }


    public static boolean checkNetworkConnection(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }


    public  void loadMovies (String url){
        if(url.equals(getString(R.string.favourite))){

            setUpViewModel();

        }else{
            Bundle bundle=new Bundle();
            bundle.putString(Constant.MOVIE_URL_EXTRA,url);
            if(checkNetworkConnection(this)){


                loaderManager=getSupportLoaderManager();
                Loader<String> moviesLoader = loaderManager.getLoader(Constant.MOVIES_LOADER);
                if (moviesLoader==null){
                    loaderManager.initLoader(Constant.MOVIES_LOADER,bundle,this);
                }else {
                    loaderManager.restartLoader(Constant.MOVIES_LOADER,bundle,this);
                }
            }else {
                Toast.makeText(this, getString(R.string.network_error_message), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void updateRecyclerView(String url,String currentTitle){

        movieArrayList.clear();
        jsonArrayList.clear();
        setTitle(title);
        loadMovies(url);
        sharedPreference.edit().putString(Constant.MOVIE_URL_KEY, url).apply();
        sharedPreference.edit().putString(Constant.TITLE_KEY, currentTitle).apply();
    }



public static AsyncTaskLoader<String> getAsyncTaskLoader(Context context, final Bundle bundle, final String key) {


return new AsyncTaskLoader<String>(context) {
    String myJson;

    @Nullable
    @Override
    public String loadInBackground() {
        String json;

        final String movieUrlString =bundle.getString(key);
        if(movieUrlString == null || TextUtils.isEmpty(movieUrlString)){
            return null;
        }


        try {
            URL myUrl =new URL(movieUrlString);
            json=NetworkUtils.getResponseFromUrl(myUrl);
            return json;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    @Override
    protected void onStartLoading() {
        if(bundle==null){
            return;}



        if(myJson!=null){
            deliverResult(myJson);
        }else{
            forceLoad();
        }
    }

    @Override
    public void deliverResult( String data) {
        myJson=data;
        super.deliverResult(data);
    }

};


}


}
