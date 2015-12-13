package popularmovies.buddyappz.com.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import popularmovies.buddyappz.com.popularmovies.beans.Trailer;

/**
 * Created by dejan on 22/07/15.
 */
public class TrailersListAdapter extends RecyclerView.Adapter<TrailersListAdapter.ViewHolder> {

    private Context mContext;
    CustomItemClickListener listener;

    List<Trailer> trailers;

        public TrailersListAdapter(Context context, List<Trailer> trailers, CustomItemClickListener listener) {
            super();
            this.trailers = trailers;
            this.mContext = context;
            this.listener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.trailer_item, viewGroup, false);
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
            Trailer trailer = trailers.get(i);

            String thumbnail_url = "http://img.youtube.com/vi/" + trailer.getKey() + "/0.jpg";
            Glide.with(mContext).load(thumbnail_url).centerCrop().into(viewHolder.trailerThumbnail);
        }

        @Override
        public int getItemCount() {

            return trailers.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView trailerThumbnail;

            public ViewHolder(View itemView) {
                super(itemView);
                trailerThumbnail = (ImageView)itemView.findViewById(R.id.trailer_thumbnail);
            }

        }
}
