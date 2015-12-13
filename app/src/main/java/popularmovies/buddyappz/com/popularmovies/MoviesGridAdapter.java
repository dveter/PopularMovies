package popularmovies.buddyappz.com.popularmovies;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by dejan on 22/07/15.
 */
public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.ViewHolder> {

    private Context mContext;
    CustomItemClickListener listener;

    List<Movie> movies;

        public MoviesGridAdapter(Context context, List<Movie> movies, CustomItemClickListener listener) {
            super();
            this.movies = movies;
            this.mContext = context;
            this.listener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_item, viewGroup, false);
            final ViewHolder viewHolder = new ViewHolder(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, viewHolder.getPosition());
                }
            });

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            Movie movie = movies.get(i);
            viewHolder.txtTitle.setText(movie.getTitle());
            Glide.with(mContext).load(CommonUtilities.MOVIE_DB_IMAGE_BASE_URL + movie.getPosterUrl()).centerCrop().into(viewHolder.imgPoster);
            if (movie.getDate().equals("") || movie.getDate().equals("null"))
                viewHolder.txtYear.setText(R.string.not_available);
            else
                viewHolder.txtYear.setText("(" + movie.getDate().substring(0, 4) + ")");

            if (movie.getRating() == 0.0)
                viewHolder.txtRating.setText(R.string.not_available);
            else
                viewHolder.txtRating.setText(String.valueOf(movie.getRating()));
        }

        @Override
        public int getItemCount() {

            return movies.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView imgPoster;
            public TextView txtTitle;
            public TextView txtRating;
            public TextView txtYear;

            public ViewHolder(View itemView) {
                super(itemView);
                imgPoster = (ImageView)itemView.findViewById(R.id.imgPoster);
                txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
                txtYear = (TextView)itemView.findViewById(R.id.txtYear);
                txtRating = (TextView)itemView.findViewById(R.id.txtRating);
            }

        }
}
