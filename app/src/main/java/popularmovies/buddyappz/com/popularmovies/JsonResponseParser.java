package popularmovies.buddyappz.com.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dejan on 22/07/15.
 */
public class JsonResponseParser {

    private String TAG = "JsonResponseParser";

    public List<Movie> getMoviesListFromJson(String jsonMovieString) throws JSONException {

        List<Movie> movies = new ArrayList<Movie>();

        // Full json received from server
        JSONObject responseJson = new JSONObject(jsonMovieString);

        // Json parsed from movies table (for each movie)
        JSONObject movieDataJson = null;


        // Extract movie json parts first
        if (responseJson != null){
            JSONArray moviesJsonArray = responseJson.getJSONArray("results");

            // .. then create movie object from each part

            for (int i = 0; i < moviesJsonArray.length(); i++) {
                movieDataJson = moviesJsonArray.getJSONObject(i);

                movies.add(new Movie(movieDataJson.getString("original_title"),
                        movieDataJson.getString("poster_path"),
                        movieDataJson.getDouble("vote_average"),
                        movieDataJson.getString("backdrop_path"),
                        movieDataJson.getString("overview"),
                        movieDataJson.getString("release_date")));

            }
        }

    return movies;

    }

    /*
     *   Will use later....
     */
    private HashMap<Integer, String> getGenresListFromJson(String jsonString) throws JSONException {

        HashMap<Integer, String> genresMap = new HashMap<>();

        // Full json received from server
        JSONObject responseJson = new JSONObject(jsonString);

        // Json parsed from movies table (for each movie)
        JSONObject genresInfoJson = null;


        // Extract movie json parts first
        if (responseJson != null){
            JSONArray genresJsonArray = responseJson.getJSONArray("genres");

            // .. then create movie object from each part

            for (int i = 0; i < genresJsonArray.length(); i++) {
                genresInfoJson = genresJsonArray.getJSONObject(i);
                genresMap.put(genresInfoJson.getInt("id"), genresInfoJson.getString("name"));
            }
        }

        return genresMap;

    }

}
