package com.basemosama.popularmovies.DataBase;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

public class CheckMovieViewModel extends ViewModel {
    private LiveData<Boolean> isInFavourite;

    public CheckMovieViewModel(MovieDatabase movieDatabase,int id) {
     isInFavourite= movieDatabase.movieDao().isInFavourite(id);
    }

    public LiveData<Boolean> IsInFavourite() {
        return isInFavourite;
    }
}
