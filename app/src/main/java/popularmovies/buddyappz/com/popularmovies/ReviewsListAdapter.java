package popularmovies.buddyappz.com.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import popularmovies.buddyappz.com.popularmovies.beans.UserReview;

/**
 * Created by dejan on 22/07/15.
 */
public class ReviewsListAdapter extends RecyclerView.Adapter<ReviewsListAdapter.ViewHolder> {

    private Context mContext;
    CustomItemClickListener listener;

    List<UserReview> reviews;

        public ReviewsListAdapter(Context context, List<UserReview> reviews, CustomItemClickListener listener) {
            super();
            this.reviews = reviews;
            this.mContext = context;
            this.listener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.review_item, viewGroup, false);
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
            UserReview review = reviews.get(i);

            viewHolder.txtAuthor.setText(review.getAuthor());
            viewHolder.txtContent.setText(review.getContent());
        }

        @Override
        public int getItemCount() {

            return reviews.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txtAuthor;
            public TextView txtContent;
            public TextView txtUrl;

            public ViewHolder(View itemView) {
                super(itemView);
                txtAuthor = (TextView)itemView.findViewById(R.id.textReviewAuthor);
                txtContent = (TextView)itemView.findViewById(R.id.textReviewContent);
            }

        }
}
