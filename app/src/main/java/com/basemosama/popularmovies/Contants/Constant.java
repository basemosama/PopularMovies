package com.basemosama.popularmovies.Contants;

import com.basemosama.popularmovies.BuildConfig;

public class Constant {

    public static final String TMDB_BASE_URL="http://api.themoviedb.org/3/movie/";
    public static final String YOUTUBE_VIDEO_BASE_URL="https://www.youtube.com/watch?v=";
    public static final String YOUTUBE_APP_BASE_URI="vnd.youtube:";

    public static final String API_KEY="?api_key=";

    public static final String POPULAR_MOVIES_URL=TMDB_BASE_URL+"popular"+API_KEY+BuildConfig.MyAPIKEY;
    public static final String TOP_RATED_MOVIES_URL=TMDB_BASE_URL+"top_rated"+API_KEY+BuildConfig.MyAPIKEY;
    public static final String IMAGE_BASE_URL="http://image.tmdb.org/t/p/w500/";
    public static final String TRAILER_STRING ="/videos"+API_KEY+BuildConfig.MyAPIKEY;;
    public static final String REVIEWS_STRING ="/reviews"+API_KEY+BuildConfig.MyAPIKEY;;

    public static final String MOVIE_INTENT_NAME_EXTRA="Movie_Url";
    public static final int MOVIES_LOADER=11;
    public static final String MOVIE_URL_EXTRA="myMovie";
    public static final int TRAILER_LOADER=22;
    public static final String TRAILER_URL_EXTRA="mytrailer";
    public static final int REVIEW_LOADER=33;
    public static final String REVIEW_URL_EXTRA="myreview";
    public static final String TRAILER_AND_REVIEW_URL_EXTRA="trailer_review";


    // Json Constents

    public static final String JSON_RESULTS_NAME="results";
    public static final String JSON_TITLE_NAME="title";
    public static final String JSON_POSTER_PATH_NAME="poster_path";
    public static final String JSON_OVERVIEW_NAME="overview";
    public static final String JSON_RATING_NAME="vote_average";
    public static final String JSON_RELEASE_DATE_NAME="release_date";
    public static final String JSON_ID_NAME="id";

    public static final String CATEGORY_NAME ="category_name";
    public static final String DATA_LOADED="data_loaded";
    public static final String MOVIE_URL_KEY ="movie_url_key";
    public static final String TITLE_KEY ="title_key";

    public static final String JSON_TRAILER_NAME="name";
    public static final String JSON_TYPE_NAME="type";
    public static final String JSON_KEY_NAME="key";

    public static final String JSON_REVIEW_AUTHOR="author";
    public static final String JSON_REVIEW_CONTENT="content";



}
