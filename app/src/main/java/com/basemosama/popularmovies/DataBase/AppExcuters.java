package com.basemosama.popularmovies.DataBase;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExcuters  {
public static final Object LOCK =new Object();
public static AppExcuters sInstance;
private final Executor diskIo;
private final Executor mainThreadExcuter;


    public Executor getDiskIo() {
        return diskIo;
    }

    public Executor getMainThreadExcuter() {
        return mainThreadExcuter;
    }

    public AppExcuters(Executor diskIo, Executor mainThreadExcuter) {
        this.diskIo = diskIo;
        this.mainThreadExcuter=mainThreadExcuter;
    }


    public static AppExcuters getExcuter(){

        if(sInstance==null){
            synchronized (LOCK){
                sInstance=new AppExcuters(
                        Executors.newSingleThreadExecutor(),
                        new MainThreadExcuter());
            }
        }

        return sInstance;
    }


    private static class MainThreadExcuter implements Executor{

        private Handler mainThreadHandler =new Handler(Looper.getMainLooper());
        @Override
        public void execute(@NonNull Runnable command) { mainThreadHandler.post(command);
        }

    }

}
