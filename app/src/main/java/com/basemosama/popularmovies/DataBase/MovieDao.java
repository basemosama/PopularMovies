package com.basemosama.popularmovies.DataBase;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDao {


    @Query("SELECT * FROM movies ORDER BY databaseId DESC")
   LiveData<List<Movie>> LoadMovies();

    @Insert
    void insertMovie (Movie movie);

    @Update (onConflict = OnConflictStrategy.REPLACE)
    void updateMovie (Movie movie);

    @Delete
    void deleteMovie (Movie movie);


    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE id = :id)")
    LiveData<Boolean> isInFavourite(int id);

    @Query("DELETE FROM movies WHERE id = :id")
    void deleteMovieById (int id);

}
