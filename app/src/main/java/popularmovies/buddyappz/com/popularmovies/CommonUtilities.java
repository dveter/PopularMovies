package popularmovies.buddyappz.com.popularmovies;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by dejan on 22/07/15.
 */
public final class CommonUtilities {

    // themoviedb.org
    static final String MOVIE_DB_API_KEY = "<YOUR_API_KEY_HERE>";
    static final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/";
    static final String MOVIE_DB_POPULAR_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    static final String MOVIE_DB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    static final String MOVIE_DB_BIG_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780/";

    static final String MOVIE_DB_GENRES_LIST = "http://api.themoviedb.org/3/genre/movie/list";

    static final String API_KEY_PARAM = "api_key";
    static final String SORT_BY_PARAM  = "sort_by";
    static final String PAGE_NUMBER_PARAM = "page";

    static final String SORT_BY_POPULAR_KEY = "popularity.desc";
    static final String SORT_BY_HIGHEST_RATED_KEY = "vote_average.desc";
    static final String SORT_BY_FAVORITES = "favorites";



}
