package popularmovies.buddyappz.com.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by dejan on 10/11/15.
 */

public interface MovieDbColumns {

    @DataType(INTEGER) @PrimaryKey
    @AutoIncrement @NotNull
    String MOVIE_ID = "_id";

    @DataType(INTEGER) @NotNull
    String MOVIE_GLOBAL_ID = "globalId";

    @DataType(TEXT) @NotNull
    String TITLE = "title";

    @DataType(TEXT)
    String POSTER_URL = "posterUrl";

    @DataType(INTEGER)
    String RATING = "rating";

    @DataType(TEXT)
    String BIG_IMAGE_URL = "bigImageUrl";

    @DataType(TEXT)
    String DATE = "date";

    @DataType(TEXT)
    String OVERVIEW = "overview";

}