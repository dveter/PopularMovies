package popularmovies.buddyappz.com.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by dejan on 10/11/15.
 */

@ContentProvider(authority = MoviesContentProvider.AUTHORITY, database = MoviesDatabase.class)
public final class MoviesContentProvider {

    public static final String AUTHORITY = "com.buddyappz.popularmovies.MoviesProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String FAVOURITES = "favourites";
    }

    @TableEndpoint(table = MoviesDatabase.FAVOURITES)
    public static class Movies {

        @ContentUri(
                path = Path.FAVOURITES,
                type = "vnd.android.cursor.dir/list",
                defaultSort = MovieDbColumns.TITLE + " ASC")
        public static final Uri FAVOURITES = Uri.parse("content://" + AUTHORITY + "/favourites");
    }

}
