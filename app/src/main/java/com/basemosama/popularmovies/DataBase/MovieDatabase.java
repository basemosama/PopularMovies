package com.basemosama.popularmovies.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;


@Database(entities = Movie.class ,version = 1,exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    public static final String LOG_TAG = MovieDatabase.class.getSimpleName();
    public static final Object LOCK =new Object();
    public static final String DATABASE_NAME="favouritemovies";
    public static MovieDatabase sInstance;

    public static MovieDatabase getInstance (Context context){
        synchronized (LOCK){
            if(sInstance==null){
                Log.i(LOG_TAG,"CreatingDatabase");
                sInstance=Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, DATABASE_NAME)
                        .build();

            }
        }
        return sInstance;
    }

    public abstract MovieDao movieDao();

}
