package com.basemosama.popularmovies.Utilities;


import com.basemosama.popularmovies.Contants.Constant;
import com.basemosama.popularmovies.DataBase.Movie;
import com.basemosama.popularmovies.Objects.Review;
import com.basemosama.popularmovies.Objects.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public  class JsonUtils {

    public static ArrayList<Review> getReviews(String json){
        ArrayList<Review> reviews=new ArrayList<>();

        try {
            JSONObject jsonObject=new JSONObject(json);
            JSONArray jsonArray=jsonObject.getJSONArray(Constant.JSON_RESULTS_NAME);
            for (int i=0;i<jsonArray.length();i++) {
                String author = jsonArray.getJSONObject(i).getString(Constant.JSON_REVIEW_AUTHOR);
                String review = jsonArray.getJSONObject(i).getString(Constant.JSON_REVIEW_CONTENT);
                reviews.add(new Review(author,review));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;

    }



public static ArrayList<Trailer> getTrailers(String json){
    ArrayList<Trailer> trailers=new ArrayList<>();

    try {
        JSONObject jsonObject=new JSONObject(json);
        JSONArray jsonArray=jsonObject.getJSONArray(Constant.JSON_RESULTS_NAME);
        for (int i=0;i<jsonArray.length();i++) {
            String name = jsonArray.getJSONObject(i).getString(Constant.JSON_TRAILER_NAME);
            String type = jsonArray.getJSONObject(i).getString(Constant.JSON_TYPE_NAME);
            String key = jsonArray.getJSONObject(i).getString(Constant.JSON_KEY_NAME);
            trailers.add(new Trailer(name,type,key));
        }

        } catch (JSONException e) {
        e.printStackTrace();
    }

    return trailers;

}

    public static ArrayList<Movie> getMovies(String json){
        ArrayList<Movie> movieArrayList=new ArrayList<>();
        ArrayList<String> titleArrayList=new ArrayList<>();
        try {
            JSONObject jsonObject=new JSONObject(json);
            JSONArray moviesJsonArray=jsonObject.getJSONArray(Constant.JSON_RESULTS_NAME);

            for (int i=0;i<moviesJsonArray.length();i++){
                int id=moviesJsonArray.getJSONObject(i).getInt(Constant.JSON_ID_NAME);
                String title=moviesJsonArray.getJSONObject(i).getString(Constant.JSON_TITLE_NAME);
                String imagePath=moviesJsonArray.getJSONObject(i).getString(Constant.JSON_POSTER_PATH_NAME);


                String imageUrl=Constant.IMAGE_BASE_URL+imagePath;

                String plot=moviesJsonArray.getJSONObject(i).getString(Constant.JSON_OVERVIEW_NAME);
                double rating=moviesJsonArray.getJSONObject(i).getDouble(Constant.JSON_RATING_NAME);
                String releaseDate=moviesJsonArray.getJSONObject(i).getString(Constant.JSON_RELEASE_DATE_NAME);


                    if(!titleArrayList.contains(title)) {
                        movieArrayList.add(new Movie(id, title, imageUrl, plot, rating, releaseDate));
                        titleArrayList.add(title);
                    }



            }




        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieArrayList;

    }

}
