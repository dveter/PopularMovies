package popularmovies.buddyappz.com.popularmovies.beans;

/**
 * Created by dejan on 19/11/15.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import popularmovies.buddyappz.com.popularmovies.beans.Trailer;

//@Generated("org.jsonschema2pojo")
public class VideosResponse {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private List<Trailer> results = new ArrayList<Trailer>();

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The results
     */
    public List<Trailer> getTrailerResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setTrailerResults(List<Trailer> results) {
        this.results = results;
    }

}