package popularmovies.buddyappz.com.popularmovies;

import popularmovies.buddyappz.com.popularmovies.beans.UserReviewResponse;
import popularmovies.buddyappz.com.popularmovies.beans.VideosResponse;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by dejan on 19/11/15.
 */
public interface MovieDbService {

    /**
     * Get the videos (trailers, teasers, clips, etc...) for a specific movie id.
     */

    @GET("movie/{id}/videos?api_key=" + CommonUtilities.MOVIE_DB_API_KEY)
    Call<VideosResponse> loadTrailers(@Path("id") long id);


    /**
     * Get user reviews for a selected movie via the /movie/{id}/reviews endpoint
     */

    @GET("movie/{id}/reviews?api_key=" + CommonUtilities.MOVIE_DB_API_KEY)
    Call<UserReviewResponse> loadMovieReviews(@Path("id") long id);
}
