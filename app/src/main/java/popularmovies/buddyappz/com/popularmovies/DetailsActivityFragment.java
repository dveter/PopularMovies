package popularmovies.buddyappz.com.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailsActivityFragment extends Fragment {

    public static String DETAILS = "movie_details";

    private Movie mSelectedMovie;

    private CollapsingToolbarLayout mCollapsingToolbar;

    public DetailsActivityFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_details, container, false);

        Intent intent = getActivity().getIntent();

        mSelectedMovie = intent.getParcelableExtra(DETAILS);

        mCollapsingToolbar = (CollapsingToolbarLayout) v.findViewById(R.id.collapsingToolbarLayout);
        mCollapsingToolbar.setTitle(mSelectedMovie.getTitle());

        final Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((DetailsActivity)getActivity()).setSupportActionBar(toolbar);
        ((DetailsActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ImageView toolbarImage = (ImageView)v.findViewById(R.id.toolbar_image);
        Picasso.with(getActivity().getApplicationContext()).load(CommonUtilities
                .MOVIE_DB_BIG_IMAGE_BASE_URL + mSelectedMovie.getBigImageUrl()).fit().into(toolbarImage);

        ImageView poster = (ImageView)v.findViewById(R.id.imgDetailsPoster);
        Picasso.with(getActivity().getApplicationContext()).load(CommonUtilities
                .MOVIE_DB_IMAGE_BASE_URL + mSelectedMovie.getPosterUrl()).fit().into(poster);

        TextView textRating = (TextView)v.findViewById(R.id.txt_details_rating);
        textRating.setText(String.valueOf(mSelectedMovie.getRating()));

        TextView textOrigTitle = (TextView)v.findViewById(R.id.txt_details_title);
        textOrigTitle.setText(mSelectedMovie.getTitle());

        TextView textReleaseDate = (TextView) v.findViewById(R.id.txt_details_release_date);
        if (mSelectedMovie.getDate().equals("null")) {
            textReleaseDate.setText(R.string.not_available);
        } else  {
            textReleaseDate.setText(mSelectedMovie.getDate());
        }

        TextView textOverview = (TextView)v.findViewById(R.id.txt_details_overview);
        if (mSelectedMovie.getOverView().equals("null")) {
            textOverview.setText(R.string.not_available);
        } else  {
            textOverview.setText(mSelectedMovie.getOverView());
        }

        return v;
    }

}
