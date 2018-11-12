package com.basemosama.popularmovies.DataBase;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
   private LiveData<List<Movie>> myMovies;
    public MainViewModel(@NonNull Application application) {
        super(application);
         MovieDatabase movieDatabase=MovieDatabase.getInstance(this.getApplication());
        this.myMovies=movieDatabase.movieDao().LoadMovies();
    }

    public LiveData<List<Movie>> getMyMovies() {
        return myMovies;
    }
}
