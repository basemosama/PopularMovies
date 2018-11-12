package com.basemosama.popularmovies.DataBase;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity (tableName = "movies")
public class Movie implements Parcelable  {

    @PrimaryKey(autoGenerate = true)
    int databaseId;
    int id;
    String title;
    String imagePath;
    String plot;
    double rating;
    String releaseDate;

    public Movie(int databaseId, int id, String title, String imagePath, String plot, double rating, String releaseDate) {
        this.databaseId = databaseId;
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.plot = plot;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    @Ignore
    public Movie(int id, String title, String imagePath, String plot, double rating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.plot = plot;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    @Ignore
    protected Movie(Parcel in) {
        databaseId = in.readInt();
        id = in.readInt();
        title = in.readString();
        imagePath = in.readString();
        plot = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();
    }
    @Ignore
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(databaseId);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(imagePath);
        dest.writeString(plot);
        dest.writeDouble(rating);
        dest.writeString(releaseDate);
    }


    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


}

