package popularmovies.buddyappz.com.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dejan on 22/07/15.
 */
public class Movie implements Parcelable {

    private String title;
    private String posterUrl;
    private String genre;
    private double rating;
    private String bigImageUrl;
    private String overView;
    private String date;

    public Movie (String title, String posterUrl, double rating, String bigImageUrl, String overView, String date) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.rating = rating;
        this.bigImageUrl = bigImageUrl;
        this.overView = overView;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getBigImageUrl() {
        return bigImageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String poster) {
        this.posterUrl = poster;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getGenre() {
        return genre;
    }

    public String getOverView() {
        return overView;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(title);
        out.writeString(posterUrl);
        out.writeString(genre);
        out.writeDouble(rating);
        out.writeString(bigImageUrl);
        out.writeString(overView);
        out.writeString(date);
    }


    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getDate() {
        return date;
    }

    private Movie(Parcel in) {
        title = in.readString();
        posterUrl = in.readString();
        genre = in.readString();

        rating = in.readDouble();
        bigImageUrl = in.readString();
        overView = in.readString();
        date = in.readString();
    }
}
