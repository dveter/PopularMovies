package popularmovies.buddyappz.com.popularmovies;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by dejan on 22/07/15.
 */
public final class ServerUtilities {

    private static String TAG = "ServerUtilities";

    public static String getPopularMovies(String sortBy, int pageNumber)
            throws IOException {

        Uri builtUri = Uri.parse(CommonUtilities.MOVIE_DB_POPULAR_BASE_URL).buildUpon()
                .appendQueryParameter(CommonUtilities.API_KEY_PARAM, CommonUtilities.MOVIE_DB_API_KEY)
                .appendQueryParameter(CommonUtilities.PAGE_NUMBER_PARAM, String.valueOf(pageNumber))
                .build();


        // If sorting provided, append sort parameter

        if (sortBy != null) {
            builtUri = builtUri.buildUpon().appendQueryParameter(CommonUtilities.SORT_BY_PARAM, sortBy)
            .build();
        }


        URL url = new URL(builtUri.toString());

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        StringBuffer response = null;

        try {

            // handle the response
            int status = connection.getResponseCode();

            if (status != 200) {
                throw new IOException("GET failed with error code " + status);
            } else {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                String inputLine;
                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }

        } finally {
            connection.disconnect();
        }
        return response.toString();
    }


    /**
     *
     * Will use that later...
     */
    public static String getMovieGenres()
            throws IOException {

        Uri builtUri = Uri.parse(CommonUtilities.MOVIE_DB_GENRES_LIST).buildUpon()
                .appendQueryParameter(CommonUtilities.API_KEY_PARAM, CommonUtilities.MOVIE_DB_API_KEY)
                .build();

        URL url = new URL(builtUri.toString());

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        StringBuffer response = null;

        try {

            // handle the response
            int status = connection.getResponseCode();

            if (status != 200) {
                throw new IOException("GET failed with error code " + status);
            } else {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                String inputLine;
                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }

        } finally {
            connection.disconnect();
        }
        return response.toString();
    }


}
